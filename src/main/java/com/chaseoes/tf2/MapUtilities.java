package com.chaseoes.tf2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.utilities.WorldEditUtilities;
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
            plugin.addMap(id, GameStatus.WAITING);
            Map map = plugin.getMap(id);
            Location b1 = new Location(p.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
            Location b2 = new Location(p.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
            map.setP1(b1);
            map.setP2(b2);
            List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("enabled-maps");
            enabled.add(id);
            DataConfiguration.getData().getDataFile().set("enabled-maps", enabled);
            DataConfiguration.getData().saveData();
        } else {
            throw new EmptyClipboardException();
        }
    }

    public void destroyMap(Map map) {
        List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("enabled-maps");
        enabled.remove(map.getName());
        DataConfiguration.getData().getDataFile().set("enabled-maps", enabled);
        List<String> disabled = DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
        disabled.remove(map.getName());
        DataConfiguration.getData().getDataFile().set("disabled-maps", disabled);
        DataConfiguration.getData().getDataFile().set("lobbywall." + map.getName() + ".w", null);
        DataConfiguration.getData().getDataFile().set("lobbywall." + map.getName() + ".x", null);
        DataConfiguration.getData().getDataFile().set("lobbywall." + map.getName() + ".y", null);
        DataConfiguration.getData().getDataFile().set("lobbywall." + map.getName() + ".z", null);
        DataConfiguration.getData().getDataFile().set("lobbywall." + map.getName(), null);
        DataConfiguration.getData().saveData();
        DataConfiguration.getData().reloadData();
        map.destroy();
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

    public List<Map> getMaps() {
        List<Map> maps = new ArrayList<Map>();
        for (String m : getEnabledMaps()) {
            maps.add(plugin.getMap(m));
        }
        return maps;
    }

    public List<String> getEnabledMaps() {
        List<String> enabled = DataConfiguration.getData().getDataFile().getStringList("enabled-maps");
        List<String> disabled = DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
        for (String dis : disabled) {
            enabled.remove(dis);
        }
        return enabled;
    }

    public List<String> getDisabledMaps() {
        return DataConfiguration.getData().getDataFile().getStringList("disabled-maps");
    }

    public void setTeamSpawn(String map, Team team, Location l) {
        switch (team) {
            case RED:
                plugin.getMap(map).setRedSpawn(l);
                break;
            case BLUE:
                plugin.getMap(map).setBlueSpawn(l);
                break;
        }
    }

    public void setTeamLobby(String map, Team team, Location l) {
        switch (team) {
            case RED:
                plugin.getMap(map).setRedLobby(l);
                break;
            case BLUE:
                plugin.getMap(map).setBlueLobby(l);
                break;
        }
    }

    public Location loadTeamLobby(String map, Team team) {
        switch (team) {
            case RED:
                return plugin.getMap(map).getRedLobby();
            case BLUE:
                return plugin.getMap(map).getBlueLobby();
        }
        return null;
    }

    public Location loadTeamSpawn(String map, Team team) {
        switch (team) {
            case RED:
                return plugin.getMap(map).getRedSpawn();
            case BLUE:
                return plugin.getMap(map).getBlueSpawn();
        }
        return null;
    }

    public void setTimeLimit(String map, Integer time) {
        plugin.getMap(map).setTimelimit(time);
    }

    public void setPlayerLimit(String map, Integer count) {
        plugin.getMap(map).setPlayerlimit(count);
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

    public boolean mapIsEnabled(String map) {
        return getEnabledMaps().contains(map);
    }

    public Map getRandomMap() {
        int n = new Random().nextInt(getMaps().size());
        return getMaps().get(n);

    }
}
