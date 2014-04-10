package com.chaseoes.tf2.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TF2DeathEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    public Player player;
    public Player killer;

    public TF2DeathEvent(Player p, Player k) {
        player = p;
        killer = k;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getKiller() {
        return killer;
    }

}
