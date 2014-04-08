package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.utilities.GeneralUtilities;
import me.chaseoes.tf2.utilities.IconMenu;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCommand {

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
        if (strings.length == 1) {
            h.wrongArgs("/tf2 set <spawn|playerlimit|redtp|timelimit|capturepoint>");
        } else if (strings[1].equalsIgnoreCase("spawn")) {
            if (strings.length == 3) {
                if (strings[2].equalsIgnoreCase("lobby")) {
                    MapUtilities.getUtilities().setLobby(player.getLocation());
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("GLOBAL-LOBBY-SET"));
                } else {
                    final String map = strings[2];
                    if (!plugin.mapExists(map)) {
                        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                        return;
                    }
                    plugin.usingSetSpawnMenu.put(cs.getName(), map);
                    IconMenu menu = plugin.setSpawnMenu;
                    menu.open((Player) cs);
                }
            } else {
                h.wrongArgs("/tf2 set spawn [lobby|<map>]");
            }
        } else if (strings[1].equalsIgnoreCase("playerlimit")) {
            if (strings.length == 4) {
                if (!plugin.mapExists(strings[2])) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[2]));
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    if (Integer.parseInt(strings[3]) % 2 == 0) {
                        MapUtilities.getUtilities().setPlayerLimit(strings[2], Integer.parseInt(strings[3]));
                        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-SET-PLAYERLIMIT").replace("%map", strings[2]).replace("%time", strings[3]));
                    } else {
                        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-PLAYERLIMIT-ODD"));
                    }
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-NOT-INTEGER").replace("%int", strings[3]));
                }
            } else {
                h.wrongArgs("/tf2 set playerlimit <map> <number>");
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            if (strings.length == 4) {
                if (!plugin.mapExists(strings[2])) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[2]));
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    CapturePointUtilities.getUtilities().defineCapturePoint(strings[2], Integer.parseInt(strings[3]), player.getLocation());
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-SET-CAPTUREPOINT").replace("%id", strings[3]).replace("%map", "\n" + strings[2]));
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-NOT-INTEGER").replace("%int", strings[3]));
                }
            } else {
                h.wrongArgs("/tf2 set capturepoint <map> <number>");
            }
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            if (strings.length == 4) {
                if (!plugin.mapExists(strings[2])) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[2]));
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    MapUtilities.getUtilities().setTimeLimit(strings[2], Integer.parseInt(strings[3]));
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-SET-TIMELIMIT").replace("%map", strings[2]).replace("%time", strings[3]));
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-NOT-INTEGER").replace("%int", strings[3]));
                }
            } else {
                h.wrongArgs("/tf2 set timelimit <map> <time>");
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            if (strings.length == 4) {
                if (!TF2.getInstance().mapExists(strings[2])) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", strings[2]));
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    TF2.getInstance().getMap(strings[2]).setRedTeamTeleportTime(Integer.parseInt(strings[3]));
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-SET-REDTP").replace("%map", strings[2]).replace("%time", strings[3]));
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-NOT-INTEGER").replace("%int", strings[3]));
                }
            } else {
                h.wrongArgs("/tf2 set redtp <map> <time>");
            }
        } else {
            h.unknownCommand();
        }
    }

}
