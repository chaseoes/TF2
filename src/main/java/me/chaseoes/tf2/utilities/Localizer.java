package me.chaseoes.tf2.utilities;

import me.chaseoes.tf2.MessagesFile;

import org.bukkit.ChatColor;

public class Localizer {

    static Localizer instance = new Localizer();

    private Localizer() {

    }

    public static Localizer getLocalizer() {
        return instance;
    }

    public String loadMessage(String name) {
        return ChatColor.translateAlternateColorCodes('&', MessagesFile.getMessages().getMessagesFile().getString(name));
    }

    public String loadPrefixedMessage(String name) {
        return ChatColor.YELLOW + "[TF2] " + ChatColor.translateAlternateColorCodes('&', MessagesFile.getMessages().getMessagesFile().getString(name));
    }

}
