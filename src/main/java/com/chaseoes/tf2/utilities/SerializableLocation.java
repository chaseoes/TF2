package com.chaseoes.tf2.utilities;

import org.bukkit.Location;
import org.bukkit.World;

import com.chaseoes.tf2.TF2;

public class SerializableLocation {

    public static String locationToString(Location l) {
        String w = l.getWorld().getName();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        float yaw = l.getYaw();
        float pitch = l.getPitch();
        return w + "." + x + "." + y + "." + z + "." + yaw + "." + pitch;
    }

    public static Location stringToLocation(String s) {
        String[] str = s.split("\\.");
        World w = TF2.getInstance().getServer().getWorld(str[0]);
        int x = Integer.parseInt(str[1]);
        int y = Integer.parseInt(str[2]);
        int z = Integer.parseInt(str[3]);

        if (str.length > 4) {
            float yaw = Float.parseFloat(str[4]);
            float pitch = Float.parseFloat(str[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }

        return new Location(w, x, y, z);
    }

    public static boolean compareLocations(Location one, Location two) {
        String w = one.getWorld().getName();
        int x = one.getBlockX();
        int y = one.getBlockY();
        int z = one.getBlockZ();

        String checkw = two.getWorld().getName();
        int checkx = two.getBlockX();
        int checky = two.getBlockY();
        int checkz = two.getBlockZ();

        return w.equalsIgnoreCase(checkw) && x == checkx && y == checky && z == checkz;
    }

}
