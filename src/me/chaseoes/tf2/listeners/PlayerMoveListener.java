package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.*;
import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;

import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
        if ((b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) && gp.isIngame() && CapturePointUtilities.getUtilities().locationIsCapturePoint(b.getLocation()) && GameUtilities.getUtilities().games.get(map.getName()).getStatus() == GameStatus.INGAME) {
            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(b.getLocation());
            CapturePoint cp = map.getCapturePoint(id);
            if (gp.getTeam() == Team.RED) {
                if (cp.getStatus().string().equalsIgnoreCase("uncaptured")) {
                    if (CapturePointUtilities.getUtilities().capturePointBeforeHasBeenCaptured(map, id)) {
                        if (cp.capturing == null) {
                            cp.startCapturing(gp);
                        }
                    } else {
                        event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-MUST-CAPTURE-PREVIOUS").replace("%id", (id - 1) + ""));
                    }

                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-ALREADY-CAPTURED-RED").replace("%id", (id + 1) + ""));
                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("capturing")) {
                    event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-ALREADY-CAPTURING").replace("%player", cp.capturing.getName()));
                }
            } else {
                if (!map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-WRONG-TEAM"));
                } else {
                    event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-ALREADY-CAPTURED-BLUE"));
                }
            }
        }
    }

}
