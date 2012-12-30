package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;

import me.chaseoes.tf2.TF2;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        GameUtilities.getUtilities().playerJoinServer(player);
        if (player.hasPermission("tf2.create") && GameUtilities.getUtilities().plugin.uc.needsUpdate()) {
            TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    TF2.getInstance().uc.nagPlayer(player);
                }
            }, 60L);
        }
    }

}
