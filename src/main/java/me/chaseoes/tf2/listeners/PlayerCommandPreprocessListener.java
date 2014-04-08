package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandPreprocessListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (GameUtilities.getUtilities().getGamePlayer(event.getPlayer()).isIngame() && !canUseCommand(event.getMessage()) && !event.getPlayer().hasPermission("tf2.create")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CANT-USE-COMMANDS-INGAME"));
        }
    }
    
    public boolean canUseCommand (String command){
    	for (String itemInList : TF2.getInstance().getConfig().getStringList("enabled-ingame-commands")){
    		if (command.startsWith(itemInList))
    			return true;
    	}
    	return false;
    }
}
