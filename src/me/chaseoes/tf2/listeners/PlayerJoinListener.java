package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("tf2.create") && GameUtilities.getUtilities().plugin.uc.needsUpdate()) {
            GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                @Override
                public void run() {
                    GameUtilities.getUtilities().plugin.uc.nagPlayer(player);
                }
            }, 60L);
        }
    }

}
