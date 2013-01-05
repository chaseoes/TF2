package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {
    
    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
            if (gp.isIngame()) {
                gp.setArrowsFired(-1);
            }
        }
    }

}
