package me.chaseoes.tf2.utilities;

import org.bukkit.ChatColor;

import me.chaseoes.tf2.MessagesFile;
import me.chaseoes.tf2.TF2;

public class Localizer {
    
    private TF2 plugin;
    static Localizer instance = new Localizer();

    private Localizer() {

    }

    public static Localizer getLocalizer() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }
    
    public String loadMessage(String name) {
        return ChatColor.translateAlternateColorCodes('&', MessagesFile.getMessages().getMessagesFile().getString(name));
    }
    
    public String loadPrefixedMessage(String name) {
        return ChatColor.YELLOW + "[TF2] " + ChatColor.translateAlternateColorCodes('&', MessagesFile.getMessages().getMessagesFile().getString(name));
    }

}
