package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private TF2 pl;

    public InventoryClickListener(TF2 tf2) {
        pl = tf2;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != null && event.getWhoClicked() instanceof Player) {
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer((Player) event.getWhoClicked());
            if (gp.isIngame() && pl.getInstance().getConfig().getBoolean("prevent-inventory-moving")) {
                event.setCancelled(true);
                gp.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-INVENTORY-MOVING-BLOCKED"));
            }
        }
    }
}
