package me.chaseoes.tf2.commands;

import java.util.ArrayList;
import java.util.List;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    private TF2 plugin;
    static ListCommand instance = new ListCommand();
    public List<String> highlightNames = new ArrayList<String>();

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
        highlightNames.add("chaseoes");
        highlightNames.add("skitscape");
        highlightNames.add("AntVenom");
        highlightNames.add("Fawdz");
        highlightNames.add("Double0Negative");
        
        if (strings.length == 2) {
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(strings[1]));
            for (String pl : game.getPlayersIngame()) {
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(plugin.getServer().getPlayerExact(pl));
                if (!highlightNames.contains(pl)) {
                    if (gp.getTeam() == Team.RED) {
                        red.append(pl).append(", ");
                    } else {
                        blue.append(pl).append(", ");
                    }
                } else {
                    if (gp.getTeam() == Team.RED) {
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
            Game game = GameUtilities.getUtilities().getCurrentGame((Player) cs);
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            Integer redc = 0;
            Integer bluec = 0;
            for (String pl : game.getPlayersIngame()) {
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(plugin.getServer().getPlayerExact(pl));
                if (!highlightNames.contains(pl)) {
                    if (gp.getTeam() == Team.RED) {
                        red.append(pl).append(", ");
                        redc++;
                    } else {
                        blue.append(pl).append(", ");
                        bluec++;
                    }
                } else {
                    if (gp.getTeam() == Team.RED) {
                        red.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                        redc++;
                    } else {
                        blue.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE).append(", ");
                        bluec++;
                    }
                }

            }
            
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Displaying players in the map " + ChatColor.BOLD + game.getMapName() + ChatColor.RESET + ChatColor.YELLOW + ":");
            cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Red team (" + redc + "): " + ChatColor.RESET + "\n" + red.toString());
            cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "Blue team (" + bluec + "): " + ChatColor.RESET + "\n" + blue.toString());
        } else {
            h.wrongArgs();
        }
    }

}
