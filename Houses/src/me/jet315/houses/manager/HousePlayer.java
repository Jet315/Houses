package me.jet315.houses.manager;

import org.bukkit.entity.Player;

public class HousePlayer implements IHousePlayer{

    private Player p;
    private int houseLevel;
    boolean isHouseLocked;
    long millisecondsOfExpiry;
    boolean isHouseBeingUpgraded;

    public HousePlayer(Player p, int houseLevel, boolean isHouseLocked, long millisecondsOfExpirey, boolean isHouseBeingUpgraded){
        this.p = p;
        this.houseLevel = houseLevel;
        this.isHouseLocked = isHouseLocked;
        this.millisecondsOfExpiry = millisecondsOfExpirey;
        this.isHouseBeingUpgraded = isHouseBeingUpgraded;
    }

    @Override
    public Player getPlayer() {
        return p;
    }

    @Override
    public int getHouseLevel() {
        return houseLevel;
    }

    @Override
    public boolean getIsHouseLocked() {
        return isHouseLocked;
    }

    @Override
    public long getMillisecondsOfExpiry() {
        return millisecondsOfExpiry;
    }

    @Override
    public boolean getIsHouseBeingUpgraded() {
        return isHouseBeingUpgraded;
    }

    @Override
    public void setHouseLevel(int houseLevel) {
        this.houseLevel = houseLevel;
    }

    @Override
    public void setIsHouseLocked(boolean isHouseLocked) {
        this.isHouseLocked = isHouseLocked;
    }

    @Override
    public void setMillisecondsOfExpirey(long millisecondsOfExpirey) {
        this.millisecondsOfExpiry = millisecondsOfExpirey;
    }

    @Override
    public void setIsHouseBeingUpgraded(boolean isHouseBeingUpgraded) {
        this.isHouseBeingUpgraded = isHouseBeingUpgraded;
    }


}
