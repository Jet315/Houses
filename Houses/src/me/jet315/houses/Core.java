package me.jet315.houses;

import me.jet315.houses.commands.CommandHandler;
import me.jet315.houses.listeners.*;
import me.jet315.houses.listeners.plotlisteners.PlotClaimEvent;
import me.jet315.houses.listeners.plotlisteners.PlotEnterEvent;
import me.jet315.houses.manager.HouseExpireTask;
import me.jet315.houses.manager.PlayerManager;
import me.jet315.houses.utils.files.GUIProperties;
import me.jet315.houses.utils.LoadSchematics;
import me.jet315.houses.utils.Locale;
import me.jet315.houses.storage.SQLite;
import me.jet315.houses.storage.Database;
import me.realized.tokenmanager.api.TokenManager;
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
    public static String serverVersion;

    public static String userID = "%%__USER__%%";

    private GUIProperties properties;
    private Locale messages;
    private Database db;
    private PlayerManager playerManager;

    /**
     * Vault - An API allowing me to get economy of players
     */
    public static Economy economy = null;

    private boolean isTokenManagerEnabled = false;

    private boolean isTokenEnchantEnabled = false;
    public static TokenManager tokenManager;

    public void onEnable(){
        //Just cool knowing how long the plugin takes to enable
        long startTime = System.currentTimeMillis();
        System.out.println("\n[Houses] Initializing Plugin");
        instance = this;


        //Loads the config & default value
        properties = new GUIProperties(this);

        //Load locale
        messages = new Locale(this,properties.getLocale());
        serverVersion = Bukkit.getServer().getClass().getPackage().getName();
        serverVersion = serverVersion.substring(serverVersion.lastIndexOf(".") + 1);

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
        if(properties.isShouldHousesExpire()) {
            new HouseExpireTask(instance, playerManager);
        }
        //Database
        this.db = new SQLite(this,properties.getSqliteTable());
        this.db.load();

        //Placeholders
        if( Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            //Registering placeholder will be use here
            new PlaceHolderRequest().register();
        }
        
        System.out.println("[Houses] Initializing Complete in" + String.valueOf(System.currentTimeMillis()-startTime) + " Ms\n");


        /**
         * /Reload support
         */
        for(Player p : Bukkit.getOnlinePlayers()){
            Core.getInstance().getDb().loadHouseValues(p);
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
        this.properties = new GUIProperties(this);
        this.messages = new Locale(this,properties.getLocale());
    }
    public static Core getInstance() {
        return instance;
    }

    public GUIProperties getProperties() {
        return properties;
    }
    public Database getDb() {
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
        Bukkit.getPluginManager().registerEvents(new PistonEvent(),this);
        //Plot listeners
        Bukkit.getPluginManager().registerEvents(new PlotEnterEvent(), this);
        Bukkit.getPluginManager().registerEvents(new PlotClaimEvent(), this);

    }

    public void registerDependencies(){
        //Possible Dependencies
        if(Bukkit.getPluginManager().isPluginEnabled("TokenManager")){
            isTokenManagerEnabled = true;
            tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");
        }else{
            if(properties.getEconomyTypeToUpgrade().equalsIgnoreCase("tokens") || properties.getEconomyTypeForRenting().equalsIgnoreCase("tokens")){
                System.out.println(ChatColor.RED + "[HOUSES ERROR NOTICE] You have Tokens as an economy type enabled yet TokenManager is not installed!");
            }
        }

        if(Bukkit.getPluginManager().isPluginEnabled("TokenEnchant")){
            isTokenEnchantEnabled = true;
        }else{
            if(properties.getEconomyTypeToUpgrade().equalsIgnoreCase("tokenenchant") || properties.getEconomyTypeForRenting().equalsIgnoreCase("tokenenchant")){
                System.out.println(ChatColor.RED + "[HOUSES ERROR NOTICE] You have TokenEnchant as an economy type enabled yet TokenEnchant is not installed!");
            }
        }

        //Possible Dependencies
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
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


    public Locale getMessages() {
        return messages;
    }

    public boolean isTokenEnchantEnabled() {
        return isTokenEnchantEnabled;
    }


    /**
     * Updated
     * /locate command
     * Errors if typing /house find, /house trust, /house untrust with no parameter
     */
}
