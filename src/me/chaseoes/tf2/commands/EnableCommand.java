package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.*;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import org.bukkit.ChatColor;
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
        if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                return;
            }
            MapUtilities.getUtilities().enableMap(map);
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
            game.setStatus(GameStatus.WAITING);
            LobbyWall.getWall().cantUpdate.remove(map);
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully enabled " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ".");
        } else {
            h.wrongArgs();
        }
    }

}
