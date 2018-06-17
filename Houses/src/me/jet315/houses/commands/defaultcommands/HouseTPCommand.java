package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseTPCommand extends CommandExecutor {

    /**
     * House TP Command
     */

    public HouseTPCommand() {
        setCommand("tp");
        setPermission("house.player.tp");
        setLength(1);
        setPlayer();
        setUsage("/house tp");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        Locale locale = Core.getInstance().getMessages();

        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)) {
            PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
            if(plotPlayer.getPlots().size() > 0){
                int timeToWait = Core.getInstance().getProperties().gettimeToWaitWhenTeleporting();
                if(timeToWait <= 0) {
                    plotPlayer.getPlots().iterator().next().teleportPlayer(plotPlayer);
                }else {

                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getMessages().getTimeToWait().replaceAll("%SECONDS%", String.valueOf(timeToWait))));


                    new BukkitRunnable() {
                        int counter = 0;
                        int playersX = p.getLocation().getBlockX();
                        int playersY = p.getLocation().getBlockY();
                        int playersZ = p.getLocation().getBlockZ();
                        World world = p.getWorld();

                        @Override
                        public void run() {
                            if (!p.isOnline()) {
                                cancel();
                                return;
                            }
                            if (p.getWorld() != world) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                                cancel();
                                return;
                            }
                            if (p.getLocation().getBlockX() != playersX || p.getLocation().getBlockY() != playersY || p.getLocation().getBlockZ() != playersZ) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                                cancel();
                                return;
                            }
                            if (counter < timeToWait) {
                                counter++;
                            } else {

                                plotPlayer.getPlots().iterator().next().teleportPlayer(plotPlayer);
                                cancel();
                                return;
                            }

                        }
                    }.runTaskTimer(Core.getInstance(), 0, 20);

                }

            }else{
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cA house cannot be found!"));
            }
            return;
        }
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getPlayerHasNoHouse()));

    }

}
