package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;

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
                Localizers.getDefaultLoc().PLAYER_NOT_PLAYING.sendPrefixed(cs);
            } else if (game.getStatus() == GameStatus.WAITING) {
                game.startMatch();
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_STARTED.sendPrefixed(cs);
            } else if (game.getStatus() == GameStatus.DISABLED) {
                Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(cs);
            } else if (game.getStatus() == GameStatus.STARTING) {
                Localizers.getDefaultLoc().MAP_ALREADY_STARTING.sendPrefixed(cs);
            } else {
                Localizers.getDefaultLoc().MAP_ALREADY_INGAME.sendPrefixed(cs);
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() == GameStatus.WAITING) {
                    game.startMatch();
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_STARTED.sendPrefixed(cs);
                } else if (game.getStatus() == GameStatus.STARTING) {
                    Localizers.getDefaultLoc().MAP_ALREADY_STARTING.sendPrefixed(cs);
                } else {
                    Localizers.getDefaultLoc().MAP_ALREADY_INGAME.sendPrefixed(cs);
                }
            } else {
                Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(cs);
            }
        } else {
            h.wrongArgs("/tf2 start [map]");
        }
    }

}
