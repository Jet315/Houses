package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.config.C;
import com.intellectualcrafters.plot.config.Settings;
import com.intellectualcrafters.plot.database.DBFunc;
import com.intellectualcrafters.plot.object.*;
import com.intellectualcrafters.plot.util.ByteArrayUtilities;
import com.intellectualcrafters.plot.util.EconHandler;
import com.intellectualcrafters.plot.util.Permissions;
import com.intellectualcrafters.plot.util.TaskManager;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HouseClaimCommand extends CommandExecutor {

    /**
     * Claim house command
     */

    public HouseClaimCommand() {
        setCommand("claim");
        setPermission("house.player.claim");
        setLength(1);
        setPlayer();
        setUsage("/house claim");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        PlotPlayer player = PlotPlayer.get((sender).getName());
        Player p = (Player) sender;
        if(player == null) return;


        Locale locale = Core.getInstance().getMessages();
        //Check if user has a house
        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && player.getPlots().size() > 0){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseAlreadyHasHouse()));
            return;
        }

        //Doesn't have a house
        //Get the price of the house, check user has the funds
        long housePrice = Core.getInstance().getProperties().getFirstHousePrice();
        if(housePrice > 0) {


            /**
             * Purchase the house using Vault Economy
             */
            if (Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("vault")) {
                //Vault is not installed on the server, good luck using a null field :(
                if (Core.economy == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cVault is not installed on the server. Please contact the server owner."));
                    return;
                }
                //Check player has the funds, if so withdraw them
                if (Core.economy.getBalance(p) >= housePrice) {
                    Core.economy.withdrawPlayer(p, housePrice);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughMoney().replaceAll("%PRICE%", String.valueOf(housePrice))));
                    return;
                }


                /**
                 * Purchase the house using Tokens
                 */
            } else if (Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokens")) {
                //Tokens is not installed :(
                if (!Core.getInstance().isTokenManagerEnabled()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cTokenManager is not installed on the server. Please contact the server owner."));
                    return;
                }
                //Check player has the tokens, if so take them away
                if (Core.tokenManager.getTokens(p) != null && Core.tokenManager.getTokens(p).getAsLong() >= housePrice) {
                    Core.tokenManager.setTokens(p, Core.tokenManager.getTokens(p).getAsLong() - housePrice);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughTokens().replaceAll("%PRICE%", String.valueOf(housePrice))));
                    return;
                }

                /**
                 * Purchase the house using token enchant
                 */
            } else if (Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant")) {
                //Tokens is not installed :(
                if (!Core.getInstance().isTokenEnchantEnabled()) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cTokenEnchant is not installed on the server. Please contact the server owner."));
                    return;
                }
                //Check player has the tokens, if so take them away
                if (TokenEnchantAPI.getInstance().getTokens(p) >= housePrice) {
                    TokenEnchantAPI.getInstance().removeTokens(p, housePrice);
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughTokens().replaceAll("%PRICE%", String.valueOf(housePrice))));
                    return;
                }

            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cAn Economy type for house upgrading cannot be identified. Please contact the server owner."));
                return;
            }

        }

        String schematic = "";
        if (args.length >= 1) {
            schematic = args[0];
        }
        Location loc = player.getLocation();
        final Plot plot = loc.getPlotAbs();
        if (plot == null) {
            player.sendMessage(""+C.NOT_IN_PLOT);
            return;
        }
        int currentPlots = Settings.Limit.GLOBAL ? player.getPlotCount() : player.getPlotCount(loc.getWorld());
        int grants = 0;
        if (currentPlots >= player.getAllowedPlots()) {
            if (player.hasPersistentMeta("grantedPlots")) {
                grants = ByteArrayUtilities.bytesToInteger(player.getPersistentMeta("grantedPlots"));
                if (grants <= 0) {
                    player.removePersistentMeta("grantedPlots");
                    player.sendMessage(""+C.CANT_CLAIM_MORE_PLOTS);
                    return;
                }
            } else {
                 player.sendMessage(""+C.CANT_CLAIM_MORE_PLOTS);
                 return;
            }
        }
        if (!plot.canClaim(player)) {
            player.sendMessage(""+C.PLOT_IS_CLAIMED);
            return;
        }
        final PlotArea area = plot.getArea();
        if (!schematic.isEmpty()) {
            if (area.SCHEMATIC_CLAIM_SPECIFY) {
                if (!area.SCHEMATICS.contains(schematic.toLowerCase())) {
                     player.sendMessage(C.SCHEMATIC_INVALID + " "+  "non-existent schematic: " + schematic);
                }
                if (!Permissions.hasPermission(player, C.PERMISSION_CLAIM_SCHEMATIC.f(schematic)) && !Permissions.hasPermission(player, C.PERMISSION_ADMIN_COMMAND_SCHEMATIC)) {
                    player.sendMessage(""+C.NO_SCHEMATIC_PERMISSION);
                    return;
                }
            }
        }
        int border = area.getBorder();
        if (border != Integer.MAX_VALUE && plot.getDistanceFromOrigin() > border) {
            player.sendMessage(""+ C.BORDER);
            return;
        }
        if (grants > 0) {
            if (grants == 1) {
                player.removePersistentMeta("grantedPlots");
            } else {
                player.setPersistentMeta("grantedPlots", ByteArrayUtilities.integerToBytes(grants - 1));
            }
            player.sendMessage(C.REMOVED_GRANTED_PLOT +  "1"+ "" + (grants - 1));
        }
        if (plot.canClaim(player)) {
            plot.owner = player.getUUID();
            final String finalSchematic = schematic;
            DBFunc.createPlotSafe(plot, new Runnable() {
                @Override
                public void run() {
                    TaskManager.IMP.sync(new RunnableVal<Object>() {
                        @Override
                        public void run(Object value) {
                            plot.claim(player, true, finalSchematic, false);
                            if (area.AUTO_MERGE) {
                                plot.autoMerge(-1, Integer.MAX_VALUE, player.getUUID(), true);
                            }
                        }
                    });
                }
            }, new Runnable() {
                @Override
                public void run() {
                    player.sendMessage(""+C.PLOT_NOT_CLAIMED);
                }
            });
        } else {
            player.sendMessage(""+C.PLOT_NOT_CLAIMED);
        }
    }
}
