package com.chaseoes.tf2.classes;

import java.util.logging.Level;


import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.Team;
import com.chaseoes.tf2.utilities.ArmorUtilities;
import com.chaseoes.tf2.utilities.Localizer;

public class TF2Class {

    String name;
    ConfigurationSection config;

    public TF2Class(String n) {
        name = n;
        config = TF2.getInstance().getConfig().getConfigurationSection("classes." + name);
    }

    // Apply the class to a player (returns true if it was successful).
    @SuppressWarnings("deprecation")
    public boolean apply(GamePlayer player) {
        // Check that the class exists.
        if (config == null) {
            player.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-CLASS").replace("%class", name));
            clearInventory(player.getPlayer());
            return false;
        }

        if (player.isIngame()) {
            try {

                // Clear their inventory.
                clearInventory(player.getPlayer());

                // Loop through potion effects.
                boolean apply = true;
                if (player.isInLobby() && TF2.getInstance().getConfig().getBoolean("potion-effects-after-start")) {
                    apply = false;
                }
                if (apply) {
                    for (String effect : TF2.getInstance().getConfig().getStringList("classes." + name + ".potion-effects")) {
                        String[] effects = effect.split("\\.");
                        PotionEffectType et = PotionEffectType.getByName(effects[0]);
                        int amplifier = Integer.parseInt(effects[1]) - 1;
                        int duration = 0;
                        if (effects[2].equalsIgnoreCase("forever")) {
                            duration = Integer.MAX_VALUE;
                        } else {
                            duration = Integer.parseInt(effects[2]) * 20;
                        }
                        PotionEffect e = new PotionEffect(et, duration, amplifier);
                        player.getPlayer().addPotionEffect(e);
                    }
                }

                // Loop through armor items.
                ItemStack[] armor = new ItemStack[4];
                int armorindex = 0;
                for (String armortype : TF2.getInstance().getConfig().getConfigurationSection("classes." + name + ".armor").getKeys(false)) {
                    Color c = Color.RED;
                    if (player.getTeam() == Team.BLUE) {
                        c = Color.BLUE;
                    }
                    ItemStack i = ArmorUtilities.setColor(parseItem(config.getString("armor." + armortype), true), c);

                    armor[armorindex] = i;
                    armorindex++;
                }

                // Add armor items.
                player.getPlayer().getInventory().setHelmet(armor[0]);
                player.getPlayer().getInventory().setChestplate(armor[1]);
                player.getPlayer().getInventory().setLeggings(armor[2]);
                player.getPlayer().getInventory().setBoots(armor[3]);

                // Loop through inventory items.
                for (String fullitem : TF2.getInstance().getConfig().getStringList("classes." + name + ".inventory")) {
                    ItemStack i = parseItem(fullitem, false);
                    player.getPlayer().getInventory().addItem(i);
                }

                player.setCurrentClass(this);

                player.getPlayer().updateInventory();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                TF2.getInstance().getLogger().log(Level.SEVERE, "The error encountered while changing a player's class is above! Note that TF2 v2.0 has a new format for defining items - click here to view the new default configuration: http://goo.gl/LdKKR");
                player.getPlayer().sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ERROR-CHANGE-CLASS"));
                clearInventory(player.getPlayer());
                return false;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public ItemStack parseItem(String str, boolean isArmor) {
        String[] items = str.split("\\.");
        String[] item = items[0].split("\\,");

        int id = Integer.parseInt(item[0]);
        byte data = 0;
        short damage = 0;
        int amount = 1;
        String name = null;

        if (id == 0) {
            return new ItemStack(0);
        }

        //Pre itemstack creation
        if (item.length > 1) {
            if (id == Material.POTION.getId()) {
                damage = Short.parseShort(item[1]);
            } else if (id == Material.SKULL_ITEM.getId()) {
                String temp = item[1];
                if (temp.equalsIgnoreCase("skeleton")) {
                    damage = 0;
                } else if (temp.equalsIgnoreCase("wither")) {
                    damage = 1;
                } else if (temp.equalsIgnoreCase("zombie")) {
                    damage = 2;
                } else if (temp.equalsIgnoreCase("creeper")) {
                    damage = 4;
                } else {
                    name = item[1];
                    damage = 3;
                }
            } else {
                data = Byte.parseByte(item[1]);
            }
            if (item.length > 2) {
                damage = Short.parseShort(item[2]);
            }
        }
        if (items.length > 1 && !isArmor) {
            amount = Integer.parseInt(items[1]);
        }

        //Itemstack creation
        MaterialData md = new MaterialData(id, data);
        ItemStack i = md.toItemStack();
        i.setAmount(amount);
        i.setDurability(damage);

        //Post itemstack creation
        //Meta datas
        if (name != null && id == Material.SKULL_ITEM.getId()) {
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            meta.setOwner(name);
            i.setItemMeta(meta);
        }
        //Enchantments
        int j = 2;
        if (isArmor) {
            j = 1;
        }
        for (; j < items.length; j++) {
            String[] enchantment = items[j].split("\\-");
            Enchantment e = Enchantment.getByName(enchantment[0]);
            int level = 1;
            if (enchantment.length > 1) {
                level = Integer.parseInt(enchantment[1]);
            }

            i.addUnsafeEnchantment(e, level);
        }
        return i;
    }

    @SuppressWarnings("deprecation")
    public void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.setItemOnCursor(new ItemStack(Material.AIR));

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.updateInventory();
    }

}