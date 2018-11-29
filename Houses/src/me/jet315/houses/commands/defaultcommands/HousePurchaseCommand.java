package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.EconHandler;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 08/02/2018.
 */
public class HousePurchaseCommand extends CommandExecutor {

    /**
     * Purchase House Command
     */

    public HousePurchaseCommand() {
        setCommand("purchase");
        setPermission("house.player.purchase");
        setLength(1);
        setPlayer();
        setUsage("/house purchase");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();
        //Check if user has a house
        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && plotPlayer.getPlots().size() > 0){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseAlreadyHasHouse()));
            return;
        }

        //Doesn't have a house
        //Get the price of the house, check user has the funds
        long housePrice = Core.getInstance().getProperties().getFirstHousePrice();
        if(housePrice <= 0){
            claimHouse(p,plotPlayer);
            return;
        }

        /**
         * Purchase the house using Vault Economy
         */
        if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("vault")){
            //Vault is not installed on the server, good luck using a null field :(
            if(Core.economy == null){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cVault is not installed on the server. Please contact the server owner."));
                return;
            }
            //Check player has the funds, if so withdraw them
            if(Core.economy.getBalance(p) >= housePrice){
                Core.economy.withdrawPlayer(p,housePrice);
                claimHouse(p,plotPlayer);
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughMoney().replaceAll("%PRICE%",String.valueOf(housePrice))));
            }
            return;

            /**
             * Purchase the house using Tokens
             */
        }else if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokens")){
            //Tokens is not installed :(
            if(!Core.getInstance().isTokenManagerEnabled()){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cTokenManager is not installed on the server. Please contact the server owner."));
                return;
            }
            //Check player has the tokens, if so take them away
            if(Core.tokenManager.getTokens(p) != null && Core.tokenManager.getTokens(p).getAsLong() >= housePrice){
                Core.tokenManager.setTokens(p, Core.tokenManager.getTokens(p).getAsLong() - housePrice);
                claimHouse(p,plotPlayer);
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughTokens().replaceAll("%PRICE%",String.valueOf(housePrice))));
            }

            /**
             * Purchase the house using token enchant
             */
        }else if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant")){
            //Tokens is not installed :(
            if(!Core.getInstance().isTokenEnchantEnabled()){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cTokenEnchant is not installed on the server. Please contact the server owner."));
                return;
            }
            //Check player has the tokens, if so take them away
            if(TokenEnchantAPI.getInstance().getTokens(p) >= housePrice){
                TokenEnchantAPI.getInstance().removeTokens(p,housePrice);
                claimHouse(p,plotPlayer);
            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseNotEnoughTokens().replaceAll("%PRICE%",String.valueOf(housePrice))));
            }

        }else {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cAn Economy type for house upgrading cannot be identified. Please contact the server owner."));
        }

    }

    /**
     * Called when user has paid and needs a house
     * @param p The player who is involved
     */
    public void claimHouse(Player p,PlotPlayer plotPlayer){
        //Teleport player to the plot world, wait a second, run the plot claim command

        //The House Claim Event is called when the plot is created, so no need to call it here. Possibly need to move it here though
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseLocate()));
        if(!p.getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())){
            //Not in the plot world, teleport there
            World world = Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName());
            if(world != null) {
                p.teleport(world.getSpawnLocation());
            }else{
                refundHouse(p,Core.getInstance().getProperties().getFirstHousePrice());
                System.out.println("The houses world found does not match the one found in the config.yml - the world '" + Core.getInstance().getProperties().getPlotsWorldName() + "' does not exist. Please modify your world name, or change the config.yml house world name");
                return;
            }
        }
        //Reason for the delay is to let the player spawn into the world before doing the command
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(!p.isOnline()){
                    refundHouse(p,Core.getInstance().getProperties().getFirstHousePrice());
                    return;
                }else if(!p.getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())){
                    refundHouse(p,Core.getInstance().getProperties().getFirstHousePrice());
                    return;
                }else {
                    PlotArea plotArea = plotPlayer.getApplicablePlotArea();
                        if (plotArea == null) {
                            if (EconHandler.manager != null) {
                                for (PlotArea area : PS.get().getPlotAreaManager().getAllPlotAreas()) {
                                    if (plotArea != null) {
                                        plotArea = area;
                                        break;
                                    }
                                    plotArea = null;

                                }
                            }
                        }
                        if (plotArea == null || plotArea.TYPE == 2) {
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.RED + "No free plots!");
                            return;
                        }

                        Plot plot = plotArea.getNextFreePlot(plotPlayer, PlotId.fromString(plotArea.id));
                        plot.claim(plotPlayer,true,null,true);
                        Locale locale = Core.getInstance().getMessages();
                    if(Core.serverVersion.startsWith("v1_12")) {
                        p.sendTitle(locale.getPurchasedTitle().equalsIgnoreCase("none") ? "": locale.getPurchasedTitle(),locale.getPurchaseSubTitle().equalsIgnoreCase("none") ? "": locale.getPurchaseSubTitle(),30,60,10);
                    }else{
                        p.sendTitle(locale.getPurchasedTitle().equalsIgnoreCase("none") ? "": locale.getPurchasedTitle(),locale.getPurchaseSubTitle().equalsIgnoreCase("none") ? "": locale.getPurchaseSubTitle());
                    }

                }
            }
        },30L);

    }

    public void refundHouse(Player p, long amount){
        System.out.println(ChatColor.RED + "An error occurred during the claiming house process for " + p.getName() + " If there is no following refund message, the user has not been refunded");
        /**
         * Refund Vault
         */
        if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("vault")){
            Core.economy.bankDeposit(p.getName(),amount);
            System.out.println(ChatColor.YELLOW + "USER " + p.getName() + " Has been refunded $" + amount);


            /**
             * refund tokens
             */
        }else if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokens")){

            Core.tokenManager.setTokens(p,Core.tokenManager.getTokens(p).getAsLong() + Core.getInstance().getProperties().getFirstHousePrice());
            System.out.println(ChatColor.YELLOW + "USER " + p.getName() + " Has been refunded " + amount + " Tokens");

        }else if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant")){
            TokenEnchantAPI.getInstance().removeTokens(p,Core.getInstance().getProperties().getFirstHousePrice());
            System.out.println(ChatColor.YELLOW + "USER " + p.getName() + " Has been refunded " + amount + " Tokens");
        }
        if(p.isOnline()){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHousePurchaseFail()));
        }
    }
}