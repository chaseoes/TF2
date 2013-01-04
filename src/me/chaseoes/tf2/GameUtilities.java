package me.chaseoes.tf2;

import java.util.HashMap;

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

    public Game getGame(Map map) {
        return games.get(map.getName());
    }

    public GamePlayer getGamePlayer(Player player) {
        if (player == null) {
            return null;
        }
        for (Game g : games.values()) {
            for (GamePlayer gp : g.playersInGame.values()) {
                if (gp.getName().equalsIgnoreCase(player.getName())) {
                    return gp;
                }
            }
        }

        if (!players.containsKey(player.getName())) {
            players.put(player.getName(), new GamePlayer(player));
        }

        return players.get(player.getName());
    }

    public void playerJoinServer(Player player) {
        players.put(player.getName(), new GamePlayer(player));
    }

    public void playerLeaveServer(Player player) {
        players.remove(player.getName());
    }

    public Game removeGame(Map m) {
        return games.remove(m.getName());
    }
}