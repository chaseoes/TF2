package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.utilities.DataChecker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[TF2]") && event.getPlayer().hasPermission("tf2.create")) {
            Location bl = event.getBlock().getLocation();
            if (event.getLine(1).equalsIgnoreCase("map")) {
                String map = event.getLine(2);
                final Player player = event.getPlayer();
                DataChecker dc = new DataChecker(map);
                if (!dc.allGoodExceptLobbyWall()) {
                    player.sendMessage(ChatColor.YELLOW + "[TF2] This map has not yet been setup.");
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Type " + ChatColor.GOLD + "/tf2 checkdata " + map + " " + ChatColor.YELLOW + "to see what else needs to be done.");
                    event.setLine(0, "");
                    event.setLine(1, "/tf2 checkdata");
                    event.setLine(2, "default");
                    event.setLine(3, "");
                    return;
                }
                LobbyWallUtilities.getUtilities().saveSignLocation(event.getLine(2), bl);
                TF2.getInstance().getMap(event.getLine(2)).load();
                DataConfiguration.getData().reloadData();
                DataConfiguration.getData().saveData();
                LobbyWall.getWall().setDirty(event.getLine(2), true);
                event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] Successfully created a join sign!");
            }
        }
    }

}
