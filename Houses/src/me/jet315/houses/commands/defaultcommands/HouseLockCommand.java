package me.jet315.houses.commands.defaultcommands;


import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.commands.Chat;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.events.HouseClaimEvent;
import me.jet315.houses.events.HouseLockEvent;
import me.jet315.houses.utils.Locale;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Jet on 07/02/2018.
 */
public class HouseLockCommand extends CommandExecutor {

    /**
     * House lock command
     */
    public HouseLockCommand() {
        setCommand("lock");
        setPermission("house.player.lock");
        setLength(1);
        setPlayer();
        setUsage("/house lock");


    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();
        //Check if user has a house
        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && plotPlayer.getPlots().size() > 0){
            //Get the plot
            Plot plot = plotPlayer.getPlots().iterator().next();
            //get the current the lock status
            boolean houseLocked = Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getIsHouseLocked();
            //Call the event so other users have a say
            HouseLockEvent houseLockEvent = new HouseLockEvent(p,plot,houseLocked);

            Core.getInstance().getServer().getPluginManager().callEvent(houseLockEvent);

            //Check if someone cancled it
            if(houseLockEvent.isCancelled()) return;

            //Update the value of the lock status
            houseLocked = houseLockEvent.toLockStatus();
            //House WAS locked, so unlock it
            if(!houseLocked){
                Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setIsHouseLocked(false);

                Core.getInstance().getDb().setHouseLocked(p.getUniqueId().toString(),false);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUnlocked()));
                if(Core.serverVersion.startsWith("v1_12")) {
                    p.sendTitle(locale.getHouseLockingTitle(), locale.getHouseUnLockedSubTitle(), 30, 60, 10);
                }else{
                    p.sendTitle(locale.getHouseLockingTitle(), locale.getHouseUnLockedSubTitle());
                    }
            }else{
                //house WAS unlocked, so lock it
                for(PlotPlayer playerInPlot : plot.getPlayersInPlot()){
                    if(playerInPlot.getName().equalsIgnoreCase(p.getName())) continue;
                    if(plot.getTrusted().contains(playerInPlot.getUUID())) continue;
                    playerInPlot.teleport(plot.getDefaultHome());
                    playerInPlot.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseLockedMessageToVisitors()));
                }

                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseLocked()));
                if(Core.serverVersion.startsWith("v1_12")) {
                p.sendTitle(locale.getHouseLockingTitle(),locale.getHouseLockedSubTitle(),30,60,10);
                }else{
                    p.sendTitle(locale.getHouseLockingTitle(), locale.getHouseLockedSubTitle());
                }
                Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setIsHouseLocked(true);

                Core.getInstance().getDb().setHouseLocked(p.getUniqueId().toString(),true);
            }
        }else{
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseLockedAndNoHouseFound()));
            return;
        }
    }
}
