package me.chaseoes.tf2;

import java.io.*;
import java.nio.charset.Charset;

import com.google.common.io.Files;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesFile {

    private TF2 plugin;
    static MessagesFile instance = new MessagesFile();
    private YamlConfiguration customConfig = null;
    private File customConfigFile = null;

    private MessagesFile() {

    }

    public static MessagesFile getMessages() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public boolean reloadMessages() {
        try {
            if (customConfigFile == null) {
                customConfigFile = new File(plugin.getDataFolder(), "messages.yml");
                customConfigFile.createNewFile();
            }

            customConfig = new YamlConfiguration();
            customConfig.loadFromString(Files.toString(customConfigFile, Charset.forName("UTF-8")));

            @SuppressWarnings("resource")
            InputStream defConfigStream = plugin.getResource("messages.yml");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            String data = customConfig.saveToString();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(customConfigFile), "UTF-8");
            out.write(data, 0, data.length());
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}