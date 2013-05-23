package me.chaseoes.tf2.utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;

import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UpdateChecker {

    TF2 plugin;
    private int[] latestVersion;
    private int[] currentVersion;

    public UpdateChecker(TF2 p) {
        plugin = p;
        latestVersion = versionToIntArray(plugin.getDescription().getVersion());
        currentVersion = versionToIntArray(plugin.getDescription().getVersion());
    }

    public boolean needsUpdate() {
        for (int i = 0; i < latestVersion.length; i++) {
            if (i < currentVersion.length) {
                if (latestVersion[i] > currentVersion[i]) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public void nagPlayer(Player player) {
        player.sendMessage(ChatColor.YELLOW + "[TF2]" + ChatColor.DARK_RED + " Version " + versionToString(latestVersion) + " is available! Please update ASAP.");
        player.sendMessage(ChatColor.RED + "http://dev.bukkit.org/server-mods/team-fortress-2/");
    }

    public void startTask() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                checkForUpdate();
            }
        }, 0L, 12000L);
    }

    public void checkForUpdate() {
        if (plugin.getConfig().getBoolean("update-checking")) {
            try {
                final URL url = new URL("http://chaseoes.com/plugins/TF2.version");
                InputStream i = url.openStream();
                Scanner scan = new Scanner(i);
                String ver = scan.nextLine();
                i.close();
                if (ver.equalsIgnoreCase("0.0")) {
                    latestVersion = versionToIntArray(plugin.getDescription().getVersion());
                } else {
                    latestVersion = versionToIntArray(ver);
                }
                return;
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "An error was encountered while attempting to check for updates.");
            }
        }
        latestVersion = versionToIntArray(plugin.getDescription().getVersion());
    }

    public int[] versionToIntArray(String version) {
        String[] number = version.split("\\-");
        String[] parts = number[0].split("\\.");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Integer.parseInt(parts[i]);
        }
        return numbers;
    }

    public String versionToString(int[] version) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < version.length; i++) {
            builder.append(version[i]);
            if ((i + 1) < version.length) {
                builder.append(".");
            }
        }
        return builder.toString();
    }

}
