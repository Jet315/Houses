package me.jet315.houses.listeners;

import me.jet315.houses.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Created by Jet on 10/02/2018.
 */
public class ChatEvent implements Listener{
    /**
     * Used to confirm players actions
     */

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        if(Core.getInstance().getPlayerManager().getConfirmation().containsKey(e.getPlayer().getName())){
            e.setCancelled(true);

            Bukkit.getScheduler().runTask(Core.getInstance(), new Runnable() {
                @Override
                public void run() {

                    //Player is confirming a confirmation message, run the command in the hashmap
                    if(e.getMessage().toLowerCase().contains("confirm")){
                        e.getPlayer().performCommand(Core.getInstance().getPlayerManager().getConfirmation().get(e.getPlayer().getName()));
                        Core.getInstance().getPlayerManager().getConfirmation().remove(e.getPlayer().getName());
                        return;

                    }
                    //Check if player is canceling a message - Already removed from hashmap
                    else if(e.getMessage().toLowerCase().contains("cancel")){
                        Core.getInstance().getPlayerManager().getConfirmation().remove(e.getPlayer().getName());
                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + Core.getInstance().getMessages().getCommandCanceled()));
                        return;
                    }
                    //Check if it is a find/trust player command, append to string and execute command
                    else {
                        String commandToExecute = Core.getInstance().getPlayerManager().getConfirmation().get(e.getPlayer().getName());
                        System.out.println(commandToExecute);
                        if(commandToExecute.contains("trust") || commandToExecute.contains("find")){
                            e.getPlayer().performCommand(commandToExecute.concat(e.getMessage()));
                            Core.getInstance().getPlayerManager().getConfirmation().remove(e.getPlayer().getName());
                            return;
                        }
                    }


                }
            });


        }
    }

}
