package me.jet315.houses.manager;

import me.jet315.houses.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Jet on 28/01/2018.
 */
public class PlayerManager {
/*    *//**
     * Stores UUID / Integer of house level
     *//*
    private HashMap<String,Integer> houseLevels = new HashMap<>();
    *//**
     * Stores UUID / Whether house is open/locked
     *//*
    private HashMap<String,Boolean> isHouseLocked = new HashMap<>();
    *//**
     * Stores UUID / milliseconds (future epoch time) remaining on the house
     *//*
    private HashMap<String,Long> millisecondstillExpiry = new HashMap<>();

    *//**
     * Stores UUID of players houses that are being upgraded
     *//*
    private ArrayList<String> houseBeingUpgraded = new ArrayList<>();

        public HashMap<String, Integer> getHouseLevels() {
        return houseLevels;
    }

    public HashMap<String, Boolean> getIsHouseLocked() {
        return isHouseLocked;
    }

    public HashMap<String, Long> getMillisecondsTillExpiry() {
        return millisecondstillExpiry;
    }

    public ArrayList<String> getHouseBeingUpgraded() {
        return houseBeingUpgraded;
    }
    */


    /**
     * Stores Player Objects followed by their HousePlayer object
     */
    private HashMap<Player,HousePlayer> housePlayerMap = new HashMap<>();

    /**
     * Stores (String) Player's name and the command to execute. Used for confirmation messages
     */
    private HashMap<String,String> confirmation = new HashMap<>();


    /**
     * @return Returns the hashmap relating to player confirmation - If adding a player, removePlayer() should be called.
     */
    public HashMap<String, String> getConfirmation() {
        return confirmation;
    }

    /**
     * After adding a player to a confirmation hashmap, this should be called to remove the player if he/she does nothing about it
     * @param playersName
     */
    public void removePlayer(String playersName){
        Bukkit.getScheduler().runTaskLater(Core.getInstance(), new Runnable() {
            @Override
            public void run() {

                if(getConfirmation().containsKey(playersName)){
                    getConfirmation().remove(playersName);
                }
            }
        },300L);
    }


    public HashMap<Player, HousePlayer> getHousePlayerMap() {
        return housePlayerMap;
    }
}
