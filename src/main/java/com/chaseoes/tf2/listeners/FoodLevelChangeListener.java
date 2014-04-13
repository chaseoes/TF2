package com.chaseoes.tf2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.commands.SpectateCommand;

public class FoodLevelChangeListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoseHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && (GameUtilities.getUtilities().getGamePlayer((Player) event.getEntity()).isIngame() || SpectateCommand.getCommand().isSpectating((Player) event.getEntity()))) {
            event.setCancelled(true);
        }
    }

}
