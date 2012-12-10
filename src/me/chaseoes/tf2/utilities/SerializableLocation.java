package me.chaseoes.tf2.utilities;

import org.bukkit.Location;
import org.bukkit.World;

import me.chaseoes.tf2.TF2;

public class SerializableLocation {
    
    private TF2 plugin;
    static SerializableLocation instance = new SerializableLocation();

    private SerializableLocation() {

    }

    public static SerializableLocation getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }
    
    public String locationToString(Location l) {
        String w = l.getWorld().getName();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        return w + "." + x + "." + y + "." + z;
    }
    
    public Location stringToLocation(String s) {
        String[] str = s.split("\\.");
        World w = plugin.getServer().getWorld(str[0]);
        int x = Integer.parseInt(str[1]);
        int y = Integer.parseInt(str[2]);
        int z = Integer.parseInt(str[3]);
        return new Location(w, x, y, z);
    }
    
    public boolean compareLocations(Location one, Location two) {
        String w = one.getWorld().getName();
        int x = one.getBlockX();
        int y = one.getBlockZ();
        int z = one.getBlockZ();

        String checkw = two.getWorld().getName();
        int checkx = two.getBlockX();
        int checky = two.getBlockZ();
        int checkz = two.getBlockZ();

        if (w.equalsIgnoreCase(checkw) && x == checkx && y == checky && z == checkz) {
            return true;
        }
        return false;
        
        // if (locationToString(one).equalsIgnoreCase(locationToString(two)) {
        // return true;
        // }
        // return false;
    }

}
