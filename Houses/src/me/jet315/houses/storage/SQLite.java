package me.jet315.houses.storage;

import me.jet315.houses.Core;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * Created by Jet on 28/01/2018.
 */
public class SQLite extends Database{


    public SQLite(Core instance,String database){
        super(instance,database);
    }

    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS "+table+" (" +
            "`uuid` varchar(32) NOT NULL," + // UUID
            "`house_level` int(4) NOT NULL," + //House level
            "`house_locked` BOOLEAN NOT NULL," + //House level
            "`milliseconds_of_expire` int(64) NOT NULL," + //Stored as milliseconds since epoch of time expire
            "PRIMARY KEY (`uuid`)" +
            ");";


    // SQL creation stuff
    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), table+".db");
        if (!dataFolder.exists()){
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "File write error: "+table+".db");
            }
        }
        try {
            if(connection!=null&&!connection.isClosed()){
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
