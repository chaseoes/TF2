package me.chaseoes.tf2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MapConfiguration {

    private TF2 plugin;
    static MapConfiguration instance = new MapConfiguration();
    public HashMap<String, FileConfiguration> customConfig = new HashMap<String, FileConfiguration>();
    public HashMap<String, File> customConfigFile = new HashMap<String, File>();

    private MapConfiguration() {

    }

    public static MapConfiguration getMaps() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void reloadMap(String mapname) {
        if (customConfigFile.get(mapname) == null) {
            customConfigFile.put(mapname, new File(plugin.getDataFolder(), mapname + ".yml"));
        }

        customConfig.put(mapname, YamlConfiguration.loadConfiguration(customConfigFile.get(mapname)));

        @SuppressWarnings("resource")
        InputStream defConfigStream = plugin.getResource(mapname + ".yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            customConfig.get(mapname).setDefaults(defConfig);
        }
    }

    public FileConfiguration getMap(String mapname) {
        if (customConfig.get(mapname) == null) {
            this.reloadMap(mapname);
        }
        return customConfig.get(mapname);
    }

    public void saveMap(String mapname) {
        if (customConfig.get(mapname) == null || customConfigFile.get(mapname) == null) {
            return;
        }
        try {
            getMap(mapname).save(customConfigFile.get(mapname));
        } catch (IOException ex) {

        }
    }

}
