package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.config.Settings;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseTrustCommand extends CommandExecutor {

    /**
     * House Trust Command
     */

    public HouseTrustCommand() {
        setCommand("trust");
        setPermission("house.player.trust");
        setLength(1);
        setPlayer();
        setUsage("/house trust <player>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();

        //Check if they have any plots
        if (plotPlayer.getPlots().size() == 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustNoHome()));
            return;
        }

        //Check they are not doing it to themselves
        if(p.getName().equalsIgnoreCase(args[1])){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustYourself()));
            return;
        }

        //They have a plot so get it
        Plot plot = plotPlayer.getPlots().iterator().next();
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if(targetPlayer == null){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustNotOnline().replaceAll("%PLAYER%",args[1])));
            return;
        }
        //Player is online, so add them to the trusted list
        HashSet<UUID> trustedUUIDs =  plot.getTrusted();
        UUID playersUUID = targetPlayer.getUniqueId();
        //Check if already trusted
        for(UUID uuid : trustedUUIDs){
            if(uuid.equals(playersUUID)){
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustAlreadyTrusted().replaceAll("%PLAYER%",args[1])));
                return;
            }
        }

        //Not trusted, add to trusted
        plot.addTrusted(playersUUID);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseTrustSuccess()));



    }

}
