package me.chaseoes.tf2.commands;

import org.bukkit.ChatColor;
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
        sender.sendMessage(ChatColor.YELLOW + "[TF2] You don't have permission for that.");
    }

    public void noConsole() {
        sender.sendMessage("You must be a player to do that!");
    }

    public void wrongArgs() {
        sender.sendMessage(ChatColor.YELLOW + "[TF2] Incorrect command usage!");
        sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.ITALIC + cmd.getUsage());
    }

    public void unknownCommand() {
        sender.sendMessage(ChatColor.YELLOW + "[TF2] Unknown command!");
        sender.sendMessage(ChatColor.YELLOW + "[TF2] Type " + ChatColor.GOLD + "/tf2 help " + ChatColor.YELLOW + "for help.");
    }

}
