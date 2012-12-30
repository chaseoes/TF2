package me.chaseoes.tf2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GameUtilities {

    public TF2 plugin;
    static GameUtilities instance = new GameUtilities();

    public HashMap<String, Game> games = new HashMap<String, Game>();

    public HashMap<String, Integer> afktimes = new HashMap<String, Integer>();
    public HashMap<String, Location> afklocations = new HashMap<String, Location>();
    public HashMap<String, String> makingclassbutton = new HashMap<String, String>();
    public HashMap<String, String> makingclassbuttontype = new HashMap<String, String>();
    public HashSet<String> makingchangeclassbutton = new HashSet<String>();
    public HashSet<String> gamestarted = new HashSet<String>(); // map
    public HashSet<String> gameinlobby = new HashSet<String>(); // map
    public HashSet<String> gamestarting = new HashSet<String>(); // map
    public HashSet<String> justspawned = new HashSet<String>(); // player
    public HashMap<String, Integer> gametimes = new HashMap<String, Integer>();
    public HashMap<String, Integer> arrows = new HashMap<String, Integer>();
    public HashMap<String, Integer> totalkills = new HashMap<String, Integer>();
    public HashMap<String, Integer> totaldeaths = new HashMap<String, Integer>();
    public List<String> coolpeople = new ArrayList<String>();
    public Integer afkchecker;

    private GameUtilities() {

    }

    public static GameUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void addGame(Map map) {
        games.put(map.getName(), new Game(map, plugin));
    }

    public void joinGame(Player player, String map, String team) {
        games.get(map).joinGame(player, team);
    }

    public void leaveCurrentGame(Player player) {
        if (isIngame(player)) {
            String map = getCurrentMap(player);
            games.get(map).leaveGame(player);
        } else {
            player.sendMessage(ChatColor.YELLOW + "[TF2] You are not playing TF2!");
        }
    }

    public void startMatch(final String map) {
        gamestarted.add(map);
        gameinlobby.remove(map);
        games.get(map).startMatch();
    }

    public void stopMatch(String map) {
        gamestarted.remove(map);
        gameinlobby.add(map);
        games.get(map).stopMatch();
    }

    public void winGame(final String map, String team) {
        games.get(map).winGame(team);
    }

    public Integer getAmountOnTeam(String map, String team) {
        return games.get(map).getAmountOnTeam(team);
    }

    public String getTeamColor(Player player) {
        String map = getCurrentMap(player);
        return games.get(map).getTeamColor(player);
    }

    public Integer getTimeLeftSeconds(String map) {
        return games.get(map).getTimeLeftSeconds();
    }

    public String decideTeam(String map) {
        return games.get(map).decideTeam();
    }

    public List<String> getIngameList(String map) {
        if (games.containsKey(map)) {
            return games.get(map).getIngameList();
        }
        List<String> list = new ArrayList<String>();
        return list;
    }

    public String getTeam(Player player) {
        String map = getCurrentMap(player);
        return games.get(map).getTeam(player);
    }

    public Boolean isIngame(Player player) {
        return getCurrentMap(player) != null;
    }

    public String getCurrentMap(Player player) {
        for (Game game : games.values()) {
            if (game.isIngame(player)) {
                return game.getName();
            }
        }
        return null;
    }

    public String getGameStatus(String map) {
        if (games.containsKey(map)) {
            GameStatus status = games.get(map).getStatus();
            if (status == GameStatus.INGAME) {
                return "In-Game";
            } else if (status == GameStatus.STARTING) {
                return "Starting";
            } else if (status == GameStatus.WAITING) {
                return "Waiting";
            } else if (status == GameStatus.DISABLED) {
                return "Disabled";
            }
        }
        return "ERROR";
    }

    public String getTimeLeft(String map) {
        return games.get(map).getTimeLeft();
    }

    public String getTimeLeftPretty(String map) {
        return games.get(map).getTimeLeftPretty();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveConfig();
    }

    public void broadcast(String map, String message) {
        games.get(map).broadcast(message);
    }

    public void checkQueue(String map) {
        games.get(map).checkQueue();
    }

    public void setKills(Player player, int kills) {
        games.get(getCurrentMap(player)).kills.put(player.getName(), kills);
    }

    public Integer getKills(Player player) {
        return games.get(getCurrentMap(player)).kills.get(player.getName());
    }

    public boolean getRedHasBeenTeleported(String map) {
        return games.get(map).redHasBeenTeleported;
    }

    public void setRedHasBeenTeleported(String map, boolean bool) {
        games.get(map).redHasBeenTeleported = bool;
    }

    public void setStatus(String map, GameStatus status) {
        games.get(map).setStatus(status);
    }

    public void getPlayerWithMostKills(String map) {

    }
}