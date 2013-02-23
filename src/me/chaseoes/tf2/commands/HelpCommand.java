package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    static HelpCommand instance = new HelpCommand();

    public static HelpCommand getCommand() {
        return instance;
    }

    public void execHelpCommand(CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(Localizer.getLocalizer().loadMessage("HELP-HEADER"));
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
