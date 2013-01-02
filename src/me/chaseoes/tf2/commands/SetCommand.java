package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.utilities.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand {

    @SuppressWarnings("unused")
    private TF2 plugin;
    static SetCommand instance = new SetCommand();

    private SetCommand() {

    }

    public static SetCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execSetCommand(final CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        final Player player = (Player) cs;
        if (strings[1].equalsIgnoreCase("spawn")) {
            if (strings.length == 3) {
                if (strings[2].equalsIgnoreCase("lobby")) {
                    MapUtilities.getUtilities().setLobby(player.getLocation());
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the global lobby.");
                } else {
                    final String map = strings[2];
                    if (!TF2.getInstance().mapExists(map)) {
                        cs.sendMessage(ChatColor.YELLOW + "[TF2] " + map + " is not a valid map.");
                        return;
                    }
                    TF2.getInstance().usingSetSpawnMenu.put(cs.getName(), map);
                    IconMenu menu = TF2.getInstance().setSpawnMenu;
                    menu.open((Player) cs);
                }
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("playerlimit")) {
            if (strings.length == 4) {
                if (Integer.parseInt(strings[3]) % 2 == 0) {
                    if (!TF2.getInstance().mapExists(strings[2])) {
                        cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[2] + " is not a valid map.");
                        return;
                    }
                    MapUtilities.getUtilities().setPlayerLimit(strings[2], Integer.parseInt(strings[3]));
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the playerlimit for " + strings[2] + " to " + strings[3] + ".");
                } else {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Yo! Don't use no odd numbers for dat shit, mofo.");
                }
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            if (strings.length == 4) {
                if (!TF2.getInstance().mapExists(strings[2])) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[2] + " is not a valid map.");
                    return;
                }
                CapturePointUtilities.getUtilities().defineCapturePoint(strings[2], Integer.parseInt(strings[3]), player.getLocation());
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully defined capturepoint ID #" + strings[3] + " for the map " + strings[2] + ".");
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            if (strings.length == 4) {
                if (!TF2.getInstance().mapExists(strings[2])) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[2] + " is not a valid map.");
                    return;
                }
                MapUtilities.getUtilities().setTimeLimit(strings[2], Integer.parseInt(strings[3]));
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the timelimit for " + strings[2] + " to " + strings[3] + ".");
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            if (strings.length == 4) {
                if (!TF2.getInstance().mapExists(strings[2])) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[2] + " is not a valid map.");
                    return;
                }
                TF2.getInstance().getMap(strings[2]).setRedTeamTeleportTime(Integer.parseInt(strings[3]));
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the red teleport time for " + strings[2] + " to " + strings[3] + ".");
            } else {
                h.wrongArgs();
            }
        } else {
            h.unknownCommand();
        }
    }

}
