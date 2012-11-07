package me.chaseoes.tf2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandHelper {

    CommandSender sender;
    Command cmd;

    public CommandHelper(CommandSender cs, Command cm) {
        sender = cs;
        cmd = cm;
    }

    public void noPermission() {
        sender.sendMessage("§e[TF2] You don't have permission for that.");
    }

    public void noConsole() {
        sender.sendMessage("You must be a player to do that!");
    }

    public void wrongArgs() {
        sender.sendMessage("§e[TF2] Incorrect command usage!");
        sender.sendMessage("§eUsage: §o" + cmd.getUsage());
    }
    
    public void unknownCommand() {
        sender.sendMessage("§e[TF2] Unknown command!");
        sender.sendMessage("§e[TF2] Type §6/tf2 help §efor help.");
    }

}
