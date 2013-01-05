package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.*;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                cs.sendMessage(ChatColor.YELLOW + "[TF2] You are not in a game.");
            } else if (game.getStatus() == GameStatus.WAITING) {
                game.startMatch();
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully started the game.");
            } else if (game.getStatus() == GameStatus.DISABLED) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That map is not enabled");
            } else if (game.getStatus() == GameStatus.STARTING) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is already starting.");
            } else {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is already in progress.");
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                return;
            }
            if (GameUtilities.getUtilities().getGame(plugin.getMap(map)).getStatus() != GameStatus.DISABLED) {
                Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
                if (game.getStatus() == GameStatus.WAITING) {
                    game.startMatch();
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully started the game.");
                } else if (game.getStatus() == GameStatus.STARTING) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is already starting.");
                } else {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] That game is already in progress.");
                }
            } else {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] That map is not enabled.");
            }
        } else {
            h.wrongArgs();
        }
    }

}
