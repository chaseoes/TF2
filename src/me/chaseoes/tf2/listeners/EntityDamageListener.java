package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.events.TF2DeathEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer((Player) event.getEntity());
            if (gp.isIngame() && !gp.isDead() && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                if (gp.getGame().getStatus() == GameStatus.INGAME && !gp.isInLobby()) {
                    if (gp.getPlayer().getHealth() - event.getDamage() <= 0) {
                        if (gp.getPlayerLastDamagedBy() == null) {
                            TF2.getInstance().getServer().getPluginManager().callEvent(new TF2DeathEvent(gp.getPlayer(), gp.getPlayer()));
                        } else {
                            TF2.getInstance().getServer().getPluginManager().callEvent(new TF2DeathEvent(gp.getPlayer(), gp.getPlayerLastDamagedBy().getPlayer()));
                        }
                        gp.setIsDead(true);
                        event.setCancelled(true);
                        return;
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }
}
