package com.chaseoes.tf2.lobbywall;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.TF2;

public class LobbyWallUtilities {

    private TF2 plugin;
    static LobbyWallUtilities instance = new LobbyWallUtilities();

    private LobbyWallUtilities() {

    }

    public static LobbyWallUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void saveSignLocation(String map, Location l) {
        DataConfiguration.getData().getDataFile().set("lobbywall." + map + ".w", l.getWorld().getName());
        DataConfiguration.getData().getDataFile().set("lobbywall." + map + ".x", l.getBlockX());
        DataConfiguration.getData().getDataFile().set("lobbywall." + map + ".y", l.getBlockY());
        DataConfiguration.getData().getDataFile().set("lobbywall." + map + ".z", l.getBlockZ());
        LobbyWall.getWall().setDirty(map, true);
        DataConfiguration.getData().saveData();
    }

    public Location loadSignLocation(String map) {
        if (DataConfiguration.getData().getDataFile().isString("lobbywall." + map + ".w")) {
            return new Location(plugin.getServer().getWorld(DataConfiguration.getData().getDataFile().getString("lobbywall." + map + ".w")), Integer.valueOf(DataConfiguration.getData().getDataFile().getString("lobbywall." + map + ".x")), DataConfiguration.getData().getDataFile().getInt("lobbywall." + map + ".y"), DataConfiguration.getData().getDataFile().getInt("lobbywall." + map + ".z"));
        }
        return null;
    }

    public void setSignLines(Sign s, String l1, String l2, String l3, String l4) {
        s.setLine(0, l1);
        s.setLine(1, l2);
        s.setLine(2, l3);
        s.setLine(3, l4);
        s.update(true);
    }

    public BlockFace rotate90Deg(BlockFace face) {
        switch (face) {
            case NORTH:
                return BlockFace.EAST;
            case EAST:
                return BlockFace.SOUTH;
            case SOUTH:
                return BlockFace.WEST;
            case WEST:
                return BlockFace.NORTH;
            default:
                return null;
        }
    }

}
