package com.chaseoes.tf2.utilities;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ArmorUtilities {

    public static ItemStack setColor(ItemStack item, org.bukkit.Color color) {
        if (item.getTypeId() == 298 || item.getTypeId() == 299 || item.getTypeId() == 300 || item.getTypeId() == 301) {
            LeatherArmorMeta i = (LeatherArmorMeta) item.getItemMeta();
            i.setColor(color);
            item.setItemMeta(i);
        }
        return item;
    }

}
