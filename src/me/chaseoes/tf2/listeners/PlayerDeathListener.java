package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
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
        if (GameUtilities.getUtilities().isIngame(damaged)) {
            damaged.setHealth(20);
            e.getDrops().clear();
            e.setDeathMessage(null);
            Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damaged));
            GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                @Override
                public void run() {
                    TF2Class c = GameUtilities.getUtilities().getCurrentGame(damaged).getPlayer(damaged).getCurrentClass();
                    c.apply(damaged);
                }
            }, 1L);
        }
    }

}
