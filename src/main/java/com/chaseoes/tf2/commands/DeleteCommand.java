package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chaseoes.tf2.TF2;

public class DeleteCommand {

    private TF2 plugin;
    static DeleteCommand instance = new DeleteCommand();

    private DeleteCommand() {

    }

    public static DeleteCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execDeleteCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 0) {
            h.wrongArgs("/tf2 delete map <name>");
            return;
        }
        if (strings[1].equalsIgnoreCase("map")) {
            if (strings.length == 3) {
                if (!plugin.mapExists(strings[2])) {
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                    return;
                }
                plugin.removeMap(strings[2]);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_DELETED.sendPrefixed(cs, strings[2]);

            } else {
                h.wrongArgs("/tf2 delete map <name>");
            }
        } else {
            h.wrongArgs("/tf2 delete map <name>");
        }
    }

}
