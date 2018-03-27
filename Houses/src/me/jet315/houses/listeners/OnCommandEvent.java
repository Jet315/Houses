package me.jet315.houses.listeners;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.SchematicHandler;
import com.plotsquared.bukkit.util.BukkitUtil;
import me.jet315.houses.Core;
import me.jet315.houses.commands.defaultcommands.HouseUpgradeCommand;
import me.jet315.houses.gui.GUI;
import me.jet315.houses.gui.PlayersGUI;
import me.jet315.houses.utils.Math;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.io.File;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Jet on 27/01/2018.
 */
public class OnCommandEvent implements Listener{

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){

        if(e.getMessage().contains("/b")){
            //Work out time left on house
            long expiryDate = Core.getInstance().getPlayerManager().getHousePlayerMap().get(e.getPlayer()).getMillisecondsOfExpiry();
            for(Integer i : Math.calculateTimeLeft(expiryDate)){
                System.out.println(i);
            }

        }
        //TODO COMMANDS TO BLOCK:     aliases: [p,plot,ps,plotsquared,p2,2,plotme]
        if(e.getMessage().contains("/a")){
            String uuid = e.getPlayer().getUniqueId().toString();
            //Save into DB
            Core.getInstance().getDatabase().setHouseValues(uuid,1,false,System.currentTimeMillis() + 600000);

            //Load into JVM
            Core.getInstance().getPlayerManager().getHousePlayerMap().get(e.getPlayer()).setMillisecondsOfExpirey(System.currentTimeMillis() + 600000);
        }

        String message[] = e.getMessage().split(" ");
        String firstCommand;
        if(message.length > 0){
            firstCommand = message[0];
        }else{
            firstCommand = e.getMessage();
        }
        for(String s : Core.getInstance().getProperties().getCommandsToBlock()){
            if(firstCommand.equalsIgnoreCase(s)){
                e.setCancelled(true);
                e.getPlayer().sendMessage(Core.getInstance().getMessages().getNonExistentPlotCommand());
                return;
            }
        }

        if(e.getMessage().contains("paste")){
            SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(),"schematics/test.schematic"));

            //Note plotplayer.get can return null if not online
            Set<Plot> plotSet = PS.get().getPlots(BukkitUtil.getPlayer(e.getPlayer()));
            if(plotSet.size() >0){
                SchematicHandler.manager.paste(schematic, plotSet.iterator().next() ,0,-2,0,true,null);
            }else{
                System.out.println("error");
            }
        }
    }
}
