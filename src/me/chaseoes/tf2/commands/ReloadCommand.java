package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.MessagesFile;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;

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
        MessagesFile.getMessages().reloadMessages();
        MessagesFile.getMessages().saveMessages();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            TF2.getInstance().getMap(map).load();
        }
        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CONFIG-RELOADED"));
    }

}
