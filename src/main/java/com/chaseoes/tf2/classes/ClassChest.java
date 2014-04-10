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
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            return ((Chest) location.getBlock().getState()).getBlockInventory().getItem(23);
        }
        return new ItemStack(Material.AIR);
    }

    public ItemStack getChestplate() {
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            return ((Chest) location.getBlock().getState()).getBlockInventory().getItem(24);
        }
        return new ItemStack(Material.AIR);
    }

    public ItemStack getLeggings() {
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            return ((Chest) location.getBlock().getState()).getBlockInventory().getItem(25);
        }
        return new ItemStack(Material.AIR);
    }

    public ItemStack getBoots() {
        Location location = getClassChestLocation();
        if (location.getBlock().getState() instanceof Chest) {
            return ((Chest) location.getBlock().getState()).getBlockInventory().getItem(26);
        }
        return new ItemStack(Material.AIR);
    }

    private Location getClassChestLocation() {
        return SerializableLocation.stringToLocation(DataConfiguration.getData().getDataFile().getString("class-chest-locations." + getClassName()));
    }

}
