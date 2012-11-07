package me.chaseoes.tf2.lobbywall;

import me.chaseoes.tf2.TF2;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class WorldEditUtilities {

    @SuppressWarnings("unused")
    private TF2 plugin;
    private static WorldEditPlugin worldEditPlugin;
    static WorldEditUtilities instance = new WorldEditUtilities();

    private WorldEditUtilities() {

    }

    public static WorldEditUtilities getWEUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void setupWorldEdit(PluginManager pm) {
        Plugin p = pm.getPlugin("WorldEdit");
        if (p != null && p instanceof WorldEditPlugin) {
            worldEditPlugin = (WorldEditPlugin) p;
        }
    }

    public static WorldEditPlugin getWorldEdit() {
        return worldEditPlugin;
    }

}
