package me.chaseoes.tf2;

import java.util.HashMap;

import me.chaseoes.tf2.utilities.Localizer;
import me.chaseoes.tf2.utilities.LocationStore;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
                            if (player == null) {
                                continue;
                            }
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

                                    if (afklimit.equals(afktime)) {
                                        GameUtilities.getUtilities().getGamePlayer(player).getGame().leaveGame(player);
                                        player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-KICKED-FOR-AFK"));
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
        if (redcounter.containsKey(map.getName())) {
            return;
        }
        redcounter.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int secondsleft = map.getRedTeamTeleportTime();

            @Override
            public void run() {
                if (secondsleft > 0) {
                    if (secondsleft % 10 == 0 || secondsleft < 6) {
                        game.broadcast(Localizer.getLocalizer().loadPrefixedMessage("RED-TEAM-TELEPORTED-IN").replace("%time", secondsleft + ""), Team.RED);
                    }
                } else {
                    stopRedTeamCountdown(map.getName());
                }
                secondsleft--;
            }
        }, 0L, 20L));
    }

    public void startCountdown(final Map map) {
        final Game game = GameUtilities.getUtilities().getGame(map);
        game.setStatus(GameStatus.STARTING);
        if (countdowns.containsKey(map.getName())) {
            return;
        }
        countdowns.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int secondsLeft = plugin.getConfig().getInt("countdown");

            @Override
            public void run() {
                if (secondsLeft > 0) {
                    if (secondsLeft % 10 == 0 || secondsLeft < 6) {
                        game.broadcast(Localizer.getLocalizer().loadPrefixedMessage("GAME-STARTING-IN").replace("%time", secondsLeft + ""));
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
        if (timelimitcounter.containsKey(map.getName())) {
            return;
        }
        timelimitcounter.put(map.getName(), plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int current = 0;
            int secondsleft = map.getTimelimit();

            @Override
            public void run() {
                try {
                    game.time = current;
                    if (secondsleft > 0) {
                        if (secondsleft % 60 == 0 || secondsleft < 10) {
                            game.broadcast(Localizer.getLocalizer().loadPrefixedMessage("GAME-ENDING-IN").replace("%time", game.getTimeLeftPretty()));
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
            redcounter.remove(map);
        }
    }

    public void stopTimeLimitCounter(String map) {
        if (timelimitcounter.get(map) != null) {
            plugin.getServer().getScheduler().cancelTask(timelimitcounter.get(map));
            timelimitcounter.remove(map);
        }
    }

    public void stopCountdown(String map) {
        if (countdowns.get(map) != null) {
            plugin.getServer().getScheduler().cancelTask(countdowns.get(map));
            countdowns.remove(map);
        }
    }

}
