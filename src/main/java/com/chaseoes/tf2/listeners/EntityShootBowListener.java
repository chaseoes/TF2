package com.chaseoes.tf2.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;

public class EntityShootBowListener implements Listener {

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
            if (gp.isIngame() && (gp.getGame().getStatus() != GameStatus.INGAME || gp.isInLobby())) {
                event.setCancelled(true);
                return;
            }
            if (gp.isIngame()) {
                gp.setArrowsFired(-1);
            }
        }
    }

}
