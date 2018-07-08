package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonEvent implements Listener {

    @EventHandler
    public void onPiston(BlockPistonExtendEvent e){
        if(e.getBlock().getWorld().getName().equals(Core.getInstance().getProperties().getPlotsWorldName())){
            for(Block block : e.getBlocks()){
                if(block.getY() >= Core.getInstance().getProperties().getGetMaxBuildHeight()){
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }
}
