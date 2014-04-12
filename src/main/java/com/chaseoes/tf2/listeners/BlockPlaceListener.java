package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame()) {
            if (TF2.getInstance().getConfig().getBoolean("prevent-block-breaking") && !player.hasPermission("tf2.create")) {
                event.setCancelled(true);
                return;
            }
        }

        GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
        if (gp.isMakingClassButton() && (event.getBlockPlaced().getType() == Material.STONE_BUTTON || event.getBlockPlaced().getType() == Material.WOOD_BUTTON)) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("classbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ() + "." + gp.getClassButtonType() + "." + gp.getClassButtonName());
            DataConfiguration.getData().saveData();
            DataConfiguration.getData().getDataFile().set("classbuttons", classbs);
            DataConfiguration.getData().saveData();
            Localizers.getDefaultLoc().BUTTON_CLASS_CREATED.sendPrefixed(player, gp.getClassButtonType(), gp.getClassButtonName());
            gp.setMakingClassButton(false);
            gp.setClassButtonName(null);
            gp.setClassButtonType(null);
        }

        if (GameUtilities.getUtilities().getGamePlayer(player).isMakingChangeClassButton() && (event.getBlockPlaced().getType() == Material.STONE_BUTTON || event.getBlockPlaced().getType() == Material.WOOD_BUTTON)) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ());
            DataConfiguration.getData().getDataFile().set("changeclassbuttons", classbs);
            gp.setMakingChangeClassButton(false);
            DataConfiguration.getData().saveData();
            Localizers.getDefaultLoc().BUTTON_CHANGE_CLASS_CREATED.sendPrefixed(player);
        }
    }

}
