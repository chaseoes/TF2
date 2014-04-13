package com.chaseoes.tf2.utilities;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SerializableInventory {

    public static String inventoryToString(Inventory invInventory) {
        try {
            ByteArrayOutputStream baOut = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitOut = new BukkitObjectOutputStream(baOut);

            int size = invInventory.getSize();
            bukkitOut.writeInt(size);
            for (int i = 0; i < size; i++) {
                bukkitOut.writeObject(invInventory.getItem(i));
            }

            byte[] data = baOut.toByteArray();
            bukkitOut.close();
            return Base64Coder.encodeLines(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize inventory to string", e);
        }
    }

    public static Inventory stringToInventory(String invString) {
        try {
            byte[] data = Base64Coder.decodeLines(invString);
            ByteArrayInputStream baIn = new ByteArrayInputStream(data);
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(baIn);

            int size = bukkitIn.readInt();
            Inventory inv = Bukkit.getServer().createInventory(null, size);
            for (int i = 0; i < size; i++) {
                inv.setItem(i, (ItemStack) bukkitIn.readObject());
            }

            baIn.close();
            bukkitIn.close();
            return inv;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize string to inventory", e);
        }
    }

}
