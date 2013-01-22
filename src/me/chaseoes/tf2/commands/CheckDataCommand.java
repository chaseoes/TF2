package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.utilities.DataChecker;

import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CheckDataCommand {

    static CheckDataCommand instance = new CheckDataCommand();

    private CheckDataCommand() {

    }

    public static CheckDataCommand getCommand() {
        return instance;
    }

    public void execCheckDataCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP"));
                return;
            }
            DataChecker dc = new DataChecker(map);
            cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-HEADER"));
            if (dc.capturePointOneHasBeenSet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-ONE-CP-SET").replace("%total", dc.totalNumberOfCapturePoints().toString()));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-ONE-CP-NOT-SET"));
            }
            if (dc.teamLobbyHasBeenSet(Team.RED)) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TEAM-LOBBY-SET"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TEAM-LOBBY-NOT-SET"));
            }
            if (dc.teamLobbyHasBeenSet(Team.BLUE)) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-BLUE-TEAM-LOBBY-SET"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-BLUE-TEAM-LOBBY-NOT-SET"));
            }
            if (dc.teamSpawnHasBeenSet(Team.RED)) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TEAM-SPAWN-SET"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TEAM-SPAWN-NOT-SET"));
            }
            if (dc.teamSpawnHasBeenSet(Team.BLUE)) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-BLUE-TEAM-SPAWN-SET"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-BLUE-TEAM-SPAWN-NOT-SET"));
            }
            if (dc.playerLimitHasBeenSet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-PLAYER-LIMIT-SET").replace("%count", dc.getPlayerLimit().toString()));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-PLAYER-LIMIT-NOT-SET"));
            }
            if (dc.timeLimitHasBeenSet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-TIME-LIMIT-SET").replace("%time", dc.getTimeLimit().toString()));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-TIME-LIMIT-NOT-SET"));
            }
            if (dc.redTPHasBeenSet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TP-DEFINED").replace("%time", dc.getRedTP().toString()));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-RED-TP-NOT-DEFINED"));
            }
            if (dc.lobbyWallHasBeenSet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-LOBBY-WALL-CREATED"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-LOBBY-WALL-NOT-CREATED"));
            }
            if (dc.globalLobbySet()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-GLOBAL-LOBBY-SET"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-GLOBAL-LOBBY-NOT-SET"));
            }
            if (dc.allGood()) {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-GOOD-TO-GO"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadMessage("DC-NOT-GOOD-TO-GO"));
            }
        } else {
            h.wrongArgs();
        }
    }

}
