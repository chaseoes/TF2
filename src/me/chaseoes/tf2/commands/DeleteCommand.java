package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DeleteCommand {

    @SuppressWarnings("unused")
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
                if (!TF2.getInstance().mapExists(strings[2])) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + strings[2] + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                    return;
                }
                TF2.getInstance().removeMap(strings[2]);
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully deleted the map " + strings[2] + ".");

            } else {
                h.wrongArgs();
            }
        } else {
            h.unknownCommand();
        }
    }

}
