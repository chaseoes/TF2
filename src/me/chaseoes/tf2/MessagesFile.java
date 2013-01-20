package me.chaseoes.tf2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesFile {

    private TF2 plugin;
    static MessagesFile instance = new MessagesFile();
    private FileConfiguration customConfig = null;
    private File customConfigFile = null;

    private MessagesFile() {

    }

    public static MessagesFile getMessages() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void reloadMessages() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(plugin.getDataFolder(), "messages.yml");
            }

            customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

            @SuppressWarnings("resource")
            InputStream defConfigStream = plugin.getResource("messages.yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
        } catch (Exception e) {

        }
    }

    public FileConfiguration getMessagesFile() {
        if (customConfig == null) {
            this.reloadMessages();
        }
        return customConfig;
    }

    public void saveMessages() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getMessagesFile().save(customConfigFile);
        } catch (IOException ex) {

        }
    }

}