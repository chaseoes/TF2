package me.chaseoes.tf2;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Queue {

    String map;
    public CopyOnWriteArrayList<String> inqueue;

    public Queue(String m) {
        map = m;
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
        String str = inqueue.remove(i);
        for (int it = i; it < inqueue.size(); it++) {
            Player p = Bukkit.getPlayerExact(inqueue.get(it));
            p.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + it + " in line for the map " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ".");
        }
        check(false);
        return str;
    }

    public void remove(String player) {
        inqueue.remove(player);
        check(false);
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

    public void check(boolean b) {
        GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map)).checkQueue(b);
    }
}
