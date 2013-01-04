package me.chaseoes.tf2;

import me.chaseoes.tf2.utilities.LocationStore;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Schedulers {

    private TF2 plugin;
    static Schedulers instance = new Schedulers();
    Integer afkchecker;
    public HashMap<String, Integer> redcounter = new HashMap<String, Integer>();
    public HashMap<String, Integer> countdowns = new HashMap<String, Integer>();
    public HashMap<String, Integer> timelimitcounter = new HashMap<String, Integer>();

    private Schedulers() {

    }

    public static Schedulers getSchedulers() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void startAFKChecker() {
        final Integer afklimit = plugin.getConfig().getInt("afk-timer");
        afkchecker = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    for (Map map : MapUtilities.getUtilities().getMaps()) {
                        for (String p : GameUtilities.getUtilities().getGame(map).getPlayersIngame()) {
                            Player player = plugin.getServer().getPlayerExact(p);
                            Integer afktime = LocationStore.getAFKTime(player);
                            Location lastloc = LocationStore.getLastLocation(player);
                            Location currentloc = new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
                            if (lastloc != null) {
                                if (lastloc.getWorld().getName().equals(currentloc.getWorld().getName()) && lastloc.getBlockX() == currentloc.getBlockX() && lastloc.getBlockY() == currentloc.getBlockY() && lastloc.getBlockZ() == currentloc.getBlockZ()) {

                                    if (afktime == null) {
                                        LocationStore.setAFKTime(player, 1);
                                    } else {
                                        LocationStore.setAFKTime(player, afktime + 1);
                                    }

                                    if (afklimit == afktime) {
                                        GameUtilities.getUtilities().getGamePlayer(player).leaveCurrentGame();
                                        player.sendMessage(ChatColor.YELLOW + "[TF2] You have been kicked from the map for being AFK.");
                                        LocationStore.setAFKTime(player, null);
                                        LocationStore.unsetLastLocation(player);
                                    }
                                } else {
                                    LocationStore.setAFKTime(player, null);
                                    LocationStore.unsetLastLocation(player);
                                }
                                LocationStore.setLastLocation(player);
                            } else {
                                LocationStore.setLastLocation(player);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 20L);
    }

    public void stopAFKChecker() {
        if (afkchecker != null) {
            plugin.getServer().getScheduler().cancelTask(afkchecker);
        }
        afkchecker = null;
    }

    public void startRedTeamCountdown(final Map map) {
        final Game game = GameUtilities.getUtilities().getGame(map);
        redcounter.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int secondsleft = map.getRedTeamTeleportTime();

            @Override
            public void run() {
                if (secondsleft > 0) {
                    if (secondsleft % 10 == 0 || secondsleft < 6) {
                        for (String playe : game.getPlayersIngame()) {
                            Player player = plugin.getServer().getPlayerExact(playe);
                            GamePlayer gp = game.getPlayer(player);
                            if (gp.getTeam() == Team.RED) {
                                player.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.DARK_RED + ChatColor.BOLD + "Red " + ChatColor.RESET + ChatColor.YELLOW + "team, you will be teleported in " + secondsleft + " seconds.");
                            }
                        }
                    }
                }
                secondsleft--;
            }
        }, 0L, 20L));
    }

    public void startCountdown(final Map map) {
        final Game game = GameUtilities.getUtilities().getGame(map);
        game.setStatus(GameStatus.STARTING);
        countdowns.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int secondsLeft = plugin.getConfig().getInt("countdown");
            @Override
            public void run() {
                if (secondsLeft > 0) {
                    if (secondsLeft % 10 == 0 || secondsLeft < 6) {
                        game.broadcast(ChatColor.BLUE + "Game starting in " + ChatColor.AQUA + secondsLeft + " " + ChatColor.BLUE + "seconds!");
                    }
                    secondsLeft--;
                } else {
                    game.startMatch();
                    stopCountdown(map.getName());
                }
            }
        }, 0L, 20L));
    }

    public void startTimeLimitCounter(final Map map) {
        final Game game = GameUtilities.getUtilities().getGame(map);
        final int limit = map.getTimelimit();
        timelimitcounter.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int current = 0;
            int secondsleft = map.getTimelimit();
            @Override
            public void run() {
                try {
                    game.time = current;
                    if (secondsleft > 0) {
                        if (secondsleft % 60 == 0 || secondsleft < 10) {
                            game.broadcast(ChatColor.BLUE + "Game ending in " + ChatColor.AQUA + game.getTimeLeftPretty() + ChatColor.BLUE + "!");
                        }
                    }
                    secondsleft--;
                    if (current >= limit) {
                        game.winMatch(Team.BLUE);
                        stopTimeLimitCounter(map.getName());
                    }
                    current++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 20L));
    }

    public void stopRedTeamCountdown(String map) {
        if (redcounter.get(map) != null) {
            plugin.getServer().getScheduler().cancelTask(redcounter.get(map));
        }
    }

    public void stopTimeLimitCounter(String map) {
        if (timelimitcounter.get(map) != null) {
            plugin.getServer().getScheduler().cancelTask(timelimitcounter.get(map));
        }
    }

    public void stopCountdown(String map) {
        if (countdowns.get(map) != null) {
            plugin.getServer().getScheduler().cancelTask(countdowns.get(map));
        }
    }

}
