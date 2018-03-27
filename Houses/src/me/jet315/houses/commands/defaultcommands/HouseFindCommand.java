package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.api.PlotAPI;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.events.HouseUnclaimEvent;
import me.jet315.houses.events.TeleportToHouseEvent;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.UnclaimReason;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseFindCommand extends CommandExecutor {

    /**
     * Find house command
     */

    public HouseFindCommand() {
        setCommand("find");
        setPermission("house.player.find");
        setLength(1);
        setPlayer();
        setUsage("/house find <player>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        Locale locale = Core.getInstance().getMessages();
        Set<Plot> plots;
        try {
            plots = PS.get().getPlots(Core.getInstance().getProperties().getPlotsWorldName(), args[1]);
        }catch (Exception ex){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindNoPlayer().replaceAll("%PLAYER%",args[1])));
            return;
        }
        if(plots.size() > 0){
            Plot plot = plots.iterator().next();
            //Create, and trigger the HouseClaimEvent so others are able to have a say in what happens
            TeleportToHouseEvent houseTeleportEvent = new TeleportToHouseEvent(p,plot);
            Core.getInstance().getServer().getPluginManager().callEvent(houseTeleportEvent);
            if(houseTeleportEvent.isCancelled()) return;
            PlotPlayer.get(p.getName()).teleport(plot.getDefaultHome());
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindTP()));
            return;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',locale.getHouseFindNoPlayer().replaceAll("%PLAYER%",args[1])));
    }

}
