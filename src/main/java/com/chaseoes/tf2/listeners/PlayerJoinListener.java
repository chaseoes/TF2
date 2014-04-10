package com.chaseoes.tf2.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.WorldEditUtilities;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        for (Map m : TF2.getInstance().getMaps()) {
            if (m.getP1() != null && m.getP2() != null && WorldEditUtilities.getWEUtilities().isInMap(player.getLocation(), m)) {
                if (!player.hasPermission("tf2.create")) {
                    player.teleport(MapUtilities.getUtilities().loadLobby());
                }
            }
        }

        GameUtilities.getUtilities().playerJoinServer(player);
        if (TF2.getInstance().getConfig().getBoolean("dedicated-join")) {
            player.performCommand("tf2 join " + MapUtilities.getUtilities().getRandomMap().getName());
        }
    }

}
