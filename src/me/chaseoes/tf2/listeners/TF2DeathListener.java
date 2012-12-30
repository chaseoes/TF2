package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.events.TF2DeathEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.Sound;
import org.bukkit.event.Listener;

public class TF2DeathListener implements Listener {
    
    @EventHandler
    public void onDeath(final TF2DeathEvent event) {
        GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                final Player killer = event.getKiller();

                player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().getCurrentMap(player), Team.match(GameUtilities.getUtilities().getTeam(player))));
                player.sendMessage(ChatColor.YELLOW + "[TF2] You were killed by " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + ClassUtilities.getUtilities().classes.get(killer.getName()) + ")!");
                killer.sendMessage(ChatColor.YELLOW + "[TF2] You killed " + GameUtilities.getUtilities().getTeamColor(player) + player.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + ClassUtilities.getUtilities().classes.get(player.getName()) + ")!");
                killer.playSound(killer.getLocation(), Sound.valueOf(GameUtilities.getUtilities().plugin.getConfig().getString("killsound.sound")), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.volume"), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.pitch"));

                GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                    @Override
                    public void run() {
                        GameUtilities.getUtilities().justspawned.remove(player.getName());
                    }
                }, 40L);

                GameUtilities.getUtilities().setKills(player, 0);
                Integer kills = GameUtilities.getUtilities().getKills(killer);
                Integer tkills = GameUtilities.getUtilities().totalkills.get(killer.getName());

                if (tkills == null) {
                    GameUtilities.getUtilities().totalkills.put(killer.getName(), 1);
                } else {
                    GameUtilities.getUtilities().totalkills.put(killer.getName(), tkills + 1);
                }

                if (kills != null && kills != 0) {
                    GameUtilities.getUtilities().setKills(killer, kills + 1);
                    if ((kills + 1) % GameUtilities.getUtilities().plugin.getConfig().getInt("killstreaks") == 0) {
                        GameUtilities.getUtilities().broadcast(GameUtilities.getUtilities().getCurrentMap(killer), ChatColor.YELLOW + "[TF2] " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "is on a " + ChatColor.DARK_RED + ChatColor.BOLD + "" + (kills + 1) + " " + ChatColor.RESET + ChatColor.YELLOW + "kill streak!");
                    } else {
                        killer.sendMessage(ChatColor.YELLOW + "[TF2] You have made " + (kills + 1) + " kills!");
                    }

                } else {
                    GameUtilities.getUtilities().setKills(killer, 1);
                }
                player.setHealth(20);
                TF2Class c = new TF2Class(ClassUtilities.getUtilities().classes.get(player.getName()));
                c.apply(player);
                GameUtilities.getUtilities().justspawned.add(player.getName());
            }
        }, 1L);
    }

}
