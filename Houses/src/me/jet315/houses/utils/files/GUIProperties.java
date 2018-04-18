package me.jet315.houses.utils.files;

import me.jet315.houses.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jet on 07/02/2018.
 */
public class GUIProperties extends Properties{

    /**
     *Stores hashmap of String name followed by HouseItem for the NoHouses menu
     */
    private HashMap<String,HouseItem> itemsInNoHouseGUI = new HashMap<>();

    /**
     * Stores hashmap of String name followed by GUI Item for the Houses menu
     */
    private HashMap<String,HouseItem> itemsInHouseGUI = new HashMap<>();
    /**
     * Stores hashmap of String followed by HouseItem for the Rent menu
     */
    private HashMap<String,RentItem> itemsInRentGUI = new HashMap<>();

    /**
     * Stores GUI names
     */
    private String noHouseGUIName;
    private String houseGUIName;
    private String rentGUIName;
    /**
     * Stores sizes of GUIs
     */
    private int noHouseGUISlots;
    private int houseGUISlots;
    private int rentGUISlots;

    /**
     * Stores the inventory for increasing rent for houses
     */
    private Inventory increaseRentInventory;


    public GUIProperties(Core instance) {
        super(instance);
        setupValues();
    }

    public void setupValues(){
        //initialiseValues();
        loadGUIConfig();
        setupRentInventory();
    }
    private void loadGUIConfig() {

        /**
         * Load display names
         */
        noHouseGUIName = ChatColor.translateAlternateColorCodes('&',super.getGuiConfig().getString("NoHouseGUI.GUIName"));
        houseGUIName = ChatColor.translateAlternateColorCodes('&',super.getGuiConfig().getString("HouseGUI.GUIName"));
        noHouseGUISlots = super.getGuiConfig().getInt("NoHouseGUI.slots");
        houseGUISlots = super.getGuiConfig().getInt("NoHouseGUI.slots");

        //TODO refactor duplicated code

        /**
         * No House GUI
         */
        for (String itemName : super.getGuiConfig().getConfigurationSection("NoHouseGUI.Items").getKeys(false)) {
            try {
                String path = "NoHouseGUI.Items." + itemName;

                Material material = Material.valueOf(super.getGuiConfig().getString(path + ".type"));

                if (material != null) {

                    ItemStack item = new ItemStack(material, 1, (short) super.getGuiConfig().getInt(path + ".data"));

                    String displayName = ChatColor.translateAlternateColorCodes('&', super.getGuiConfig().getString(path + ".name"));

                    int slotID = super.getGuiConfig().getInt(path + ".slot");

                    List<String> lore = super.getGuiConfig().getStringList(path+".lore");

                    List<String> formatedLore = new ArrayList<>();
                    if(lore != null && lore.size() > 0){

                        for(String loreLine : lore){
                            formatedLore.add(ChatColor.translateAlternateColorCodes('&',loreLine));
                        }
                    }
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(displayName);
                    itemMeta.setLore(formatedLore);
                    item.setItemMeta(itemMeta);
                    HouseItem houseItem = new HouseItem(itemName,item,slotID, super.getGuiConfig().getString(path+".commandToPerform"));
                    itemsInNoHouseGUI.put(itemName, houseItem);
                } else {
                    System.out.println("An error has occured while loading an item:");
                    System.out.println("File: guiconfig.yml, (the material type is invalid for the item " + itemName + ")");
                    continue;
                }

            }catch(Exception e){
                System.out.println("An error has occured while loading an item:");
                System.out.println("File: guiconfig.yml, on item: " + itemName);
                System.out.println("Details about the error: " + e);
            }
        }
        /**
         * House GUI
         */
        for (String itemName : super.getGuiConfig().getConfigurationSection("HouseGUI.Items").getKeys(false)) {
           try {
                String path = "HouseGUI.Items." + itemName;
                Material material = Material.valueOf(super.getGuiConfig().getString(path + ".type"));
                if (material != null) {
                    ItemStack item = new ItemStack(material, 1, (short) super.getGuiConfig().getInt(path + ".data"));
                    String displayName = ChatColor.translateAlternateColorCodes('&', super.getGuiConfig().getString(path + ".name"));
                    int slotID = super.getGuiConfig().getInt(path + ".slot");
                    List<String> lore = super.getGuiConfig().getStringList(path+".lore");
                    List<String> formatedLore = new ArrayList<>();
                    if(lore != null && lore.size() > 0){
                        for(String loreLine : lore){
                            formatedLore.add(ChatColor.translateAlternateColorCodes('&',loreLine));
                        }
                    }
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(displayName);
                    itemMeta.setLore(formatedLore);
                    item.setItemMeta(itemMeta);
                    HouseItem houseItem = new HouseItem(itemName,item,slotID, super.getGuiConfig().getString(path+".commandToPerform"));
                    itemsInHouseGUI.put(itemName, houseItem);
                } else {
                    System.out.println("An error has occured while loading an item:");
                    System.out.println("File: guiconfig.yml, (the material type is invalid for the item " + itemName + ")");
                    continue;
                }

            }catch(Exception e){
                System.out.println("An error has occured while loading an item:");
                System.out.println("File: guiconfig.yml, on item: " + itemName);
            }
        }

    }

    private void setupRentInventory(){
        rentGUIName = ChatColor.translateAlternateColorCodes('&',super.getGuiConfig().getString("RentGUI.GUIName"));
        rentGUISlots = super.getGuiConfig().getInt("RentGUI.slots");

        increaseRentInventory = Bukkit.createInventory(null,rentGUISlots,ChatColor.GRAY + "Extending Rental");

        int pricePerDay = super.getCostToRentPerDay();

        for (String itemName : super.getGuiConfig().getConfigurationSection("RentGUI.Items").getKeys(false)) {
            try {
                String path = "RentGUI.Items." + itemName;
                Material material = Material.valueOf(super.getGuiConfig().getString(path + ".type"));
                if (material != null) {
                    ItemStack item = new ItemStack(material, 1, (short) super.getGuiConfig().getInt(path + ".data"));
                    String displayName = ChatColor.translateAlternateColorCodes('&', super.getGuiConfig().getString(path + ".name"));
                    int slotID = super.getGuiConfig().getInt(path + ".slot");
                    int daysToRent = super.getGuiConfig().getInt(path + ".daysToAddToRent");
                    List<String> lore = super.getGuiConfig().getStringList(path+".lore");
                    List<String> formatedLore = new ArrayList<>();
                    if(lore != null && lore.size() > 0){
                        for(String loreLine : lore){
                            formatedLore.add(ChatColor.translateAlternateColorCodes('&',loreLine).replaceAll("%COST%",String.valueOf(pricePerDay*daysToRent)));
                        }
                    }
                    ItemMeta itemMeta = item.getItemMeta();
                    itemMeta.setDisplayName(displayName);
                    itemMeta.setLore(formatedLore);
                    item.setItemMeta(itemMeta);
                    RentItem rentItem = new RentItem(itemName,item,slotID,daysToRent);
                    itemsInRentGUI.put(itemName, rentItem);
                } else {
                    System.out.println("An error has occured while loading an item:");
                    System.out.println("File: guiconfig.yml, (the material type is invalid for the item " + itemName + ")");
                    continue;
                }

            }catch(Exception e){
                System.out.println("An error has occured while loading an item:");
                System.out.println("File: guiconfig.yml, on item: " + itemName);
            }
        }
        for(RentItem item : itemsInRentGUI.values()){
            increaseRentInventory.setItem(item.getSlotID(),item.getItem());
        }

    }

    public HashMap<String, HouseItem> getItemsInNoHouseGUI() {
        return itemsInNoHouseGUI;
    }

    public HashMap<String, HouseItem> getItemsInHouseGUI() {
        return itemsInHouseGUI;
    }

    public String getNoHouseGUIName() {
        return noHouseGUIName;
    }

    public String getHouseGUIName() {
        return houseGUIName;
    }

/*    public ItemStack getTeleportToHouseWorldItem() {
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
    }*/


    public Inventory getIncreaseRentInventory() {
        return increaseRentInventory;
    }

    public String getRentGUIName() {
        return rentGUIName;
    }

    public int getNoHouseGUISlots() {
        return noHouseGUISlots;
    }

    public int getHouseGUISlots() {
        return houseGUISlots;
    }

    public int getRentGUISlots() {
        return rentGUISlots;
    }

    public HashMap<String, RentItem> getItemsInRentGUI() {
        return itemsInRentGUI;
    }
}
