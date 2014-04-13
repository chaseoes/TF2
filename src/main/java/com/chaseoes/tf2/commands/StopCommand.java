package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;

public class StopCommand {

    private TF2 plugin;
    static StopCommand instance = new StopCommand();

    private StopCommand() {

    }

    public static StopCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execStopCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            Game game = GameUtilities.getUtilities().getGamePlayer((Player) cs).getGame();
            if (game == null) {
                for (Map map : TF2.getInstance().getMaps()) {
                    Game gm = GameUtilities.getUtilities().getGame(map);
                    if (gm.getStatus() == GameStatus.DISABLED) {
                        continue;
                    } else if (gm.getStatus() != GameStatus.WAITING) {
                        gm.stopMatch(false);
                    }
                }
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_STOPPED_ALL.sendPrefixed(cs);
            } else if (game.getStatus() == GameStatus.INGAME || game.getStatus() == GameStatus.STARTING) {
                game.stopMatch(false);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_STOPPED_SINGLE.sendPrefixed(cs);
            } else if (game.getStatus() == GameStatus.DISABLED) {
                Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(cs);
            } else {
                Localizers.getDefaultLoc().MAP_INFO_NOT_INGAME.sendPrefixed(cs);
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() != GameStatus.WAITING) {
                    game.stopMatch(false);
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_STOPPED_SINGLE.sendPrefixed(cs);
                } else {
                    Localizers.getDefaultLoc().MAP_INFO_NOT_INGAME.sendPrefixed(cs);
                }
            } else {
                Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(cs);
            }
        } else {
            h.wrongArgs("/tf2 stop [map]");
        }
    }

}
