package me.chaseoes.tf2;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Queue {

    Map map;
    Game game;
    private ArrayList<String> playersInQueue = new ArrayList<String>();

    public Queue(Map m) {
        map = m;
        game = GameUtilities.getUtilities().getGame(m);
    }

    public void addPlayer(Player player) {
        playersInQueue.add(player.getName());
    }

    public void removePlayer(Player player) {
        removePlayer(player.getName());
    }

    public void removePlayer(String player) {
        playersInQueue.remove(player);
    }

    public boolean contains(Player player) {
        return playersInQueue.contains(player.getName());
    }

    public int getPosition(Player player) {
        int position = 0;
        for (String p : playersInQueue) {
            position++;
            if (player.getName().equalsIgnoreCase(p)) {
                return position;
            }
        }
        return position;
    }

    public void check(Player playerWantingIn) {
        if (playerWantingIn != null && !contains(playerWantingIn)) {
            addPlayer(playerWantingIn);
        }
        int amountAllowedIn = map.getPlayerlimit() - game.getPlayersIngame().size();
        for (String player : playersInQueue) {
            if (amountAllowedIn != 0) {
                removePlayer(player);
                game.joinGame(GameUtilities.getUtilities().getGamePlayer(TF2.getInstance().getServer().getPlayer(player)), game.decideTeam());
                amountAllowedIn--;
            }
        }
        if (playerWantingIn != null) {
            playerWantingIn.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + getPosition(playerWantingIn) + " in line for this map.");
        }
    }

}
