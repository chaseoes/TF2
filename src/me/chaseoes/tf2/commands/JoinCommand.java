package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.Queue;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.DataChecker;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand {

    @SuppressWarnings("unused")
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
        if (strings.length == 1) {
            if (GameUtilities.getUtilities().isIngame(player)) {
                player.sendMessage("§e[TF2] You are already playing on a map!");
                return;
            }
            if (globalLobbySet()) {
                player.teleport(MapUtilities.getUtilities().loadLobby());
                player.sendMessage("§e[TF2] Teleported to the TF2 lobby.");
            } else {
                player.sendMessage("§e[TF2] The global lobby hasn't been set.");
            }
            return;
        }
        if (strings.length == 2 || strings.length == 3) {
            String map = strings[1];
            String team = GameUtilities.getUtilities().decideTeam(map);
            
            if (strings.length == 3) {
                if (!player.hasPermission("tf2.join.specific")) {
                    h.noPermission();
                    return;
                }
                team = strings[2];
            }
            
            DataChecker dc = new DataChecker(map);
            if (!dc.allGood()) {
                player.sendMessage("§e[TF2] This map has not yet been setup.");
                if (player.hasPermission("tf2.create")) {
                    player.sendMessage("§e[TF2] Type §6/tf2 checkdata " + map + " §eto see what else needs to be done.");
                }
                return;
            }

            if (GameUtilities.getUtilities().isIngame(player)) {
                player.sendMessage("§e[TF2] You are already playing on a map!");
                return;
            }

            if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                player.sendMessage("§e[TF2] That map is disabled.");
                return;
            }

            if (GameUtilities.getUtilities().getIngameList(map).size() == MapConfiguration.getMaps().getMap(map).getInt("playerlimit") && !(strings.length == 3)) {
                player.sendMessage("§e[TF2] That map is currently full.");
                return;
            }
            
            GameUtilities.getUtilities().joinGame(player, map, team);

            if (GameUtilities.getUtilities().getIngameList(map).size() >= MapConfiguration.getMaps().getMap(map).getInt("playerlimit")) {
                player.sendMessage("§e[TF2] You have joined a full map.");
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
