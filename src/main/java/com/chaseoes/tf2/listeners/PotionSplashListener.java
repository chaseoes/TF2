package com.chaseoes.tf2.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;

public class PotionSplashListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
        for (LivingEntity e : event.getAffectedEntities()) {
            if (e instanceof Player) {
                Player damaged = (Player) e;
                GamePlayer gDamaged = GameUtilities.getUtilities().getGamePlayer(damaged);
                if (event.getPotion().getShooter() != null && event.getPotion().getShooter() instanceof Player) {
                    Player throwee = (Player) event.getPotion().getShooter();
                    GamePlayer gThrowee = GameUtilities.getUtilities().getGamePlayer(throwee);
                    if (gThrowee.isIngame() && gDamaged.isIngame()) {
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
