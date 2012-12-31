package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

public class PotionSplashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
        for (LivingEntity e : event.getAffectedEntities()) {
            if (e instanceof Player) {
                Player damaged = (Player) e;
                if (event.getPotion().getShooter() != null && event.getPotion().getShooter() instanceof Player) {
                    Player throwee = (Player) event.getPotion().getShooter();
                    if (GameUtilities.getUtilities().isIngame(throwee) && GameUtilities.getUtilities().isIngame(damaged)) {
                        GamePlayer gthrowee = GameUtilities.getUtilities().getGamePlayer(throwee);
                        GamePlayer gdamaged = GameUtilities.getUtilities().getGamePlayer(damaged);
                        if (gthrowee.getTeam() == gdamaged.getTeam()) {
                            e.setNoDamageTicks(1);
                        }
                    }
                }
            }
        }
    }

}
