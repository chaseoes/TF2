package me.chaseoes.tf2.capturepoints;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class CapturePoint {

    String map;
    Integer id;
    Location location;
    Integer task = 0;
    Integer ptask = 0;
    CaptureStatus status;
    public Player capturing;

    public CapturePoint(String map, Integer i, Location loc) {
        capturing = null;
        setStatus(CaptureStatus.UNCAPTURED);
        id = i;
        this.map = map;
        location = loc;
    }

    public Integer getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public CaptureStatus getStatus() {
        return status;
    }

    public void setStatus(CaptureStatus s) {
        status = s;
    }

    public void startCapturing(final Player player) {
        capturing = player;
        setStatus(CaptureStatus.CAPTURING);
        Game game = GameUtilities.getUtilities().getCurrentGame(player);
        game.broadcast(ChatColor.YELLOW + "[TF2] Capture point " + ChatColor.DARK_RED + "#" + id + " " + ChatColor.YELLOW + "is being captured!");

        task = CapturePointUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncRepeatingTask(CapturePointUtilities.getUtilities().plugin, new Runnable() {
            Integer timeRemaining = CapturePointUtilities.getUtilities().plugin.getConfig().getInt("capture-timer");
            Integer timeTotal = CapturePointUtilities.getUtilities().plugin.getConfig().getInt("capture-timer");
            Game game = GameUtilities.getUtilities().getCurrentGame(capturing);
            double diff = 1.0d / (timeTotal * 20);
            int currentTick = 0;

            @Override
            public void run() {
                game.setExpOfPlayers(diff * currentTick);
                if (timeRemaining != 0 && currentTick % 20 == 0) {
                    // player.sendMessage(ChatColor.YELLOW + "[TF2] " +
                    // ChatColor.BOLD + ChatColor.DARK_RED + timeRemaining + " "
                    // + ChatColor.RESET + ChatColor.RED +
                    // "seconds remaining!");
                    player.getWorld().strikeLightningEffect(player.getLocation());
                }

                if (timeRemaining == 0 && currentTick % 20 == 0) {
                    for (final GamePlayer gp : game.playersInGame.values()) {
                        TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                            @Override
                            public void run() {
                                gp.getPlayer().playSound(gp.getPlayer().getLocation(), Sound.ANVIL_LAND, 1, 1);
                            }
                        }, 1L);
                    }
                    stopCapturing();
                    setStatus(CaptureStatus.CAPTURED);
                    game.broadcast(ChatColor.YELLOW + "[TF2] Capture point " + ChatColor.DARK_RED + "#" + id + " " + ChatColor.YELLOW + "has been captured by " + ChatColor.DARK_RED + ChatColor.BOLD + player.getName() + ChatColor.RESET + ChatColor.YELLOW + "!");
                    game.setExpOfPlayers(0);

                    if (TF2.getInstance().getMap(map).allCaptured()) {
                        game.winMatch(Team.RED);
                        return;
                    }
                }
                currentTick++;
                if (currentTick % 20 == 0) {
                    timeRemaining--;
                    timeTotal++;
                }
            }
        }, 0L, 1L);

        ptask = CapturePointUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncRepeatingTask(CapturePointUtilities.getUtilities().plugin, new Runnable() {
            @Override
            public void run() {
                Player p = capturing;
                if (p == null) {
                    stopCapturing();
                    return;
                }

                if (!CapturePointUtilities.getUtilities().locationIsCapturePoint(player.getLocation())) {
                    stopCapturing();
                    return;
                }
            }
        }, 0L, 1L);
    }

    public void stopCapturing() {
        if (ptask != 0) {
            Bukkit.getScheduler().cancelTask(ptask);
            ptask = 0;
        }
        if (task != 0) {
            Bukkit.getScheduler().cancelTask(task);
            task = 0;
        }

        if (capturing != null && GameUtilities.getUtilities().getCurrentGame(capturing) != null) {
            GameUtilities.getUtilities().getCurrentGame(capturing).setExpOfPlayers(0d);
            capturing = null;
        }
        setStatus(CaptureStatus.UNCAPTURED);
    }

}
