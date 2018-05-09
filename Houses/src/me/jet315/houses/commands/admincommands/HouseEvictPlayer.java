package me.jet315.houses.commands.admincommands;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.SchematicHandler;
import me.jet315.houses.Core;
import me.jet315.houses.commands.CommandExecutor;
import me.jet315.houses.events.HouseUnclaimEvent;
import me.jet315.houses.utils.UnclaimReason;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jet on 09/02/2018.
 */
public class HouseEvictPlayer extends CommandExecutor {

    /**
     * Force remove a players house
     */

    public HouseEvictPlayer() {
        setCommand("delete");
        setPermission("house.admin.delete");
        setLength(2);
        setBoth();
        setUsage("/house delete <player>");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cPlayer '" + args[1] + "' is not online"));
            return;
        }
        UUID uuid = targetPlayer.getUniqueId();
        Set<Plot> plots = PS.get().getPlots(uuid);
        if (plots.size() > 0) {
            Plot plot = plots.iterator().next();
            //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
            HouseUnclaimEvent houseUnclaimEvent = new HouseUnclaimEvent(targetPlayer, plot, UnclaimReason.EVICTED);
            Core.getInstance().getServer().getPluginManager().callEvent(houseUnclaimEvent);
            plot.deletePlot(null);

            if(!Core.getInstance().getProperties().getSchematicToPasteonExpiry().equalsIgnoreCase("none")){
                SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(),"schematics/"+Core.getInstance().getProperties().getSchematicToPasteonExpiry()));
                SchematicHandler.manager.paste(schematic, plot,Core.getInstance().getProperties().getMoveExpirySchematicXDirection(),Core.getInstance().getProperties().getMoveExpirySchematicYDirection(),Core.getInstance().getProperties().getMoveExpirySchematicZDirection(),true,null);
            }

            if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(targetPlayer)){
                Core.getInstance().getPlayerManager().getHousePlayerMap().remove(targetPlayer);
            }

            Core.getInstance().getDb().deleteRecord(uuid.toString());

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&aSuccessfully deleted the players '" + args[1] + "' house"));


        }else{
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Core.getInstance().getProperties().getPluginPrefix() + "&cPlayer '" + args[1] + "' has no houses!"));
        }
    }

}
