package me.jet315.houses.commands.admincommands;

import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddRentCommand extends CommandExecutor {

    /**
     * Rental Command
     */

    public AddRentCommand() {
        setCommand("addrent");
        setPermission("house.admin.addrent");
        setLength(3);
        setBoth();
        setUsage("/house addrent <player> <days>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.RED + "That player cannot be found!");
            return;
        }
        if (!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)) {

            sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() +ChatColor.RED + "That player does not have a house!");
            return;
        }
        int days;

        try {
            days = Integer.valueOf(args[2]);
        }catch (NumberFormatException e) {
            sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.RED + args[2] + " is not a valid integer! Usage: /house addrent <player> <days>");
            return;
        }

            //Get current expiry date & future date:
            long currentExpiryDate = Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).getMillisecondsOfExpiry();
            long futureExpiryDate = currentExpiryDate + (days * 86400000);

            //Update cashed data
            Core.getInstance().getPlayerManager().getHousePlayerMap().get(p).setMillisecondsOfExpirey(futureExpiryDate);
            //Update database
            Core.getInstance().getDb().setHouseRentalTime(p.getUniqueId().toString(), futureExpiryDate);

        sender.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.GREEN + "Successfully updated " + p.getName() + "'s rent by " + days +" days!");
        }




}