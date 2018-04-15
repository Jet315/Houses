package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.files.Properties;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by Jet on 28/01/2018.
 */
public class PlaceEvent  implements Listener {

    /**
     * Stores the Properties object (Saves typing & looks more neat)
     */
    private Properties properties = Core.getInstance().getProperties();
    private Locale locale = Core.getInstance().getMessages();

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        if(e.getBlockPlaced().getLocation().getWorld().getName().equalsIgnoreCase(properties.getPlotsWorldName())){
            if(e.getBlockPlaced().getLocation().getBlockY() >= properties.getGetMaxBuildHeight()){
                if(e.getPlayer().hasPermission("house.admin.build")) return;
                //If block being placed is a sign and the properties allow it
                if((e.getBlockPlaced().getType() == Material.SIGN_POST || e.getBlockPlaced().getType() == Material.WALL_SIGN) && properties.isAllowSignsInHouse()){
                    if(e.getBlockAgainst().getType() == Material.CHEST){
                        return;
                    }else{
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',properties.getPluginPrefix() + locale.getSignNotOnChest()));
                    }
                }else{
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',properties.getPluginPrefix() + locale.getBlockBrokenInHouse()));
                }
            }
        }
    }
}
