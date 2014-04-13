package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
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
                Localizers.getDefaultLoc().PLAYER_ALREADY_PLAYING.sendPrefixed(player);
                return;
            }
            if (globalLobbySet()) {
                player.teleport(MapUtilities.getUtilities().loadLobby());
                Localizers.getDefaultLoc().PLAYER_TELEPORT_GLOBAL_LOBBY.sendPrefixed(player);
            } else {
                Localizers.getDefaultLoc().GLOBAL_LOBBY_NOT_SET.sendPrefixed(player);
            }
            return;
        }
        if (strings.length == 2 || strings.length == 3) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
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
                Localizers.getDefaultLoc().MAP_NOT_SETUP.sendPrefixed(player);
                if (player.hasPermission("tf2.create")) {
                    Localizers.getDefaultLoc().MAP_NOT_SETUP_COMMAND_HELP.sendPrefixed(player, map);
                }
                return;
            }

            if (gp.isIngame()) {
                Localizers.getDefaultLoc().PLAYER_ALREADY_PLAYING.sendPrefixed(player);
                return;
            }

            if (SpectateCommand.getCommand().isSpectating(player)) {
                Localizers.getDefaultLoc().PLAYER_ALREADY_SPECTATING.sendPrefixed(player);
                return;
            }

            if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(player);
                return;
            }

            if (game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit() && !(strings.length == 3) && !player.hasPermission("tf2.create")) {
                Localizers.getDefaultLoc().MAP_INFO_FULL.sendPrefixed(player);
                return;
            }

            if (game.getSizeOfTeam(team) >= TF2.getInstance().getMap(map).getPlayerlimit() / 2 && player.hasPermission("tf2.create")) {
                Localizers.getDefaultLoc().MAP_INFO_TEAM_FULL.sendPrefixed(player);
                return;
            }

            game.joinGame(GameUtilities.getUtilities().getGamePlayer(player), team);

            if (game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit()) {
                Localizers.getDefaultLoc().PLAYER_JOIN_FULL_MAP.sendPrefixed(player);
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
