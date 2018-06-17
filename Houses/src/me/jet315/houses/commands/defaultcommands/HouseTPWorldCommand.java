package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.PlotPlayer;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.utils.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Jet on 08/02/2018.
 */
public class HouseTPWorldCommand extends CommandExecutor {

    /**
     * World TP command
     */

    public HouseTPWorldCommand() {
        setCommand("world");
        setPermission("house.player.world");
        setLength(1);
        setPlayer();
        setUsage("/house world");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        Locale locale = Core.getInstance().getMessages();

        if(p.getWorld().getName().equalsIgnoreCase(Core.getInstance().getProperties().getPlotsWorldName())){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseWorldTPAlreadyInWorld()));
        }else{
            World world = Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName());
            if(world == null){
                p.sendMessage(Core.getInstance().getProperties().getPluginPrefix() + ChatColor.RED + "The world found in the config.yml does not exist! ");
                return;
            }

            int timeToWait = Core.getInstance().getProperties().gettimeToWaitWhenTeleporting();
            if(timeToWait <= 0) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseWorldTP()));
                p.teleport(Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName()).getSpawnLocation());
            }else {

                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getMessages().getTimeToWait().replaceAll("%SECONDS%", String.valueOf(timeToWait))));


                new BukkitRunnable() {
                    int counter = 0;
                    int playersX = p.getLocation().getBlockX();
                    int playersY = p.getLocation().getBlockY();
                    int playersZ = p.getLocation().getBlockZ();
                    World world = p.getWorld();

                    @Override
                    public void run() {
                        if (!p.isOnline()) {
                            cancel();
                            return;
                        }
                        if (p.getWorld() != world) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                            cancel();
                            return;
                        }
                        if (p.getLocation().getBlockX() != playersX || p.getLocation().getBlockY() != playersY || p.getLocation().getBlockZ() != playersZ) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getPlayerMovedWhileTryingToTeleport()));
                            cancel();
                            return;
                        }
                        if (counter < timeToWait) {
                            counter++;
                        } else {

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseWorldTP()));
                            p.teleport(Bukkit.getWorld(Core.getInstance().getProperties().getPlotsWorldName()).getSpawnLocation());
                            cancel();
                            return;
                        }

                    }
                }.runTaskTimer(Core.getInstance(), 0, 20);

            }


        }

    }

}
