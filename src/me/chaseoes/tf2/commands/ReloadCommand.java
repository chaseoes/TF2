package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand {

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
        plugin.reloadConfig();
        plugin.saveConfig();
        GameUtilities.getUtilities().plugin.saveConfig();
        DataConfiguration.getData().reloadData();
        DataConfiguration.getData().reloadData();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            TF2.getInstance().getMap(map).load();
        }
        cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully reloaded the configuration.");
    }

}
