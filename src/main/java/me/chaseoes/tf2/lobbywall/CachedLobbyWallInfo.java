package me.chaseoes.tf2.lobbywall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePoint;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

public class CachedLobbyWallInfo {

    private Map map;
    private Game game;
    private String name;
    private List<Sign> signs = new ArrayList<Sign>();
    private List<CapturePoint> cps = new ArrayList<CapturePoint>();
    private byte dataFacing;
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
            dataFacing = block.getState().getRawData();
            BlockFace searchDirection = LobbyWallUtilities.getUtilities().rotate90Deg(matSign.getAttachedFace());
            int amountToAdd;
            if (TF2.getInstance().getConfig().getBoolean("capture-point-signs")) {
                amountToAdd = cps.size();
            }
            else {
                amountToAdd = 0;
            }
            for (int i = 1; i < 4 + amountToAdd; i++) {
                block = block.getRelative(searchDirection);
                if (block.getType() != Material.WALL_SIGN) {
                    block.setTypeIdAndData(Material.WALL_SIGN.getId(), dataFacing, false);
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
                block.setTypeIdAndData(Material.WALL_SIGN.getId(), dataFacing, false);
                signs.set(i, (Sign) block.getState());
            }
        }
    }
}
