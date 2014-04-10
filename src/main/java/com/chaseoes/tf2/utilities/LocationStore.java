package com.chaseoes.tf2.utilities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationStore {

    public static HashMap<String, Location> locations = new HashMap<String, Location>();
    public static HashMap<String, Integer> times = new HashMap<String, Integer>();

    public static Location getLastLocation(Player player) {
        if (player != null) {
            return locations.get(player.getName());
        }
        return null;
    }

    public static void setLastLocation(Player player) {
        if (player != null) {
            locations.put(player.getName(), new Location(player.getWorld(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ()));
        }
        return;
    }

    public static void unsetLastLocation(Player player) {
        if (player != null) {
            locations.remove(player.getName());
        }
    }

    public static Integer getAFKTime(Player player) {
        if (player != null) {
            try {
                return times.get(player.getName());
            } catch (NullPointerException e) {

            }
        }
        return null;
    }

    public static void setAFKTime(Player player, Integer time) {
        if (player != null) {
            if (time != null) {
                times.put(player.getName(), time);
            } else {
                times.remove(player.getName());
            }
        }
    }

}
