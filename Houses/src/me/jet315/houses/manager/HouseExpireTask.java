package me.jet315.houses.manager;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.util.BukkitUtil;
import me.jet315.houses.Core;
import me.jet315.houses.events.HouseUnclaimEvent;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.utils.UnclaimReason;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Jet on 08/02/2018.
 */
public class HouseExpireTask {
    /**
     * Calculates plot expire time (Only for online players)
     */

    //Stores instances
    private Core instance;
    private Locale locale;
    private PlayerManager playerManager;

    public HouseExpireTask(Core instance,PlayerManager playerManager){
        this.instance = instance;
        this.locale = instance.getMessages();
        this.playerManager = playerManager;
        runTask();
    }

    //Runs every 5 seconds
    public void runTask(){

        Bukkit.getScheduler().runTaskTimer(instance, new Runnable() {
            @Override
            public void run() {
                for (HousePlayer housePlayer : playerManager.getHousePlayerMap().values()) {
                    //Reduce by 5 seconds
                    /*long expiryTime = playerManager.getMillisecondsTillExpiry().get(uuid);
                    playerManager.getMillisecondsTillExpiry().put(uuid, expiryTime);*/
                    long expiryTime = housePlayer.getMillisecondsOfExpiry();
                    expiryTime = expiryTime - System.currentTimeMillis();
                    //TODO: replace with configurable times from the config
                    if (expiryTime > 600000) continue;

                    //10 Minute warning
                    if (expiryTime < 600000 && expiryTime > 595000) {
                        housePlayer.getPlayer().sendMessage(instance.getProperties().getPluginPrefix() + locale.getHouseExpireMessage().replaceAll("%MINUTES%","10"));
                        continue;
                    }
                    //8 Minute warning
                    if (expiryTime < 480000 && expiryTime > 475000) {
                        housePlayer.getPlayer().sendMessage(instance.getProperties().getPluginPrefix() + locale.getHouseExpireMessage().replaceAll("%MINUTES%","8"));
                        continue;
                    }
                    //6 Minute warning
                    if (expiryTime < 360000 && expiryTime > 355000) {
                        housePlayer.getPlayer().sendMessage(instance.getProperties().getPluginPrefix() + locale.getHouseExpireMessage().replaceAll("%MINUTES%","6"));
                        continue;
                    }
                    //4 Minute warning
                    if (expiryTime < 240000 && expiryTime > 235000) {
                        housePlayer.getPlayer().sendMessage(instance.getProperties().getPluginPrefix() + locale.getHouseExpireMessage().replaceAll("%MINUTES%","4"));
                        continue;
                    }
                    //2 Minute warning
                    if (expiryTime < 120000 && expiryTime > 115000) {
                        housePlayer.getPlayer().sendMessage(instance.getProperties().getPluginPrefix() + locale.getHouseExpireMessage().replaceAll("%MINUTES%","2"));
                        continue;
                    }
/*                    //1 Minute warning
                    if (expiryTime < 60000 && expiryTime > 55000) {
                        Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&c&lYour house will expire in 1 Minutes &6/house rent"));
                        continue;
                    }*/

                    //Delete house
                    if (expiryTime <= 0) {
                        Core.getInstance().getPlayerManager().getHousePlayerMap().remove(housePlayer.getPlayer());

                        Set<Plot> plots = PS.get().getPlots(housePlayer.getPlayer().getUniqueId());
                        if (plots.size() > 0) {
                            Plot plot = plots.iterator().next();
                            //Create, and trigger the HouseClaimEvent so others are able to have a say in what happens
                            HouseUnclaimEvent houseUnclaimEvent = new HouseUnclaimEvent(housePlayer.getPlayer(), plot, UnclaimReason.TIME_EXPIRY);
                            Core.getInstance().getServer().getPluginManager().callEvent(houseUnclaimEvent);
                            plot.deletePlot(null);

                            Core.getInstance().getDb().deleteRecord(housePlayer.getPlayer().getUniqueId().toString());
                            housePlayer.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&f&lYour house has been deleted as you did not pay rent"));
                        }

                    }
                }
            }
        },0,100L);
    }



}
