package com.chaseoes.tf2.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.Team;
import com.chaseoes.tf2.utilities.DataChecker;
import com.chaseoes.tf2.utilities.Localizer;

public class JoinCommand {

    private TF2 plugin;
    static JoinCommand instance = new JoinCommand();

    private JoinCommand() {

    }

    public static JoinCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execJoinCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        Player player = (Player) cs;
        GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
        if (strings.length == 1) {
            if (gp.isIngame()) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-ALREADY-PLAYING"));
                return;
            }
            if (globalLobbySet()) {
                player.teleport(MapUtilities.getUtilities().loadLobby());
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-TELEPORT-GLOBAL-LOBBY"));
            } else {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("GLOBAL-LOBBY-NOT-SET"));
            }
            return;
        }
        if (strings.length == 2 || strings.length == 3) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
            Team team = game.decideTeam();

            if (strings.length == 3) {
                if (!player.hasPermission("tf2.create")) {
                    h.noPermission();
                    return;
                }
                team = Team.match(strings[2]);
            }

            DataChecker dc = new DataChecker(map);
            if (!dc.allGood()) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-NOT-SETUP"));
                if (player.hasPermission("tf2.create")) {
                    player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-NOT-SETUP-COMMAND-HELP").replace("%map", map));
                }
                return;
            }

            if (gp.isIngame()) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-ALREADY-PLAYING"));
                return;
            }

            if (SpectateCommand.getCommand().isSpectating(player)) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-ALREADY-SPECTATING"));
                return;
            }

            if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
                return;
            }

            if (game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit() && !(strings.length == 3) && !player.hasPermission("tf2.create")) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-FULL"));
                return;
            }

            if (game.getSizeOfTeam(team) >= TF2.getInstance().getMap(map).getPlayerlimit() / 2 && player.hasPermission("tf2.create")) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-TEAM-FULL"));
                return;
            }

            game.joinGame(GameUtilities.getUtilities().getGamePlayer(player), team);

            if (game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit()) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-JOIN-FULL-MAP"));
            }

        } else {
            h.wrongArgs("/tf2 join [map] [team]");
        }
    }

    public Boolean globalLobbySet() {
        try {
            MapUtilities.getUtilities().loadLobby();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
