package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Team;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class PlayerReceiveNameTagListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTag(PlayerReceiveNameTagEvent event) {
        if (GameUtilities.getUtilities().getGamePlayer(event.getNamedPlayer()).isIngame()) {
            GamePlayer namedPlayer = GameUtilities.getUtilities().getGamePlayer(event.getNamedPlayer());
            if (namedPlayer.getTeam() == Team.RED) {
                event.setTag(ChatColor.RED + event.getNamedPlayer().getName());
            } else if (namedPlayer.getTeam() == Team.BLUE) {
                event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());
            }
        }
    }

}
