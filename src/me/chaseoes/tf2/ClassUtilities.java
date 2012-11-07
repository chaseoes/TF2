package me.chaseoes.tf2;

import java.awt.Color;
import java.util.HashMap;

import me.chaseoes.tf2.utilities.ArmorUtilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ClassUtilities {

    private TF2 plugin;
    static ClassUtilities instance = new ClassUtilities();
    public HashMap<String, Integer> effects = new HashMap<String, Integer>();
    public HashMap<String, String> classes = new HashMap<String, String>();

    private ClassUtilities() {

    }

    public static ClassUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void changeClass(final Player player, String classto) {
        if (classto != null) {
            for (String classname : plugin.getConfig().getConfigurationSection("classes").getKeys(false)) {
                if (classname.equalsIgnoreCase(classto)) {
                    plugin.reloadConfig();
                    plugin.saveConfig();
                    player.getInventory().clear();
                    player.getInventory().setHelmet(new ItemStack(Material.AIR));
                    player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    player.getInventory().setLeggings(new ItemStack(Material.AIR));
                    player.getInventory().setBoots(new ItemStack(Material.AIR));
                    player.updateInventory();
                    classes.put(player.getName(), classname);

                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        player.removePotionEffect(effect.getType());
                    }

                    for (String pot : plugin.getConfig().getStringList("classes." + classname + ".potion-effects")) {
                        final String[] poti = pot.split("\\.");
                        final PotionEffectType e = PotionEffectType.getByName(poti[0]);
                        if (poti[2].equalsIgnoreCase("forever")) {
                            player.addPotionEffect(e.createEffect(Integer.MAX_VALUE, Integer.parseInt(poti[1])));
                        } else {
                            Integer dur = Integer.parseInt(poti[2]) * 20;
                            player.addPotionEffect(e.createEffect(dur, Integer.parseInt(poti[1])));
                        }
                    }

                    String[] helmet = plugin.getConfig().getString("classes." + classname + ".armor.helmet").split("\\.");
                    String[] chest = plugin.getConfig().getString("classes." + classname + ".armor.chest").split("\\.");
                    String[] legs = plugin.getConfig().getString("classes." + classname + ".armor.legs").split("\\.");
                    String[] boots = plugin.getConfig().getString("classes." + classname + ".armor.boots").split("\\.");

                    ItemStack helm = new ItemStack(Integer.parseInt(helmet[0]));
                    ItemStack ches = new ItemStack(Integer.parseInt(chest[0]));
                    ItemStack leg = new ItemStack(Integer.parseInt(legs[0]));
                    ItemStack boot = new ItemStack(Integer.parseInt(boots[0]));

                    if (helmet.length > 1 && helm.getType() != Material.AIR) {
                        helm.addEnchantment(Enchantment.getByName(helmet[1].toUpperCase()), Integer.parseInt(helmet[2]));
                        if (helmet.length > 3) {
                            helm.addEnchantment(Enchantment.getByName(helmet[3].toUpperCase()), Integer.parseInt(helmet[4]));
                        }
                        if (helmet.length > 5) {
                            helm.addEnchantment(Enchantment.getByName(helmet[5].toUpperCase()), Integer.parseInt(helmet[6]));
                        }
                        if (helmet.length > 7) {
                            helm.addEnchantment(Enchantment.getByName(helmet[7].toUpperCase()), Integer.parseInt(helmet[8]));
                        }
                        if (helmet.length > 9) {
                            helm.addEnchantment(Enchantment.getByName(helmet[10].toUpperCase()), Integer.parseInt(helmet[11]));
                        }
                    }
                    if (chest.length > 1 && ches.getType() != Material.AIR) {
                        ches.addEnchantment(Enchantment.getByName(chest[1]), Integer.parseInt(chest[2]));
                        if (chest.length > 3) {
                            ches.addEnchantment(Enchantment.getByName(chest[3].toUpperCase()), Integer.parseInt(chest[4]));
                        }
                        if (chest.length > 5) {
                            ches.addEnchantment(Enchantment.getByName(chest[5].toUpperCase()), Integer.parseInt(chest[6]));
                        }
                        if (chest.length > 7) {
                            ches.addEnchantment(Enchantment.getByName(chest[7].toUpperCase()), Integer.parseInt(chest[8]));
                        }
                        if (chest.length > 9) {
                            ches.addEnchantment(Enchantment.getByName(chest[10].toUpperCase()), Integer.parseInt(chest[11]));
                        }
                    }
                    if (legs.length > 1 && ches.getType() != Material.AIR) {
                        leg.addEnchantment(Enchantment.getByName(legs[1]), Integer.parseInt(legs[2]));
                        if (legs.length > 3) {
                            leg.addEnchantment(Enchantment.getByName(legs[3].toUpperCase()), Integer.parseInt(legs[4]));
                        }
                        if (legs.length > 5) {
                            leg.addEnchantment(Enchantment.getByName(legs[5].toUpperCase()), Integer.parseInt(legs[6]));
                        }
                        if (legs.length > 7) {
                            leg.addEnchantment(Enchantment.getByName(legs[7].toUpperCase()), Integer.parseInt(legs[8]));
                        }
                        if (legs.length > 9) {
                            leg.addEnchantment(Enchantment.getByName(legs[10].toUpperCase()), Integer.parseInt(legs[11]));
                        }
                    }
                    if (boots.length > 1 && ches.getType() != Material.AIR) {
                        boot.addEnchantment(Enchantment.getByName(boots[1]), Integer.parseInt(boots[2]));
                        if (boots.length > 3) {
                            boot.addEnchantment(Enchantment.getByName(boots[3].toUpperCase()), Integer.parseInt(boots[4]));
                        }
                        if (boots.length > 5) {
                            boot.addEnchantment(Enchantment.getByName(boots[5].toUpperCase()), Integer.parseInt(boots[6]));
                        }
                        if (boots.length > 7) {
                            boot.addEnchantment(Enchantment.getByName(boots[7].toUpperCase()), Integer.parseInt(boots[8]));
                        }
                        if (boots.length > 9) {
                            boot.addEnchantment(Enchantment.getByName(boots[10].toUpperCase()), Integer.parseInt(boots[11]));
                        }
                    }

                    for (String str : plugin.getConfig().getStringList("classes." + classname + ".inventory")) {
                        String[] split = str.split("\\.");
                        String[] sd = split[0].split("\\,");
                        ItemStack i = null;

                        if (sd.length > 1) {
                            i = new ItemStack(Integer.parseInt(sd[0]), Integer.parseInt(split[1]), (short) 0, (byte) Integer.parseInt(sd[1]));
                        } else {
                            i = new ItemStack(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                        }

                        if (split.length > 2) {
                            i.addEnchantment(Enchantment.getByName(split[2]), Integer.parseInt(split[3]));
                            if (split.length > 4) {
                                i.addEnchantment(Enchantment.getByName(split[4].toUpperCase()), Integer.parseInt(split[5]));
                            }
                            if (split.length > 6) {
                                i.addEnchantment(Enchantment.getByName(split[6].toUpperCase()), Integer.parseInt(split[7]));
                            }
                            if (split.length > 8) {
                                i.addEnchantment(Enchantment.getByName(split[8].toUpperCase()), Integer.parseInt(split[9]));
                            }
                            if (split.length > 10) {
                                i.addEnchantment(Enchantment.getByName(split[10].toUpperCase()), Integer.parseInt(split[11]));
                            }
                        }

                        player.getInventory().addItem(i);
                    }
                    String team = GameUtilities.getUtilities().getTeam(player);
                    if (Integer.parseInt(helmet[0]) != 0) {
                        if (team.equalsIgnoreCase("red")) {
                            player.getInventory().setHelmet(ArmorUtilities.setColor(helm, Color.RED));
                        } else {
                            player.getInventory().setHelmet(ArmorUtilities.setColor(helm, Color.BLUE));
                        }
                    }
                    if (Integer.parseInt(chest[0]) != 0) {
                        if (team.equalsIgnoreCase("red")) {
                            player.getInventory().setChestplate(ArmorUtilities.setColor(ches, Color.RED));
                        } else {
                            player.getInventory().setChestplate(ArmorUtilities.setColor(ches, Color.BLUE));
                        }
                    }
                    if (Integer.parseInt(legs[0]) != 0) {
                        if (team.equalsIgnoreCase("red")) {
                            player.getInventory().setLeggings(ArmorUtilities.setColor(leg, Color.RED));
                        } else {
                            player.getInventory().setLeggings(ArmorUtilities.setColor(leg, Color.BLUE));
                        }
                    }
                    if (Integer.parseInt(boots[0]) != 0) {
                        if (team.equalsIgnoreCase("red")) {
                            player.getInventory().setBoots(ArmorUtilities.setColor(boot, Color.RED));
                        } else {
                            player.getInventory().setBoots(ArmorUtilities.setColor(boot, Color.BLUE));
                        }
                    }

                    if (GameUtilities.getUtilities().usingchangeclassbutton.contains(player.getName())) {
                        GameUtilities.getUtilities().usingchangeclassbutton.remove(player.getName());
                        player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().ingame.get(player.getName()), GameUtilities.getUtilities().teams.get(player.getName())));
                    }

                    if (GameUtilities.getUtilities().getGameStatus(GameUtilities.getUtilities().ingame.get(player.getName())).equalsIgnoreCase("in-game")) {
                        if (!GameUtilities.getUtilities().getTeam(player).equalsIgnoreCase("red")) {
                            player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().ingame.get(player.getName()), GameUtilities.getUtilities().teams.get(player.getName())));
                        } else {
                            if (GameUtilities.getUtilities().redHasBeenTeleported != null && GameUtilities.getUtilities().getCurrentMap(player) != null && player != null) {
                                if (GameUtilities.getUtilities().redHasBeenTeleported.get(GameUtilities.getUtilities().getCurrentMap(player))) {
                                    player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().ingame.get(player.getName()), GameUtilities.getUtilities().teams.get(player.getName())));
                                }
                            }
                        }
                    }
                    player.updateInventory();
                }
            }
        }
    }

    public Location loadClassButtonLocation(String l) {
        String[] split = l.split("\\.");
        return new Location(plugin.getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }

    public String loadClassFromLocation(String l) {
        String[] split = l.split("\\.");
        return split[5];
    }

    public String loadClassButtonTypeFromLocation(String l) {
        String[] split = l.split("\\.");
        return split[4];
    }

}
