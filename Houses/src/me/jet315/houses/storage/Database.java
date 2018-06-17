package me.jet315.houses.storage;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.util.SchematicHandler;
import me.jet315.houses.Core;
import me.jet315.houses.events.HouseUnclaimEvent;
import me.jet315.houses.manager.HousePlayer;
import me.jet315.houses.utils.UnclaimReason;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Created by Jet on 28/01/2018.
 */
public abstract class Database {

    Core plugin;
    Connection connection;
    // The name of the table we created back in SQLite class.
    public String table = "houses";

    public Database(Core instance, String tableName) {
        this.table = tableName;
        this.plugin = instance;
        initialize();
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table);
            ResultSet rs = ps.executeQuery();
            if(plugin.getProperties().getDeleteExpiredHousesOnStartup() && plugin.getProperties().isShouldHousesExpire()) {
                long currentMilliseconds = System.currentTimeMillis();
                ArrayList<UUID> uuids = new ArrayList<>();
                while (rs.next()) {
                    if(rs.getLong("milliseconds_of_expire") < currentMilliseconds){
                        //plot has expired
                        UUID uuid = UUID.fromString(rs.getString("uuid"));
                        uuids.add(uuid);
                        Set<Plot> plots = PS.get().getPlots(uuid);
                        if(plots.size() > 0){
                            Plot plot = plots.iterator().next();
                            //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
                            HouseUnclaimEvent houseUnclaimEvent = new HouseUnclaimEvent(Bukkit.getOfflinePlayer(uuid),plot, UnclaimReason.TIME_EXPIRY);
                            Core.getInstance().getServer().getPluginManager().callEvent(houseUnclaimEvent);
                            plot.deletePlot(null);
                            if(!Core.getInstance().getProperties().getSchematicToPasteonExpiry().equalsIgnoreCase("none")){
                                SchematicHandler.Schematic schematic = SchematicHandler.manager.getSchematic(new File(Core.getInstance().getDataFolder(),"schematics/"+Core.getInstance().getProperties().getSchematicToPasteonExpiry()));
                                SchematicHandler.manager.paste(schematic, plot,Core.getInstance().getProperties().getMoveExpirySchematicXDirection(),Core.getInstance().getProperties().getMoveExpirySchematicYDirection(),Core.getInstance().getProperties().getMoveExpirySchematicZDirection(),true,null);
                            }
                        }
                    }
                }

                if(uuids.size() != 0) {
                    for(UUID uuid : uuids){
                        PreparedStatement deleteRow = connection.prepareStatement("DELETE FROM " + table + " WHERE uuid='" + uuid +"'");
                        deleteRow.execute();
                        deleteRow.close();
                    }
                    System.out.println(uuids.size() + " Houses have expired and been purged!");
                }
            }
            close(ps, rs);
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "Unable to retrieve connection (First time starting the plugin?)", ex);
        }
    }

    // Loads values from databse
    public void loadHouseValues(Player p) {
        String playersUUID = p.getUniqueId().toString();
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                ResultSet rs;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE uuid = '" + playersUUID + "';");

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        long milisecondTillExpire = rs.getLong("milliseconds_of_expire");
                        //Plot has expired, delete plot
                        if(milisecondTillExpire < System.currentTimeMillis() && Core.getInstance().getProperties().isShouldHousesExpire()){
                            Bukkit.getScheduler().runTask(Core.getInstance(),new Runnable(){
                                @Override
                                public void run() {
                                    UUID uuid = UUID.fromString(playersUUID);
                                    Set<Plot> plots = PS.get().getPlots(uuid);
                                    if(plots.size() > 0){
                                        Plot plot = plots.iterator().next();
                                        //Create, and trigger the HouseClaimCommand so others are able to have a say in what happens
                                        HouseUnclaimEvent houseUnclaimEvent = new HouseUnclaimEvent(p,plot, UnclaimReason.TIME_EXPIRY);
                                        Core.getInstance().getServer().getPluginManager().callEvent(houseUnclaimEvent);
                                        plot.deletePlot(null);
                                    }
                                }
                            });
                            return;
                        }
                        HousePlayer housePlayer = new HousePlayer(p,rs.getInt("house_level"),rs.getBoolean("house_locked"),milisecondTillExpire,false);
                        Core.getInstance().getPlayerManager().getHousePlayerMap().put(p,housePlayer);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
/*                        if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Called when a house is first created
     * @param playersUUID The players UUID
     * @param houseLevel The players house level
     * @param isHouseLocked Whether the house is locked or not
     * @param milsecondsTillExpire The time until it expires
     */
    public void setHouseValues(String playersUUID, int houseLevel, boolean isHouseLocked, long milsecondsTillExpire) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("REPLACE INTO " + table + " (uuid,house_level,house_locked,milliseconds_of_expire) VALUES(?,?,?,?)");
                    ps.setString(1, playersUUID);
                    ps.setInt(2, houseLevel);
                    ps.setBoolean(3, isHouseLocked);
                    ps.setLong(4, milsecondsTillExpire);
                    ps.executeUpdate();
                    return;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
/*                        if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });

    }

    /**
     * Called when the house lock status is changed
     * @param playersUUID The players UUID
     * @param houseLocked Whether the hosue is locked or not
     */
    public void setHouseLocked(String playersUUID, boolean houseLocked) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("UPDATE " + table + " SET house_locked='" + (houseLocked ? "1": "0") + "' WHERE uuid='" + playersUUID + "'");
                    ps.executeUpdate();
                    return;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
/*                        if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });
    }


    /**
     * Called when level of the house is changed
     * @param playersUUID The players UUID
     * @param houseLevel the new level of the house
     */
    public void setHouseLevel(String playersUUID, int houseLevel) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("UPDATE " + table + " SET house_level='" + houseLevel + "' WHERE uuid='" + playersUUID + "'");
                    ps.executeUpdate();
                    return;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
  /*                      if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });
    }

    /**
     * Sets the time in the future (in milliseconds since epoch) of when the house expires
     * @param playersUUID The players UUID
     * @param timeOfExpiry The time in the future of when the house expires
     */
    public void setHouseRentalTime(String playersUUID, long timeOfExpiry) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("UPDATE " + table + " SET milliseconds_of_expire='" + timeOfExpiry + "' WHERE uuid='" + playersUUID + "'");
                    ps.executeUpdate();
                    return;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
       /*                 if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });
    }

    /**
     * Deletes a players record from the database
     * @param UUID The players UUID
     */
    public void deleteRecord(String UUID) {
        Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                PreparedStatement ps = null;
                try {
                    conn = connection;
                    ps = conn.prepareStatement("DELETE FROM " + table + " WHERE uuid='" + UUID +"'");
                    ps.execute();
                    return;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (ps != null)
                            ps.close();
/*                        if (conn != null)
                            conn.close();*/
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });
    }

    /**
     *
     * @param playersUUID Needs to be called asyncly!
     * @return
     */
    public Boolean isHouseLocked(String playersUUID) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        try {

            conn = connection;
            ps = conn.prepareStatement("SELECT house_locked FROM " + table + " WHERE uuid = '" + playersUUID + "';");

            rs = ps.executeQuery();

            while (rs.next()) {

                return rs.getBoolean("house_locked");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
/*                if (conn != null)
                    conn.close();*/
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

/*    ExecutorService executor = Executors.newCachedThreadPool();
    Future<Boolean> future = executor.submit(new Callable<Boolean>() {

        @Override
        public Boolean call() throws Exception {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs;
            try {
                System.out.println("doing connection");
                conn = getSQLConnection();
                System.out.println(playersUUID);
                ps = conn.prepareStatement("SELECT house_locked FROM " + table + " WHERE uuid = '" + playersUUID + "';");

                rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.println("returning connection");
                    System.out.println("returning "+ rs.getBoolean("house_locked"));
                    return rs.getBoolean("house_locked");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (ps != null)
                        ps.close();
                    if (conn != null)
                        conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return future;
        }
    });
        return future;*/




    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null)
                ps.close();
            if (rs != null)
                rs.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
