package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-STOPPED-ALL"));
            } else if (game.getStatus() == GameStatus.INGAME || game.getStatus() == GameStatus.STARTING) {
                game.stopMatch(false);
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-STOPPED-SINGLE"));
            } else if (game.getStatus() == GameStatus.DISABLED) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-NOT-INGAME"));
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() != GameStatus.WAITING) {
                    game.stopMatch(false);
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-STOPPED-SINGLE"));
                } else {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-NOT-INGAME"));
                }
            } else {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
            }
        } else {
            h.wrongArgs("/tf2 stop [map]");
        }
    }

}
