package com.chaseoes.tf2.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.lobbywall.LobbyWall;
import com.chaseoes.tf2.utilities.Localizer;

public class DisableCommand {

    private TF2 plugin;
    static DisableCommand instance = new DisableCommand();

    private DisableCommand() {

    }

    public static DisableCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execDisableCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            for (Map map : TF2.getInstance().getMaps()) {
                if (GameUtilities.getUtilities().getGame(map).getStatus() != GameStatus.DISABLED) {
                    Game game = GameUtilities.getUtilities().getGame(map);
                    // game.getQueue().clear(true);
                    game.stopMatch(false);
                    MapUtilities.getUtilities().disableMap(map.getName());
                    game.setStatus(GameStatus.DISABLED);
                    String[] creditlines = new String[4];
                    creditlines[0] = " ";
                    creditlines[1] = "--------------------------";
                    creditlines[2] = "--------------------------";
                    creditlines[3] = " ";
                    LobbyWall.getWall().setAllLines(map.getName(), null, creditlines, false, false);
                }
            }
            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-DISABLED-ALL"));
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
            if (game.getStatus() != GameStatus.DISABLED) {
                // game.getQueue().clear(true);
                game.stopMatch(false);
                MapUtilities.getUtilities().disableMap(map);
                game.setStatus(GameStatus.DISABLED);
                String[] creditlines = new String[4];
                creditlines[0] = " ";
                creditlines[1] = "--------------------------";
                creditlines[2] = "--------------------------";
                creditlines[3] = " ";
                LobbyWall.getWall().setAllLines(map, null, creditlines, false, false);
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-DISABLED-SINGLE").replace("%map", map));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY-DISABLED").replace("%map", map));
            }
        } else {
            h.wrongArgs("/tf2 disable <map>");
        }
    }

}
