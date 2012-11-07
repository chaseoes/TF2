package me.chaseoes.tf2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataConfiguration {

    private TF2 plugin;
    static DataConfiguration instance = new DataConfiguration();
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    private DataConfiguration() {

    }

    public static DataConfiguration getData() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void reloadData() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(plugin.getDataFolder(), "data.yml");
            }

            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

            @SuppressWarnings("resource")
            InputStream defConfigStream = plugin.getResource("data.yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
        } catch (Exception e) {

        }
    }

    public FileConfiguration getDataFile() {
        if (customConfig == null) {
            this.reloadData();
        }
        return customConfig;
    }

    public void saveData() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getDataFile().save(customConfigFile);
        } catch (IOException ex) {

        }
    }

}
