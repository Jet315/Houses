package me.jet315.houses.commands;

import me.jet315.houses.Core;
import me.jet315.houses.commands.admincommands.AddRentCommand;
import me.jet315.houses.commands.admincommands.DatabasePurgeCommand;
import me.jet315.houses.commands.admincommands.HouseEvictPlayerCommand;
import me.jet315.houses.commands.admincommands.HouseReloadCommand;
import me.jet315.houses.commands.defaultcommands.*;
import me.jet315.houses.gui.PlayersGUI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jet on 28/01/2018.
 */
public class CommandHandler implements org.bukkit.command.CommandExecutor {

    private Map<String, CommandExecutor> commands = new HashMap<String, CommandExecutor>();

    public CommandHandler() {
        //Player commands
        commands.put("lock", new HouseLockCommand());
        commands.put("purchase", new HousePurchaseCommand());
        commands.put("world", new HouseTPWorldCommand());
        commands.put("rent", new HouseRentalCommand());
        commands.put("upgrade", new HouseUpgradeCommand());
        commands.put("tp", new HouseTPCommand());
        commands.put("find", new HouseFindCommand());
        commands.put("trust", new HouseTrustCommand());
        commands.put("untrust", new HouseUntrustCommand());
        commands.put("abandon", new HouseAbandon());
        commands.put("locate", new HouseLocateCommand());
        commands.put("trustlist", new HouseTrustListCommand());
        commands.put("about", new HouseAboutCommand());
        commands.put("claim", new HouseClaimCommand());

        //Admin commands
        commands.put("reload", new HouseReloadCommand());
        commands.put("delete", new HouseEvictPlayerCommand());
        commands.put("addrent", new AddRentCommand());
        commands.put("purge", new DatabasePurgeCommand());
    }
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("house")) {
            if (args.length == 0) {

                if(sender instanceof Player) {
                    //Open the GUI
                    Player p = (Player) sender;
                    if (Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)){
                            new PlayersGUI((Player) sender, Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getHouseLevel(), Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getIsHouseLocked(), Core.getInstance());
                        return true;
                    }
                    new PlayersGUI((Player) sender, 0, false, Core.getInstance());
                    return true;
                }else{
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + "&cMust be a Player!"));
                    return true;
                }
            }


            if (args[0] != null) {
                String name = args[0].toLowerCase();
                if(name.equalsIgnoreCase("unlock")) name = "lock";
                if(name.equalsIgnoreCase("tp") && args.length > 1){
                    name = "find";
                }
                if (commands.containsKey(name)) {
                    final CommandExecutor command = commands.get(name);

                    if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
                        if(!(command.getPermission().startsWith("house.player") && sender.hasPermission("house.player.*"))) {
                            sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getNoPermissionMessage());
                            return true;
                        }

                    }

                    if (!command.isBoth()) {
                        if (command.isConsole() && sender instanceof Player) {
                            sender.sendMessage(ChatColor.RED + "Only console can use that command!");
                            return true;
                        }
                        if (command.isPlayer() && sender instanceof ConsoleCommandSender) {
                            sender.sendMessage(ChatColor.RED + "Only players can use that command!");
                            return true;
                        }
                    }

                    if (command.getLength() > args.length) {
                        sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getCommandHelp().replaceAll("%COMMAND_USAGE%", command.getUsage()));
                        return true;
                    }

                    command.execute(sender, args);
                    return true;
                }
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getNonExistentPlotCommand()));
        }
        return true;
    }
}
