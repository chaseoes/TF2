package me.chaseoes.tf2.lobbywall;

import java.util.logging.Level;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class LobbyWall {

    private TF2 plugin;
    static LobbyWall instance = new LobbyWall();

    private LobbyWall() {

    }

    public static LobbyWall getWall() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void updateWall() {
        try {
            for (final String map : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
                if (DataConfiguration.getData().getDataFile().getString("lobbywall." + map + ".w") != null) {
                    Location start = LobbyWallUtilities.getUtilities().loadSignLocation(map);
                    final Block startblock = start.getBlock();
                    final Sign startsign = (Sign) start.getBlock().getState();
                    final org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) start.getBlock().getState().getData();
                    final Map m = plugin.getMap(map);
                    if (m != null) {
                        BlockFace direction = rotate90Deg(matSign.getAttachedFace());
                        Sign status = null;
                        Sign teamcount = null;
                        Sign timeleft = null;

                        if (!startsign.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                            Block block = startsign.getBlock().getRelative(direction);
                            block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                            block.getState().setRawData(startblock.getState().getRawData());
                            block.getState().update();
                        }
                        status = (org.bukkit.block.Sign) startsign.getBlock().getRelative(direction).getState();
                        if (!status.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                            Block block = status.getBlock().getRelative(direction);
                            block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                            block.getState().setRawData(startblock.getState().getRawData());
                            block.getState().update();
                        }
                        teamcount = (org.bukkit.block.Sign) status.getBlock().getRelative(direction).getState();
                        if (!teamcount.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                            Block block = teamcount.getBlock().getRelative(direction);
                            block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                            block.getState().setRawData(startblock.getState().getRawData());
                            block.getState().update();
                        }
                        timeleft = (Sign) teamcount.getBlock().getRelative(direction).getState();

                        LobbyWallUtilities.getUtilities().setSignLines(startsign, "Team Fortress 2", "Click here", "to join:", ChatColor.BOLD + "" + map);
                        if (!GameUtilities.getUtilities().getGameStatus(map).equalsIgnoreCase("disabled")) {
                            LobbyWallUtilities.getUtilities().setSignLines(status, " ", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Status:", GameUtilities.getUtilities().getGameStatus(map), " ");
                            LobbyWallUtilities.getUtilities().setSignLines(teamcount, "" + ChatColor.DARK_RED + ChatColor.BOLD + "Red Team:", GameUtilities.getUtilities().getAmountOnTeam(map, "red") + "/" + MapConfiguration.getMaps().getMap(map).getInt("playerlimit") / 2 + " Players", ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team:", GameUtilities.getUtilities().getAmountOnTeam(map, "blue") + "/" + MapConfiguration.getMaps().getMap(map).getInt("playerlimit") / 2 + " Players");
                            LobbyWallUtilities.getUtilities().setSignLines(timeleft, " ", ChatColor.BLUE + "" + ChatColor.BOLD + "Time Left:", GameUtilities.getUtilities().getTimeLeft(map), " ");
                        } else {
                            LobbyWallUtilities.getUtilities().setSignLines(status, " ", ChatColor.BOLD + "Status:", ChatColor.DARK_RED + "" + ChatColor.BOLD + "Disabled", " ");
                            LobbyWallUtilities.getUtilities().setSignLines(teamcount, " ", "---------------------------------------------", "-------------------------------------", " ");
                            LobbyWallUtilities.getUtilities().setSignLines(timeleft, " ", "---------------------------------------------", "-------------------------------------", " ");
                        }
                        // Last sign that isnt a capture point so we can
                        // bounce off of it
                        Sign po = timeleft;
                        for (Location point : m.getCapturePoints()) {
                            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(point);
                            if (!po.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                                Block block = po.getBlock().getRelative(direction);
                                block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                                block.getState().setRawData(startblock.getState().getRawData());
                                block.getState().update();
                            }
                            po = (Sign) po.getBlock().getRelative(direction).getState();
                            String color = ChatColor.BLUE + "" + ChatColor.BOLD;
                            if (getFriendlyCaptureStatus(map, id).equalsIgnoreCase("captured")) {
                                color = ChatColor.DARK_RED + "" + ChatColor.BOLD;
                            }

                            if (!GameUtilities.getUtilities().getGameStatus(map).equalsIgnoreCase("disabled")) {
                                LobbyWallUtilities.getUtilities().setSignLines(po, "Capture Point", "#" + id, "Status:", color + getFriendlyCaptureStatus(map, id));
                            } else {
                                LobbyWallUtilities.getUtilities().setSignLines(po, " ", "---------------------------------------------", "-------------------------------------", " ");
                            }

                        }
                    }
                }

            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Encountered an error while trying to update the lobby wall.");
        }
    }

    public void setAllLines(final String map, final Integer duration, final String[] lines, final Boolean s1, final Boolean s2) {
        try {
            final Block startblock = LobbyWallUtilities.getUtilities().loadSignLocation(map).getBlock();
            final Sign startsign = (Sign) startblock.getState();
            final org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) startblock.getState().getData();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @SuppressWarnings("unused")
                @Override
                public void run() {
                    BlockFace direction = rotate90Deg(matSign.getAttachedFace());
                    Sign status = null;
                    Sign teamcount = null;
                    Sign timeleft = null;

                    if (!startsign.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                        Block block = startsign.getBlock().getRelative(direction);
                        block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                        block.getState().setRawData(startblock.getState().getRawData());
                        block.getState().update();
                    }
                    status = (org.bukkit.block.Sign) startsign.getBlock().getRelative(direction).getState();
                    if (!status.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                        Block block = status.getBlock().getRelative(direction);
                        block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                        block.getState().setRawData(startblock.getState().getRawData());
                        block.getState().update();
                    }
                    teamcount = (org.bukkit.block.Sign) status.getBlock().getRelative(direction).getState();
                    if (!teamcount.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                        Block block = teamcount.getBlock().getRelative(direction);
                        block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                        block.getState().setRawData(startblock.getState().getRawData());
                        block.getState().update();
                    }
                    timeleft = (Sign) teamcount.getBlock().getRelative(direction).getState();

                    if (s1) {
                        LobbyWallUtilities.getUtilities().setSignLines(startsign, lines[0], lines[1], lines[2], lines[3]);
                    } else {
                        LobbyWallUtilities.getUtilities().setSignLines(startsign, "Team Fortress 2", "Click here", "to join:", ChatColor.BOLD + "" + map);
                    }

                    if (s2) {
                        LobbyWallUtilities.getUtilities().setSignLines(status, lines[0], lines[1], lines[2], lines[3]);
                    } else {
                        if (!GameUtilities.getUtilities().getGameStatus(map).equalsIgnoreCase("disabled")) {
                            LobbyWallUtilities.getUtilities().setSignLines(status, " ", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Status:", GameUtilities.getUtilities().getGameStatus(map), " ");
                        } else {
                            LobbyWallUtilities.getUtilities().setSignLines(status, " ", ChatColor.BOLD + "Status:", "" + ChatColor.DARK_RED + ChatColor.BOLD + "Disabled", " ");
                        }
                    }

                    LobbyWallUtilities.getUtilities().setSignLines(teamcount, lines[0], lines[1], lines[2], lines[3]);
                    LobbyWallUtilities.getUtilities().setSignLines(timeleft, lines[0], lines[1], lines[2], lines[3]);

                    Sign po = timeleft;
                    for (Location point : plugin.getMap(map).getCapturePoints()) {
                        if (!po.getBlock().getRelative(direction).getType().equals(Material.WALL_SIGN)) {
                            Block block = po.getBlock().getRelative(direction);
                            block.setTypeIdAndData(Material.WALL_SIGN.getId(), startblock.getData(), false);
                            block.getState().setRawData(startblock.getState().getRawData());
                            block.getState().update();
                        }
                        po = (Sign) po.getBlock().getRelative(direction).getState();
                        LobbyWallUtilities.getUtilities().setSignLines(po, lines[0], lines[1], lines[2], lines[3]);
                    }
                    if (duration != null) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                updateWall();
                            }
                        }, duration * 20L);
                    }
                }
            }, 20L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFriendlyCaptureStatus(String map, Integer id) {
        Map m = plugin.getMap(map);
        String ss = m.getCapturePoint(id).getStatus().string();
        if (ss.equalsIgnoreCase("uncaptured")) {
            return "Uncaptured";
        }
        if (ss.equalsIgnoreCase("capturing")) {
            return "Capturing";
        }
        if (ss.equalsIgnoreCase("captured")) {
            return "Captured";
        }
        return null;
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
        }
        return null;
    }

    public void startTask() {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                updateWall();
            }
        }, 0L, 20L);
    }
}
