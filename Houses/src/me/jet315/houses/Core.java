package me.jet315.houses;

import me.jet315.houses.commands.CommandHandler;
import me.jet315.houses.listeners.*;
import me.jet315.houses.listeners.plotlisteners.PlotClaimEvent;
import me.jet315.houses.listeners.plotlisteners.PlotEnterEvent;
import me.jet315.houses.manager.HouseExpireTask;
import me.jet315.houses.manager.PlayerManager;
import me.jet315.houses.utils.GUIProperties;
import me.jet315.houses.utils.LoadSchematics;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.storage.SQLite;
import me.jet315.houses.storage.Database;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jet on 27/01/2018.
 */
public class Core extends JavaPlugin{

    private static Core instance;

    private GUIProperties properties;
    private Locale messages;
    private Database db;
    private PlayerManager playerManager;

    private boolean isTokenManagerEnabled = false;

    /**
     * Vault - An API allowing me to get economy of players
     */
    private boolean isVaultEnabled = false;
    public static Economy economy = null;


    public void onEnable(){
        //Just cool knowing how long the plugin takes to enable
        long startTime = System.currentTimeMillis();
        System.out.println("\n[Houses] Initializing Plugin");
        instance = this;


        //Loads the config & default value
        properties = new GUIProperties(this.getConfig());

        //Load locale
        messages = new Locale(this,properties.getLocale());

        //PlayerManager
        playerManager = new PlayerManager();
        //Load schematics
        new LoadSchematics(this);

        //Register Listeners
        registerListeners();
        //Register Command
        getCommand("house").setExecutor(new CommandHandler());

        //setup economy
        setupEconomy();

        //dependencies
        registerDependencies();

        //House expire task
        new HouseExpireTask(instance,playerManager);

        //Database
        this.db = new SQLite(this,properties.getSqliteTable());
        this.db.load();
        
        System.out.println("[Houses] Initializing Complete - Time took " + String.valueOf(System.currentTimeMillis()-startTime) +"Ms\n");


/*        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), new Runnable() {
            int counter = 1;
            public void run() {

                System.out.println(counter++);
            }
        }, 0L, 1L);*/

        /**
         * /Reload support
         */
        for(Player p : Bukkit.getOnlinePlayers()){
            Core.getInstance().getDatabase().loadHouseValues(p);
        }

    }

    public void onDisable(){
        for(Player p : Bukkit.getOnlinePlayers()){
            if(Core.getInstance().getPlayerManager().getHousePlayerMap().containsKey(p)){
                Core.getInstance().getPlayerManager().getHousePlayerMap().remove(p);
            }
        }
/*        playerManager = null;
        properties = null;
        db = null;*/
        instance = null;
    }

    /**
     * Reloads the config
     */
    public void reloadProperties(){
        this.properties = null;
        this.messages = null;
        this.reloadConfig();
        this.properties = new GUIProperties(this.getConfig());
        this.messages = new Locale(this,properties.getLocale());
    }
    public static Core getInstance() {
        return instance;
    }

    public GUIProperties getProperties() {
        return properties;
    }
    public Database getDatabase() {
        return db;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void registerListeners(){
        //Player Listeners
        Bukkit.getPluginManager().registerEvents(new OnCommandEvent(),this);
        Bukkit.getPluginManager().registerEvents(new MineEvent(),this);
        Bukkit.getPluginManager().registerEvents(new PlaceEvent(),this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(),this);
        Bukkit.getPluginManager().registerEvents(new LeaveEvent(),this);
        Bukkit.getPluginManager().registerEvents(new GUIClickEvent(),this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(),this);

        //Plot listeners
        Bukkit.getPluginManager().registerEvents(new PlotEnterEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlotClaimEvent(), this);

    }

    public void registerDependencies(){
        //Possible Dependencies
        if(Bukkit.getPluginManager().isPluginEnabled("TokenManager")){
            isTokenManagerEnabled = true;
        }else{
            if(properties.getEconomyTypeToUpgrade().equalsIgnoreCase("tokens") || properties.getEconomyTypeForRenting().equalsIgnoreCase("tokens")){
                System.out.println(ChatColor.RED + "[HOUSES ERROR NOTICE] You have Tokens as an economy type enabled yet TokenManager is not installed!");
            }
        }
        //Possible Dependencies
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            isVaultEnabled = true;
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }
        }else{
            if(properties.getEconomyTypeToUpgrade().equalsIgnoreCase("vault") || properties.getEconomyTypeForRenting().equalsIgnoreCase("vault")){
                System.out.println(ChatColor.RED + "[HOUSES ERROR NOTICE] You have vault as an economy type enabled yet vault is not installed!");
            }
        }
    }

    /**
     * Sets the economy up
     */
    private boolean setupEconomy()
    {
        try {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                economy = economyProvider.getProvider();
            }

            return (economy != null);
        }catch(NoClassDefFoundError e){
            return false;
        }
    }

    public boolean isTokenManagerEnabled() {
        return isTokenManagerEnabled;
    }

    public boolean isVaultEnabled() {
        return isVaultEnabled;
    }

    public Locale getMessages() {
        return messages;
    }


    /**
     * When clicking on GUI elements check if max house/has perm
     */
}