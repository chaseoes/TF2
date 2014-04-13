package com.chaseoes.tf2.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.events.TF2DeathEvent;
import com.chaseoes.tf2.utilities.LocationStore;

public class PlayerDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            GamePlayer gDamaged = GameUtilities.getUtilities().getGamePlayer(damaged);
            if (gDamaged.isIngame()) {

                GamePlayer gdamaged = GameUtilities.getUtilities().getGamePlayer(damaged);
                if (gdamaged.isInLobby() || gdamaged.getGame().getStatus() != GameStatus.INGAME || gdamaged.isDead()) {
                    event.setCancelled(true);
                    return;
                }

                // Set Armor Durability
                if (damaged.getInventory().getHelmet() != null && damaged.getInventory().getHelmet().getType() != Material.SKULL_ITEM) {
                    damaged.getInventory().getHelmet().setDurability((short) -100);
                }
                if (damaged.getInventory().getChestplate() != null) {
                    damaged.getInventory().getChestplate().setDurability((short) -100);
                }
                if (damaged.getInventory().getLeggings() != null) {
                    damaged.getInventory().getLeggings().setDurability((short) -100);
                }
                if (damaged.getInventory().getBoots() != null) {
                    damaged.getInventory().getBoots().setDurability((short) -100);
                }

                // AFK Timer
                LocationStore.setAFKTime(damaged, null);
                LocationStore.unsetLastLocation(damaged);

                // Check Game Status
                Game game = gDamaged.getGame();
                if (game.getStatus() != GameStatus.INGAME) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    GamePlayer gdamager = GameUtilities.getUtilities().getGamePlayer(damager);
                    if (!gdamager.isIngame()) {
                        event.setCancelled(true);
                        return;
                    }
                    if (gdamaged.getTeam() == gdamager.getTeam()) {
                        event.setCancelled(true);
                        return;
                    }

                    if (!gdamager.getGame().getMapName().equalsIgnoreCase(gdamaged.getGame().getMapName())) {
                        event.setCancelled(true);
                        return;
                    }

                    if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.POTION) {
                        damager.getItemInHand().setDurability((short) -100);
                    }

                    gdamaged.setPlayerLastDamagedBy(gdamager);

                    if (damaged.getHealth() - event.getDamage() <= 0) {
                        TF2.getInstance().getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                        gdamaged.setIsDead(true);
                        event.setCancelled(true);
                        return;
                    }

                }

                if (event.getDamager() instanceof Projectile) {
                    Projectile pro = (Projectile) event.getDamager();
                    if (pro.getShooter() instanceof Player) {
                        Player damager = (Player) pro.getShooter();
                        GamePlayer gdamager = GameUtilities.getUtilities().getGamePlayer(damager);
                        if (!gdamager.isIngame()) {
                            event.setCancelled(true);
                            return;
                        }
                        if (gdamaged.getTeam() == gdamager.getTeam()) {
                            event.setCancelled(true);
                            return;
                        }

                        if (!gdamager.getGame().getMapName().equalsIgnoreCase(gdamaged.getGame().getMapName())) {
                            event.setCancelled(true);
                            return;
                        }

                        if (damager.getItemInHand() != null && damager.getItemInHand().getType() != Material.POTION) {
                            damager.getItemInHand().setDurability((short) -100);
                        }

                        if (pro instanceof Arrow) {
                            damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 60f, 0f);
                        }

                        gdamaged.setPlayerLastDamagedBy(gdamager);

                        if (damaged.getHealth() - event.getDamage() <= 0) {
                            TF2.getInstance().getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                            gdamaged.setIsDead(true);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

}
