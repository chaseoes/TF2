package com.chaseoes.tf2.capturepoints;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitTask;

import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.SerializableLocation;

public class CapturePointUtilities {

    TF2 plugin;
    static CapturePointUtilities instance = new CapturePointUtilities();
    BukkitTask reminderTimer = null;

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
                if (SerializableLocation.compareLocations(loc, point.getLocation())) {
                    return point.getId();
                }
            }
        }
        return null;
    }

    public String getMapFromLocation(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (CapturePoint point : plugin.getMap(map).getCapturePoints()) {
                if (SerializableLocation.compareLocations(loc, point.getLocation())) {
                    return map;
                }
            }
        }
        return null;
    }

    public Boolean locationIsCapturePoint(Location loc) {
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            for (Location capturepoint : plugin.getMap(map).getCapturePointsLocations()) {
                if (SerializableLocation.compareLocations(capturepoint, loc)) {
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

    public CapturePoint getFirstUncaptured(Map map) {
        int id = 1;
        while (id <= map.getCapturePoints().size()) {
            CapturePoint cp = map.getCapturePoint(id);
            if (cp.getStatus() == CaptureStatus.UNCAPTURED) {
                return cp;
            }
            id++;
        }
        return null;
    }

}
