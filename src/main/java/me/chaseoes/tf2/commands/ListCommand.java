package me.chaseoes.tf2.commands;

import java.util.ArrayList;
import java.util.List;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.utilities.Localizer;

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
        highlightNames.add("Oceangrass");
        highlightNames.add("psycowithespn");

        if (strings.length == 2) {
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            if (!TF2.getInstance().mapExists(strings[1])) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[1]));
                return;
            }
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(strings[1]));
            for (int i = 0; i < game.getPlayersIngame().size(); i++) {
                String pl = game.getPlayersIngame().get(i);
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(plugin.getServer().getPlayerExact(game.getPlayersIngame().get(i)));
                if (!highlightNames.contains(pl)) {
                    if (gp.getTeam() == Team.RED) {
                        red.append(pl);
                    } else {
                        blue.append(pl);
                    }
                } else {
                    if (gp.getTeam() == Team.RED) {
                        red.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE);
                    } else {
                        blue.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE);
                    }
                }
                if (gp.getTeam() == Team.RED) {
                    red.append(", ");
                } else {
                    blue.append(", ");
                }
            }
            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("LIST-DISPLAYING-MAP").replace("%map", strings[1]));
            if (red.length() != 0) {
                cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("RED-TEAM") + ": " + ChatColor.RESET + "\n" + red.toString().trim().substring(0, red.toString().length() - 2));
            } else {
                cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("RED-TEAM") + ": " + ChatColor.RESET + "\n");
            }
            if (blue.length() != 0) {
                cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("BLUE-TEAM") + ": " + ChatColor.RESET + "\n" + blue.toString().trim().substring(0, blue.toString().length() - 2));
            } else {
                cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("BLUE-TEAM") + ": " + ChatColor.RESET + "\n");
            }
        } else if (GameUtilities.getUtilities().getGamePlayer((Player) cs).isIngame()) {
            Game game = GameUtilities.getUtilities().getGamePlayer((Player) cs).getGame();
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            Integer redc = 0;
            Integer bluec = 0;
            for (int i = 0; i < game.getPlayersIngame().size(); i++) {
                String pl = game.getPlayersIngame().get(i);
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(plugin.getServer().getPlayerExact(game.getPlayersIngame().get(i)));
                if (!highlightNames.contains(pl)) {
                    if (gp.getTeam() == Team.RED) {
                        red.append(pl);
                        redc++;
                    } else {
                        blue.append(pl);
                        bluec++;
                    }
                } else {
                    if (gp.getTeam() == Team.RED) {
                        red.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE);
                        redc++;
                    } else {
                        blue.append(ChatColor.DARK_RED).append(ChatColor.BOLD).append(pl).append(ChatColor.RESET).append(ChatColor.WHITE);
                        bluec++;
                    }
                }
                if (gp.getTeam() == Team.RED) {
                    red.append(", ");
                } else {
                    blue.append(", ");
                }
            }

            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("LIST-DISPLAYING-MAP").replace("%map", game.getMapName()));
            if (red.length() != 0) {
                cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("RED-TEAM") + " (" + redc + "): " + ChatColor.RESET + "\n" + red.toString().trim().substring(0, red.toString().length() - 2));
            } else {
                cs.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("RED-TEAM") + " (0): " + ChatColor.RESET + "\n");
            }
            if (blue.length() != 0) {
                cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("BLUE-TEAM") + " (" + bluec + "): " + ChatColor.RESET + "\n" + blue.toString().trim().substring(0, blue.toString().length() - 2));
            } else {
                cs.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + Localizer.getLocalizer().loadPrefixedMessage("BLUE-TEAM") + " (0): " + ChatColor.RESET + "\n");
            }
        } else {
            h.wrongArgs();
        }
    }

}
