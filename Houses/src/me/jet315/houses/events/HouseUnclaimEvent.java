package me.jet315.houses.events;

import com.intellectualcrafters.plot.object.Plot;
import me.jet315.houses.utils.UnclaimReason;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * Created by Jet on 08/02/2018.
 */
public class HouseUnclaimEvent extends Event {

    /**
     * Called when a house is unclaimed
     */
    //Plot that this is occuring at
    private Plot plot;
    //previous owner of the house
    private OfflinePlayer player;
    //Unclaim reason
    private UnclaimReason reason;

    private static final HandlerList handlers = new HandlerList();

    public HouseUnclaimEvent(OfflinePlayer player, Plot plot, UnclaimReason reason) {
        this.player = player;
        this.plot = plot;
        this.reason = reason;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    public UnclaimReason getReason() {
        return reason;
    }

    public Plot getPlot() {
        return plot;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }
}
