package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.chaseoes.tf2.GameUtilities;

public class PlayerDropItemListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame()) {
            ItemStack[] armor = event.getPlayer().getInventory().getArmorContents();
            Localizers.getDefaultLoc().CANT_DROP_ITEMS_INGAME.sendPrefixed(event.getPlayer());
            event.setCancelled(true);
            event.getPlayer().getInventory().setArmorContents(armor);
            event.getPlayer().updateInventory();
        }
    }

}
