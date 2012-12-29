package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.LobbyWall;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EnableCommand {

    @SuppressWarnings("unused")
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
            MapUtilities.getUtilities().enableMap(map);
            GameUtilities.getUtilities().setStatus(map, GameStatus.WAITING);
            cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully enabled " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ".");
        } else {
            h.wrongArgs();
        }
    }

}
