package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.SchematicHandler;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.events.HouseUpgradeEvent;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.Math;
import me.realized.tm.api.TMAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseUpgradeCommand extends CommandExecutor {

    /**
     * House upgrade command
     */

    public HouseUpgradeCommand() {
        setCommand("upgrade");
        setPermission("house.player.upgrade");
        setLength(1);
        setPlayer();
        setUsage("/house upgrade");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();
        //Check if user has a valid house to upgradehouse
        if (!(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && plotPlayer.getPlots().size() > 0)) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeNoHome()));
            return;
        }
        int currentHouseLevel = Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getHouseLevel();

        /**
         * Check if user has the highest house already
         */
        if (currentHouseLevel >= Core.getInstance().getProperties().getMaxHouseLevel()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeMaxHouse()));
            return;
        }

        /**
         * Check if user is already upgrading house
         */
        if (Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getIsHouseBeingUpgraded()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeInProcess()));
            return;
        }


        //Get the price of the house, check user has the funds
        int housePriceUpgrade = Math.calculateHousePrice(currentHouseLevel, Core.getInstance().getProperties().getHousePriceAlgorithm());
        if (housePriceUpgrade <= 0) {
            upgradeHouse(p, plotPlayer.getPlots().iterator().next());
            return;
        }


        /**
         * Purchase the house using Vault Economy
         */
        if (Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("vault")) {
            //Vault is not installed on the server, good luck using a null field :(
            if (!Core.getInstance().isVaultEnabled()) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cVault is not installed on the server. Please contact the server owner."));
                return;
            }
            //Check player has the funds, if so withdraw them
            if (Core.economy.getBalance(p) >= housePriceUpgrade) {
                Core.economy.withdrawPlayer(p, housePriceUpgrade);
                upgradeHouse(p, plotPlayer.getPlots().iterator().next());
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeNotEnoughMoney().replaceAll("%PRICE%",String.valueOf(housePriceUpgrade))));
            }
            return;

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
            if (TMAPI.getTokens(p) >= housePriceUpgrade) {
                TMAPI.removeTokens(p, housePriceUpgrade);
                upgradeHouse(p, plotPlayer.getPlots().iterator().next());
            } else {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeNotEnoughTokens().replaceAll("%PRICE%",String.valueOf(housePriceUpgrade))));
            }

        }else if(Core.getInstance().getProperties().getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant")){
        //Tokens is not installed :(
        if(!Core.getInstance().isTokenEnchantEnabled()){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cTokenEnchant is not installed on the server. Please contact the server owner."));
            return;
        }
        //Check player has the tokens, if so take them away
        if(TokenEnchantAPI.getInstance().getTokens(p) >= housePriceUpgrade){
            TokenEnchantAPI.getInstance().removeTokens(p,housePriceUpgrade);
            upgradeHouse(p, plotPlayer.getPlots().iterator().next());
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeNotEnoughTokens().replaceAll("%PRICE%",String.valueOf(housePriceUpgrade))));}

    }else {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cAn Economy type for house upgrading cannot be identified. Please contact the server owner."));
    }



    }

    /**
     * Called when user a user upgrades his house
     *
     * @param p The player who is involved
     */
    public void upgradeHouse(Player p, Plot plot) {

        //Create, and trigger the HouseEnterEvent so others are able to have a say in what happens
        int currentHouseLevel = Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getHouseLevel();
        HouseUpgradeEvent houseUpgradeEvent = new HouseUpgradeEvent(p, plot, currentHouseLevel, currentHouseLevel + 1);
        Core.getInstance().getServer().getPluginManager().callEvent(houseUpgradeEvent);

        if (houseUpgradeEvent.isCancelled()) return;
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&e&lUpgrading House!"));
        /**
         * Stores the level the house is going to
         */
        int houseLevelTo = houseUpgradeEvent.getLevelTo();

        /**
         * Put player into the upgrading list
         */
        Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setIsHouseBeingUpgraded(true);

        /**
         * Get the two points at which the schematic is being pasted between
         */
        Location point1 = null;
        Location point2 = null;
        int locCounter = 0;
        for (Location l : plot.getAllCorners()) {
            if (locCounter == 0) {
                point1 = l.add(0, Core.getInstance().getProperties().getGetMaxBuildHeight(), 0);
            } else if (locCounter == 2) {
                //The point at which the ground + the highest chest is located at
                point2 = l.add(0, Core.getInstance().getProperties().getGetMaxBuildHeight() + Core.getInstance().getProperties().getChestSearchLevel(), 0);
            }
            locCounter++;
        }
        Location finalPoint = point1;
        Location finalPoint1 = point2;
        Bukkit.getScheduler().runTaskTimer(Core.getInstance(), new Runnable() {
            int counter = 0;
            ArrayList<Inventory> chestInventoriesBeforeDeletetion;
            ArrayList<Inventory> chestInventoriesAferDeletion;

            @Override
            public void run() {
                counter++;
                if (counter == 1) {
                    /**
                     * Teleport player out of plot, if in plot
                     */
                    for (PlotPlayer playerInPlot : plot.getPlayersInPlot()) {
                        playerInPlot.teleport(plot.getDefaultHome());
                        playerInPlot.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cThis house is being upgraded!"));
                    }
                } else if (counter == 2) {
                    /**
                     * Get chests & items in current plot
                     */
                    chestInventoriesBeforeDeletetion = chestInventoriesBetweenTwoPoints(finalPoint, finalPoint1, false);

                } else if (counter == 3) {
                    /**
                     * Paste schematic
                     */
                    SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(), "schematics/house" + houseLevelTo + ".schematic"));
                    SchematicHandler.manager.paste(schematic, plot, Core.getInstance().getProperties().getMoveSchematicXDirection(), Core.getInstance().getProperties().getMoveSchematicYDirection(), Core.getInstance().getProperties().getMoveSchematicZDirection(), true, null);

                } else if (counter == 4) {
                    /**
                     * Get the chests in the new locations
                     */
                    chestInventoriesAferDeletion = chestInventoriesBetweenTwoPoints(finalPoint, finalPoint1, true);

                } else if (counter == 5) {
                    /**
                     * Iterate through inventories and put old inventories into new ones
                     */
                //Check if inventories exist
                if(chestInventoriesBeforeDeletetion.size() > 0){
                    //Store all old items from chests into an array
                    ArrayList<ItemStack> deletedItems = new ArrayList<ItemStack>();

                    for(Inventory invBeforeDeleted : chestInventoriesBeforeDeletetion){

                        for(ItemStack item : invBeforeDeleted.getContents()){
                            if(item == null || item.getType() == Material.AIR){
                                continue;
                            }
                            deletedItems.add(item);
                        }
                    }
                    for(Inventory inv : chestInventoriesAferDeletion){
                        //Clear the inventory - Shouldn't have anything in as a new house
                        inv.clear();
                        for(ItemStack slotInInventory : inv){
                            //Check if free slot
                            if(slotInInventory == null || slotInInventory.getType() == Material.AIR){
                                if(deletedItems.size() == 0){
                                    break;
                                }
                                inv.addItem(deletedItems.get(0));
                                deletedItems.remove(0);
                            }
                            if(deletedItems.size() == 0){

                                break;
                            }
                        }
                    }
                }
                } else if (counter == 6) {
                    /**
                     * Save to database
                     */
                    //Update database

                    //Add new level
                    Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setHouseLevel(houseLevelTo);

                    //Update sqllite
                    Core.getInstance().getDb().setHouseLevel(p.getUniqueId().toString(), houseLevelTo);

                    //Remove from upgrading list
                    Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setIsHouseBeingUpgraded(false);

                    //Send user a message saying it's done
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&aHome has successfully been upgraded! Items from your previous house will be in chests!"));
                    return;
                }
            }
        }, 0L, 50L);


    }

    /**
     * @param loc1                The lower point
     * @param loc2                The upper Point
     * @param getEmptyInventories Whether to return empty inventories - Should only be used when getting the inventories to place blocks into
     * @return inventories  within these two points
     */
    private ArrayList<Inventory> chestInventoriesBetweenTwoPoints(Location loc1, Location loc2, boolean getEmptyInventories) {

        ArrayList<Inventory> inventories = new ArrayList<>();
        ArrayList<org.bukkit.Location> checkedDoubleChests = new ArrayList<>();
        int bottomPointX = loc1.getX();
        int bottomPointY = loc1.getY();
        int bottomPointZ = loc1.getZ();

        int topPointX = loc2.getX();
        int topPointY = loc2.getY();
        int topPointZ = loc2.getZ();
        World world = Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName());
        for (int x = bottomPointX; x <= topPointX; x++) {
            for (int z = bottomPointZ; z <= topPointZ; z++) {
                for (int y = bottomPointY; y <= topPointY; y++) {
                    if (world.getBlockAt(x, y, z).getState() instanceof Chest) {
                        Chest chest = (Chest) world.getBlockAt(x, y, z).getState();

                        //Check if chest is a double checked && there is a chest <1 block away
                        if (chest.getInventory().getHolder() instanceof DoubleChest) {

                            boolean shouldSkip = false;
                            for (org.bukkit.Location l : checkedDoubleChests) {
                                if (l.distance(chest.getLocation()) <= 1) {
                                    //It has been checked
                                    shouldSkip = true;
                                    break;
                                }
                            }
                            if(shouldSkip) break;
                            //Add location to array otherwise so it is not double checked
                            checkedDoubleChests.add(chest.getLocation());

                        }

                        Inventory inv = ((InventoryHolder) chest).getInventory();

                        if (getEmptyInventories) {
                            inventories.add(inv);
                        } else {
                            for(ItemStack item : inv.getContents()){
                                if(item == null) continue;
                                if(item.getType() == Material.AIR){
                                    continue;
                                }else{
                                    inventories.add(inv);
                                    break;
                                }
                            }

                        }
                    }
                }
            }
        }

        return inventories;
    }

}