package com.chaseoes.tf2.classes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.utilities.SerializableLocation;

public class ClassChest {

    private String className;

    public ClassChest(String className) {
        this.className = className;
    }

    public boolean exists() {
        return DataConfiguration.getData().getDataFile().getString("class-chest-locations." + getClassName()) != null;
    }

    public String getClassName() {
        return className;
    }

    public List<ItemStack> getClassItems() {
        List<ItemStack> classItems = new ArrayList<ItemStack>();
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            int slot = 0;
            while (slot < 23) {
                ItemStack i = ((Chest) location.getBlock().getState()).getBlockInventory().getItem(slot);
                if (i != null && i.getType() != Material.AIR) {
                    classItems.add(i);
                }
                slot++;
            }
        }
        return classItems;
    }

    public ItemStack getHelmet() {
        return getItemFromSlot(23);
    }

    public ItemStack getChestplate() {
        return getItemFromSlot(24);
    }

    public ItemStack getLeggings() {
        return getItemFromSlot(25);
    }

    public ItemStack getBoots() {
        return getItemFromSlot(26);
    }

    private ItemStack getItemFromSlot(int id) {
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            return ((Chest) location.getBlock().getState()).getBlockInventory().getItem(id);
        }
        return new ItemStack(Material.AIR);
    }

    private Location getClassChestLocation() {
        return SerializableLocation.stringToLocation(DataConfiguration.getData().getDataFile().getString("class-chest-locations." + getClassName()));
    }

}
