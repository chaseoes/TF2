package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    private TF2 plugin;
    static ListCommand instance = new ListCommand();

    private ListCommand() {

    }

    public static ListCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execListCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 2) {
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            for (String pl : GameUtilities.getUtilities().getIngameList(strings[1])) {

                if (!GameUtilities.getUtilities().coolpeople.contains(pl)) {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(pl).append(", ");
                    } else {
                        blue.append(pl).append(", ");
                    }
                } else {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                    } else {
                        blue.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                    }
                }

            }
            
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Displaying players in the map " + ChatColor.BOLD + strings[1] + ChatColor.RESET + ChatColor.YELLOW + ":");
            cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Red team: " + ChatColor.RESET + "\n" + red.toString());
            cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Blue team: " + ChatColor.RESET + "\n" + blue.toString());
        } else if (GameUtilities.getUtilities().isIngame((Player) cs)) {
            String map = GameUtilities.getUtilities().getCurrentMap((Player) cs);
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            Integer redc = 0;
            Integer bluec = 0;
            for (String pl : GameUtilities.getUtilities().getIngameList(map)) {
                if (!GameUtilities.getUtilities().coolpeople.contains(pl)) {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(pl).append(", ");
                        redc++;
                    } else {
                        blue.append(pl).append(", ");
                        bluec++;
                    }
                } else {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                        redc++;
                    } else {
                        blue.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                        bluec++;
                    }
                }

            }
            
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Displaying players in the map " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ":");
            cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Red team (" + redc + "): " + ChatColor.RESET + "\n" + red.toString());
            cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Blue team (" + bluec + "): " + ChatColor.RESET + "\n" + blue.toString());
        } else {
            h.wrongArgs();
        }
    }

}
