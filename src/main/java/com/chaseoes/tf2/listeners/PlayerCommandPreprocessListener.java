package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.chaseoes.tf2.GameUtilities;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame() && !event.getMessage().startsWith("/tf2") && !event.getPlayer().hasPermission("tf2.create")) {
            event.setCancelled(true);
            Localizers.getDefaultLoc().CANT_USE_COMMANDS_INGAME.sendPrefixed(event.getPlayer());
        }
    }

}
