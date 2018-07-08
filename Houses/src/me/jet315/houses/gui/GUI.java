package me.jet315.houses.gui;

import me.jet315.houses.Core;
import me.jet315.houses.events.PlayerOpenHouseGUIEvent;
import me.jet315.houses.utils.files.HouseItem;
import me.jet315.houses.utils.files.GUIProperties;
import me.jet315.houses.utils.Math;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jet on 07/02/2018.
 */
public abstract class GUI {

    private Core instance;
    private GUIProperties properties;

    /**
     * @param instance The Core's instance
     */
    public GUI(Core instance) {
        this.instance = instance;
        this.properties = instance.getProperties();
    }

    /**
     * Opens the House GUI for a particular player
     *
     * @param p The Player
     */
    public void openGUI(Player p) {

        //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
        PlayerOpenHouseGUIEvent openHouseGUIEvent = new PlayerOpenHouseGUIEvent(p);
        Core.getInstance().getServer().getPluginManager().callEvent(openHouseGUIEvent);
        //Check if event has been canceled
        if (openHouseGUIEvent.isCancelled()) return;
        Inventory houseInventory;

        if (houseLevel() <= 0) {
            houseInventory = Bukkit.createInventory(null, Core.getInstance().getProperties().getNoHouseGUISlots(),Core.getInstance().getProperties().getNoHouseGUIName());

            //User does not have house
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.world"))
                if (properties.getItemsInNoHouseGUI().containsKey("TeleportToHouseWorldItem")) {
                    HouseItem item = properties.getItemsInNoHouseGUI().get("TeleportToHouseWorldItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());
                }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.purchase"))
                if (properties.getItemsInNoHouseGUI().containsKey("PurchaseHomeItem")) {
                    HouseItem item = properties.getItemsInNoHouseGUI().get("PurchaseHomeItem");
                    ItemStack itemStack = item.getItem().clone();

                    if(itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().size() >0){

                        List<String> formattedLore = new ArrayList<>();
                        for(String loreLine : itemStack.getItemMeta().getLore()){
                            formattedLore.add(loreLine.replace("%COST%",String.valueOf(properties.getFirstHousePrice())));
                        }

                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setLore(formattedLore);
                        itemStack.setItemMeta(meta);
                    }
                    houseInventory.setItem(item.getSlotID(),itemStack);
                }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.find"))
                if (properties.getItemsInNoHouseGUI().containsKey("FindHomeItem")) {
                    HouseItem item = properties.getItemsInNoHouseGUI().get("FindHomeItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());
                }

            if (properties.getItemsInNoHouseGUI().containsKey("CloseInventoryItem")) {
                HouseItem item = properties.getItemsInNoHouseGUI().get("CloseInventoryItem");
                houseInventory.setItem(item.getSlotID(),item.getItem());
            }

        } else {
            houseInventory = Bukkit.createInventory(null, Core.getInstance().getProperties().getHouseGUISlots(),Core.getInstance().getProperties().getHouseGUIName());

            //User has house!
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.world"))
                if (properties.getItemsInHouseGUI().containsKey("TeleportToHouseWorldItem")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("TeleportToHouseWorldItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());

                }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.tp"))
                if (properties.getItemsInHouseGUI().containsKey("TeleportToHouseItem")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("TeleportToHouseItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());

                }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.find"))
                if (properties.getItemsInHouseGUI().containsKey("FindHomeItem")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("FindHomeItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());

                }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.trust"))
                if (properties.getItemsInHouseGUI().containsKey("TrustPlayerItem")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("TrustPlayerItem");
                    houseInventory.setItem(item.getSlotID(),item.getItem());

                }

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.upgrade")) {

                if(!(houseLevel() >= Core.getInstance().getProperties().getMaxHouseLevel())) {
                    //not max house level
                    if (properties.getItemsInHouseGUI().containsKey("UpgradeHomeItem")) {
                        HouseItem item = properties.getItemsInHouseGUI().get("UpgradeHomeItem");
                        ItemStack itemStack = item.getItem().clone();
                        if (itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().size() > 0) {
                            List<String> formattedLore = new ArrayList<>();
                            for (String loreLine : itemStack.getItemMeta().getLore()) {
                                formattedLore.add(loreLine.replaceAll("%COST%", String.valueOf(Math.calculateHousePrice(houseLevel(), properties.getHousePriceAlgorithm()))));
                            }
                            ItemMeta meta = itemStack.getItemMeta();
                            meta.setLore(formattedLore);
                            itemStack.setItemMeta(meta);
                        }

                        houseInventory.setItem(item.getSlotID(), itemStack);
                    }
                }else{
                    //max house level
                    if (properties.getItemsInHouseGUI().containsKey("MaxHouseLevel")) {
                        HouseItem item = properties.getItemsInHouseGUI().get("MaxHouseLevel");
                        houseInventory.setItem(item.getSlotID(),item.getItem());
                    }
                }

            }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.rent")) {

                if (properties.getItemsInHouseGUI().containsKey("IncreaseRentItem")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("IncreaseRentItem");
                    ItemStack itemStack = item.getItem().clone();

                    if (itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore().size() > 0) {
                        //Work out time left on house
                        Integer[] expiryDate = Math.calculateTimeLeft(instance.getPlayerManager().getHousePlayerMap().get(p).getMillisecondsOfExpiry());
                        String expiry = (ChatColor.translateAlternateColorCodes('&', "&6" + (expiryDate[0] == 0 ? "" : (expiryDate[0] == 1 ? expiryDate[0] + " &aDay,&6 " : expiryDate[0] + " &aDays,&6 ")) +
                                (expiryDate[1] == 1 ? expiryDate[1] + " &aHour,&6 " : expiryDate[1] + " &aHours,&6 ") +
                                (expiryDate[2] == 1 ? expiryDate[2] + " &aMinute " : expiryDate[2] + " &aMinutes ") +
                                (expiryDate[3] != 0 ? "&aand &6" : "&6") +
                                (expiryDate[3] == 0 ? "" : (expiryDate[3] == 1 ? expiryDate[3] + " &aSecond " : expiryDate[3] + " &aSeconds "))
                        ));
                        List<String> formattedLore = new ArrayList<>();
                        for (String loreLine : itemStack.getItemMeta().getLore()) {
                            formattedLore.add(loreLine.replaceAll("%RENTTIMELEFT%", expiry));
                        }
                        ItemMeta meta = itemStack.getItemMeta();
                        meta.setLore(formattedLore);
                        itemStack.setItemMeta(meta);
                    }

                    houseInventory.setItem(item.getSlotID(), itemStack);
                }


            }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.abandon"))
                if (properties.getItemsInHouseGUI().containsKey("AbandonHome")) {
                    HouseItem item = properties.getItemsInHouseGUI().get("AbandonHome");
                    houseInventory.setItem(item.getSlotID(),item.getItem());
                }

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.player.lock")) {
                if (isHouseLocked()) {
                    if (properties.getItemsInHouseGUI().containsKey("LockHomeItem")) {
                        HouseItem item = properties.getItemsInHouseGUI().get("LockHomeItem");
                        houseInventory.setItem(item.getSlotID(),item.getItem());
                    }
                } else {
                    if (properties.getItemsInHouseGUI().containsKey("UnLockHomeItem")) {
                        HouseItem item = properties.getItemsInHouseGUI().get("UnLockHomeItem");
                        houseInventory.setItem(item.getSlotID(),item.getItem());
                    }
                }
            }
            if (properties.getItemsInHouseGUI().containsKey("CloseInventoryItem")) {
                HouseItem item = properties.getItemsInHouseGUI().get("CloseInventoryItem");
                houseInventory.setItem(item.getSlotID(),item.getItem());
            }


        }
        p.openInventory(houseInventory);
    }

    /**
     * @return Returns the player who is opening the GUI
     */
    public abstract Player getPlayer();

    /**
     * @return Returns 0 if the user does not have a house
     */
    public abstract int houseLevel();

    /**
     * @return Returns false it the user does have house
     */
    public abstract boolean isHouseLocked();


}
