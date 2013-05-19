package me.chaseoes.tf2.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;
import me.chaseoes.tf2.utilities.PastebinPoster;
import me.chaseoes.tf2.utilities.PastebinPoster.PasteCallback;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DebugCommand {

    private TF2 plugin;
    static DebugCommand instance = new DebugCommand();

    private DebugCommand() {

    }

    public static DebugCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execDebugCommand(final CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DEBUG-UPLOADING"));
        PastebinPoster.paste(getDebugInformation(), new PasteCallback() {

            @Override
            public void handleSuccess(String url) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DEBUG-SUCCESS"));
                cs.sendMessage(ChatColor.YELLOW + url);
            }

            @Override
            public void handleError(String err) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DEBUG-ERROR"));
            }
        });
    }

    public String getDebugInformation() {
        StringBuilder info = new StringBuilder();
        info.append("====== SERVER INFORMATION ======\n");
        info.append("TF2 Version: ").append(plugin.getDescription().getVersion()).append("\n");
        info.append("Bukkit Version: ").append(plugin.getServer().getVersion()).append("\n");
        StringBuilder plugins = new StringBuilder();
        for (int i = 0; i < plugin.getServer().getPluginManager().getPlugins().length; i++) {
            plugins.append(plugin.getServer().getPluginManager().getPlugins()[i]);
            if (i + 1 < plugin.getServer().getPluginManager().getPlugins().length) {
                plugins.append(", ");
            }
        }
        info.append("Plugins: ").append(plugins.toString().trim()).append("\n\n");
        info.append("====== TF2 CONFIGURATION ======\n");
        File config = new File(plugin.getDataFolder() + "/config.yml").getAbsoluteFile();
        File data = new File(plugin.getDataFolder() + "/data.yml").getAbsoluteFile();
        try {
            String confi = new Scanner(config).useDelimiter("\\A").next();
            confi = confi.replaceFirst("(password:)(\\s*)(\\S+)", "PASSWORD CENSORED");
            confi = confi.replaceFirst("(username:)(\\s*)(\\S+)", "USERNAME CENSORED");
            String df = new Scanner(data).useDelimiter("\\A").next();
            info.append(confi);
            info.append("\n====== DATA CONFIGURATION ======\n");
            info.append(df);
            for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
                try {
                    File mf = new File(plugin.getDataFolder() + "/" + map + ".yml").getAbsoluteFile();
                    String mapfile = new Scanner(mf).useDelimiter("\\A").next();
                    info.append("\n====== ").append(map).append(" MAP CONFIGURATION ======\n");
                    info.append(mapfile);
                } catch (Exception e) {
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return info.toString();
    }

}
