package me.jet315.houses.commands.defaultcommands;

import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HouseAboutCommand extends CommandExecutor {

    public String userID = "%%__USER__%%";

    public HouseAboutCommand() {
        setCommand("about");
        setLength(1);
        setBoth();
        setUsage("/house about");

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&m---------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Plugin Name:&e " + Core.getInstance().getDescription().getName()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Plugin Version:&e " + Core.getInstance().getDescription().getVersion()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Author:&e " + Core.getInstance().getDescription().getAuthors()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Website:&e " + Core.getInstance().getDescription().getWebsite()));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cPlugin registered to:&4 https://www.spigotmc.org/members/" + userID));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&m---------------------------------"));
    }
}

