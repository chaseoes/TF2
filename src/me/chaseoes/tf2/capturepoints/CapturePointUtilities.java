package me.chaseoes.tf2.capturepoints;

import java.util.HashMap;

import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.SerializableLocation;

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
        String location = SerializableLocation.getUtilities().locationToString(l);
        MapConfiguration.getMaps().getMap(map).set("capture-points." + id, location);
        MapConfiguration.getMaps().saveMap(map);
    }

    public Location getLocationFromID(String map, Integer id) {
        Location l = SerializableLocation.getUtilities().stringToLocation(MapConfiguration.getMaps().getMap(map).getString("capture-points." + id));
        return l;
    }

    public Integer getIDFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (String key : MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false)) {
                Integer id = Integer.parseInt(key);
                Location l = getLocationFromID(map, id);
                if (SerializableLocation.getUtilities().compareLocations(loc, l)) {
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
                if (SerializableLocation.getUtilities().compareLocations(loc, l)) {
                    return map;
                }
            }
        }
        return null;
    }

    public Boolean locationIsCapturePoint(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (Location capturepoint : plugin.getMap(map).getCapturePoints()) {
                if (SerializableLocation.getUtilities().compareLocations(capturepoint, loc)) {
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
