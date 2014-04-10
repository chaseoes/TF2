package com.chaseoes.tf2.classes;

import java.util.logging.Level;


import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.Localizer;

public class TF2Class {

    String name;
    ConfigurationSection config;

    public TF2Class(String n) {
        name = n;
        config = TF2.getInstance().getConfig().getConfigurationSection("classes." + name);
    }

    public String getName() {
        return name;
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

                // Loop through chest items.
                ClassChest chest = new ClassChest(getName());
                for (ItemStack i : chest.getClassItems()) {
                    // Check the name of water bottle for custom potion effects.
                    // Should be in this format: POTION_NAME AMPLIFIER TIME_IN_SECONDS
                    if (i.getType() == Material.POTION) {
                        for (PotionEffectType type : PotionEffectType.values()) {
                            if (i.getItemMeta().getDisplayName().toLowerCase().startsWith(type.getName().toLowerCase())) {
                                if (!(player.isInLobby() && TF2.getInstance().getConfig().getBoolean("potion-effects-after-start"))) {
                                    String[] parts = i.getItemMeta().getDisplayName().split(" ");
                                    PotionEffectType potionType = PotionEffectType.getByName(parts[0].toUpperCase());
                                    int amplifier = Integer.parseInt(parts[1]) - 1;
                                    int duration = 0;

                                    if (parts[2].equalsIgnoreCase("forever")) {
                                        duration = Integer.MAX_VALUE;
                                    } else {
                                        duration = Integer.parseInt(parts[2]) * 20;
                                    }

                                    PotionEffect e = new PotionEffect(potionType, duration, amplifier);
                                    player.getPlayer().addPotionEffect(e);
                                    continue;
                                }
                            }
                        }
                    }

                    player.getPlayer().getInventory().addItem(i);
                }

                // Add armor items.
                player.getPlayer().getInventory().setHelmet(chest.getHelmet());
                player.getPlayer().getInventory().setChestplate(chest.getChestplate());
                player.getPlayer().getInventory().setLeggings(chest.getLeggings());
                player.getPlayer().getInventory().setBoots(chest.getBoots());

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