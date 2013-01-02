package me.chaseoes.tf2;

import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CaptureStatus;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.utilities.Container;
import me.chaseoes.tf2.utilities.SerializableInventory;
import me.chaseoes.tf2.utilities.SerializableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Map {

    private TF2 plugin;
    private String name;
    private Location p1;
    private Location p2;
    private Location blueLobby;
    private Location redLobby;
    private Location blueSpawn;
    private Location redSpawn;
    private HashMap<Integer, CapturePoint> points = new HashMap<Integer, CapturePoint>();
    private Integer redTeamTeleportTime;
    private Integer timelimit;
    private Integer playerlimit;
    private HashSet<Container> containers = new HashSet<Container>();

    private File customConfigFile;
    private FileConfiguration customConfig;

    public Map(TF2 plugin, String map) {
        this.plugin = plugin;
        name = map;
        load();
    }

    public void load() {
        customConfigFile = new File(plugin.getDataFolder(), name + ".yml");
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
        points.clear();
        containers.clear();
        if (customConfig.isString("region.p1.w")) {
            p1 = new Location(Bukkit.getWorld(customConfig.getString("region.p1.w")), customConfig.getInt("region.p1.x"), customConfig.getInt("region.p1.y"), customConfig.getInt("region.p1.z"));
        } else {
            p1 = null;
        }
        if (customConfig.isString("region.p2.w")) {
            p2 = new Location(Bukkit.getWorld(customConfig.getString("region.p2.w")), customConfig.getInt("region.p2.x"), customConfig.getInt("region.p2.y"), customConfig.getInt("region.p2.z"));
        } else {
            p2 = null;
        }
        if (customConfig.isString("blue.lobby.w")) {
            blueLobby = new Location(Bukkit.getWorld(customConfig.getString("blue.lobby.w")), customConfig.getInt("blue.lobby.x") + 0.5f, customConfig.getInt("blue.lobby.y"), customConfig.getInt("blue.lobby.z") + 0.5f, (float) customConfig.getDouble("blue.lobby.yaw"), (float) customConfig.getDouble("blue.lobby.pitch"));
        } else {
            blueLobby = null;
        }
        if (customConfig.isString("red.lobby.w")) {
            redLobby = new Location(Bukkit.getWorld(customConfig.getString("red.lobby.w")), customConfig.getInt("red.lobby.x") + 0.5f, customConfig.getInt("red.lobby.y"), customConfig.getInt("red.lobby.z") + 0.5f, (float) customConfig.getDouble("red.lobby.yaw"), (float) customConfig.getDouble("red.lobby.pitch"));
        } else {
            redLobby = null;
        }
        if (customConfig.isString("blue.spawn.w")) {
            blueSpawn = new Location(Bukkit.getWorld(customConfig.getString("blue.spawn.w")), customConfig.getInt("blue.spawn.x") + 0.5f, customConfig.getInt("blue.spawn.y"), customConfig.getInt("blue.spawn.z") + 0.5f, (float) customConfig.getDouble("blue.spawn.yaw"), (float) customConfig.getDouble("blue.spawn.pitch"));
        } else {
            blueSpawn = null;
        }
        if (customConfig.isString("red.spawn.w")) {
            redSpawn = new Location(Bukkit.getWorld(customConfig.getString("red.spawn.w")), customConfig.getInt("red.spawn.x") + 0.5f, customConfig.getInt("red.spawn.y"), customConfig.getInt("red.spawn.z") + 0.5f, (float) customConfig.getDouble("red.spawn.yaw"), (float) customConfig.getDouble("red.spawn.pitch"));
        } else {
            redSpawn = null;
        }
        if (customConfig.isConfigurationSection("capture-points")) {
            for (String id : customConfig.getConfigurationSection("capture-points").getKeys(false)) {
                Integer iid = Integer.parseInt(id);
                Location loc = SerializableLocation.getUtilities().stringToLocation(customConfig.getString("capture-points." + iid));
                points.put(iid, new CapturePoint(name, iid, loc));
            }
        }
        if (customConfig.isInt("teleport-red-team")) {
            redTeamTeleportTime = customConfig.getInt("teleport-red-team");
        } else {
            redTeamTeleportTime = null;
        }
        if (customConfig.isInt("timelimit")) {
            timelimit = customConfig.getInt("timelimit");
        } else {
            timelimit = null;
        }
        if (customConfig.isInt("playerlimit")) {
            playerlimit = customConfig.getInt("playerlimit");
        } else {
            playerlimit = null;
        }
        if (customConfig.isList("containers")) {
            for (String str : customConfig.getStringList("containers")) {
                String[] split = str.split(",");
                Location loc = SerializableLocation.getUtilities().stringToLocation(split[0]);
                Inventory inv = SerializableInventory.StringToInventory(split[1]);
                containers.add(new Container(loc, inv));
            }
        }
    }

    public void destroy(){
        customConfigFile.delete();
    }

    public void saveCapturePoints() {
        customConfig.set("capture-points", null);
        ConfigurationSection section = customConfig.createSection("capture-points");
        for (java.util.Map.Entry<Integer, CapturePoint> keyVal : points.entrySet()) {
            section.set(String.valueOf(keyVal.getKey()), SerializableLocation.getUtilities().locationToString(keyVal.getValue().getLocation()));
        }
        saveConfig();
    }

    private void saveConfig() {
        try {
            customConfig.save(customConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CapturePoint getCapturePoint(Integer id) {
        return points.get(id);
    }

    public void setCapturePoint(Integer id, CapturePoint point) {
        if (point != null) {
            points.put(id, point);
            customConfig.set("capture-points." + id, SerializableLocation.getUtilities().locationToString(point.getLocation()));
        } else {
            points.remove(id);
            customConfig.set("capture-points." + id, null);
        }
        LobbyWall.getWall().setDirty(name, true);
        saveConfig();
    }

    public List<Location> getCapturePointsLocations() {
        List<Location> locs = new ArrayList<Location>();
        for (CapturePoint point : points.values()) {
            locs.add(point.getLocation());
        }
        return locs;
    }

    public Set<CapturePoint> getCapturePoints() {
        return new HashSet<CapturePoint>(points.values());
    }

    public String getName() {
        return name;
    }

    public Location getP1() {
        return p1;
    }

    public void setP1(Location p1) {
        this.p1 = p1;
        customConfig.set("region.p1.w", p1.getWorld().getName());
        customConfig.set("region.p1.x", p1.getBlockX());
        customConfig.set("region.p1.y", p1.getBlockY());
        customConfig.set("region.p1.z", p1.getBlockZ());
        saveConfig();
    }

    public Location getP2() {
        return p2;
    }

    public void setP2(Location p2) {
        this.p2 = p2;
        customConfig.set("region.p2.w", p2.getWorld().getName());
        customConfig.set("region.p2.x", p2.getBlockX());
        customConfig.set("region.p2.y", p2.getBlockY());
        customConfig.set("region.p2.z", p2.getBlockZ());
        saveConfig();
    }

    public Location getRedLobby() {
        return redLobby;
    }

    public void setRedLobby(Location redLobby) {
        this.redLobby = redLobby;
        customConfig.set("red.lobby.w", redLobby.getWorld().getName());
        customConfig.set("red.lobby.x", redLobby.getBlockX());
        customConfig.set("red.lobby.y", redLobby.getBlockY());
        customConfig.set("red.lobby.z", redLobby.getBlockZ());
        customConfig.set("red.lobby.pitch", redLobby.getPitch());
        customConfig.set("red.lobby.yaw", redLobby.getYaw());
        saveConfig();
    }

    public Location getBlueLobby() {
        return blueLobby;
    }

    public void setBlueLobby(Location blueLobby) {
        this.blueLobby = blueLobby;
        customConfig.set("blue.lobby.w", blueLobby.getWorld().getName());
        customConfig.set("blue.lobby.x", blueLobby.getBlockX());
        customConfig.set("blue.lobby.y", blueLobby.getBlockY());
        customConfig.set("blue.lobby.z", blueLobby.getBlockZ());
        customConfig.set("blue.lobby.pitch", blueLobby.getPitch());
        customConfig.set("blue.lobby.yaw", blueLobby.getYaw());
        saveConfig();
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public void setBlueSpawn(Location blueSpawn) {
        this.blueSpawn = blueSpawn;
        customConfig.set("blue.spawn.w", blueSpawn.getWorld().getName());
        customConfig.set("blue.spawn.x", blueSpawn.getBlockX());
        customConfig.set("blue.spawn.y", blueSpawn.getBlockY());
        customConfig.set("blue.spawn.z", blueSpawn.getBlockZ());
        customConfig.set("blue.spawn.pitch", blueSpawn.getPitch());
        customConfig.set("blue.spawn.yaw", blueSpawn.getYaw());
        saveConfig();
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public void setRedSpawn(Location redSpawn) {
        this.redSpawn = redSpawn;
        customConfig.set("red.spawn.w", redSpawn.getWorld().getName());
        customConfig.set("red.spawn.x", redSpawn.getBlockX());
        customConfig.set("red.spawn.y", redSpawn.getBlockY());
        customConfig.set("red.spawn.z", redSpawn.getBlockZ());
        customConfig.set("red.spawn.pitch", redSpawn.getPitch());
        customConfig.set("red.spawn.yaw", redSpawn.getYaw());
        saveConfig();
    }

    public Integer getRedTeamTeleportTime() {
        return redTeamTeleportTime;
    }

    public void setRedTeamTeleportTime(Integer redTeamTeleportTime) {
        this.redTeamTeleportTime = redTeamTeleportTime;
        customConfig.set("teleport-red-team", redTeamTeleportTime);
        saveConfig();
    }

    public Integer getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(Integer timelimit) {
        this.timelimit = timelimit;
        customConfig.set("timelimit", timelimit);
        saveConfig();
    }

    public Integer getPlayerlimit() {
        return playerlimit;
    }

    public void setPlayerlimit(Integer playerlimit) {
        this.playerlimit = playerlimit;
        customConfig.set("playerlimit", playerlimit);
        saveConfig();
    }

    public Boolean allCaptured() {
        Integer possiblepoints = 0;
        Integer captured = 0;
        for (CapturePoint point : points.values()) {
            possiblepoints++;
            if (point.getStatus() == CaptureStatus.CAPTURED)
                captured++;
        }

        return possiblepoints.equals(captured);

    }

    public Boolean capturePointBeforeHasBeenCaptured(Integer i) {
        if (i == 1) {
            return true;
        }
        Integer before = i - 1;
        String capped = getCapturePoint(before).getStatus().string();
        return capped.equalsIgnoreCase("captured");
    }

    public void uncaptureAll() {
        for (CapturePoint point : points.values()) {
            point.setStatus(CaptureStatus.UNCAPTURED);
        }
    }

    public Set<Container> getContainers() {
        return containers;
    }

    public void addContainer(Location loc, Inventory inv) {
        containers.add(new Container(SerializableLocation.getUtilities().stringToLocation(SerializableLocation.getUtilities().locationToString(loc)), SerializableInventory.StringToInventory(SerializableInventory.InventoryToString(inv))));
        saveContainers();
    }

    public void removeContainer(Location loc) {
        loop:
        for (Container container : containers) {
            if (SerializableLocation.getUtilities().compareLocations(loc, container.getLocation())) {
                containers.remove(container);
                break loop;
            }
        }
        saveContainers();
    }

    public boolean isContainerRegistered(Location loc) {
        for (Container container : containers) {
            if (SerializableLocation.getUtilities().compareLocations(loc, container.getLocation()))
                return true;
        }
        return false;
    }

    private void saveContainers() {
        List<String> confStringList = new ArrayList<String>();
        for (Container container : containers) {
            String confString = SerializableLocation.getUtilities().locationToString(container.getLocation()) + "," + SerializableInventory.InventoryToString(container.getInventory());
            confStringList.add(confString);
        }
        customConfig.set("containers", confStringList);
        saveConfig();
    }
}
