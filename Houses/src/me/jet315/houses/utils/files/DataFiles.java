package me.jet315.houses.utils.files;

import me.jet315.houses.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataFiles {
    /**
     * Stores the configuration object for the plugin
     */
    private FileConfiguration config;

    /**
     * Stores the configuration object for the gui file
     */
    private FileConfiguration guiConfig;

    public DataFiles(Core instance){
        createPluginConfig(instance);
        createGuiConfig(instance);
        loadYamls(instance);
    }


    private void createPluginConfig(Core instance) {
        try {
            if (!instance.getDataFolder().exists()) {
                instance.getDataFolder().mkdirs();
            }
            File file = new File(instance.getDataFolder(), "config.yml");
            if (!file.exists()) {
                instance.getLogger().info("Config.yml not found, creating!");
                instance.saveDefaultConfig();
            } else {
                instance.getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void createGuiConfig(Core instance) {
        try {
            if (!instance.getDataFolder().exists()) {
                instance.getDataFolder().mkdirs();
            }
            File file = new File(instance.getDataFolder(), "guiconfig.yml");
            if (!file.exists()) {
                instance.getLogger().info("guiconfig.yml not found, creating!");
                instance.saveResource("guiconfig.yml",false);
            } else {
                instance.getLogger().info("guiconfig.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private void loadYamls(Core instance){
        config = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(),"config.yml"));
        guiConfig = YamlConfiguration.loadConfiguration(new File(instance.getDataFolder(),"guiconfig.yml"));
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getGuiConfig() {
        return guiConfig;
    }
}
