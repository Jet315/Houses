package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.files.Properties;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.material.Sign;

/**
 * Created by Jet on 28/01/2018.
 */
public class MineEvent implements Listener{

    @EventHandler
    public void onMine(BlockBreakEvent e){
        //They are in the plots world
        if(e.getPlayer().getLocation().getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())) {

            if (e.getBlock().getLocation().getBlockY() >= Core.getInstance().getProperties().getGetMaxBuildHeight()) {

                if (e.getPlayer().hasPermission("house.admin.build")) return;

                if ((e.getBlock().getType() == Material.SIGN_POST || e.getBlock().getType() == Material.WALL_SIGN) && Core.getInstance().getProperties().isAllowSignsInHouse()) {
                    Sign sign = (Sign) e.getBlock().getState().getData();
                    Block attached = e.getBlock().getRelative(sign.getAttachedFace());
                    if (attached.getType() == Material.CHEST) {
                        return;
                    }
                }

                e.setCancelled(true);
                e.getPlayer().sendMessage(Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getBlockBrokenInHouse());
            }

        }
    }



}
