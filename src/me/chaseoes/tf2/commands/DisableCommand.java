package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.LobbyWall;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
                    //game.getQueue().clear(true);
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
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully disabled all enabled maps.");
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                return;
            }
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
            if (game.getStatus() != GameStatus.DISABLED) {
                //game.getQueue().clear(true);
                game.stopMatch(false);
                MapUtilities.getUtilities().disableMap(map);
                game.setStatus(GameStatus.DISABLED);
                String[] creditlines = new String[4];
                creditlines[0] = " ";
                creditlines[1] = "--------------------------";
                creditlines[2] = "--------------------------";
                creditlines[3] = " ";
                LobbyWall.getWall().setAllLines(map, null, creditlines, false, false);
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully disabled " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ".");
            } else {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] The map " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + " is already disabled.");
            }
        } else {
            h.wrongArgs();
        }
    }

}
