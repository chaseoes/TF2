package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
            cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-ENABLED-ALL"));
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }
            if (GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map)).getStatus() == GameStatus.DISABLED) {
                MapUtilities.getUtilities().enableMap(map);
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                game.setStatus(GameStatus.WAITING);
                LobbyWall.getWall().cantUpdate.remove(map);
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-ENABLED-SINGLE").replace("%map", map));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-ALREADY-ENABLED").replace("%map", map));
            }
        } else {
            h.wrongArgs();
        }
    }

}
