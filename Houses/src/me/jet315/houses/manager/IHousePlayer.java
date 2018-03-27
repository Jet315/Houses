package me.jet315.houses.manager;

import org.bukkit.entity.Player;

public interface IHousePlayer {


    Player getPlayer();
    int getHouseLevel();
    boolean getIsHouseLocked();
    long getMillisecondsOfExpiry();
    boolean getIsHouseBeingUpgraded();
    void setHouseLevel(int houseLevel);
    void setIsHouseLocked(boolean isHouseLocked);
    void setMillisecondsOfExpirey(long millisecondsOfExpirey);
    void setIsHouseBeingUpgraded(boolean isHouseBeingUpgraded);



}
