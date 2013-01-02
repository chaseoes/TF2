package me.chaseoes.tf2.utilities;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class Container {

    private Location loc;
    private Inventory inv;

    public Container(Location loc, Inventory inv) {
        this.loc = loc;
        this.inv = inv;
    }

    public void applyItems() {
        InventoryHolder inventoryHolder = (InventoryHolder) loc.getBlock().getState();
        inventoryHolder.getInventory().setContents(inv.getContents());
        loc.getBlock().getState().update();
    }

    public Inventory getInventory() {
        return inv;
    }

    public Location getLocation() {
        return loc;
    }
}
