package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        if (strings[1].equalsIgnoreCase("map")) {
            if (strings.length == 3) {
                if (!plugin.mapExists(strings[2])) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[2]));
                    return;
                }
                plugin.removeMap(strings[2]);
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-DELETED").replace("%map", strings[2]));

            } else {
                h.wrongArgs();
            }
        } else {
            h.unknownCommand();
        }
    }

}
