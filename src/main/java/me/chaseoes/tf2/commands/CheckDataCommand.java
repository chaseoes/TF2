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
            cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "------------------" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + "TF2 Data Check " + ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------------" + ChatColor.AQUA + "]");
            if (dc.capturePointOneHasBeenSet()) {
                cs.sendMessage(ChatColor.AQUA + "At least one capture point set? " + ChatColor.DARK_GREEN + "Yes. " + ChatColor.GRAY + "(Total: " + ChatColor.DARK_GREEN + dc.totalNumberOfCapturePoints() + ChatColor.GRAY + ")");
            } else {
                cs.sendMessage(ChatColor.AQUA + "At least one capture point set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.teamLobbyHasBeenSet(Team.RED)) {
                cs.sendMessage(ChatColor.AQUA + "Red team lobby has set? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Red team lobby has set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.teamLobbyHasBeenSet(Team.BLUE)) {
                cs.sendMessage(ChatColor.AQUA + "Blue team lobby has set? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Blue team lobby has set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.teamSpawnHasBeenSet(Team.RED)) {
                cs.sendMessage(ChatColor.AQUA + "Red team spawn has set? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Red team spawn has set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.teamSpawnHasBeenSet(Team.BLUE)) {
                cs.sendMessage(ChatColor.AQUA + "Blue team spawn has set? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Blue team spawn has set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.playerLimitHasBeenSet()) {
                cs.sendMessage(ChatColor.AQUA + "Player limit defined? " + ChatColor.DARK_GREEN + "Yes. " + ChatColor.GRAY + "(" + ChatColor.DARK_GREEN + dc.getPlayerLimit() + ChatColor.GRAY + ")");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Player limit defined? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.timeLimitHasBeenSet()) {
                cs.sendMessage(ChatColor.AQUA + "Time limit defined? " + ChatColor.DARK_GREEN + "Yes. " + ChatColor.GRAY + "(" + ChatColor.DARK_GREEN + dc.getTimeLimit() + ChatColor.GRAY + ")");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Time limit defined? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.redTPHasBeenSet()) {
                cs.sendMessage(ChatColor.AQUA + "Red team teleport timer defined? " + ChatColor.DARK_GREEN + "Yes. " + ChatColor.GRAY + "(" + ChatColor.DARK_GREEN + dc.getRedTP() + ChatColor.GRAY + ")");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Red team teleport timer defined? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.lobbyWallHasBeenSet()) {
                cs.sendMessage(ChatColor.AQUA + "Lobby wall has been created? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.AQUA + "Lobby wall has been created? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.globalLobbySet()) {
                cs.sendMessage(ChatColor.GREEN + "Global lobby set? " + ChatColor.DARK_GREEN + "Yes.");
            } else {
                cs.sendMessage(ChatColor.GREEN + "Global lobby set? " + ChatColor.DARK_RED + ChatColor.BOLD + "No.");
            }
            if (dc.allGood()) {
                cs.sendMessage(ChatColor.DARK_GREEN + "Good to go! Your map is ready to be played.");
            } else {
                cs.sendMessage(ChatColor.RED + "Not good to go! You have yet to finish setting it up.");
            }
        } else {
            h.wrongArgs();
        }
    }

}
