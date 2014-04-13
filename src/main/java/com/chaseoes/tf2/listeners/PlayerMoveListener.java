package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameStatus;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.Team;
import com.chaseoes.tf2.capturepoints.CapturePoint;
import com.chaseoes.tf2.capturepoints.CapturePointUtilities;

public class PlayerMoveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (!(event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
            return;
        }

        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isInLobby() || !GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame()) {
            return;
        }

        Player player = event.getPlayer();
        GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
        Block b = event.getTo().getBlock();
        TF2 plugin = TF2.getInstance();

        Map map = plugin.getMap(CapturePointUtilities.getUtilities().getMapFromLocation(b.getLocation()));
        if (map == null) {
            return;
        }

        // Capture Points
        if (gp.isIngame() && GameUtilities.getUtilities().games.get(map.getName()).getStatus() == GameStatus.INGAME && CapturePointUtilities.getUtilities().locationIsCapturePoint(b.getLocation())) {
            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(b.getLocation());
            CapturePoint cp = map.getCapturePoint(id);
            if (gp.getTeam() == Team.RED) {
                if (cp.getStatus().string().equalsIgnoreCase("uncaptured")) {
                    if (TF2.getInstance().getConfig().getBoolean("capture-in-order")) {
                        if (CapturePointUtilities.getUtilities().capturePointBeforeHasBeenCaptured(map, id)) {
                            if (cp.capturing == null) {
                                cp.startCapturing(gp);
                            }
                        } else {
                            Localizers.getDefaultLoc().CP_MUST_CAPTURE_PREVIOUS.sendPrefixed(event.getPlayer(), CapturePointUtilities.getUtilities().getFirstUncaptured(map).getId());
                        }
                    } else {
                        if (cp.capturing == null) {
                            cp.startCapturing(gp);
                        }
                    }
                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    Localizers.getDefaultLoc().CP_ALREADY_CAPTURED_RED.sendPrefixed(event.getPlayer(), id + 1);
                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("capturing")) {
                    Localizers.getDefaultLoc().CP_ALREADY_CAPTURING.sendPrefixed(event.getPlayer(), cp.capturing.getName());
                }
            } else {
                if (!map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    Localizers.getDefaultLoc().CP_WRONG_TEAM.sendPrefixed(event.getPlayer());
                } else {
                    Localizers.getDefaultLoc().CP_ALREADY_CAPTURED_BLUE.sendPrefixed(event.getPlayer());
                }
            }
        }
    }

}
