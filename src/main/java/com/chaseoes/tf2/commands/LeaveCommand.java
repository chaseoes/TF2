package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;

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
        Player player = (Player) cs;
        GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
        if (gp.isIngame()) {
            gp.getGame().leaveGame(gp.getPlayer());
            Localizers.getDefaultLoc().PLAYER_LEAVE_GAME.sendPrefixed(cs);
        } else {
            Localizers.getDefaultLoc().PLAYER_NOT_PLAYING.sendPrefixed(cs);
        }
    }

}
