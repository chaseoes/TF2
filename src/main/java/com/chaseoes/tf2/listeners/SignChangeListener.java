package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.lobbywall.LobbyWall;
import com.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import com.chaseoes.tf2.utilities.DataChecker;

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
                    Localizers.getDefaultLoc().MAP_NOT_SETUP.sendPrefixed(player);
                    Localizers.getDefaultLoc().MAP_NOT_SETUP_COMMAND_HELP.sendPrefixed(player, map);
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
                Localizers.getDefaultLoc().LOBBYWALL_CREATED.sendPrefixed(event.getPlayer());
            }
        }
    }

}
