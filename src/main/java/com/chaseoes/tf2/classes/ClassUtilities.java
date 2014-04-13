package com.chaseoes.tf2.classes;

import java.util.HashMap;


import org.bukkit.Location;

import com.chaseoes.tf2.TF2;

public class ClassUtilities {

    private TF2 plugin;
    static ClassUtilities instance = new ClassUtilities();
    public HashMap<String, Integer> effects = new HashMap<String, Integer>();
    public HashMap<String, String> classes = new HashMap<String, String>();

    private ClassUtilities() {

    }

    public static ClassUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public Location loadClassButtonLocation(String l) {
        String[] split = l.split("\\.");
        return new Location(plugin.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }

    public String loadClassFromLocation(String l) {
        String[] split = l.split("\\.");
        return split[5];
    }

    public String loadClassButtonTypeFromLocation(String l) {
        String[] split = l.split("\\.");
        return split[4];
    }

}
