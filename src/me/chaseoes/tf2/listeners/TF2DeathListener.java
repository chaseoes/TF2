package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.events.TF2DeathEvent;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TF2DeathListener implements Listener {

    @EventHandler
    public void onDeath(final TF2DeathEvent event) {
        GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                final GamePlayer playerg = GameUtilities.getUtilities().getGamePlayer(player);
                final Player killer = event.getKiller();
                GamePlayer killerg = GameUtilities.getUtilities().getGamePlayer(killer);

                player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().getCurrentMap(player), Team.match(GameUtilities.getUtilities().getTeam(player))));
                player.sendMessage(ChatColor.YELLOW + "[TF2] You were killed by " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + killerg.getCurrentClass().getName() + ")!");
                killer.sendMessage(ChatColor.YELLOW + "[TF2] You killed " + GameUtilities.getUtilities().getTeamColor(player) + player.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + playerg.getCurrentClass().getName() + ")!");
                killer.playSound(killer.getLocation(), Sound.valueOf(GameUtilities.getUtilities().plugin.getConfig().getString("killsound.sound")), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.volume"), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.pitch"));

                GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                    @Override
                    public void run() {
                        playerg.setJustSpawned(false);
                    }
                }, 40L);

                // Reset the kills of the player who died.
                playerg.setKills(0);
                playerg.setDeaths(-1);

                // Add one kill to the kills the killer has made.
                killerg.setKills(-1);
                int kills = killerg.getKills();

                if (kills % GameUtilities.getUtilities().plugin.getConfig().getInt("killstreaks") == 0) {
                    GameUtilities.getUtilities().broadcast(GameUtilities.getUtilities().getCurrentMap(killer), ChatColor.YELLOW + "[TF2] " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "is on a " + ChatColor.DARK_RED + ChatColor.BOLD + "" + kills + " " + ChatColor.RESET + ChatColor.YELLOW + "kill streak!");
                } else {
                    killer.sendMessage(ChatColor.YELLOW + "[TF2] You have made " + kills + " kills!");
                }

                player.setHealth(20);
                TF2Class c = playerg.getCurrentClass();
                c.apply(player);
                playerg.setJustSpawned(true);
            }
        }, 1L);
    }

}
