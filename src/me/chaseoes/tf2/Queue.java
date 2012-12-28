package me.chaseoes.tf2;

import java.util.concurrent.CopyOnWriteArrayList;

import me.chaseoes.tf2.classes.GameUtilities;

import org.bukkit.entity.Player;

public class Queue {

    String map;
    public CopyOnWriteArrayList<String> inqueue;

    public Queue(String m) {
        m = map;
        inqueue = new CopyOnWriteArrayList<String>();
    }

    public CopyOnWriteArrayList<String> getQueue() {
        return inqueue;
    }

    public void add(Player player) {
        for (String m : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
            Queue q = GameUtilities.getUtilities().plugin.getQueue(m);
            if (q != null) {
                if (q.contains(player)) {
                    q.remove(q.getPosition(player.getName()));
                }
            }
        }
        inqueue.add(player.getName());
    }

    public String remove(int i) {
        return inqueue.remove(i);
    }
    
    public void remove(String player) {
        inqueue.remove(player);
    }

    public Integer getPosition(String player) {
        int position = 0;
        for (String pl : inqueue) {
            if (pl.equalsIgnoreCase(player)) {
                return position;
            }
            position++;
        }
        return null;
    }

    public Boolean contains(Player player) {
        return inqueue.contains(player.getName());
    }

}
