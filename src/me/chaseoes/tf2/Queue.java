package me.chaseoes.tf2;

import java.util.Iterator;
import java.util.LinkedList;

import com.sun.xml.internal.ws.server.StatefulInstanceResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Queue {

    /*Map map;
    Game game;
    private LinkedList<String> players = new LinkedList<String>();

    public Queue(Map m, Game game) {
        map = m;
        this.game = game;
    }

    public void addPlayer(Player player) {
        for (String m : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
            if (m.equals(map.getName())) {
                continue;
            }
            Game game = GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(m));
            Queue q = game.getQueue();
            if (q != null) {
                if (q.contains(player)) {
                    q.removePlayer(player.getName());
                }
            }
        }
        players.add(player.getName());
    }

    public void removePlayer(Player player) {
        removePlayer(player.getName());
    }

    public void removePlayer(String player) {
        if (contains(Bukkit.getPlayerExact(player))) {
            int index = players.indexOf(player);
            if (index == -1) {
                return;
            }
            String nextPlayer = null;
            if (index + 1 < players.size()) {
                nextPlayer = players.get(index + 1);
            }
            players.remove(player);
            check(false);
            if (nextPlayer != null) {
                int newIndex = -1;
                if (contains(nextPlayer)) {
                    newIndex = players.indexOf(nextPlayer);
                }
                if (newIndex == -1) {
                    newIndex = 0;
                }
                for (int i = newIndex; i < players.size(); i++) {
                    Bukkit.getPlayerExact(players.get(i)).sendMessage(ChatColor.YELLOW + "[TF2] You are #" + getPosition(player, false) + " in line for the map " + ChatColor.BOLD + map.getName() + ChatColor.RESET + ChatColor.YELLOW + ".");
                }
            }
        }
    }

    public boolean contains(Player player) {
        if (player != null) {
            return contains(player.getName());
        }
        return false;
    }

    public boolean contains(String player) {
        return players.contains(player);
    }

    public int getPosition(Player player, boolean zeroBased) {
        return getPosition(player.getName(), zeroBased);
    }

    public int getPosition(String player, boolean zeroBased) {
        return players.indexOf(player) + (zeroBased ? 0 : 1);
    }

    public void clear(boolean messagePlayers) {
        if (messagePlayers) {
            for (String player : players) {
                Bukkit.getPlayerExact(player).sendMessage(ChatColor.YELLOW + "[TF2] The queue for the map " + ChatColor.BOLD + map.getName() + ChatColor.RESET + ChatColor.YELLOW + " was cleared.");
            }
        }
        players.clear();
    }

    public void check(boolean messagePlayers) {
        int amtAllowed = map.getPlayerlimit() - game.getPlayersIngame().size();
        Iterator<String> it = players.iterator();
        while (it.hasNext()) {
            String player = it.next();
            if (player != null) {
                if (amtAllowed > 0) {
                    it.remove();
                    if (GameUtilities.getUtilities().getGamePlayer(Bukkit.getPlayerExact(player)) == null) {
                        continue;
                    }

                    game.joinGame(GameUtilities.getUtilities().getGamePlayer(Bukkit.getPlayerExact(player)), game.decideTeam());
                    amtAllowed--;
                } else {
                    if (messagePlayers) {
                        if (Bukkit.getPlayerExact(player) != null) {
                            Bukkit.getPlayerExact(player).sendMessage(ChatColor.YELLOW + "[TF2] You are #" + getPosition(player, false) + " in line for the map " + ChatColor.BOLD + map.getName() + ChatColor.RESET + ChatColor.YELLOW + ".");
                        }
                    }
                }
            }
        }
    }*/
}
