package com.chaseoes.tf2;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class GameQueue {

    Game game;
    ArrayList<String> queue = new ArrayList<String>();

    public GameQueue(Game g) {
        game = g;
    }

    public int getPosition(Player player) {
        if (contains(player)) {
            return queue.indexOf(player.getName());
        }
        return -1;
    }

    public boolean contains(Player player) {
        return queue.contains(player.getName());
    }

    public boolean gameHasRoom() {
        int i = TF2.getInstance().getMap(game.getMapName()).getPlayerlimit() - (game.playersInGame.size() + 1);
        return i >= 0;
    }

    public boolean add(Player player) {
        if (contains(player)) {
            return false;
        }

        for (Game g : GameUtilities.getUtilities().games.values()) {
            Map gm = TF2.getInstance().getMap(g.getMapName());
            gm.getQueue().remove(player.getPlayer());
        }

        queue.add(player.getName());
        return true;
    }

    public boolean remove(Player player) {
        queue.remove(player.getName());
        return true;
    }

    public void check() {
        if (TF2.getInstance().enabled) {
            TF2.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(TF2.getInstance(), new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> playersInQueue = new ArrayList<String>(queue);
                    for (String p : playersInQueue) {
                        Player player = TF2.getInstance().getServer().getPlayer(p);
                        if (player != null) {
                            if (gameHasRoom()) {
                                game.joinGame(GameUtilities.getUtilities().getGamePlayer(player), game.decideTeam());
                            }
                        } else {
                            queue.remove(p);
                        }
                    }
                }
            }, 5L);
        }
    }

}
