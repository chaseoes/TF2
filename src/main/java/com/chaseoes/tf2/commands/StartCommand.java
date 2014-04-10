package com.chaseoes.tf2.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.Localizer;

public class StartCommand {

    private TF2 plugin;
    static StartCommand instance = new StartCommand();

    private StartCommand() {

    }

    public static StartCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execStartCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            Game game = GameUtilities.getUtilities().getGamePlayer((Player) cs).getGame();
            if (game == null) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-NOT-PLAYING"));
            } else if (game.getStatus() == GameStatus.WAITING) {
                game.startMatch();
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-STARTED"));
            } else if (game.getStatus() == GameStatus.DISABLED) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
            } else if (game.getStatus() == GameStatus.STARTING) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY-STARTING"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY_INGAME"));
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() == GameStatus.WAITING) {
                    game.startMatch();
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-STARTED"));
                } else if (game.getStatus() == GameStatus.STARTING) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY-STARTING"));
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY-INGAME"));
                }
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
            }
        } else {
            h.wrongArgs("/tf2 start [map]");
        }
    }

}
