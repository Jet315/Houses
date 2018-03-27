package me.jet315.houses.events;

import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jet on 06/02/2018.
 */
public class HouseLockEvent extends Event implements Cancellable {

    /**
     * Called when a user locks his house
     */

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    private Plot plot;

    private boolean lockStatus;

    private boolean isCancelled = false;

    public HouseLockEvent(Player player,Plot plot,boolean lockStatus){
        this.player = player;
        this.plot = plot;
        this.lockStatus = lockStatus;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    /**
     * @return Returns the Player who entered the house
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
    /**
     * @return Returns the plot the user has locked/unlocked
     */
    public Plot getPlot() {
        return plot;
    }

    /**
     * @return Returns the CURRENT lock status
     */
    public boolean currentLockStatus() {
        return lockStatus;
    }

    /**
     *
     * @return Returns what the lock status is turning into
     */
    public boolean toLockStatus() {
        return !lockStatus;
    }

    /**
     *
     * @param lockStatus Sets the final lock status of the house
     */
    public void setLockStatus(boolean lockStatus){
        this.lockStatus = !lockStatus;
    }
}

