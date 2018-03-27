package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by Jet on 28/01/2018.
 */
public class JoinEvent implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Core.getInstance().getDb().loadHouseValues(e.getPlayer());

    }
}
