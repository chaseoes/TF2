package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameStatus;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;

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

        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isInLobby()) {
            return;
        }

        Player player = event.getPlayer();
        Block b = event.getTo().getBlock();
        TF2 plugin = TF2.getInstance();

        Map map = plugin.getMap(CapturePointUtilities.getUtilities().getMapFromLocation(b.getLocation()));
        if (map == null) {
            return;
        }

        // Capture Points
        if ((b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) && GameUtilities.getUtilities().isIngame(player) && CapturePointUtilities.getUtilities().locationIsCapturePoint(b.getLocation()) && GameUtilities.getUtilities().games.get(map.getName()).getStatus() == GameStatus.INGAME) {
            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(b.getLocation());
            CapturePoint cp = map.getCapturePoint(id);
            Game game = GameUtilities.getUtilities().getGame(map);
            if (game.getPlayer(player).getTeam() == Team.RED) {
                if (cp.getStatus().string().equalsIgnoreCase("uncaptured")) {
                    if (CapturePointUtilities.getUtilities().capturePointBeforeHasBeenCaptured(map, id)) {
                        if (cp.capturing == null) {
                            cp.startCapturing(player);
                        }
                    } else {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You must capture point #" + (id - 1) + " first!");
                    }

                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] This point has already been captured! Head to #" + (id + 1) + "!");
                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("capturing")) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] This point is being captured by " + cp.capturing.getName() + "!");
                }
            } else {
                if (!map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You must be on the " + ChatColor.DARK_RED + ChatColor.BOLD + "red " + ChatColor.RESET + ChatColor.YELLOW + "team to capture points! " + ChatColor.BLUE + ChatColor.BOLD + "Blue " + ChatColor.RESET + ChatColor.YELLOW + "is for defending.");
                } else {
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] The " + ChatColor.DARK_RED + ChatColor.BOLD + "red " + ChatColor.RESET + ChatColor.YELLOW + "team has already captured this point!");
                }
            }
        }
    }

}
