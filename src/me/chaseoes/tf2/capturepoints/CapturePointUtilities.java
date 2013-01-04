package me.chaseoes.tf2.capturepoints;

import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.SerializableLocation;

import org.bukkit.Location;

public class CapturePointUtilities {

    TF2 plugin;
    static CapturePointUtilities instance = new CapturePointUtilities();

    private CapturePointUtilities() {

    }

    public static CapturePointUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void defineCapturePoint(String map, Integer id, Location l) {
        plugin.getMap(map).setCapturePoint(id, new CapturePoint(map, id, l));
    }

    public Location getLocationFromID(String map, Integer id) {
        return plugin.getMap(map).getCapturePoint(id).getLocation();
    }

    public Integer getIDFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (CapturePoint point : plugin.getMap(map).getCapturePoints()) {
                if (SerializableLocation.getUtilities().compareLocations(loc, point.getLocation())) {
                    return point.getId();
                }
            }
        }
        return null;
    }

    public String getMapFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (CapturePoint point : plugin.getMap(map).getCapturePoints()) {
                if (SerializableLocation.getUtilities().compareLocations(loc, point.getLocation())) {
                    return map;
                }
            }
        }
        return null;
    }

    public Boolean locationIsCapturePoint(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (Location capturepoint : plugin.getMap(map).getCapturePointsLocations()) {
                if (SerializableLocation.getUtilities().compareLocations(capturepoint, loc)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean capturePointBeforeHasBeenCaptured(Map map, Integer i) {
        return map.capturePointBeforeHasBeenCaptured(i);
    }

    public void uncaptureAll(Map map) {
        map.uncaptureAll();
    }

}
