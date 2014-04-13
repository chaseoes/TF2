package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.localization.Localizers;
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
        TF2.getInstance().saveConfig();
        DataConfiguration.getData().reloadData();
        DataConfiguration.getData().reloadData();
        Localizers.getInstance().reload();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            TF2.getInstance().getMap(map).load();
        }
        Localizers.getDefaultLoc().CONFIG_RELOADED.sendPrefixed(cs);
    }

}
