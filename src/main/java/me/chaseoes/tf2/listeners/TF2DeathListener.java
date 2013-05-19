package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.events.TF2DeathEvent;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TF2DeathListener implements Listener {

    @EventHandler
    public void onDeath(final TF2DeathEvent event) {
        TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                final GamePlayer playerg = GameUtilities.getUtilities().getGamePlayer(player);
                final Player killer = event.getKiller();
                GamePlayer killerg = GameUtilities.getUtilities().getGamePlayer(killer);

                Game game = playerg.getGame();
                Map map = game.getMap();

                player.teleport(MapUtilities.getUtilities().loadTeamSpawn(map.getName(), playerg.getTeam()));
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-KILLED-BY").replace("%teamcolor", killerg.getTeamColor()).replace("%player", killer.getName()).replace("%class", killerg.getCurrentClass().getName()));
                killer.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-KILLED").replace("%teamcolor", playerg.getTeamColor()).replace("%player", player.getName()).replace("%class", playerg.getCurrentClass().getName()));
                killer.playSound(killer.getLocation(), Sound.valueOf(TF2.getInstance().getConfig().getString("killsound.sound")), TF2.getInstance().getConfig().getInt("killsound.volume"), TF2.getInstance().getConfig().getInt("killsound.pitch"));

                TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        playerg.setJustSpawned(false);
                    }
                }, 40L);

                // Reset the kills of the player who died.
                playerg.addKillstreak(playerg.getKills());
                playerg.setKills(0);
                playerg.setDeaths(-1);
                playerg.settotalDeaths(-1);

                // Add one kill to the kills the killer has made.
                killerg.setTotalKills(-1);
                killer.setLevel(killerg.getTotalKills());
                killerg.setKills(-1);

                int kills = killerg.getKills();
                if (kills % TF2.getInstance().getConfig().getInt("killstreaks") == 0) {
                    game.broadcast(ChatColor.YELLOW + "[TF2] " + playerg.getTeamColor() + killer.getName() + " " + ChatColor.RESET + ChatColor.YELLOW + "is on a " + ChatColor.DARK_RED + ChatColor.BOLD + "" + kills + " " + ChatColor.RESET + ChatColor.YELLOW + "kill streak!");
                }

                player.setHealth(20);
                player.setFireTicks(0);
                TF2Class c = playerg.getCurrentClass();
                c.apply(playerg);
                playerg.setJustSpawned(true);
                playerg.setIsDead(false);
                playerg.setPlayerLastDamagedBy(null);
            }
        }, 1L);
    }

}
