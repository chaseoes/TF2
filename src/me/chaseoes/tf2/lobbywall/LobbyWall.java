package me.chaseoes.tf2.lobbywall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;

public class LobbyWall {

    private TF2 plugin;
    static LobbyWall instance = new LobbyWall();
    public List<String> cantUpdate = new ArrayList<String>();
    private HashMap<String, CachedLobbyWallInfo> cache = new HashMap<String, CachedLobbyWallInfo>();

    private LobbyWall() {

    }

    public static LobbyWall getWall() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    private void updateWall(String map) {
        if (!cantUpdate.contains(map)) {
            try {
                CachedLobbyWallInfo info = getCacheInfo(map);
                if (info.isDirty()) {
                    info.recache();
                }
                List<Sign> signs = info.getSignLocations();
                if (signs.size() == 0) {
                    return;
                }
                info.verifySigns();
                LobbyWallUtilities.getUtilities().setSignLines(signs.get(0), "Team Fortress 2", "Click here", "to join:", ChatColor.BOLD + "" + map);
                if (info.getGame().getStatus() != GameStatus.DISABLED) {
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(1), " ", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Status:", info.getGame().getPrettyStatus(), " ");
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(2), "" + ChatColor.DARK_RED + ChatColor.BOLD + "Red Team:", info.getGame().getSizeOfTeam(Team.RED) + "/" + plugin.getMap(map).getPlayerlimit() / 2 + " Players", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team:", info.getGame().getSizeOfTeam(Team.BLUE) + "/" + plugin.getMap(map).getPlayerlimit() / 2 + " Players");
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(3), " ", ChatColor.BLUE + "" + ChatColor.BOLD + "Time Left:", info.getGame().getTimeLeft(), " ");
                } else {
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(1), " ", ChatColor.BOLD + "Status:", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Disabled", " ");
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(2), " ", "---------------------------------------------", "-------------------------------------", " ");
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(3), " ", "---------------------------------------------", "-------------------------------------", " ");
                }
                for (int i = 4; i < 4 + info.getCapturePoints().size(); i++) {
                    String color = ChatColor.BLUE + "" + ChatColor.BOLD;
                    if (getFriendlyCaptureStatus(map, i - 3).equalsIgnoreCase("captured")) {
                        color = ChatColor.DARK_RED + "" + ChatColor.BOLD;
                    }
                    if (info.getGame().getStatus() != GameStatus.DISABLED) {
                        LobbyWallUtilities.getUtilities().setSignLines(signs.get(i), "Capture Point", "#" + (i - 3), "Status:", color + getFriendlyCaptureStatus(map, i - 3));
                    } else {
                        LobbyWallUtilities.getUtilities().setSignLines(signs.get(i), " ", "---------------------------------------------", "-------------------------------------", " ");
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().log(Level.WARNING, "Encountered an error while trying to update the lobby wall.");
            }
        }
    }

    public void setAllLines(final String map, final Integer duration, final String[] lines, final Boolean s1, final Boolean s2) {
        try {
            if (!cantUpdate.contains(map)) {
                cantUpdate.add(map);
            }
            CachedLobbyWallInfo info = getCacheInfo(map);
            if (info.isDirty()) {
                info.recache();
            }
            Game game = info.getGame();
            List<Sign> signs = info.getSignLocations();
            if (signs.size() == 0) {
                return;
            }
            info.verifySigns();
            if (s1) {
                LobbyWallUtilities.getUtilities().setSignLines(signs.get(0), lines[0], lines[1], lines[2], lines[3]);
            } else {
                LobbyWallUtilities.getUtilities().setSignLines(signs.get(0), "Team Fortress 2", "Click here", "to join:", ChatColor.BOLD + "" + map);
            }
            if (s2) {
                LobbyWallUtilities.getUtilities().setSignLines(signs.get(1), lines[0], lines[1], lines[2], lines[3]);
            } else {
                if (game.getStatus() != GameStatus.DISABLED) {
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(1), " ", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Status:", game.getPrettyStatus(), " ");
                } else {
                    LobbyWallUtilities.getUtilities().setSignLines(signs.get(1), " ", ChatColor.BOLD + "Status:", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Disabled", " ");
                }
            }
            LobbyWallUtilities.getUtilities().setSignLines(signs.get(2), lines[0], lines[1], lines[2], lines[3]);
            LobbyWallUtilities.getUtilities().setSignLines(signs.get(3), lines[0], lines[1], lines[2], lines[3]);
            for (int i = 4; i < 4 + info.getCapturePoints().size(); i++) {
                LobbyWallUtilities.getUtilities().setSignLines(signs.get(i), lines[0], lines[1], lines[2], lines[3]);
            }
            if (duration != null) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        cantUpdate.remove(map);
                    }
                }, duration * 20L);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Encountered an error while trying to update the lobby wall.");
        }
    }

    public String getFriendlyCaptureStatus(String map, Integer id) {
        Map m = plugin.getMap(map);
        String ss = m.getCapturePoint(id).getStatus().string();
        if (ss.equalsIgnoreCase("uncaptured")) {
            return "Uncaptured";
        }
        if (ss.equalsIgnoreCase("capturing")) {
            return "Capturing";
        }
        if (ss.equalsIgnoreCase("captured")) {
            return "Captured";
        }
        return null;
    }

    int lobby = -1;

    public void startTask() {
        if (lobby == -1) {
            lobby = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    for (final String map : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
                        if (!cantUpdate.contains(map)) {
                            updateWall(map);
                        }
                    }
                }
            }, 0L, 20L);
        }
    }

    public void setDirty(String map, boolean dirty) {
        getCacheInfo(map).setDirty(dirty);
    }

    public CachedLobbyWallInfo getCacheInfo(String map) {
        if (cache.containsKey(map)) {
            return cache.get(map);
        }
        CachedLobbyWallInfo info = new CachedLobbyWallInfo(map);
        cache.put(map, info);
        return info;
    }

    public void unloadCacheInfo(String map) {
        cache.remove(map);
    }
}
