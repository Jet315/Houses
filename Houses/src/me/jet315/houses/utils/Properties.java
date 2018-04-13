package me.jet315.houses.utils;

import me.jet315.houses.Core;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * Created by Jet on 28/01/2018.
 */
public abstract class Properties {

    /**
     * Stores the prefix name
     */
    private String pluginPrefix = "&6&lHOUSES &e&l> ";

    /**
     * Stores the Locale being used
     */
    private String locale = "en-US";
    /**
     * Also stores whether GUI items should be shown if the user does not have permission
     */
    private boolean showGUIItemsWithoutPermission = false;

    /**
     * Stores the maximum height a player is allowed to build at.
     * This prevents them from being able to build in there house
     */
    private int getMaxBuildHeight = 63;

    /**
     * Whether plays can place/break signs in the plot
     */
    private boolean allowSignsInHouse = true;

    /**
     * Whether to show confirmation messages
     */
    private boolean houseConfirmationMessages = true;

    /**
     * Max house level allowed
     */
    private int maxHouseLevel = 6;

    /**
     * The highest height, from the ground, that we should search for chests
     */
    private int chestSearchLevel = 30;

    /**
     * Stores the world name that the plots are in
     */
    private String plotsWorldName = "plots";
    /**
     * Stores the name of the SQLite table
     */
    private String sqliteTable = "Houses";

    /**
     * Stores the configuration object
     */
    private FileConfiguration config;

    /**
     * Stores the commands to block
     */
    private List<String> commandsToBlock;

    /**
     * The Economy system that is being used - VAULT or TOKENS accepted
     */
    private String economyTypeToUpgrade = "TOKENS";
    private String housePriceAlgorithm = "{CURRENTHOUSELEVEL} * 500";
    private int firstHousePrice = 500;

    private boolean shouldHousesExpire = true;
    private String economyTypeForRenting = "VAULT";
    private int costToRentPerDay = 5000;
    private int maxAmountOfRentTime = 672;
    private int givenRentTime = 7;

    private boolean deleteExpiredHousesOnStartup = true;


    /**
     * Stores imformation about the schematics
     */
    private int moveSchematicXDirection = 0;
    private int moveSchematicYDirection = -2;
    private int moveSchematicZDirection = 0;
    private boolean shouldPasteFirstHouseOnClaim = true;


    private String schematicToPasteOnExpiry = "none";
    private int moveExpirySchematicXDirection = 0;
    private int moveExpirySchematicYDirection = 2;
    private int moveExpirySchematicZDirection = 0;


    public Properties(FileConfiguration config){
        this.config = config;
    }


    public void reloadConfig(){
        createConfig();

        /**
         * Settings
         */
        pluginPrefix = ChatColor.translateAlternateColorCodes('&',config.getString("PluginsPrefix"));
        locale = config.getString("Locale");
        showGUIItemsWithoutPermission = config.getBoolean("HideGUIItemsIfNoPermission");

        getMaxBuildHeight = config.getInt("MaxBuildHeight");
        plotsWorldName = config.getString("PlotWorldName");
        maxHouseLevel = config.getInt("MaxHouseLevel");
        chestSearchLevel = config.getInt("ChestSearchLevel");
        allowSignsInHouse = config.getBoolean("AllowSignsInHouse");
        houseConfirmationMessages = config.getBoolean("HouseConfirmation");
        /**
         * Table name
         */
        sqliteTable = config.getString("SQLiteTable");
        /**

        /**
         * Economy
         */
        economyTypeToUpgrade = config.getString("EconomyTypeToUpgrade");
        housePriceAlgorithm = config.getString("NextHousePrice");
        firstHousePrice = config.getInt("FirstHousePrice");

        shouldHousesExpire = config.getBoolean("ShouldHousesExpire");
        economyTypeForRenting = config.getString("EconomyTypeToRent");
        costToRentPerDay = config.getInt("CostToRentPerDay");
        maxAmountOfRentTime = config.getInt("MaxAmountOfRentTime");
        givenRentTime = config.getInt("GivenRentTime");

        deleteExpiredHousesOnStartup = config.getBoolean("DeleteExpiredHousesOnStartup");

        /**
         * Commands to block
         */
        commandsToBlock = config.getStringList("CommandsToBlock");

        /**
         * Schematic values
         */
        moveSchematicXDirection = config.getInt("MoveSchematicXDirection");
        moveSchematicYDirection = config.getInt("MoveSchematicYDirection");
        moveSchematicZDirection = config.getInt("MoveSchematicZDirection");
        shouldPasteFirstHouseOnClaim = config.getBoolean("PasteFirstHouseOnClaim");

        schematicToPasteOnExpiry = config.getString("SchematicOnExpirey");
        moveExpirySchematicXDirection = config.getInt("MoveExpirySchematicXDirection");
        moveExpirySchematicYDirection = config.getInt("MoveExpirySchematicYDirection");
        moveExpirySchematicZDirection = config.getInt("MoveExpirySchematicZDirection");

    }

    private void createConfig() {
        try {
            if (!Core.getInstance().getDataFolder().exists()) {
                Core.getInstance().getDataFolder().mkdirs();
            }
            File file = new File(Core.getInstance().getDataFolder(), "config.yml");
            if (!file.exists()) {
                Core.getInstance().getLogger().info("Config.yml not found, creating!");
                Core.getInstance().saveDefaultConfig();
            } else {
                Core.getInstance().getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    /**
     *Getters for field values
     */

    /**
     * @return Plugins Prefix
     */
    public String getPluginPrefix() {
        return pluginPrefix;
    }
    /**
     * @return Max Build Height of the Plot World
     */
    public int getGetMaxBuildHeight() {
        return getMaxBuildHeight;
    }
    /**
     * @return The plot world name
     */
    public String getPlotsWorldName() {
        return plotsWorldName;
    }

    public String getSqliteTable() {
        return sqliteTable;
    }


    public int getMoveSchematicXDirection() {
        return moveSchematicXDirection;
    }

    public int getMoveSchematicYDirection() {
        return moveSchematicYDirection;
    }

    public int getMoveSchematicZDirection() {
        return moveSchematicZDirection;
    }

    public boolean isShouldPasteFirstHouseOnClaim() {
        return shouldPasteFirstHouseOnClaim;
    }

    public String getEconomyTypeToUpgrade() {
        return economyTypeToUpgrade;
    }

    public String getHousePriceAlgorithm() {
        return housePriceAlgorithm;
    }

    public boolean isShowGUIItemsWithoutPermission() {
        return showGUIItemsWithoutPermission;
    }

    public boolean getDeleteExpiredHousesOnStartup() {
        return deleteExpiredHousesOnStartup;
    }

    public boolean isShouldHousesExpire() {
        return shouldHousesExpire;
    }

    public String getEconomyTypeForRenting() {
        return economyTypeForRenting;
    }

    public int getCostToRentPerDay() {
        return costToRentPerDay;
    }

    public int getMaxAmountOfRentTime() {
        return maxAmountOfRentTime;
    }

    public int getGivenRentTime() {
        return givenRentTime;
    }

    public int getFirstHousePrice() {
        return firstHousePrice;
    }

    public int getMaxHouseLevel() {
        return maxHouseLevel;
    }

    public int getChestSearchLevel() {
        return chestSearchLevel;
    }

    public String getLocale() {
        return locale;
    }

    public boolean isAllowSignsInHouse() {
        return allowSignsInHouse;
    }

    public boolean isHouseConfirmationMessages() {
        return houseConfirmationMessages;
    }

    public List<String> getCommandsToBlock() {
        return commandsToBlock;
    }

    public String getSchematicToPasteonExpiry() {
        return schematicToPasteOnExpiry;
    }

    public int getMoveExpirySchematicXDirection() {
        return moveExpirySchematicXDirection;
    }

    public int getMoveExpirySchematicYDirection() {
        return moveExpirySchematicYDirection;
    }

    public int getMoveExpirySchematicZDirection() {
        return moveExpirySchematicZDirection;
    }
}
