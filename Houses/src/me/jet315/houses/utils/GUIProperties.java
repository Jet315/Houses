package me.jet315.houses.utils;

import com.intellectualcrafters.plot.commands.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Jet on 07/02/2018.
 */
public class GUIProperties extends Properties{

    /**
     * Stores the items in the Houses GUI
     */

    private ItemStack teleportToHouseWorldItem; //Slot 0

    private ItemStack teleportToHome; //Slot 1
    private ItemStack purchaseHome; //Slot 1 (Loaded if user does not have home)

    private ItemStack findHome; //slot 2

    //Loaded if users have a home
    private ItemStack trustUser; //slot 3

    private ItemStack upgradeHome; //slot 4

    private ItemStack lockedHome; //slot 5
    private ItemStack unlockedHome; //slot 5

    private ItemStack increaseRent; //slots 6

    private ItemStack closeInventory; //Slot 8

    /**
     * Stores the inventory for increasing rent for houses
     */
    private Inventory increaseRentInventory;


    public GUIProperties(FileConfiguration config) {
        super(config);
        reloadConfig();
    }

    public void reloadConfig(){
        super.reloadConfig();
        initialiseValues();
        setupRentInventory();
    }

    public void initialiseValues(){
        ArrayList<String> lore = new ArrayList<>();

        teleportToHouseWorldItem = new ItemStack(Material.GRASS,1);
        teleportToHome = new ItemStack(Material.WOOD_DOOR,1);
        purchaseHome = new ItemStack(Material.WOOD_DOOR,1);
        findHome = new ItemStack(Material.COMPASS,1);
        trustUser = new ItemStack(Material.PAPER,1);
        upgradeHome = new ItemStack(Material.EMERALD,1);
        lockedHome = new ItemStack(Material.IRON_DOOR,1);
        unlockedHome = new ItemStack(Material.IRON_DOOR,1);
        increaseRent = new ItemStack(Material.WATCH,1);
        closeInventory = new ItemStack(Material.BARRIER,1);
        /**
         * Teleport To House World Meta
         */
        ItemMeta teleportToHouseWorldMeta = teleportToHouseWorldItem.getItemMeta();
        teleportToHouseWorldMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "House World");
        lore.add(ChatColor.GRAY + "Click to Teleport");
        lore.add(ChatColor.GRAY + "to the House World!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house world");
        teleportToHouseWorldMeta.setLore(lore);
        teleportToHouseWorldItem.setItemMeta(teleportToHouseWorldMeta);
        lore.clear();

        /**
         * Teleport To House Meta
         */
        ItemMeta teleportToHomeMeta = teleportToHome.getItemMeta();
        teleportToHomeMeta.setDisplayName(ChatColor.GREEN+ "" + ChatColor.BOLD  + "Teleport to Home");
        lore.add(ChatColor.GRAY + "Click to Teleport");
        lore.add(ChatColor.GRAY + "to your House!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house tp");
        teleportToHomeMeta.setLore(lore);
        teleportToHome.setItemMeta(teleportToHomeMeta);
        lore.clear();

        /**
         * Purchase Home Meta
         */
        ItemMeta purchaseHomeMeta = purchaseHome.getItemMeta();
        purchaseHomeMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Purchase a House");
        lore.add(ChatColor.GRAY + "Purchase a House!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GRAY + "This will cost you");
        if(super.getEconomyTypeToUpgrade().equalsIgnoreCase("vault")){
            lore.add(ChatColor.YELLOW +"$" + super.getFirstHousePrice());
        }else if(super.getEconomyTypeToUpgrade().equalsIgnoreCase("tokens")){
            lore.add(ChatColor.YELLOW + "" + super.getFirstHousePrice() + " Tokens!");
        }else if(super.getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant")){
            lore.add(ChatColor.YELLOW + "" + super.getFirstHousePrice() + " Tokens!");
        }else{
            System.out.println("[Houses] A invalid Economy Upgrade setting has been set: " + super.getEconomyTypeToUpgrade() + " is not a valid type");
            lore.add(ChatColor.RED +"Error - Contact Staff member to check console");
        }
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house purchase");
        purchaseHomeMeta.setLore(lore);
        purchaseHome.setItemMeta(purchaseHomeMeta);
        lore.clear();

        /**
         * Find home meta
         */
        ItemMeta findHomeMeta = findHome.getItemMeta();
        findHomeMeta.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "Find a Players House");
        lore.add(ChatColor.GRAY + "Click to find a");
        lore.add(ChatColor.GRAY + "players House!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house find <player>");
        findHomeMeta.setLore(lore);
        findHome.setItemMeta(findHomeMeta);
        lore.clear();

        /**
         * Find home meta
         */
        ItemMeta trustUserMeta = trustUser.getItemMeta();
        trustUserMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Trust a Player");
        lore.add(ChatColor.GRAY + "Allows a Player To");
        lore.add(ChatColor.GRAY + "Build in your basement!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house trust <player>");
        trustUserMeta.setLore(lore);
        trustUser.setItemMeta(trustUserMeta);
        lore.clear();
        /**
         * Upgrade Home Meta
         */
        ItemMeta upgradeHomeMeta = upgradeHome.getItemMeta();
        upgradeHomeMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Upgrade your House");
        lore.add(ChatColor.GRAY + "Click to upgrade your");
        lore.add(ChatColor.GRAY + "House!");
        lore.add(ChatColor.GRAY + " ");
        upgradeHomeMeta.setLore(lore);
        upgradeHome.setItemMeta(upgradeHomeMeta);
        lore.clear();

        /**
         * Locked Home Meta
         */
        ItemMeta lockedHomeMeta = lockedHome.getItemMeta();
        lockedHomeMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Status: LOCKED");
        lore.add(ChatColor.GRAY + "Your house is currently Locked!");
        lore.add(ChatColor.GRAY + "Click to unlock!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house unlock");
        lockedHomeMeta.setLore(lore);
        lockedHome.setItemMeta(lockedHomeMeta);
        lore.clear();

        /**
         * unLock Home Meta
         */
        ItemMeta unlockedHomeMeta = unlockedHome.getItemMeta();
        unlockedHomeMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Status: UNLOCKED");
        lore.add(ChatColor.GRAY + "Your house is currently Locked!");
        lore.add(ChatColor.GRAY + "Click to unlock!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.GOLD + "Alias: /house unlock");
        unlockedHomeMeta.setLore(lore);
        unlockedHome.setItemMeta(unlockedHomeMeta);
        lore.clear();

        /**
         * Increase Rent Time Meta
         */
        ItemMeta increaseRentMeta = increaseRent.getItemMeta();
        increaseRentMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Extend Rental");
        lore.add(ChatColor.GRAY + "Click to add rental");
        lore.add(ChatColor.GRAY + "days to your house!");
        lore.add(ChatColor.GRAY + " ");
        lore.add(ChatColor.RED + "Your house will expire in:");
        increaseRentMeta.setLore(lore);
        increaseRent.setItemMeta(increaseRentMeta);
        lore.clear();


        /**
         * Close Inventory
         */
        ItemMeta closeInv = closeInventory.getItemMeta();
        closeInv.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close Menu");
        closeInventory.setItemMeta(closeInv);
    }

    public void setupRentInventory(){
        increaseRentInventory = Bukkit.createInventory(null,18,ChatColor.GRAY + "Extending Rental");
        int pricePerDay = super.getCostToRentPerDay();
        ArrayList<String> lore = new ArrayList<>();
        for(int i = 1; i<=9; i++){
            ItemStack watch = new ItemStack(Material.WATCH);
            ItemMeta watchItemMeta = watch.getItemMeta();
            watchItemMeta.setDisplayName(ChatColor.GREEN +""+ ChatColor.BOLD + i + (i == 1 ? " Day":" Days"));
            lore.add(ChatColor.GRAY +"Increase rent by " + i + (i == 1 ? " Day":" Days"));
            lore.add(ChatColor.GRAY +"Cost:");
            lore.add(ChatColor.YELLOW +(super.getEconomyTypeForRenting().equalsIgnoreCase("vault") ? "$": "")+pricePerDay*i + (super.getEconomyTypeForRenting().equalsIgnoreCase("vault") ? "": " Tokens"));
            lore.add(ChatColor.GRAY +" ");
            lore.add(ChatColor.GRAY +"Click to purchase!");
            watchItemMeta.setLore(lore);
            watch.setItemMeta(watchItemMeta);
            lore.clear();
            increaseRentInventory.setItem(i-1,watch);
        }
        increaseRentInventory.setItem(17,closeInventory);
    }

    public ItemStack getTeleportToHouseWorldItem() {
        return teleportToHouseWorldItem;
    }

    public ItemStack getTeleportToHome() {
        return teleportToHome;
    }

    public ItemStack getPurchaseHome() {
        return purchaseHome;
    }

    public ItemStack getFindHome() {
        return findHome;
    }
    public ItemStack getTrustUser() {
        return trustUser;
    }

    public ItemStack getUpgradeHome() {
        return upgradeHome;
    }


    public ItemStack getCloseInventory() {
        return closeInventory;
    }

    public ItemStack getLockedHome() {
        return lockedHome;
    }

    public ItemStack getUnlockedHome() {
        return unlockedHome;
    }
    public ItemStack getIncreaseRent() {
        return increaseRent;
    }


    public Inventory getIncreaseRentInventory() {
        return increaseRentInventory;
    }
}
