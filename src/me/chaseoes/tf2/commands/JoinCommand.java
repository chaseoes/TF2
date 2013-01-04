package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.*;
import me.chaseoes.tf2.utilities.DataChecker;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                player.sendMessage(ChatColor.YELLOW + "[TF2] You are already playing on a map!");
                return;
            }
            if (globalLobbySet()) {
                player.teleport(MapUtilities.getUtilities().loadLobby());
                player.sendMessage(ChatColor.YELLOW + "[TF2] Teleported to the TF2 lobby.");
            } else {
                player.sendMessage(ChatColor.YELLOW + "[TF2] The global lobby hasn't been set.");
            }
            return;
        }
        if (strings.length == 2 || strings.length == 3) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                return;
            }
            Game game = GameUtilities.getUtilities().getGame(plugin.getMap(map));
            Team team = game.decideTeam();

            if (strings.length == 3) {
                if (!player.hasPermission("tf2.join.specific")) {
                    h.noPermission();
                    return;
                }
                team = Team.match(strings[2]);
            }

            DataChecker dc = new DataChecker(map);
            if (!dc.allGood()) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] This map has not yet been setup.");
                if (player.hasPermission("tf2.create")) {
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Type " + ChatColor.GOLD + "/tf2 checkdata " + map + " " + ChatColor.YELLOW + "to see what else needs to be done.");
                }
                return;
            }

            if (gp.isIngame()) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] You are already playing on a map!");
                return;
            }

            if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] That map is disabled.");
                return;
            }

            if ((game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit() && !(strings.length == 3)) && !player.hasPermission("tf2.create")) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] That map is currently full.");
                return;
            }

            if (game.getSizeOfTeam(team) >= TF2.getInstance().getMap(map).getPlayerlimit() / 2 && player.hasPermission("tf2.create")) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] That team is currently full.");
                return;
            }

            game.joinGame(GameUtilities.getUtilities().getGamePlayer(player), team);

            if (game.getPlayersIngame().size() >= TF2.getInstance().getMap(map).getPlayerlimit()) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] You have joined a full map.");
            }

        } else {
            h.wrongArgs();
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
