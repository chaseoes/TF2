package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.utilities.DataChecker;

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
            DataChecker dc = new DataChecker(map);
            cs.sendMessage("§b[§6------------------§b] §3TF2 Data Check §b[§6-----------------§b]");
            if (dc.capturePointOneHasBeenSet()) {
                cs.sendMessage("§bAt least one capture point set? §2Yes. §7(Total: §2" + dc.totalNumberOfCapturePoints() + "§7)");
            } else {
                cs.sendMessage("§bAt least one capture point set? §4§lNo.");
            }
            if (dc.teamLobbyHasBeenSet("red")) {
                cs.sendMessage("§bRed team lobby has set? §2Yes.");
            } else {
                cs.sendMessage("§bRed team lobby has set? §4§lNo.");
            }
            if (dc.teamLobbyHasBeenSet("blue")) {
                cs.sendMessage("§bBlue team lobby has set? §2Yes.");
            } else {
                cs.sendMessage("§bBlue team lobby has set? §4§lNo.");
            }
            if (dc.teamSpawnHasBeenSet("red")) {
                cs.sendMessage("§bRed team spawn has set? §2Yes.");
            } else {
                cs.sendMessage("§bRed team spawn has set? §4§lNo.");
            }
            if (dc.teamSpawnHasBeenSet("blue")) {
                cs.sendMessage("§bBlue team spawn has set? §2Yes.");
            } else {
                cs.sendMessage("§bBlue team spawn has set? §4§lNo.");
            }
            if (dc.playerLimitHasBeenSet()) {
                cs.sendMessage("§bPlayer limit defined? §2Yes. §7(§2" + dc.getPlayerLimit() + "§7)");
            } else {
                cs.sendMessage("§bPlayer limit defined? §4§lNo.");
            }
            if (dc.timeLimitHasBeenSet()) {
                cs.sendMessage("§bTime limit defined? §2Yes. §7(§2" + dc.getTimeLimit() + "§7)");
            } else {
                cs.sendMessage("§bTime limit defined? §4§lNo.");
            }
            if (dc.redTPHasBeenSet()) {
                cs.sendMessage("§bRed team teleport timer defined? §2Yes. §7(§2" + dc.getRedTP() + "§7)");
            } else {
                cs.sendMessage("§bRed team teleport timer defined? §4§lNo.");
            }
            if (dc.globalLobbySet()) {
                cs.sendMessage("§aGlobal lobby set? §2Yes.");
            } else {
                cs.sendMessage("§aGlobal lobby set? §4§lNo.");
            }
            if (dc.allGood()) {
                cs.sendMessage("§2Good to go! Your map is ready to be played.");
            } else {
                cs.sendMessage("§cNot good to go! You have yet to finish setting it up."); 
            }
        } else {
            h.wrongArgs();
        }
    }

}
