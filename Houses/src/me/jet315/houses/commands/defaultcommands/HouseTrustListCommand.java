package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.UUIDHandler;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.*;

public class HouseTrustListCommand extends CommandExecutor {

    /**
     * House Trust Command
     */

    public HouseTrustListCommand() {
        setCommand("trustlist");
        setPermission("house.player.trustlist");
        setLength(1);
        setPlayer();
        setUsage("/house trustlist");

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();

        //Check if they have any plots
        if (plotPlayer.getPlots().size() == 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getTrustListNoHome()));
            return;
        }
        Plot plot = plotPlayer.getPlots().iterator().next();
        HashSet<UUID> uuidHashSet = plot.getTrusted();
        if(uuidHashSet.size() == 0){
            p.sendMessage(locale.getTrustListNoPlayers());
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int loops = 0;
        for(UUID uuid : uuidHashSet){
            String playerName = UUIDHandler.getName(uuid);
            if(Bukkit.getPlayer(playerName) != null){
                stringBuilder.append(ChatColor.GREEN + playerName);
            }else{
                stringBuilder.append(ChatColor.RED + playerName);
            }
            loops++;
            if(loops < uuidHashSet.size()){
                stringBuilder.append(ChatColor.GRAY+", ");
            }
        }
        p.sendMessage(locale.getTrustListFormat().replaceAll("%TRUSTLIST%",stringBuilder.toString()));

    }
}
