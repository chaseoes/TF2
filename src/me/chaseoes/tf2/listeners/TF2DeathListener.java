package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.events.TF2DeathEvent;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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

                Game game = GameUtilities.getUtilities().getCurrentGame(player);
                Map map = TF2.getInstance().getMap(game.getMapName());

                player.teleport(MapUtilities.getUtilities().loadTeamSpawn(map.getName(), playerg.getTeam()));
                player.sendMessage(ChatColor.YELLOW + "[TF2] You were killed by " + killerg.getTeamColor() + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + killerg.getCurrentClass().getName() + ")!");
                killer.sendMessage(ChatColor.YELLOW + "[TF2] You killed " + playerg.getTeamColor() + player.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "(" + playerg.getCurrentClass().getName() + ")!");
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
                playerg.settotalDeaths(-1);

                // Add one kill to the kills the killer has made.
                killerg.setTotalKills(-1);
                killer.setLevel(killerg.getTotalKills());
                killerg.setKills(-1);
                int kills = killerg.getKills();

                if (kills % GameUtilities.getUtilities().plugin.getConfig().getInt("killstreaks") == 0) {
                    game.broadcast(ChatColor.YELLOW + "[TF2] " + playerg.getTeamColor() + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "is on a " + ChatColor.DARK_RED + ChatColor.BOLD + "" + kills + " " + ChatColor.RESET + ChatColor.YELLOW + "kill streak!");
                }

                player.setHealth(20);
                player.setFireTicks(0);
                TF2Class c = playerg.getCurrentClass();
                c.apply(player);
                playerg.setJustSpawned(true);
                playerg.setIsDead(false);
                playerg.setPlayerLastDamagedBy(null);
            }
        }, 1L);
    }

}
