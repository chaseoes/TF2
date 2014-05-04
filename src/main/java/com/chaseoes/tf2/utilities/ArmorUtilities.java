package com.chaseoes.tf2.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorUtilities {

    public static ItemStack setColor(ItemStack item, org.bukkit.Color color) {
        if (item != null && (item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS)) {
            LeatherArmorMeta i = (LeatherArmorMeta) item.getItemMeta();
            i.setColor(color);
            item.setItemMeta(i);
        }
        return item;
    }

}
