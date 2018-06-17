package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotArea;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.EconHandler;
import com.intellectualcrafters.plot.util.MainUtil;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HouseLocateCommand extends CommandExecutor {

    /**
     * House lock command
     */
    public HouseLocateCommand() {
        setCommand("locate");
        setPermission("house.player.locate");
        setLength(1);
        setPlayer();
        setUsage("/house locate");


    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        PlotArea plotArea = plotPlayer.getApplicablePlotArea();

        if (p.getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())) {
            if (plotArea == null) {
                if (EconHandler.manager != null) {
                    for (PlotArea area : PS.get().getPlotAreaManager().getAllPlotAreas()) {
                            if (plotArea != null) {
                                plotArea = null;
                                break;
                            }
                            plotArea = area;

                    }
                }
                }
                if (plotArea.TYPE == 2) {
                    p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.RED + "No free plots!");
                    return;
                }

            int timeToWait = Core.getInstance().getProperties().gettimeToWaitWhenTeleporting();
            if(timeToWait <= 0) {
                plotPlayer.teleport(plotArea.getNextFreePlot(plotPlayer, PlotId.fromString(plotArea.id)).getDefaultHome());
            }else {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getMessages().getTimeToWait().replaceAll("%SECONDS%", String.valueOf(timeToWait))));

                PlotArea finalPlotArea = plotArea;
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

                            plotPlayer.teleport(finalPlotArea.getNextFreePlot(plotPlayer, PlotId.fromString(finalPlotArea.id)).getDefaultHome());
                            cancel();
                            return;
                        }

                    }
                }.runTaskTimer(Core.getInstance(), 0, 20);

            }

            } else {
                //TODO NOT IN PLOTS WORLD
            }
        }


}