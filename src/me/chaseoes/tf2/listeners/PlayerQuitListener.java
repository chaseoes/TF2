package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        GamePlayer gPlayer = GameUtilities.getUtilities().getGamePlayer(event.getPlayer());
        if (gPlayer.isIngame()) {
            Game game = gPlayer.getGame();
            if (game != null) {
                game.leaveGame(gPlayer.getPlayer());
            }
        }
        GameUtilities.getUtilities().playerLeaveServer(event.getPlayer());
    }

}
