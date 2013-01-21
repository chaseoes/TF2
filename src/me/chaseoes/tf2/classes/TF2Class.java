package me.chaseoes.tf2.classes;

import java.util.logging.Level;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.utilities.ArmorUtilities;

import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
                            duration = Integer.parseInt(effects[2]);
                        }
                        PotionEffect e = new PotionEffect(et, duration, amplifier);
                        player.getPlayer().addPotionEffect(e);
                    }
                }

                // Loop through armor items.
                ItemStack[] armor = new ItemStack[4];
                int armorindex = 0;
                for (String armortype : TF2.getInstance().getConfig().getConfigurationSection("classes." + name + ".armor").getKeys(false)) {
                    String[] items = config.getString("armor." + armortype).split("\\.");
                    Color c = Color.RED;
                    if (player.getTeam() == Team.BLUE) {
                        c = Color.BLUE;
                    }
                    ItemStack i = ArmorUtilities.setColor(new ItemStack(Material.getMaterial(Integer.parseInt(items[0]))), c);

                    if (Integer.parseInt(items[0]) == 0) {
                        i = new ItemStack(Material.AIR);
                    }

                    int enchantindex = 0;
                    for (String enchant : items) {
                        if (enchantindex != 0) {
                            String[] enchantment = enchant.split("\\-");
                            Enchantment e = Enchantment.getByName(enchantment[0]);
                            int level = 1;
                            if (enchantment.length > 1) {
                                level = Integer.parseInt(enchantment[1]);
                            }

                            i.addUnsafeEnchantment(e, level);
                        }
                        enchantindex++;
                    }

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
                    String[] items = fullitem.split("\\.");
                    String[] item = items[0].split("\\,");
                    byte data = 0;
                    if (item.length > 1) {
                        data = (byte) Integer.parseInt(item[1]);
                    }

                    MaterialData md = new MaterialData(Integer.parseInt(item[0]), data);
                    ItemStack i = md.toItemStack();
                    i.setAmount(Integer.parseInt(items[1]));

                    int enchantindex = 0;
                    for (String enchant : items) {
                        if (enchantindex > 1) {
                            String[] enchantment = enchant.split("\\-");
                            Enchantment e = Enchantment.getByName(enchantment[0]);
                            int level = 1;
                            if (enchantment.length > 1) {
                                level = Integer.parseInt(enchantment[1]);
                            }

                            i.addUnsafeEnchantment(e, level);
                        }
                        enchantindex++;
                    }

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

    @SuppressWarnings("deprecation")
    public void clearInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        player.updateInventory();
    }

}