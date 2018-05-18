package me.jet315.houses.listeners;

import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import me.jet315.houses.Core;
import me.jet315.houses.utils.files.HouseItem;
import me.jet315.houses.utils.files.GUIProperties;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.files.RentItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Jet on 09/02/2018.
 */
public class GUIClickEvent implements Listener {

    @EventHandler
    public void onGUIClick(InventoryClickEvent e) {
        ItemStack itemClicked = e.getCurrentItem();
        //First null check to see if item exists
        if (itemClicked == null) return;

        //Second (May click like air block this prevents that)
        if (itemClicked.getItemMeta() == null) return;

        //Third, check that there is a valid display name
        if (itemClicked.getItemMeta().getDisplayName() == null) return;


        /**
         * House GUI
         */

        if (e.getClickedInventory().getName().equals(Core.getInstance().getProperties().getHouseGUIName())) {

            //Will always cancel the event, as I want a custom thing to happen
            e.setCancelled(true);
            //Would use a switch statement however case statements have to be compile-time evaluable so it would not work
            GUIProperties properties = Core.getInstance().getProperties();
            Locale locale = Core.getInstance().getMessages();
            Player p = (Player) e.getWhoClicked();

            for(HouseItem houseItem : Core.getInstance().getProperties().getItemsInHouseGUI().values()){
                if(houseItem.isItemEqual(itemClicked)){
                    if(houseItem.getCommandToPerform().equalsIgnoreCase("house closemenu")){
                        closeAndUpdateInventory(p);
                        return;
                    }

                    if (properties.isHouseConfirmationMessages()) {
                         if(houseItem.getCommandToPerform().equalsIgnoreCase("house find")){
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindMessage());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house find ");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;
                        }else if(houseItem.getCommandToPerform().equalsIgnoreCase("house trust")){
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustMessage());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house trust ");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;

                        }else if(houseItem.getCommandToPerform().equalsIgnoreCase("house trust")){
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeConfirmation());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house upgrade");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;

                        }
                    }
                    closeAndUpdateInventory(p);
                    p.performCommand(houseItem.getCommandToPerform());
                    return;
                }
            }

/*            if (properties.getTeleportToHouseWorldItem().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                p.performCommand("house world");
                closeAndUpdateInventory(p);
            } else if (properties.getTeleportToHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                p.performCommand("house tp");
                closeAndUpdateInventory(p);
            } else if (properties.getPurchaseHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                if (properties.isHouseConfirmationMessages()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseConfirmation()));
                    Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house purchase");
                    Core.getInstance().getPlayerManager().removePlayer(p.getName());
                } else {
                    p.performCommand("house purchase");
                }
                closeAndUpdateInventory(p);
            } else if (properties.getFindHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindMessage()));
                Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house find ");
                Core.getInstance().getPlayerManager().removePlayer(p.getName());
                closeAndUpdateInventory(p);
            } else if (properties.getTrustUser().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustMessage()));
                Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house trust ");
                Core.getInstance().getPlayerManager().removePlayer(p.getName());
                closeAndUpdateInventory(p);
            } else if (properties.getUpgradeHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                //p.performCommand("house upgrade");
                if (properties.isHouseConfirmationMessages()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUpgradeConfirmation()));
                    Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house upgrade");
                    Core.getInstance().getPlayerManager().removePlayer(p.getName());
                } else {
                    p.performCommand("house upgrade");
                }
                closeAndUpdateInventory(p);
            } else if (properties.getLockedHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName) || properties.getUnlockedHome().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                p.performCommand("house lock");
                closeAndUpdateInventory(p);
            } else if (properties.getIncreaseRent().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                closeAndUpdateInventory(p);
                p.performCommand("house rent");
                return;
            } else if (properties.getCloseInventory().getItemMeta().getDisplayName().equalsIgnoreCase(displayName)) {
                closeAndUpdateInventory(p);
            }
            return;*/
        }
        /**
         * NoHouseGUI
         */
        if (e.getClickedInventory().getName().equals(Core.getInstance().getProperties().getNoHouseGUIName())) {

            //Will always cancel the event, as I want a custom thing to happen
            e.setCancelled(true);
            //Would use a switch statement however case statements have to be compile-time evaluable so it would not work
            GUIProperties properties = Core.getInstance().getProperties();
            Locale locale = Core.getInstance().getMessages();
            Player p = (Player) e.getWhoClicked();


            for (HouseItem houseItem : Core.getInstance().getProperties().getItemsInNoHouseGUI().values()) {
                if(houseItem.isItemEqual(itemClicked)){

                    if (houseItem.getCommandToPerform().equalsIgnoreCase("house closemenu")) {
                        closeAndUpdateInventory(p);
                        return;
                    }

                    if (properties.isHouseConfirmationMessages()) {
                        if (houseItem.getCommandToPerform().equalsIgnoreCase("house purchase")) {
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHousePurchaseConfirmation());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house purchase");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;
                        } else if (houseItem.getCommandToPerform().equalsIgnoreCase("house find")) {
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindMessage());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house find ");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;
                        } else if (houseItem.getCommandToPerform().equalsIgnoreCase("house trust")) {
                            p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustMessage());
                            Core.getInstance().getPlayerManager().getConfirmation().put(p.getName(), "house trust ");
                            Core.getInstance().getPlayerManager().removePlayer(p.getName());
                            closeAndUpdateInventory(p);
                            return;

                        }
                    }

                    p.performCommand(houseItem.getCommandToPerform());
                    closeAndUpdateInventory(p);
                    return;
                }
            }
        }

        /**
         * Increase rental GUI
         */

        if (e.getClickedInventory().getName().equals(Core.getInstance().getProperties().getRentGUIName())) {

            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            GUIProperties properties = Core.getInstance().getProperties();
            //Check if close button
            for (RentItem rentItem : Core.getInstance().getProperties().getItemsInRentGUI().values()) {
                if(rentItem.isItemEqual(itemClicked)){

                   if(rentItem.getRentDays() < 0){
                       closeAndUpdateInventory(p);
                       return;
                   }
                    int rentPrice = rentItem.getRentDays() * properties.getCostToRentPerDay();

                    /**
                     * Check user has not got max days already
                     */
                    int daysLeft = (int) (((Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getMillisecondsOfExpiry() - System.currentTimeMillis()) / 86400000)) + rentItem.getRentDays();
                    int maxNumberOfDays = Core.getInstance().getProperties().getMaxAmountOfRentTime();

                    if (daysLeft > maxNumberOfDays) {
                        p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseRentalLimit().replaceAll("%DAYS%", String.valueOf(maxNumberOfDays)));
                        closeAndUpdateInventory(p);
                        return;
                    }

                    /**
                     * Purchase the rent using Vault Economy
                     */
                    if (Core.getInstance().getProperties().getEconomyTypeForRenting().equalsIgnoreCase("vault")) {
                        //Vault is not installed on the server, good luck using a null field :(
                        if (!Core.getInstance().isVaultEnabled()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cVault is not installed on the server. Please contact the server owner."));
                            closeAndUpdateInventory(p);
                            return;
                        }
                        //Check player has the funds, if so withdraw them
                        if (Core.economy.getBalance(p) >= rentPrice) {
                            Core.economy.withdrawPlayer(p, rentPrice);
                            addRent(p, rentItem.getRentDays());
                            closeAndUpdateInventory(p);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getNoFundsEconomyRental().replaceAll("%RENT%", String.valueOf(rentPrice))));
                        }
                        return;

                        /**
                         * Purchase the rent using Tokens
                         */
                    } else if (Core.getInstance().getProperties().getEconomyTypeForRenting().equalsIgnoreCase("tokens")) {
                        //Tokens is not installed :(
                        if (!Core.getInstance().isTokenManagerEnabled()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cTokenManager is not installed on the server. Please contact the server owner."));
                            return;
                        }
                        //Check player has the tokens, if so take them away
                        if (Core.tokenManager.getTokens(p) != null && Core.tokenManager.getTokens(p).getAsLong() >= rentPrice) {
                            Core.tokenManager.setTokens(p, Core.tokenManager.getTokens(p).getAsLong() - rentPrice);
                            addRent(p, rentItem.getRentDays());
                            closeAndUpdateInventory(p);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getNoFundsTokensRental().replaceAll("%RENT%", String.valueOf(rentPrice))));
                        }

                        /**
                         * Purchase the rent using TokenEnchant
                         */
                    } else if (Core.getInstance().getProperties().getEconomyTypeForRenting().equalsIgnoreCase("tokenenchant")) {
                        //TokensEnchant is not installed :(
                        if (!Core.getInstance().isTokenEnchantEnabled()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cTokenEnchant is not installed on the server. Please contact the server owner."));
                            return;
                        }
                        //Check player has the tokens, if so take them away
                        if (TokenEnchantAPI.getInstance().getTokens(p) >= rentPrice) {
                            TokenEnchantAPI.getInstance().removeTokens(p, rentPrice);
                            addRent(p, rentItem.getRentDays());
                            closeAndUpdateInventory(p);
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getNoFundsTokensRental().replaceAll("%RENT%", String.valueOf(rentPrice))));
                        }

                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cAn Economy type for extending house rent cannot be identified. Please contact the server owner."));
                    }

                }
            }


        }


    }

    public void closeAndUpdateInventory(Player p) {
        p.closeInventory();
        p.updateInventory();
    }

    public void addRent(Player p, int days) {
        //Get current expiry date & future date:
        long currentExpiryDate = Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getMillisecondsOfExpiry();
        long futureExpiryDate = currentExpiryDate + (days * 86400000);

        //Update cashed data
        Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setMillisecondsOfExpirey(futureExpiryDate);
        //Update database
        Core.getInstance().getDb().setHouseRentalTime(p.getUniqueId().toString(), futureExpiryDate);

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseRentExtended().replaceAll("%DAYS%", String.valueOf(days + (days == 1 ? " day" : " days")))));
    }
}
