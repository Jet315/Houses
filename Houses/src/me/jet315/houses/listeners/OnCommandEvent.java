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
public class OnCommandEvent implements Listener {

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if(e.getPlayer().hasPermission("house.admin.bypasscommands")) return;
        String message[] = e.getMessage().split(" ");
        String firstCommand;
        if (message.length > 0) {
            firstCommand = message[0];
        } else {
            firstCommand = e.getMessage();
        }

        for (String s : Core.getInstance().getProperties().getCommandsToBlock()) {
            if (firstCommand.equalsIgnoreCase(s)) {

                e.setCancelled(true);
                e.getPlayer().sendMessage(Core.getInstance().getMessages().getNonExistentPlotCommand());
                return;
            }

        }
    }
}
