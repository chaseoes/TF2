package com.chaseoes.tf2.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.Localizer;

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
            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-LEAVE-GAME"));
        } else {
            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-NOT-PLAYING"));
        }
    }

}
