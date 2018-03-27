package me.jet315.houses.commands.defaultcommands;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
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

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseUntrustCommand extends CommandExecutor {

    /**
     * House Trust HouseUntrustCommand
     */

    public HouseUntrustCommand() {
        setCommand("untrust");
        setPermission("house.player.untrust");
        setLength(1);
        setPlayer();
        setUsage("/house untrust <player>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Can cast as was already checked
        Player p = (Player) sender;
        PlotPlayer plotPlayer = PlotPlayer.get(p.getName());
        Locale locale = Core.getInstance().getMessages();

        //Check if they have any plots
        if (plotPlayer.getPlots().size() == 0) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUntrustNoHome()));
            return;
        }

        //Check they are not doing it to themselves
        if(p.getName().equalsIgnoreCase(args[1])){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUntrustYourself()));
            return;
        }

        //They have a plot so get it
        Plot plot = plotPlayer.getPlots().iterator().next();

        //As this is a very cpu intensive process, needs to be off min thread to prevent lag
        //Task - get stats for the player ( called once the other future returns)
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<OfflinePlayer> task = new Callable<OfflinePlayer>() {
            public OfflinePlayer call() throws Exception {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

                return offlinePlayer;
            }
        };

        Future<OfflinePlayer> offlinePlayerFuture = executorService.submit(task);

        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                int counter = 1;
                while (!offlinePlayerFuture.isDone() && counter <= 100) {
                    counter++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //10 seconds no response
                    if (counter == 100) {
                        offlinePlayerFuture.cancel(true);
                        //When accessing Bukkit, needs to be within Synchronized
                        Bukkit.getScheduler().runTask(Core.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                p.sendMessage(ChatColor.RED + locale.getHouseUntrustError());
                            }
                        });
                        return;
                    }
                }

                    //When accessing Bukkit, needs to be within Synchronized
                    Bukkit.getScheduler().runTask(Core.getInstance(), new Runnable() {
                        @Override
                        public void run() {

                            OfflinePlayer targetPlayer;
                            try {
                                targetPlayer = offlinePlayerFuture.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                p.sendMessage(ChatColor.RED + locale.getHouseUntrustError());
                                return;
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                                p.sendMessage(ChatColor.RED + locale.getHouseUntrustError());
                                return;
                            }
                            if (!targetPlayer.hasPlayedBefore()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUntrustNeverPlayedBefore().replaceAll("%PLAYER%",args[1])));
                                return;
                            }

                            HashSet<UUID> trustedUUIDs = plot.getTrusted();
                            UUID playersUUID = targetPlayer.getUniqueId();

                            //Check if ! trusted
                            if (!trustedUUIDs.contains(playersUUID)) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUntrustNotTrusted().replaceAll("%PLAYER%",args[1])));
                                return;
                            }

                            //Not trusted, add to trusted
                            plot.removeTrusted(playersUUID);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + locale.getHouseUntrustSuccess().replaceAll("%PLAYER%",args[1])));

                        }
                    });
                    return;

            }

        });

    }
    }
