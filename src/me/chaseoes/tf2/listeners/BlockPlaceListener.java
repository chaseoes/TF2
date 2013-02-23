package me.chaseoes.tf2.listeners;

import java.util.List;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

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
            player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CLASS-CREATED").replace("%type", gp.getClassButtonType()).replace("%class", gp.getClassButtonName()));
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
            player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CHANGE-CLASS-CREATED"));
        }
    }

}
