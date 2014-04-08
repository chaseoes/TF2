package me.chaseoes.tf2.utilities;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

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
            return Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize inventory to string", e);
        }
    }

    public static Inventory stringToInventory(String invString) {
        try {
            byte[] data = Base64.getDecoder().decode(invString);
            ByteArrayInputStream baIn = new ByteArrayInputStream(data);
            BukkitObjectInputStream bukkitIn = new BukkitObjectInputStream(baIn);

            int size = bukkitIn.readInt();
            Inventory inv = Bukkit.getServer().createInventory(null, size);
            for (int i = 0; i < size; i++) {
                inv.setItem(i, (ItemStack) bukkitIn.readObject());
            }

            baIn.close();
            return inv;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize string to inventory", e);
        }
    }
}
