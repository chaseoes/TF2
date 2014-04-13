package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.capturepoints.CapturePointUtilities;
import com.chaseoes.tf2.classes.ClassUtilities;
import com.chaseoes.tf2.lobbywall.LobbyWall;
import com.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

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
                        Localizers.getDefaultLoc().BUTTON_CLASS_REMOVE.sendPrefixed(player);
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
                        Localizers.getDefaultLoc().BUTTON_CHANGE_CLASS_REMOVE.sendPrefixed(player);
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (b.getType() == Material.WALL_SIGN) {
            Sign s = (Sign) b.getState();
            if (s.getLine(0).equalsIgnoreCase(Localizers.getDefaultLoc().LOBBYWALL_JOIN_1.getString()) && s.getLine(1).equalsIgnoreCase(Localizers.getDefaultLoc().LOBBYWALL_JOIN_2.getString())) {
                String map = ChatColor.stripColor(s.getLine(3));
                Location signLoc = LobbyWallUtilities.getUtilities().loadSignLocation(map);
                if (signLoc == null) {
                    return;
                }
                if (b.getLocation().toString().equalsIgnoreCase(signLoc.toString())) {
                    if (player.hasPermission("tf2.create")) {
                        DataConfiguration.getData().getDataFile().set("lobbywall." + map, null);
                        DataConfiguration.getData().saveData();
                        LobbyWall.getWall().setDirty(map, true);
                        Localizers.getDefaultLoc().LOBBYWALL_REMOVE.sendPrefixed(player);
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
                Localizers.getDefaultLoc().CP_REMOVE.sendPrefixed(player);
            } else {
                event.setCancelled(true);
            }
        }

        if (b.getState() instanceof InventoryHolder) {
            for (Map map : TF2.getInstance().getMaps()) {
                if (map.isContainerRegistered(b.getLocation())) {
                    if (player.hasPermission("tf2.create")) {
                        map.removeContainer(b.getLocation());
                        Localizers.getDefaultLoc().CONTAINER_REMOVE.sendPrefixed(player);
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
