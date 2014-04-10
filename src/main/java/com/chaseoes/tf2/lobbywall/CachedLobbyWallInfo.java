package com.chaseoes.tf2.lobbywall;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.capturepoints.CapturePoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CachedLobbyWallInfo {

    private Map map;
    private Game game;
    private String name;
    private List<Sign> signs = new ArrayList<Sign>();
    private List<CapturePoint> cps = new ArrayList<CapturePoint>();
    private BlockFace facing;
    private boolean dirty = true;

    public CachedLobbyWallInfo(String name) {
        this.name = name;
        recache();
    }

    public void recache() {
        signs.clear();
        cps.clear();
        map = TF2.getInstance().getMap(name);
        game = GameUtilities.getUtilities().getGame(map);
        cps = new ArrayList<CapturePoint>(map.getCapturePoints());
        Collections.sort(cps);
        Location loc = LobbyWallUtilities.getUtilities().loadSignLocation(name);
        if (loc != null) {
            signs.add((Sign) loc.getBlock().getState());
            Block block = loc.getBlock();
            final org.bukkit.material.Sign matSign = (org.bukkit.material.Sign) loc.getBlock().getState().getData();
            facing = matSign.getFacing();
            BlockFace searchDirection = LobbyWallUtilities.getUtilities().rotate90Deg(matSign.getAttachedFace());
            int amountToAdd = 0;
            if (TF2.getInstance().getConfig().getBoolean("capture-point-signs")) {
                amountToAdd = cps.size();
            }
            for (int i = 1; i < 4 + amountToAdd; i++) {
                block = block.getRelative(searchDirection);
                if (block.getType() != Material.WALL_SIGN) {
                    BlockState bs = block.getState();
                    bs.setType(Material.WALL_SIGN);
                    ((org.bukkit.material.Sign) bs.getData()).setFacingDirection(facing);
                    bs.update(true, false);
                }
                signs.add((Sign) block.getState());
            }
        }
        dirty = false;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty;
    }

    public List<Sign> getSignLocations() {
        return signs;
    }

    public List<CapturePoint> getCapturePoints() {
        return cps;
    }

    public Map getMap() {
        return map;
    }

    public Game getGame() {
        return game;
    }

    public void verifySigns() {
        for (int i = 0; i < signs.size(); i++) {
            Block block = signs.get(i).getBlock();
            if (block.getType() != Material.WALL_SIGN) {
                BlockState state = block.getState();
                state.setType(Material.WALL_SIGN);
                ((org.bukkit.material.Sign) state.getData()).setFacingDirection(facing);
                state.update(true, false);
                signs.set(i, (Sign) block.getState());
            }
        }
    }
}
