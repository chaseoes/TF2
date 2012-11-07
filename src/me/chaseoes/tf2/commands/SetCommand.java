package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.lobbywall.LobbyWall;

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

    public void execSetCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        Player player = (Player) cs;
        if (strings[1].equalsIgnoreCase("spawn")) {
            if (strings.length == 3 && strings[2].equalsIgnoreCase("lobby")) {
                MapUtilities.getUtilities().setLobby(player.getLocation());
                cs.sendMessage("§e[TF2] Successfully set the global lobby.");
            } else if (strings.length == 4 && (strings[2].equalsIgnoreCase("bluelobby") || strings[2].equalsIgnoreCase("redlobby"))) {
                MapUtilities.getUtilities().setTeamLobby(strings[3], strings[2].replace("lobby", ""), player.getLocation());
                cs.sendMessage("§e[TF2] Successfully set the " + strings[2].replace("lobby", "") + " team's lobby.");
            } else if (strings.length == 4 && (strings[2].equalsIgnoreCase("blueteam") || strings[2].equalsIgnoreCase("redteam"))) {
                MapUtilities.getUtilities().setTeamSpawn(strings[3], strings[2].replace("team", ""), player.getLocation());
                cs.sendMessage("§e[TF2] Successfully set the " + strings[2].replace("team", "") + " team's spawn.");
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("playerlimit")) {
            if (strings.length == 4) {
                if (Integer.parseInt(strings[3]) % 2 == 0) {
                    MapUtilities.getUtilities().setPlayerLimit(strings[2], Integer.parseInt(strings[3]));
                    LobbyWall.getWall().update();
                    cs.sendMessage("§e[TF2] Successfully set the playerlimit for " + strings[2] + " to " + strings[3] + ".");
                } else {
                    cs.sendMessage("§e[TF2] Yo! Don't use no odd numbers for dat shit, mofo.");
                }
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            if (strings.length == 4) {
                CapturePointUtilities.getUtilities().defineCapturePoint(strings[2], Integer.parseInt(strings[3]), player.getLocation());
                cs.sendMessage("§e[TF2] Successfully defined capturepoint ID #" + strings[3] + " for the map " + strings[2] + ".");
            }
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            if (strings.length == 4) {
                MapUtilities.getUtilities().setTimeLimit(strings[2], Integer.parseInt(strings[3]));
                cs.sendMessage("§e[TF2] Successfully set the timelimit for " + strings[2] + " to " + strings[3] + ".");
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            if (strings.length == 4) {
                MapConfiguration.getMaps().getMap(strings[2]).set("teleport-red-team", Integer.parseInt(strings[3]));
                MapConfiguration.getMaps().saveMap(strings[2]);
                cs.sendMessage("§e[TF2] Successfully set the red teleport time for " + strings[2] + " to " + strings[3] + ".");
            }
        } else {
            h.unknownCommand();
        }
    }

}
