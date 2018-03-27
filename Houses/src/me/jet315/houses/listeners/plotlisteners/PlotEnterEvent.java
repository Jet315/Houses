package me.jet315.houses.listeners.plotlisteners;

import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.plotsquared.bukkit.events.PlayerEnterPlotEvent;
import me.jet315.houses.Core;
import me.jet315.houses.events.HouseEnterEvent;
import me.jet315.houses.events.HouseUpgradeEvent;
import me.jet315.houses.manager.HousePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Created by Jet on 28/01/2018.
 */
public class PlotEnterEvent implements Listener{

    @EventHandler
    public void onPlayerPlotEnter(PlayerEnterPlotEvent e) {
        //Create, and trigger the HouseEnterEvent so others are able to have a say in what happens
        HouseEnterEvent houseEnterEvent = new HouseEnterEvent(e.getPlayer(),e.getPlot());
        Core.getInstance().getServer().getPluginManager().callEvent(houseEnterEvent);

        Set<UUID> plotOwner = e.getPlot().getOwners();
        if(plotOwner.size() == 0){
            return;
        }
        Player p = e.getPlayer();
        //Load the plot owners UUID into varible
        String plotOwnersUUID = plotOwner.iterator().next().toString();

        /**
         * Check to see if plot is being upgraded
         */
        //Check to see if upgrading
        if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p) && Core.getInstance().getPlayerManager().getHousePlayerMap().get(e.getPlayer()).getIsHouseBeingUpgraded()){
            blockPlayerFromEnteringPlot(e.getPlot(),p);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getHouseUpgrading()));
        }
        /**
         * Check to see person entering plot is plot owner, if so return
         */
        if(p.getUniqueId().toString().equals(plotOwnersUUID)) return;

        /**
         * Check to see if person enetering plot is trusted, if so return
         */
        for(UUID trustedUUIDs : e.getPlot().getTrusted()){
            if(trustedUUIDs.equals(p.getUniqueId())) return;
        }
        /**
         * Check to see if user is online
         */
        Player plotPlayer = null;
        for(HousePlayer housePlayer : Core.getInstance().getPlayerManager().getHousePlayerMap().values()){
            if(housePlayer.getPlayer().getUniqueId().toString().equalsIgnoreCase(plotOwnersUUID)){
                plotPlayer = housePlayer.getPlayer();
                break;
            }
        }
        if(plotPlayer != null){
            //Check to see if locked
            if(Core.getInstance().getPlayerManager().getHousePlayerMap().get(plotPlayer).getIsHouseLocked()) {
                //House is locked, add to block list then return
                blockPlayerFromEnteringPlot(e.getPlot(), p);
            }

            //House is not locked, return
            return;
        }

        //Returns a future to whether the house is locked or not
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Callable<Boolean> task = new Callable<Boolean>() {
            public Boolean call() throws Exception {

                return Core.getInstance().getDb().isHouseLocked(plotOwnersUUID);
            }
        };
        //Execute the future
        Future<Boolean> future = executorService.submit(task);
        //Async task to wait for Future to return
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                int counter = 1;
                while (!future.isDone() && counter != 20) {
                    counter++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                //An error has occurred pretty much if this happens
                if(counter == 20){
                    future.cancel(true);
                    return;
                }
                //Must be done
                try {
                    boolean houseLocked = future.get();
                    //If house is not locked, return, players are allowed to enter
                    if(!houseLocked){
                        return;
                    }
                    //House is locked, kick players out - This must be Synchronised
                    //When accessing Bukkit, needs to be within Synchronized

                    //Could boost out plot - get pos one wait get pos 2, work out diff, reverse
                    Bukkit.getScheduler().runTask(Core.getInstance(),new Runnable(){
                        @Override
                        public void run() {
                            blockPlayerFromEnteringPlot(e.getPlot(),p);
                        }
                    });

                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();

                }

            }
        });

        //Get the plot owner, see if online, pull from database whether private is on/off

        //Will have to deny people I think (add them to plot deny list)
    }

    public void blockPlayerFromEnteringPlot(Plot plot, Player p){
        if(Core.serverVersion.startsWith("v1_12")) {
            p.sendTitle(Core.getInstance().getMessages().getDeniedTitle(), Core.getInstance().getMessages().getDeniedSubTitle(), 30, 60, 30);
        }else{
            p.sendTitle(Core.getInstance().getMessages().getDeniedTitle(), Core.getInstance().getMessages().getDeniedSubTitle());
        }
        //e.getPlot().addDenied(p.getUniqueId());
        Location plotLoc = plot.getCenter();
        org.bukkit.Location plotMiddle = new org.bukkit.Location(Bukkit.getWorld(plotLoc.getWorld()),plotLoc.getX(),plotLoc.getY(),plotLoc.getZ());
        if(p.isFlying()){
            //Needs a slightly bigger booster if they are flying
            p.setVelocity(p.getLocation().toVector().subtract(plotMiddle.toVector()).divide(new Vector(10,0,10)).setY(0.5));
            if(Core.serverVersion.startsWith("v1_12")) {
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1, 1);
            }
        }else{
            p.setVelocity(p.getLocation().toVector().subtract(plotMiddle.toVector()).divide(new Vector(15,0,15)).setY(0.5));
            if(Core.serverVersion.startsWith("v1_12")) {
                p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 1, 1);
            }
        }
    }
}
