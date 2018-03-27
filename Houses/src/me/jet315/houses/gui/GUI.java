package me.jet315.houses.gui;

import com.intellectualcrafters.plot.commands.Chat;
import me.jet315.houses.Core;
import me.jet315.houses.events.PlayerOpenHouseGUIEvent;
import me.jet315.houses.utils.GUIProperties;
import me.jet315.houses.utils.Math;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jet on 07/02/2018.
 */
public abstract class GUI{

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
        //Create, and trigger the HouseClaimEvent so others are able to have a say in what happens
        PlayerOpenHouseGUIEvent openHouseGUIEvent = new PlayerOpenHouseGUIEvent(p);
        Core.getInstance().getServer().getPluginManager().callEvent(openHouseGUIEvent);
        //Check if event has been canceled
        if (openHouseGUIEvent.isCancelled()) return;

        Inventory houseInventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', instance.getMessages().getGuiTitle()));

        if (houseLevel() <= 0) {
            //User does not have house
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.worldtp"))
                houseInventory.setItem(0, properties.getTeleportToHouseWorldItem());

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.purchasehouse"))
                houseInventory.setItem(1, properties.getPurchaseHome());

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.findhouse"))
                houseInventory.setItem(2, properties.getFindHome());

            houseInventory.setItem(8, properties.getCloseInventory());
        } else {
            //User has house!
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.worldtp"))
                houseInventory.setItem(0, properties.getTeleportToHouseWorldItem());

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.housetp"))
                houseInventory.setItem(1, properties.getTeleportToHome());

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.findhouse"))
                houseInventory.setItem(2, properties.getFindHome());

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.trust"))
                houseInventory.setItem(3, properties.getTrustUser());


            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.upgradehome")) {
                ItemStack upgradeHome = properties.getUpgradeHome().clone();
                ArrayList<String> lore = (ArrayList<String>) upgradeHome.getItemMeta().getLore();
                //Check if has max house level
                if (!(houseLevel() >= Core.getInstance().getProperties().getMaxHouseLevel())) {
                    lore.add(ChatColor.GRAY + "This will cost you");
                    if (properties.getEconomyTypeToUpgrade().equalsIgnoreCase("vault")) {
                        lore.add(ChatColor.YELLOW + "$" + Math.calculateHousePrice(houseLevel(), properties.getHousePriceAlgorithm()));
                    } else if (properties.getEconomyTypeToUpgrade().equalsIgnoreCase("tokens")) {
                        lore.add(ChatColor.YELLOW + "" + Math.calculateHousePrice(houseLevel(), properties.getHousePriceAlgorithm()) + " Tokens!");
                    } else {
                        lore.add(ChatColor.GRAY + "Nothing!");
                    }
                    lore.add(" ");
                    lore.add(ChatColor.GOLD + "Alias: /house upgrade");
                }else{
                    //Has max house level
                    lore.add(ChatColor.RED +"Maximum House level");
                    lore.add(ChatColor.RED +"already achieved");

                }

                //Setting the lore
                ItemMeta upgradeMeta = upgradeHome.getItemMeta();
                upgradeMeta.setLore(lore);
                upgradeHome.setItemMeta(upgradeMeta);

                houseInventory.setItem(4, upgradeHome);
            }
            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.rent")) {
                ItemStack rentHouse = properties.getIncreaseRent().clone();
                ArrayList<String> lore = (ArrayList<String>) rentHouse.getItemMeta().getLore();

                //Work out time left on house
                Integer[] expiryDate = Math.calculateTimeLeft(instance.getPlayerManager().getHousePlayerMap().get(p).getMillisecondsOfExpiry());

                lore.add(ChatColor.translateAlternateColorCodes('&', "&6" + (expiryDate[0] == 0 ? "" : (expiryDate[0] == 1 ? expiryDate[0] + " &aDay,&6 " : expiryDate[0] + " &aDays,&6 ")) +
                        (expiryDate[1] == 1 ? expiryDate[1] + " &aHour,&6 " : expiryDate[1] + " &aHours,&6 ") +
                        (expiryDate[2] == 1 ? expiryDate[2] + " &aMinute " : expiryDate[2] + " &aMinutes ") +
                        (expiryDate[3] != 0 ? "&aand &6": "&6") +
                        (expiryDate[3] == 0 ? "" : (expiryDate[3] == 1 ? expiryDate[3] + " &aSecond " : expiryDate[3] + " &aSeconds "))
                ));
                lore.add(" ");
                lore.add(ChatColor.GOLD + "Alias: /house rent");
                ItemMeta rentMeta = rentHouse.getItemMeta();
                rentMeta.setLore(lore);
                rentHouse.setItemMeta(rentMeta);
                houseInventory.setItem(5,rentHouse);

            }

            if (properties.isShowGUIItemsWithoutPermission() || p.hasPermission("house.lock")) {
                if (isHouseLocked()) {
                    houseInventory.setItem(6, properties.getLockedHome());
                } else {
                    houseInventory.setItem(6, properties.getUnlockedHome());
                }
            }

            houseInventory.setItem(8, properties.getCloseInventory());

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
