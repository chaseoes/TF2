package me.chaseoes.tf2.listeners;

import com.chaseoes.forcerespawn.event.ForceRespawnEvent;
import me.chaseoes.tf2.GameUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ForceRespawnListener implements Listener {

    @EventHandler
    public void onForceRespawn(ForceRespawnEvent event) {
        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame()) {
            event.setForcedRespawn(true);
        }
    }
}
