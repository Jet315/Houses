package me.jet315.houses.events;

import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jet on 07/02/2018.
 */
public class TeleportToHouseEvent extends Event implements Cancellable {

    /**
     * Called when a user teleports to a users house
     */

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    private Plot plot;

    private boolean isCancelled = false;


    public TeleportToHouseEvent(Player player,Plot plot){
        this.player = player;
        this.plot = plot;

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     *
     * @return The player who is teleporting
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
     *
     * @return The plot that the player is teleporting to
     */
    public Plot getPlot() {
        return plot;
    }

}

