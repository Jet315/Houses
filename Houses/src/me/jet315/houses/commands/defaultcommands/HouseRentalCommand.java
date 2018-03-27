package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Jet on 08/02/2018.
 */
public class HouseRentalCommand extends CommandExecutor {

    /**
     * Rental Command
     */

    public HouseRentalCommand() {
        setCommand("rent");
        setPermission("house.player.rent");
        setLength(1);
        setPlayer();
        setUsage("/house rent");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        //Check if user does not have a house
        if(!Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseRentalNoHouse()));
            return;
        }

        p.openInventory(Core.getInstance().getProperties().getIncreaseRentInventory());


    }

}
