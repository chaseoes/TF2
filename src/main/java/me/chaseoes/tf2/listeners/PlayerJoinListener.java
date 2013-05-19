package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.WorldEditUtilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        if (player.hasPermission("tf2.create") && TF2.getInstance().uc.needsUpdate()) {
            TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    TF2.getInstance().uc.nagPlayer(player);
                }
            }, 60L);
        }
    }

}
