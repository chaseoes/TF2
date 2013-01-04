package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.utilities.LocationStore;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileLaunchListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION && event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (GameUtilities.getUtilities().isIngame(shooter)) {
                LocationStore.setAFKTime(shooter, null);
                LocationStore.unsetLastLocation(shooter);
            }

        }
    }

}
