package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.classes.TF2Class;
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
            damaged.setHealth(20);
            e.getDrops().clear();
            e.setDeathMessage(null);
            Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damaged));
            TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    TF2Class c = GameUtilities.getUtilities().getGamePlayer(damaged).getCurrentClass();
                    c.apply(gDamaged);
                    gDamaged.setIsDead(false);
                }
            }, 1L);
        }
    }

}
