package me.chaseoes.tf2.listeners;

import java.util.List;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        Player player = event.getPlayer();

        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame()) {
            if (TF2.getInstance().getConfig().getBoolean("prevent-block-breaking") && !player.hasPermission("tf2.create")) {
                event.setCancelled(true);
                return;
            }
        }

        if ((b.getType() == Material.STONE_BUTTON || b.getType() == Material.WOOD_BUTTON) && player.hasPermission("tf2.create")) {
            for (String s : DataConfiguration.getData().getDataFile().getStringList("classbuttons")) {
                if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(b.getLocation().toString())) {
                    if (player.hasPermission("tf2.create")) {
                        List<String> buttons = DataConfiguration.getData().getDataFile().getStringList("classbuttons");
                        buttons.remove(s);
                        DataConfiguration.getData().getDataFile().set("classbuttons", buttons);
                        DataConfiguration.getData().saveData();
                        player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CLASS-REMOVE"));
                    } else {
                        event.setCancelled(true);
                    }
                }
            }

            for (String s : DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons")) {
                if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(b.getLocation().toString())) {
                    if (player.hasPermission("tf2.create")) {
                        List<String> buttons = DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons");
                        buttons.remove(s);
                        DataConfiguration.getData().getDataFile().set("changeclassbuttons", buttons);
                        DataConfiguration.getData().saveData();
                        player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CHANGE-CLASS-REMOVE"));
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (b.getType() == Material.WALL_SIGN) {
            Sign s = (Sign) b.getState();
            if (s.getLine(0).equalsIgnoreCase(Localizer.getLocalizer().loadMessage("LOBBYWALL-JOIN-1")) && s.getLine(1).equalsIgnoreCase(Localizer.getLocalizer().loadMessage("LOBBYWALL-JOIN-2"))) {
                String map = ChatColor.stripColor(s.getLine(3));
                if (b.getLocation().toString().equalsIgnoreCase(LobbyWallUtilities.getUtilities().loadSignLocation(map).toString())) {
                    if (player.hasPermission("tf2.create")) {
                        DataConfiguration.getData().getDataFile().set("lobbywall." + map, null);
                        DataConfiguration.getData().saveData();
                        LobbyWall.getWall().setDirty(map, true);
                        player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("LOBBYWALL-REMOVE"));
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if ((b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) && CapturePointUtilities.getUtilities().locationIsCapturePoint(b.getLocation())) {
            if (player.hasPermission("tf2.create")) {
                String map = CapturePointUtilities.getUtilities().getMapFromLocation(b.getLocation());
                Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(b.getLocation());
                Map mMap = TF2.getInstance().getMap(map);
                mMap.setCapturePoint(id, null);
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CP-REMOVE"));
            } else {
                event.setCancelled(true);
            }
        }

        if (b.getState() instanceof InventoryHolder) {
            for (Map map : TF2.getInstance().getMaps()) {
                if (map.isContainerRegistered(b.getLocation())) {
                    if (player.hasPermission("tf2.create")) {
                        map.removeContainer(b.getLocation());
                        player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CONTAINER-REMOVE"));
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
