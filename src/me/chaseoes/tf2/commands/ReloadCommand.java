package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

    @SuppressWarnings("unused")
    private TF2 plugin;
    static ReloadCommand instance = new ReloadCommand();

    private ReloadCommand() {

    }

    public static ReloadCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execReloadCommand(CommandSender cs, String[] strings, Command cmnd) {
        GameUtilities.getUtilities().reloadConfig();
        GameUtilities.getUtilities().plugin.saveConfig();
        DataConfiguration.getData().reloadData();
        DataConfiguration.getData().reloadData();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            MapConfiguration.getMaps().reloadMap(map);
            MapConfiguration.getMaps().saveMap(map);
        }
        cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully reloaded the configuration.");
    }

}
