package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Queue;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getPlayer())) {
            Player player = event.getPlayer();
            String name = player.getName();
            Game game = GameUtilities.getUtilities().getCurrentGame(player);
            game.leaveGame(player);
            Queue q = game.getQueue();
            q.removePlayer(name);
        }
        GameUtilities.getUtilities().playerLeaveServer(event.getPlayer());
    }

}
