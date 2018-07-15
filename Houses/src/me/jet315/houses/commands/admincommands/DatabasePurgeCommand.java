package me.jet315.houses.commands.admincommands;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.SchematicHandler;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.events.HouseUnclaimEvent;
import me.jet315.houses.utils.UnclaimReason;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.UUID;

public class DatabasePurgeCommand extends CommandExecutor {

    /**
     * Force remove a players house
     */

    public DatabasePurgeCommand() {
        setCommand("purge");
        setPermission("house.admin.purge");
        setLength(1);
        setBoth();
        setUsage("/house purge");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.GREEN + "Deleting expired plots.. Please note that the server may lag while performing this command");
        Core.getInstance().getDb().purgeDatabase(sender);
    }

}
