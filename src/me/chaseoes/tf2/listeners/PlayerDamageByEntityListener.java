package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.events.TF2DeathEvent;
import me.chaseoes.tf2.utilities.LocationStore;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (GameUtilities.getUtilities().isIngame(damaged)) {

                // Set Armor Durability
                if (damaged.getInventory().getHelmet() != null) {
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
                if (!GameUtilities.getUtilities().getGameStatus(GameUtilities.getUtilities().getCurrentMap(damaged)).equalsIgnoreCase("in-game")) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (GameUtilities.getUtilities().getTeam(damaged).equalsIgnoreCase(GameUtilities.getUtilities().getTeam(damager))) {
                        event.setCancelled(true);
                        return;
                    }

                    if (damager.getItemInHand() != null && damager.getItemInHand().getType().getId() != 373) {
                        damager.getItemInHand().setDurability((short) -100);
                    }

                    if (damaged.getHealth() - event.getDamage() <= 0) {
                        GameUtilities.getUtilities().plugin.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                    }

                }

                if (event.getDamager() instanceof Projectile) {
                    Projectile pro = (Projectile) event.getDamager();
                    if (pro.getShooter() instanceof Player) {
                        Player damager = (Player) pro.getShooter();
                        if (GameUtilities.getUtilities().getTeam(damaged).equalsIgnoreCase(GameUtilities.getUtilities().getTeam(damager))) {
                            event.setCancelled(true);
                            return;
                        }

                        if (damager.getItemInHand() != null) {
                            damager.getItemInHand().setDurability((short) -100);
                        }

                        if (pro instanceof Arrow) {
                            damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 60f, 0f);
                        }

                        if (damaged.getHealth() - event.getDamage() <= 0) {
                            GameUtilities.getUtilities().plugin.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                            event.setCancelled(true);
                        }
                    }
                }
                damaged.damage(event.getDamage());
                event.setCancelled(true);
            }
        }
    }

}