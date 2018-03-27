package me.jet315.houses.commands.admincommands;

import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseReloadCommand extends CommandExecutor {

    /**
     * Reloads the configuration file
     */

    public HouseReloadCommand() {
        setCommand("reload");
        setPermission("house.admin.reload");
        setLength(1);
        setBoth();
        setUsage("/house reload");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&aStarting Reload"));
        long startTime = System.currentTimeMillis();
        Core.getInstance().reloadProperties();
        Core.getInstance().registerDependencies();
        long endtime = System.currentTimeMillis() - startTime;
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&aReload Complete: &6" + endtime + "ms"));

    }

}