package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.GameUtilities;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class PlayerReceiveNameTagListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTag(PlayerReceiveNameTagEvent event) {
        try {
            if (GameUtilities.getUtilities().isIngame(event.getNamedPlayer())) {
                if (GameUtilities.getUtilities().getTeam(event.getNamedPlayer()).equalsIgnoreCase("red")) {
                    event.setTag(ChatColor.RED + event.getNamedPlayer().getName());
                } else if (GameUtilities.getUtilities().getTeam(event.getNamedPlayer()).equalsIgnoreCase("blue")) {
                    event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());
                }
            }
        } catch (Exception e) {

        }
    }

}
