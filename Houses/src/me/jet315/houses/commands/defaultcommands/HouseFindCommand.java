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
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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
        setLength(2);
        setPlayer();
        setUsage("/house find <player>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        String targetStringPlayer = args[1];
        Player p = (Player) sender;
        Locale locale = Core.getInstance().getMessages();
        Set<Plot> plots;
        OfflinePlayer target;
        if(Bukkit.getPlayer(targetStringPlayer) != null){
            target = Bukkit.getPlayer(targetStringPlayer);
        }else{
            target= Bukkit.getOfflinePlayer(targetStringPlayer);
        }

        try {
            plots = PlotPlayer.wrap(target).getPlots();//target could be null
        }catch (Exception ex) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindNoPlayer().replaceAll("%PLAYER%", targetStringPlayer)));

            return;
        }
        if(plots.size() > 0){
            //bukkit runnable
            int timeToWait = Core.getInstance().getProperties().gettimeToWaitWhenTeleporting();
            if(timeToWait <= 0) {
                teleportToPlot(plots, p);
            }else{

                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getMessages().getTimeToWait().replaceAll("%SECONDS%",String.valueOf(timeToWait))));

                new BukkitRunnable() {
                    int counter = 0;
                    int playersX = p.getLocation().getBlockX();
                    int playersY = p.getLocation().getBlockY();
                    int playersZ = p.getLocation().getBlockZ();
                    World world = p.getWorld();
                    @Override
                    public void run() {
                        if(!p.isOnline()){
                            cancel();
                            return;
                        }
                        if(p.getWorld() != world){
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                            cancel();
                            return;
                        }
                        if(p.getLocation().getBlockX() != playersX || p.getLocation().getBlockY() != playersY || p.getLocation().getBlockZ() != playersZ){
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                            cancel();
                            return;
                        }
                        if(counter < timeToWait){
                            counter++;
                        }else{

                            teleportToPlot(plots, p);
                            cancel();
                            return;
                        }

                    }
                }.runTaskTimer(Core.getInstance(), 0, 20);
            }
            return;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseFindNoPlayer().replaceAll("%PLAYER%",targetStringPlayer)));
    }

    private void teleportToPlot(Set<Plot> plots, Player p){
        Plot plot = plots.iterator().next();

        //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
        TeleportToHouseEvent houseTeleportEvent = new TeleportToHouseEvent(p,plot);
        Core.getInstance().getServer().getPluginManager().callEvent(houseTeleportEvent);
        if(houseTeleportEvent.isCancelled()) return;

        PlotPlayer.get(p.getName()).teleport(plot.getDefaultHome());
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseFindTP()));
        return;

    }
}
