package me.chaseoes.tf2.capturepoints;

import java.util.HashMap;

import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.Location;

public class CapturePointUtilities {

    TF2 plugin;
    static CapturePointUtilities instance = new CapturePointUtilities();
    public HashMap<String, Integer> capturepoints = new HashMap<String, Integer>();
    public HashMap<String, Integer> capturecounter = new HashMap<String, Integer>();
    public HashMap<Integer, String> status = new HashMap<Integer, String>();

    private CapturePointUtilities() {

    }

    public static CapturePointUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void defineCapturePoint(String map, Integer id, Location l) {
        MapConfiguration.getMaps().getMap(map).set("capture-points." + id + ".w", l.getWorld().getName());
        MapConfiguration.getMaps().getMap(map).set("capture-points." + id + ".x", l.getBlockX());
        MapConfiguration.getMaps().getMap(map).set("capture-points." + id + ".y", l.getBlockY());
        MapConfiguration.getMaps().getMap(map).set("capture-points." + id + ".z", l.getBlockZ());
        MapConfiguration.getMaps().saveMap(map);
    }
    
    public Location getLocationFromID(String map, Integer id) {
        return new Location(plugin.getServer().getWorld(MapConfiguration.getMaps().getMap(map).getString("capture-points." + id + ".w")), MapConfiguration.getMaps().getMap(map).getInt("capture-points." + id + ".x"), MapConfiguration.getMaps().getMap(map).getInt("capture-points." + id + ".y"), MapConfiguration.getMaps().getMap(map).getInt("capture-points." + id + ".z"));
    }
    
    public Integer getIDFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (String key : MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false)) {
                Integer id = Integer.parseInt(key);
                Location l = getLocationFromID(map, id);
                if (l.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && l.getBlockX() == loc.getBlockX() && l.getBlockY() == loc.getBlockY() && l.getBlockZ() == loc.getBlockZ()) {
                    return id;
                }
            }
        }
        return null;
    }
    
    public String getMapFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (String key : MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false)) {
                Integer id = Integer.parseInt(key);
                Location l = getLocationFromID(map, id);
                if (l.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && l.getBlockX() == loc.getBlockX() && l.getBlockY() == loc.getBlockY() && l.getBlockZ() == loc.getBlockZ()) {
                    return map;
                }
            }
        }
        return null;
    }
    
    public Boolean locationIsCapturePoint(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (Location capturepoint : plugin.getMap(map).getCapturePoints()) {
                if (capturepoint.getWorld().getName().equalsIgnoreCase(loc.getWorld().getName()) && capturepoint.getBlockX() == loc.getBlockX() && capturepoint.getBlockY() == loc.getBlockY() && capturepoint.getBlockZ() == loc.getBlockZ()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Boolean allCaptured(String map) {
        Integer possiblepoints = 0;
        Integer captured = 0;
        for (String id : MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false)) {
            possiblepoints++;
            if (plugin.getMap(map).getCapturePoint(Integer.parseInt(id)).getStatus().string().equalsIgnoreCase("captured")) {
                captured++;
            }
        }
        
        if (possiblepoints == captured) {
            return true;
        }
        
        return false;
    }
    
    public Boolean capturePointBeforeHasBeenCaptured(Map map, Integer i) {
        if (i == 1) {
            return true;
        }
        Integer before = i - 1;
        String capped = map.getCapturePoint(before).getStatus().string();
        if (capped.equalsIgnoreCase("captured")) {
            return true;
        }
        return false;
    }
    
    public void uncaptureAll(Map map) {
        for (Location l : map.getCapturePoints()) {
            Integer i = getIDFromLocation(l);
            CapturePoint cp = map.getCapturePoint(i);
            cp.setStatus(CaptureStatus.UNCAPTURED);
        }
    }

}
