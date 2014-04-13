package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.capturepoints.CapturePointUtilities;
import com.chaseoes.tf2.localization.Localizers;
import com.chaseoes.tf2.utilities.GeneralUtilities;
import com.chaseoes.tf2.utilities.IconMenu;
import com.chaseoes.tf2.utilities.WorldEditUtilities;
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
            String map = null;
            if (strings.length == 2) {
                Map m = WorldEditUtilities.getWEUtilities().getMap(player.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(player);
                    h.wrongArgs("/tf2 set spawn [map]");
                    return;
                }
                map = m.getName();
            } else {
                map = strings[2];
            }
            if (map.equalsIgnoreCase("lobby")) {
                MapUtilities.getUtilities().setLobby(player.getLocation());
                Localizers.getDefaultLoc().GLOBAL_LOBBY_SET.sendPrefixed(cs);
            } else {
                if (!plugin.mapExists(map)) {
                    Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                    return;
                }
                plugin.usingSetSpawnMenu.put(cs.getName(), map);
                IconMenu menu = plugin.setSpawnMenu;
                menu.open((Player) cs);
            }
        } else if (strings[1].equalsIgnoreCase("playerlimit")) {
            String map;
            String playerLimitString;
            if (strings.length >= 3) {
                if (strings.length == 3) {
                    Map m = WorldEditUtilities.getWEUtilities().getMap(player.getLocation());
                    if (m == null) {
                        Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(player);
                        h.wrongArgs("/tf2 set playerlimit [map] <number>");
                        return;
                    }
                    map = m.getName();
                    playerLimitString = strings[2];
                } else {
                    map = strings[2];
                    playerLimitString = strings[3];

                }
            } else {
                h.wrongArgs("/tf2 set playerlimit [map] <number>");
                return;
            }
            if (!plugin.mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                return;
            }
            int playerlimit;
            if (GeneralUtilities.isInteger(playerLimitString)) {
                playerlimit = Integer.parseInt(playerLimitString);
            } else {
                Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[2]);
                return;
            }
            if (playerlimit % 2 == 0) {
                MapUtilities.getUtilities().setPlayerLimit(map, playerlimit);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_PLAYERLIMIT.sendPrefixed(cs, map, playerlimit);
            } else {
                Localizers.getDefaultLoc().ERROR_PLAYERLIMIT_ODD.sendPrefixed(cs);
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            String map;
            String numberString;
            if (strings.length == 3) {
                Map m = WorldEditUtilities.getWEUtilities().getMap(player.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(player);
                    h.wrongArgs("/tf2 set capturepoint [map] <number>");
                    return;
                }
                map = m.getName();
                numberString = strings[2];
            } else {
                map = strings[2];
                numberString = strings[3];

            }
            if (!plugin.mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            int number;
            if (GeneralUtilities.isInteger(numberString)) {
                number = Integer.parseInt(numberString);
            } else {
                Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, strings[2]);
                return;
            }
            CapturePointUtilities.getUtilities().defineCapturePoint(map, number, player.getLocation());
            Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_CAPTUREPOINT.sendPrefixed(cs, map, number);
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            String map;
            String timelimitString;
            if (strings.length == 3) {
                Map m = WorldEditUtilities.getWEUtilities().getMap(player.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(player);
                    h.wrongArgs("/tf2 set timelimit [map] <timelimit>");
                    return;
                }
                map = m.getName();
                timelimitString = strings[2];
            } else {
                map = strings[2];
                timelimitString = strings[3];
            }

            if (!plugin.mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[2]);
                return;
            }
            if (GeneralUtilities.isInteger(timelimitString)) {
                int time = Integer.parseInt(timelimitString);
                MapUtilities.getUtilities().setTimeLimit(map, time);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_TIMELIMIT.sendPrefixed(cs, map, time);
            } else {
                Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, timelimitString);
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            String map;
            String redTpString;
            if (strings.length == 3) {
                Map m = WorldEditUtilities.getWEUtilities().getMap(player.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(player);
                    h.wrongArgs("/tf2 set redtp [map] <time>");
                    return;
                }
                map = m.getName();
                redTpString = strings[2];
            } else {
                map = strings[2];
                redTpString = strings[3];
            }
            if (!TF2.getInstance().mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            if (GeneralUtilities.isInteger(redTpString)) {
                int time = Integer.parseInt(redTpString);
                TF2.getInstance().getMap(map).setRedTeamTeleportTime(time);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_SET_REDTP.sendPrefixed(cs, map, time);
            } else {
                Localizers.getDefaultLoc().ERROR_NOT_INTEGER.sendPrefixed(cs, redTpString);
            }
        } else {
            h.unknownCommand();
        }
    }

}
