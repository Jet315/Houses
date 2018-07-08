package me.jet315.houses.utils.files;

import me.jet315.houses.Core;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created by Jet on 28/01/2018.
 */
public abstract class Properties extends DataFiles{

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
     * Stores the commands to block
     */
    private List<String> commandsToBlock;

    /**
     * The Economy system that is being used - VAULT or TOKENS accepted
     */
    private String economyTypeToUpgrade = "TOKENS";
    private String housePriceAlgorithm = "{CURRENTHOUSELEVEL} * 500";
    private long firstHousePrice = 500;

    private boolean shouldHousesExpire = true;
    private String economyTypeForRenting = "VAULT";
    private int costToRentPerDay = 5000;
    private int maxAmountOfRentTime = 672;
    private int givenRentTime = 7;

    private boolean deleteExpiredHousesOnStartup = true;

    private int timeToWaitWhenTeleporting =  0;
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


    public Properties(Core instance){
        super(instance);
        setupValues();

    }


    private void setupValues(){

        /**
         * Settings
         */
        pluginPrefix = ChatColor.translateAlternateColorCodes('&',super.getConfig().getString("PluginsPrefix"));
        locale = super.getConfig().getString("Locale");
        showGUIItemsWithoutPermission = !super.getConfig().getBoolean("HideGUIItemsIfNoPermission");

        getMaxBuildHeight = super.getConfig().getInt("MaxBuildHeight");
        plotsWorldName = super.getConfig().getString("PlotWorldName");
        maxHouseLevel = super.getConfig().getInt("MaxHouseLevel");
        chestSearchLevel = super.getConfig().getInt("ChestSearchLevel");
        allowSignsInHouse = super.getConfig().getBoolean("AllowSignsInHouse");
        houseConfirmationMessages = super.getConfig().getBoolean("HouseConfirmation");
        /**
         * Table name
         */
        sqliteTable = super.getConfig().getString("SQLiteTable");
        /**

        /**
         * Economy
         */
        economyTypeToUpgrade = super.getConfig().getString("EconomyTypeToUpgrade");
        housePriceAlgorithm = super.getConfig().getString("NextHousePrice");
        firstHousePrice = super.getConfig().getLong("FirstHousePrice");

        shouldHousesExpire = super.getConfig().getBoolean("ShouldHousesExpire");
        economyTypeForRenting = super.getConfig().getString("EconomyTypeToRent");
        costToRentPerDay = super.getConfig().getInt("CostToRentPerDay");
        maxAmountOfRentTime = super.getConfig().getInt("MaxAmountOfRentTime");
        givenRentTime = super.getConfig().getInt("GivenRentTime");

        deleteExpiredHousesOnStartup = super.getConfig().getBoolean("DeleteExpiredHousesOnStartup");

        /**
         * Commands to block
         */
        commandsToBlock = super.getConfig().getStringList("CommandsToBlock");

        timeToWaitWhenTeleporting = super.getConfig().getInt("TimeToWaitWhenTeleporting");
        /**
         * Schematic values
         */
        moveSchematicXDirection = super.getConfig().getInt("MoveSchematicXDirection");
        moveSchematicYDirection = super.getConfig().getInt("MoveSchematicYDirection");
        moveSchematicZDirection = super.getConfig().getInt("MoveSchematicZDirection");
        shouldPasteFirstHouseOnClaim = super.getConfig().getBoolean("PasteFirstHouseOnClaim");

        schematicToPasteOnExpiry = super.getConfig().getString("SchematicOnExpirey");
        moveExpirySchematicXDirection = super.getConfig().getInt("MoveExpirySchematicXDirection");
        moveExpirySchematicYDirection = super.getConfig().getInt("MoveExpirySchematicYDirection");
        moveExpirySchematicZDirection = super.getConfig().getInt("MoveExpirySchematicZDirection");


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

    public long getFirstHousePrice() {
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

    public int gettimeToWaitWhenTeleporting() {
        return timeToWaitWhenTeleporting;
    }
}
