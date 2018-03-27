package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Jet on 06/02/2018.
 */
public class LeaveEvent implements Listener{

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(e.getPlayer())){
            Core.getInstance().getPlayerManager().getHousePlayerMap().remove(e.getPlayer());
        }

    }
}
