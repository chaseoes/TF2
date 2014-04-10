package com.chaseoes.tf2.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chaseoes.tf2.utilities.Localizer;

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

    public void wrongArgs(String usage) {
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("WRONG-ARGS"));
        sender.sendMessage(Localizer.getLocalizer().loadMessage("WRONG-ARGS-USAGE").replace("%usage", usage));
    }

    public void unknownCommand() {
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("UNKNOWN-COMMAND"));
        sender.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("UNKNOWN-COMMAND-HELP"));
    }

}
