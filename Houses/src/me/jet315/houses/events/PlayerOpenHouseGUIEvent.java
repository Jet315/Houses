package me.jet315.houses.events;

import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jet on 07/02/2018.
 */
public class PlayerOpenHouseGUIEvent extends Event implements Cancellable {

    /**
     * Called when a user upgrades a house
     */

    private static final HandlerList handlers = new HandlerList();

    private Player player;


    private boolean isCancelled = false;


    public PlayerOpenHouseGUIEvent(Player player){
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

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

}

