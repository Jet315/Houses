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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                plotPlayer.teleport(plotArea.getNextFreePlot(plotPlayer, PlotId.fromString(plotArea.id)).getDefaultHome());


            } else {
                //TODO NOT IN PLOTS WORLD
            }
        }


}