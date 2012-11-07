package me.chaseoes.tf2;

import java.util.List;

import me.chaseoes.tf2.lobbywall.WorldEditUtilities;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class MapUtilities {

    private TF2 plugin;
    static MapUtilities instance = new MapUtilities();

    private MapUtilities() {

    }

    public static MapUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void createMap(String id, Player p) throws EmptyClipboardException {
        Selection sel = WorldEditUtilities.getWorldEdit().getSelection(p);
        if (sel != null) {
            MapConfiguration.getMaps().reloadMap(id);
            Location b1 = new Location(p.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
            Location b2 = new Location(p.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
            MapConfiguration.getMaps().getMap(id).set("region.p1.w", b1.getWorld().getName());
            MapConfiguration.getMaps().getMap(id).set("region.p1.x", b1.getBlockX());
            MapConfiguration.getMaps().getMap(id).set("region.p1.y", b1.getBlockY());
            MapConfiguration.getMaps().getMap(id).set("region.p1.z", b1.getBlockZ());
            MapConfiguration.getMaps().getMap(id).set("region.p2.w", b2.getWorld().getName());
            MapConfiguration.getMaps().getMap(id).set("region.p2.x", b2.getBlockX());
            MapConfiguration.getMaps().getMap(id).set("region.p2.y", b2.getBlockY());
            MapConfiguration.getMaps().getMap(id).set("region.p2.z", b2.getBlockZ());
            MapConfiguration.getMaps().saveMap(id);
            List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("enabled-maps");
            enabled.add(id);
            DataConfiguration.getData().getDataFile().set("enabled-maps", enabled);
            DataConfiguration.getData().saveData();
        } else {
            throw new EmptyClipboardException();
        }
    }

    public void disableMap(String id) {
        List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
        if (!enabled.contains(id)) {
            enabled.add(id);
            DataConfiguration.getData().getDataFile().set("disabled-maps", enabled);
            DataConfiguration.getData().saveData();
        }
    }
    
    public void enableMap(String id) {
        List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
        if (enabled.contains(id)) {
            enabled.remove(id);
            DataConfiguration.getData().getDataFile().set("disabled-maps", enabled);
            DataConfiguration.getData().saveData();
        }
    }
    
    public List<String> getEnabledMaps() {
        List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("enabled-maps");
        List<String> disabled = DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
        for (String dis : disabled) {
            enabled.remove(dis);
        }
        return enabled;
    }
    
    public void setTeamSpawn(String map, String team, Location l) {
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.w", l.getWorld().getName());
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.x", l.getBlockX());
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.y", l.getBlockY());
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.z", l.getBlockZ());
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.pitch", l.getPitch());
        MapConfiguration.getMaps().getMap(map).set(team + ".spawn.yaw", l.getYaw());
        MapConfiguration.getMaps().saveMap(map);
    }
    
    public void setTeamLobby(String map, String team, Location l) {
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.w", l.getWorld().getName());
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.x", l.getBlockX());
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.y", l.getBlockY());
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.z", l.getBlockZ());
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.pitch", l.getPitch());
        MapConfiguration.getMaps().getMap(map).set(team + ".lobby.yaw", l.getYaw());
        MapConfiguration.getMaps().saveMap(map);
    }
    
    public Location loadTeamLobby(String map, String team) {
        return new Location(plugin.getServer().getWorld(MapConfiguration.getMaps().getMap(map).getString(team + ".lobby.w")), MapConfiguration.getMaps().getMap(map).getInt(team + ".lobby.x") + .5, MapConfiguration.getMaps().getMap(map).getInt(team + ".lobby.y"), MapConfiguration.getMaps().getMap(map).getInt(team + ".lobby.z") + .5, MapConfiguration.getMaps().getMap(map).getInt(team + ".lobby.yaw"), MapConfiguration.getMaps().getMap(map).getInt(team + ".lobby.pitch"));
    }
    
    public Location loadTeamSpawn(String map, String team) {
        return new Location(plugin.getServer().getWorld(MapConfiguration.getMaps().getMap(map).getString(team + ".spawn.w")), MapConfiguration.getMaps().getMap(map).getInt(team + ".spawn.x") + .5, MapConfiguration.getMaps().getMap(map).getInt(team + ".spawn.y"), MapConfiguration.getMaps().getMap(map).getInt(team + ".spawn.z") + .5, MapConfiguration.getMaps().getMap(map).getInt(team + ".spawn.yaw"), MapConfiguration.getMaps().getMap(map).getInt(team + ".spawn.pitch"));
    }
    
    public void setTimeLimit(String map, Integer time) {
        MapConfiguration.getMaps().getMap(map).set("timelimit", time);
        MapConfiguration.getMaps().saveMap(map);
    }
    
    public void setPlayerLimit(String map, Integer count) {
        MapConfiguration.getMaps().getMap(map).set("playerlimit", count);
        MapConfiguration.getMaps().saveMap(map);
    }
    
    public void setLobby(Location l) {
        DataConfiguration.getData().getDataFile().set("lobby.w", l.getWorld().getName());
        DataConfiguration.getData().getDataFile().set("lobby.x", l.getBlockX());
        DataConfiguration.getData().getDataFile().set("lobby.y", l.getBlockY());
        DataConfiguration.getData().getDataFile().set("lobby.z", l.getBlockZ());
        DataConfiguration.getData().getDataFile().set("lobby.pitch", l.getPitch());
        DataConfiguration.getData().getDataFile().set("lobby.yaw", l.getYaw());
        DataConfiguration.getData().saveData();
    }
    
    public Location loadLobby() {
        return new Location(plugin.getServer().getWorld(DataConfiguration.getData().getDataFile().getString("lobby.w")), DataConfiguration.getData().getDataFile().getInt("lobby.x") + .5, DataConfiguration.getData().getDataFile().getInt("lobby.y"), DataConfiguration.getData().getDataFile().getInt("lobby.z") + .5, DataConfiguration.getData().getDataFile().getInt("lobby.yaw"), DataConfiguration.getData().getDataFile().getInt("lobby.pitch"));
    }
    
    public Boolean mapIsEnabled(String map) {
        if (MapUtilities.getUtilities().getEnabledMaps().contains(map)) {
            return true;
        }
        return false;
    }

}
