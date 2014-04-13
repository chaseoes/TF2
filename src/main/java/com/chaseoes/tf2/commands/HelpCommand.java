package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    static HelpCommand instance = new HelpCommand();

    public static HelpCommand getCommand() {
        return instance;
    }

    public void execHelpCommand(CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "]" + ChatColor.DARK_AQUA + " Team Fortress 2 Help " + ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "]");
        if (cs.hasPermission("tf2.play")) {
            Localizers.getDefaultLoc().HELP_JOIN.send(cs);
            Localizers.getDefaultLoc().HELP_LEAVE.send(cs);
            Localizers.getDefaultLoc().HELP_LIST.send(cs);
        }
        if (cs.hasPermission("tf2.start") || cs.hasPermission("tf2.create")) {
            Localizers.getDefaultLoc().HELP_START.send(cs);
        }
        if (cs.hasPermission("tf2.stop") || cs.hasPermission("tf2.create")) {
            Localizers.getDefaultLoc().HELP_STOP.send(cs);
        }
        if (cs.hasPermission("tf2.create")) {
            Localizers.getDefaultLoc().HELP_CREATE.send(cs);
            cs.sendMessage(ChatColor.AQUA + "https://github.com/chaseoes/TF2/wiki/Commands");
        }
    }

}
