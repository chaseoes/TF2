package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.lobbywall.LobbyWall;

public class EnableCommand {

    private TF2 plugin;
    static EnableCommand instance = new EnableCommand();

    private EnableCommand() {

    }

    public static EnableCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execEnableCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            for (Map map : TF2.getInstance().getMaps()) {
                if (GameUtilities.getUtilities().getGame(map).getStatus() == GameStatus.DISABLED) {
                    MapUtilities.getUtilities().enableMap(map.getName());
                    Game game = GameUtilities.getUtilities().getGame(map);
                    game.setStatus(GameStatus.WAITING);
                    LobbyWall.getWall().cantUpdate.remove(map.getName());
                }
            }
            Localizers.getDefaultLoc().MAP_SUCCESSFULLY_ENABLED_ALL.sendPrefixed(cs);
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            if (GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map)).getStatus() == GameStatus.DISABLED) {
                MapUtilities.getUtilities().enableMap(map);
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                game.setStatus(GameStatus.WAITING);
                LobbyWall.getWall().cantUpdate.remove(map);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_ENABLED_SINGLE.sendPrefixed(cs, map);
            } else {
                Localizers.getDefaultLoc().MAP_ALREADY_ENABLED.sendPrefixed(cs, map);
            }
        } else {
            h.wrongArgs("/tf2 enable <map>");
        }
    }

}
