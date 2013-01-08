package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.*;

import org.bukkit.ChatColor;
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
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully stopped all running games.");
            } else if (game.getStatus() == GameStatus.INGAME|| game.getStatus() == GameStatus.STARTING) {
                game.stopMatch(false);
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully stopped the game.");
            } else if (game.getStatus() == GameStatus.DISABLED) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That map is not enabled");
            } else {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is not in progress.");
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() != GameStatus.WAITING) {
                    game.stopMatch(false);
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully stopped the game.");
                } else {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is not in progress.");
                }
            } else {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That map is not enabled.");
            }
        } else {
            h.wrongArgs();
        }
    }

}
