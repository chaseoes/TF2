package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.utilities.Localizer;
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
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("NO-PERMISSION"));
    }

    public void noConsole() {
        sender.sendMessage(Localizer.getLocalizer().loadMessage("NO-CONSOLE"));
    }

    public void wrongArgs() {
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("WRONG-ARGS"));
        sender.sendMessage(ChatColor.YELLOW + "Usage: " + ChatColor.ITALIC + cmd.getUsage());
    }

    public void unknownCommand() {
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("UNKNOWN-COMMAND"));
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("UNKNOWN-COMMAND-HELP"));
    }

}
