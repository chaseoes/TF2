package me.chaseoes.tf2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

public class GameUtilities {

    public TF2 plugin;
    static GameUtilities instance = new GameUtilities();
    public HashMap<String, Game> games = new HashMap<String, Game>();
    public HashMap<String, GamePlayer> players = new HashMap<String, GamePlayer>();

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
    
    public void leaveCurrentGame(Player player) {
        getCurrentGame(player).leaveGame(player, false);
    }
    
    public Team teamFromString(String s) {
        Team t = Team.BLUE;
        if (s.equalsIgnoreCase("red")) {
            t = Team.RED;
        }
        return t;
    }
    
    public int getAmountOnTeam(String map, String team) {
        return getAmountOnTeam(map, teamFromString(team));
    }

    public void joinGame(Player player, String map, String team) {
        joinGame(player, map, teamFromString(team));
    }
    
    public int getKills(Player player) {
        return getCurrentGame(player).getPlayer(player).getKills();
    }
    
    public void setKills(Player player, int i) {
       getCurrentGame(player).getPlayer(player).setKills(i);
    }

    public void joinGame(Player player, String map, Team team) {
        GamePlayer gp = getGamePlayer(player);
        games.get(map).joinGame(gp, team);
    }

    public boolean isIngame(Player player) {
        return getCurrentGame(player) != null;
    }

    public GamePlayer getGamePlayer(Player player) {
        for (Game g : games.values()) {
            for (GamePlayer gp : g.playersInGame) {
                if (gp.getName().equalsIgnoreCase(player.getName())) {
                    return gp;
                }
            }
        }
        return null;
    }

    public void playerJoinServer(Player player) {
        GamePlayer gp = new GamePlayer(player);
        players.put(player.getName(), gp);
    }

    public void playerLeaveServer(Player player ){
        players.remove(player.getName());
    }

    public Game getCurrentGame(Player player) {
        GamePlayer gp = getGamePlayer(player);
        return gp.getGame();
    }

    public void startMatch(final String map) {
        games.get(map).startMatch();
    }

    public void stopMatch(String map) {
        games.get(map).stopMatch();
    }

    public void winGame(final String map, String team) {
        games.get(map).winGame(team);
    }

    public Integer getAmountOnTeam(String map, Team team) {
        return games.get(map).getAmountOnTeam(team);
    }

    public String getTeamColor(Player player) {
        Game g = getCurrentGame(player);
        return g.getPlayer(player).getTeamColor(player);
    }

    public Integer getTimeLeftSeconds(String map) {
        return games.get(map).getTimeLeftSeconds();
    }

    public Team decideTeam(Map m) {
        String map = m.getName();
        return games.get(map).decideTeam();
    }

    public List<String> getIngameList(String map) {
        if (games.containsKey(map)) {
            return games.get(map).getIngameList();
        }
        List<String> list = new ArrayList<String>();
        return list;
    }

    public String decideTeam(String map) {
        return games.get(map).decideTeam().name();
    }

    public String getCurrentMap(Player player) {
        return getCurrentGame(player).getName();
    }

    public String getTeam(Player player) {
        Game g = getCurrentGame(player);
        return g.getPlayer(player).getTeam().name();
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