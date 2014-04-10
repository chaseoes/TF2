package com.chaseoes.tf2.commands;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chaseoes.tf2.utilities.Localizer;

public class HelpCommand {

    static HelpCommand instance = new HelpCommand();

    public static HelpCommand getCommand() {
        return instance;
    }

    public void execHelpCommand(CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "]" + ChatColor.DARK_AQUA + " Team Fortress 2 Help " + ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "]");
        if (cs.hasPermission("tf2.play")) {
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-JOIN"));
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-LEAVE"));
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-LIST"));
        }
        if (cs.hasPermission("tf2.start") || cs.hasPermission("tf2.create")) {
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-START"));
        }
        if (cs.hasPermission("tf2.stop") || cs.hasPermission("tf2.create")) {
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-STOP"));
        }
        if (cs.hasPermission("tf2.create")) {
            cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-CREATE"));
            cs.sendMessage(ChatColor.AQUA + "https://github.com/chaseoes/TF2/wiki/Commands");
        }
    }

}
