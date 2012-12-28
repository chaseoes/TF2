package me.chaseoes.tf2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;

import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;

public class Map {
    
    String map;
    public HashMap<Integer, CapturePoint> points = new HashMap<Integer, CapturePoint>();
    
    public Map(String m) {
        map = m;
        for (Location l : getCapturePoints()) {
            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(l);
            points.put(id, new CapturePoint(map, id));
        }
    }
    
    public CapturePoint getCapturePoint(Integer id) {
        return points.get(id);
    }
    
    public List<Location> getCapturePoints() {
        List<Location> locs = new ArrayList<Location>();
        for (String id : MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false)) {
            locs.add(CapturePointUtilities.getUtilities().getLocationFromID(map, Integer.parseInt(id)));
        }
        return locs;
    }
    
    public String getName() {
        return map;
    }

}
