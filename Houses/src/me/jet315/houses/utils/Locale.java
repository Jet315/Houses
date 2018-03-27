package me.jet315.houses.utils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import me.jet315.houses.Core;
import org.apache.commons.io.FileUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by Jet on 28/02/2018.
 */
public class Locale {

    // Localization Strings
    private FileConfiguration locale = null;
    private File localeFile = null;
    private Core plugin;
    //Stores the Locale as a string - en-US as default
    private String localeName = "en-US";

    /**
     * Messages
     */

    /**
     * Stores the main messages for the plugin
     */
    private String noPermissionMessage = "You do not have permission for this command";
    private String houseExpireMessage = "Your house will expire in 10 Minutes /house rent";
    private String houseExpired = "House gone!";
    private String blockBrokenInHouse = "No breaking blocks here!";
    private String signNotOnChest = "Signs need to be placed on chests!";
    private String housePurchaseConfirmation = "Type 'confirm' to purchase this house, or 'cancel' to cancel";
    private String houseUpgradeConfirmation = "Type 'confirm' to upgrade this house, or 'cancel' to cancel";
    private String houseTrustMessage = "Type the players name of whom you wish to trust";
    private String houseFindMessage = "Type the players name of whom you wish to find";
    private String houseRentalLimit = "You are not allowed to extend rent any further! you have reached the max!";
    //Having both of these is kind of redundant, as the user could set the message depending on if he/she was using tokens/economy. It means the plugin works out the box though
    private String noFundsEconomyRental = "You do not have the $%RENT% needed for this!";
    private String noFundsTokensRental = "You do not have the %RENT% tokens needed for this!";

    private String commandCanceled = "Command Canceled!";
    private String nonExistentPlotCommand = "This command does not exist";

    /**
     * Stores the sub title messages for when a user enters a denied plot
     */
    private String deniedTitle = "Houses";
    private String deniedSubTitle ="This House is Locked!";
    private String houseUpgrading = "This house is upgrading";
    /**
     * House Commands
     */
    private String houseUpgradeNoHome = "You do not have a house to upgrade!";

    private String houseUpgradeMaxHouse = "You have the max house level already!";
    private String houseUpgradeInProcess = "Please wait for your current house to finish upgrading!";
    private String houseUpgradeNotEnoughMoney = "You do not have $%PRICE% to upgrade this house!";
    private String houseUpgradeNotEnoughTokens = "You do not have %PRICE% Tokens to upgrade this house!";

    private String houseUntrustNoHome = "You do not have a house to untrust people to!";
    private String houseUntrustYourself = "You cannot untrust yourself to your own house!";
    private String houseUntrustNeverPlayedBefore = "&cThe player '%PLAYER%' has never played before!";
    private String houseUntrustNotTrusted = "This player is not currently trusted";
    private String houseUntrustSuccess = "This player is no longer trusted!";
    private String houseUntrustError = "Error occurred while processing player, please try again later";

    private String houseTrustNoHome = "You do not have a house to trust people to!";
    private String houseTrustYourself = "You cannot Trust yourself to your own house!";
    private String houseTrustNotOnline = "The player '%PLAYER%' is not online!";
    private String houseTrustAlreadyTrusted =  "This player is already trusted! &aType /house untrust %PLAYER% to untrust the player.";
    private String houseTrustSuccess = "This player is now trusted on your home!";

    private String houseFindNoPlayer = "A house for player '%PLAYER%' cannot be found!";
    private String houseFindTP = "Teleported!";

    private String houseWorldTPAlreadyInWorld = "You are already in the House World!";
    private String houseWorldTP = "Teleporting!";

    private String playerHasNoHouse =  "You do not have a house!";

    private String houseRentalNoHouse = "You do not have a house to increase rent on!";
    private String houseRentExtended = "Increased the rent!";

    private String housePurchaseAlreadyHasHouse = "You already have a house!";
    private String housePurchaseNotEnoughMoney = "You do not have $%PRICE% to purchase a house!";
    private String housePurchaseNotEnoughTokens = "You do not have %PRICE% Tokens to purchase a house!";
    private String housePurchaseFail = "An error occurred while claiming a house. If you have not been automatically refunded, contact support.";

    private String houseLocked = "Locked House!";
    private String houseLockedMessageToVisitors = "This house has been locked!";
    private String houseUnlocked = "Unlocked House!";
    private String houseLockingTitle = "HOUSES";
    private String houseLockedSubTitle = "House locked!";
    private String houseUnLockedSubTitle = "House unlocked!";
    private String houseLockedAndNoHouseFound = "No House Found!";

    /**
     * Stores the GUI Messages
     */
    private String guiTitle = "HOUSE >";




    //Constructor
    public Locale(Core instance, String localeName){
        this.plugin = instance;
        this.localeName = localeName;
        reloadLocale(localeName);
        loadLocale();

    }


    /**
     * Reloads the locale file
     */
    public void reloadLocale(String localeName) {
        // Make directory if it doesn't exist
        File localeDir = new File(plugin.getDataFolder() + File.separator + "locale");

        if (!localeDir.exists()) {
            localeDir.mkdir();
        }

        if (localeFile == null) {
            InputStream in = plugin.getResource("locale/"+localeName +".yml");

            localeFile = new File(localeDir.getPath(), localeName + ".yml");
            File outFile = new File(localeDir, localeName +".yml");
            try {
                if (!outFile.exists()) {
                    OutputStream out = new FileOutputStream(outFile);
                    byte[] b = new byte[1024];
                    int read;
                    while ((read = in.read(b)) > 0) {
                        out.write(b, 0, read);
                    }
                    out.close();
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("Failed to create File " + localeName +" (Incorrect Locale specified?) - This will cause an error.");
                localeFile.delete();
            }catch (NullPointerException e){
                System.out.println("Failed to create File " + localeName +" (Incorrect Locale specified?) - This will cause an error.");
                localeFile.delete();
            }
        }

        if (localeFile.exists()) {
            //File may have created, but may not contain anything
            if(localeFile.length() == 0){
                System.out.println("Failed to create File " + localeName +" (Incorrect Locale specified?) - This will cause an error.");
                localeFile.delete();
            }else {

                locale = YamlConfiguration.loadConfiguration(localeFile);
            }



        } else {
            // Look for defaults in the jar
            if (plugin.getResource("locale/" + localeName + ".yml") != null) {
                plugin.saveResource("locale/" + localeName + ".yml", true);
                localeFile = new File(plugin.getDataFolder() + File.separator + "locale", localeName + ".yml");
                locale = YamlConfiguration.loadConfiguration(localeFile);
            } else {
                System.out.println("Failed to create File " + localeName +" (Incorrect Locale specified? The file must exist within the /locale file) - This will cause errors :(.");
            }
        }
    }
    /**
     * Loads the properties in the locale file to variables
     */
    public void loadLocale(){
        noPermissionMessage = ChatColor.translateAlternateColorCodes('&',locale.getString("NoPermission"));
        houseExpireMessage = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseRemainingTime"));
        houseExpired = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseExpired"));
        blockBrokenInHouse = ChatColor.translateAlternateColorCodes('&',locale.getString("BlockBrokenInHouse"));
        signNotOnChest = ChatColor.translateAlternateColorCodes('&',locale.getString("SignNotOnChest"));
        housePurchaseConfirmation = ChatColor.translateAlternateColorCodes('&',locale.getString("HousePurchaseConfirmation"));
        houseUpgradeConfirmation = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeConfirmation"));
        houseTrustMessage = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustMessage"));
        houseFindMessage = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseFindMessage"));
        houseRentalLimit = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseRentalLimit"));
        noFundsEconomyRental = ChatColor.translateAlternateColorCodes('&',locale.getString("NoFundsEconomyRental"));
        noFundsTokensRental = ChatColor.translateAlternateColorCodes('&',locale.getString("NoFundsTokensRental"));
        commandCanceled = ChatColor.translateAlternateColorCodes('&',locale.getString("CommandCanceled"));
        nonExistentPlotCommand = ChatColor.translateAlternateColorCodes('&',locale.getString("NonExistentPlotCommand"));

        deniedTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("DeniedTitle"));
        deniedSubTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("DeniedSubTitle"));
        houseUpgrading = ChatColor.translateAlternateColorCodes('&',locale.getString("DeniedHouseUpgrading"));

        guiTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("GUITitle"));

        houseUpgradeNoHome = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeNoHome"));
        houseUpgradeMaxHouse = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeMaxHouse"));
        houseUpgradeInProcess = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeInProcess"));
        houseUpgradeNotEnoughMoney = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeNotEnoughMoney"));
        houseUpgradeNotEnoughTokens = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUpgradeNotEnoughTokens"));

        houseUntrustNoHome = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustNoHome"));
        houseUntrustYourself = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustYourself"));
        houseUntrustNeverPlayedBefore = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustNeverPlayedBefore"));
        houseUntrustNotTrusted = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustNotTrusted"));
        houseUntrustSuccess = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustSuccess"));
        houseUntrustError = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUntrustError"));

        houseTrustNoHome = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustNoHome"));
        houseTrustYourself = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustYourself"));
        houseTrustNotOnline = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustNotOnline"));
        houseTrustAlreadyTrusted = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustAlreadyTrusted"));
        houseTrustSuccess = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseTrustSuccess"));

        houseFindNoPlayer = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseFindNoPlayer"));
        houseFindTP = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseFindTP"));

        houseWorldTPAlreadyInWorld = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseWorldTPAlreadyInWorld"));
        houseWorldTP = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseWorldTP"));

        playerHasNoHouse = ChatColor.translateAlternateColorCodes('&',locale.getString("PlayerHasNoHouse"));

        houseRentalNoHouse = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseRentalNoHouse"));
        houseRentExtended = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseRentExtended"));

        housePurchaseAlreadyHasHouse = ChatColor.translateAlternateColorCodes('&',locale.getString("HousePurchaseAlreadyHasHouse"));
        housePurchaseNotEnoughMoney = ChatColor.translateAlternateColorCodes('&',locale.getString("HousePurchaseNotEnoughMoney"));
        housePurchaseNotEnoughTokens = ChatColor.translateAlternateColorCodes('&',locale.getString("HousePurchaseNotEnoughTokens"));
        housePurchaseFail = ChatColor.translateAlternateColorCodes('&',locale.getString("HousePurchaseFail"));

        houseLocked = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseLocked"));
        houseLockedMessageToVisitors = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseLockedMessageToVisitors"));
        houseUnlocked = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUnlocked"));
        houseLockingTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseLockingTitle"));
        houseLockedSubTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseLockedSubTitle"));
        houseUnLockedSubTitle = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseUnLockedSubTitle"));
        houseLockedAndNoHouseFound = ChatColor.translateAlternateColorCodes('&',locale.getString("HouseLockedAndNoHouseFound"));

    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getDeniedTitle() {
        return deniedTitle;
    }

    public String getDeniedSubTitle() {
        return deniedSubTitle;
    }

    public String getGuiTitle() {
        return guiTitle;
    }

    public String getHouseExpireMessage() {
        return houseExpireMessage;
    }

    public String getHouseExpired() {
        return houseExpired;
    }

    public String getBlockBrokenInHouse() {
        return blockBrokenInHouse;
    }

    public String getSignNotOnChest() {
        return signNotOnChest;
    }

    public String getHousePurchaseConfirmation() {
        return housePurchaseConfirmation;
    }

    public String getHouseUpgradeConfirmation() {
        return houseUpgradeConfirmation;
    }

    public String getHouseTrustMessage() {
        return houseTrustMessage;
    }

    public String getHouseFindMessage() {
        return houseFindMessage;
    }

    public String getHouseRentalLimit() {
        return houseRentalLimit;
    }

    public String getNoFundsEconomyRental() {
        return noFundsEconomyRental;
    }

    public String getNoFundsTokensRental() {
        return noFundsTokensRental;
    }

    public String getCommandCanceled() {
        return commandCanceled;
    }

    public String getHouseUpgradeNoHome() {
        return houseUpgradeNoHome;
    }
    public String getHouseUpgradeMaxHouse() {
        return houseUpgradeMaxHouse;
    }

    public String getHouseUpgradeInProcess() {
        return houseUpgradeInProcess;
    }

    public String getHouseUpgradeNotEnoughMoney() {
        return houseUpgradeNotEnoughMoney;
    }

    public String getHouseUpgradeNotEnoughTokens() {
        return houseUpgradeNotEnoughTokens;
    }

    public String getHouseUntrustNoHome() {
        return houseUntrustNoHome;
    }

    public String getHouseUntrustYourself() {
        return houseUntrustYourself;
    }

    public String getHouseUntrustNeverPlayedBefore() {
        return houseUntrustNeverPlayedBefore;
    }

    public String getHouseUntrustNotTrusted() {
        return houseUntrustNotTrusted;
    }

    public String getHouseUntrustSuccess() {
        return houseUntrustSuccess;
    }

    public String getHouseUntrustError() {
        return houseUntrustError;
    }

    public String getHouseTrustNoHome() {
        return houseTrustNoHome;
    }

    public String getHouseTrustYourself() {
        return houseTrustYourself;
    }

    public String getHouseTrustNotOnline() {
        return houseTrustNotOnline;
    }

    public String getHouseTrustAlreadyTrusted() {
        return houseTrustAlreadyTrusted;
    }

    public String getHouseTrustSuccess() {
        return houseTrustSuccess;
    }

    public String getHouseFindNoPlayer() {
        return houseFindNoPlayer;
    }

    public String getHouseFindTP() {
        return houseFindTP;
    }

    public String getHouseWorldTPAlreadyInWorld() {
        return houseWorldTPAlreadyInWorld;
    }

    public String getHouseWorldTP() {
        return houseWorldTP;
    }

    public String getPlayerHasNoHouse() {
        return playerHasNoHouse;
    }

    public String getHouseRentalNoHouse() {
        return houseRentalNoHouse;
    }

    public String getHousePurchaseAlreadyHasHouse() {
        return housePurchaseAlreadyHasHouse;
    }

    public String getHousePurchaseNotEnoughMoney() {
        return housePurchaseNotEnoughMoney;
    }

    public String getHousePurchaseNotEnoughTokens() {
        return housePurchaseNotEnoughTokens;
    }

    public String getHousePurchaseFail() {
        return housePurchaseFail;
    }

    public String getHouseLocked() {
        return houseLocked;
    }

    public String getHouseLockedMessageToVisitors() {
        return houseLockedMessageToVisitors;
    }

    public String getHouseUnlocked() {
        return houseUnlocked;
    }

    public String getHouseLockingTitle() {
        return houseLockingTitle;
    }

    public String getHouseLockedSubTitle() {
        return houseLockedSubTitle;
    }

    public String getHouseUnLockedSubTitle() {
        return houseUnLockedSubTitle;
    }

    public String getHouseLockedAndNoHouseFound() {
        return houseLockedAndNoHouseFound;
    }

    public String getNonExistentPlotCommand() {
        return nonExistentPlotCommand;
    }

    public String getHouseUpgrading() {
        return houseUpgrading;
    }

    public String getHouseRentExtended() {
        return houseRentExtended;
    }
}
