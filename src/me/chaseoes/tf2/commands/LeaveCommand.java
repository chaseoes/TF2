package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand {

    @SuppressWarnings("unused")
    private TF2 plugin;
    static LeaveCommand instance = new LeaveCommand();

    private LeaveCommand() {

    }

    public static LeaveCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execLeaveCommand(CommandSender cs, String[] strings, Command cmnd) {
        try {
        Player player = (Player) cs;
        GameUtilities.getUtilities().leaveCurrentGame(player);
        cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully left the game.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
