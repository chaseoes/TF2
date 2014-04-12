package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
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
        Localizers.getDefaultLoc().NO_PERMISSION.sendPrefixed(sender);
    }

    public void noConsole() {
        Localizers.getDefaultLoc().NO_CONSOLE.send(sender);
    }

    public void wrongArgs(String usage) {
        Localizers.getDefaultLoc().WRONG_ARGS.sendPrefixed(sender);
        Localizers.getDefaultLoc().WRONG_ARGS_USAGE.send(sender, usage);
    }

    public void unknownCommand() {
        Localizers.getDefaultLoc().UNKNOWN_COMMAND.sendPrefixed(sender);
        Localizers.getDefaultLoc().UNKNOWN_COMMAND_HELP.sendPrefixed(sender);
    }

}
