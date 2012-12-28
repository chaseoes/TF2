package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.LobbyWall;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class DisableCommand {

    @SuppressWarnings("unused")
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
        if (strings.length == 2) {
            String map = strings[1];
            GameUtilities.getUtilities().stopMatch(map);
            MapUtilities.getUtilities().disableMap(map);
            GameUtilities.getUtilities().setStatus(map, GameStatus.DISABLED);
            String[] creditlines = new String[4];
            creditlines[0] = " ";
            creditlines[1] = "--------------------------";
            creditlines[2] = "--------------------------";
            creditlines[3] = " ";
            LobbyWall.getWall().setAllLines(map, null, creditlines, false, false);
            cs.sendMessage("§e[TF2] Successfully disabled §l" + map + "§r§e.");
        } else {
            h.wrongArgs();
        }
    }

}
