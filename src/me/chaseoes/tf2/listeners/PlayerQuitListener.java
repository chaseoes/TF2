package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;

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
            Game game = GameUtilities.getUtilities().getCurrentGame(player);
            game.leaveGame(player);
            for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
                Game game2 = GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map));
                game2.getQueue().removePlayer(player);
            }
        }
        GameUtilities.getUtilities().playerLeaveServer(event.getPlayer());
    }

}
