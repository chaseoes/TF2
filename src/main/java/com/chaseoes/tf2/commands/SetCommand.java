package com.chaseoes.tf2.commands;


import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.capturepoints.CapturePointUtilities;
import com.chaseoes.tf2.utilities.GeneralUtilities;
import com.chaseoes.tf2.utilities.IconMenu;

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
                    Localizers.getDefaultLoc().GLOBAL_LOBBY_SET.sendPrefixed(cs);
                } else {
                    final String map = strings[2];
                    if (!plugin.mapExists(map)) {
                        Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
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
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    if (Integer.parseInt(strings[3]) % 2 == 0) {
                        int limit = Integer.parseInt(strings[3]);
                        MapUtilities.getUtilities().setPlayerLimit(strings[2], limit);
                        Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_PLAYERLIMIT.sendPrefixed(cs, strings[2], limit);
                    } else {
                        Localizers.getDefaultLoc().ERROR_PLAYERLIMIT_ODD.sendPrefixed(cs);
                    }
                } else {
                    Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[3]);
                }
            } else {
                h.wrongArgs("/tf2 set playerlimit <map> <number>");
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            if (strings.length == 4) {
                if (!plugin.mapExists(strings[2])) {
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    int cpNumber = Integer.parseInt(strings[3]);
                    CapturePointUtilities.getUtilities().defineCapturePoint(strings[2], cpNumber, player.getLocation());
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_CAPTUREPOINT.sendPrefixed(cs, strings[2], cpNumber);
                } else {
                    Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[3]);
                }
            } else {
                h.wrongArgs("/tf2 set capturepoint <map> <number>");
            }
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            if (strings.length == 4) {
                if (!plugin.mapExists(strings[2])) {
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    int time = Integer.parseInt(strings[3]);
                    MapUtilities.getUtilities().setTimeLimit(strings[2], time);
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_TIMELIMIT.sendPrefixed(cs, strings[2], time);
                } else {
                    Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[3]);
                }
            } else {
                h.wrongArgs("/tf2 set timelimit <map> <time>");
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            if (strings.length == 4) {
                if (!TF2.getInstance().mapExists(strings[2])) {
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                    return;
                }
                if (GeneralUtilities.isInteger(strings[3])) {
                    int time = Integer.parseInt(strings[3]);
                    TF2.getInstance().getMap(strings[2]).setRedTeamTeleportTime(time);
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_REDTP.sendPrefixed(cs, strings[2], time);
                } else {
                    Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[3]);
                }
            } else {
                h.wrongArgs("/tf2 set redtp <map> <time>");
            }
        } else {
            h.unknownCommand();
        }
    }

}
