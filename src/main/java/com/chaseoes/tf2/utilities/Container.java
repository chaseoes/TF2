package com.chaseoes.tf2.utilities;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.chaseoes.tf2.Map;

public class Container {

    private Location loc;
    private Inventory inv;
    private Map map;

    public Container(Location loc, Inventory inv, Map map) {
        this.loc = loc;
        this.inv = inv;
        this.map = map;
    }

    public void applyItems() {
        if (loc.getBlock().getState() instanceof InventoryHolder) {
            InventoryHolder inventoryHolder = (InventoryHolder) loc.getBlock().getState();
            inventoryHolder.getInventory().setContents(inv.getContents());
            loc.getBlock().getState().update();
        } else {
            map.removeContainer(loc);
        }
    }

    public Inventory getInventory() {
        return inv;
    }

    public Location getLocation() {
        return loc;
    }

}
