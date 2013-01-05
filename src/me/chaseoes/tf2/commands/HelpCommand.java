package me.chaseoes.tf2.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand {

    static HelpCommand instance = new HelpCommand();

    public static HelpCommand getCommand() {
        return instance;
    }

    public void execHelpCommand(CommandSender cs, String[] strings, Command cmnd) {
        cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + "Team Fortress 2 Help " + ChatColor.AQUA + "[" + ChatColor.GOLD + "---------------" + ChatColor.AQUA + "]");
        if (cs.hasPermission("tf2.play")) {
            cs.sendMessage(ChatColor.AQUA + "/tf2 join <map> [team]" + ChatColor.GRAY + ": Join a map. If a team is not provided, you will be placed randomly.");
            cs.sendMessage(ChatColor.AQUA + "/tf2 leave" + ChatColor.GRAY + ": Leave the current game.");
            cs.sendMessage(ChatColor.AQUA + "/tf2 list [map]" + ChatColor.GRAY + ": List players currently in the game.");
        }
        if (cs.hasPermission("tf2.start") || cs.hasPermission("tf2.create")) {
            cs.sendMessage(ChatColor.RED + "/tf2 start [map]" + ChatColor.GRAY + ": Forces a map to start instead of starting automatically.");
        }
        if (cs.hasPermission("tf2.stop") || cs.hasPermission("tf2.create")) {
            cs.sendMessage(ChatColor.RED + "/tf2 stop [map]" + ChatColor.GRAY + ": Forces a map to stop.");
        }
        if (cs.hasPermission("tf2.create")) {
            cs.sendMessage(ChatColor.DARK_RED + "Please go here for a full list of map setup commands:");
            cs.sendMessage(ChatColor.AQUA + "https://github.com/chaseoes/TF2/wiki/Commands");
        }
    }

}
