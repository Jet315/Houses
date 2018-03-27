package me.jet315.houses.events;

import com.intellectualcrafters.plot.object.Plot;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Jet on 06/02/2018.
 */
public class HouseUpgradeEvent extends Event implements Cancellable {

    /**
     * Called when a user upgrades a house
     */

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    private Plot plot;

    private boolean isCancelled = false;

    private int levelFrom;
    private int levelTo;

    public HouseUpgradeEvent(Player player,Plot plot,int levelFrom, int levelTo){
        this.player = player;
        this.plot = plot;
        this.levelFrom = levelFrom;
        this.levelTo = levelTo;
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

    /**
     *
     * @return The plot that is being upgraded
     */
    public Plot getPlot() {
        return plot;
    }

    /**
     *
     * @return The original house level
     */
    public int getLevelFrom() {
        return levelFrom;
    }
    /**
     *
     * @return The house level the plot is being set to
     */
    public int getLevelTo() {
        return levelTo;
    }

    /**
     *
     * @param levelTo the house level (the schematic) the house is being set to
     */
    public void setLevelTo(int levelTo) {
        this.levelTo = levelTo;
    }
}

