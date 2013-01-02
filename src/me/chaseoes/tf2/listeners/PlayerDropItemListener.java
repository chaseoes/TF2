package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerDropItemListener implements Listener {
    
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getPlayer())) {
            ItemStack[] armor = event.getPlayer().getInventory().getArmorContents();
            event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You cannot drop items while in a game!");
            event.setCancelled(true);
            event.getPlayer().getInventory().setArmorContents(armor);
            event.getPlayer().updateInventory();
        }
    }

}
