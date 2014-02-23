package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.events.TF2DeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        final Player damaged = e.getEntity();
        final GamePlayer gDamaged = GameUtilities.getUtilities().getGamePlayer(damaged);
        if (gDamaged.isIngame()) {
            e.getDrops().clear();
            e.setDeathMessage(null);
            e.setDroppedExp(0);
            if (gDamaged.getPlayerLastDamagedBy() == null) {
                Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damaged));
            } else {
                Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, gDamaged.getPlayerLastDamagedBy().getPlayer()));
            }
        }
    }

}
