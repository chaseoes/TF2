package me.chaseoes.tf2.lobbywall;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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

    public boolean isInMap(Entity entity, Map map) {
        return isInMap(entity.getLocation(), map);
    }

    public boolean isInMap(Location loc, Map map) {
        Selection sel = new CuboidSelection(map.getP1().getWorld(), map.getP1(), map.getP2());
        return sel.contains(loc);
    }
}
