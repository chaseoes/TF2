package me.chaseoes.tf2;

import java.util.List;

import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.events.TF2DeathEvent;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.utilities.DataChecker;
import me.chaseoes.tf2.utilities.LocationStore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.PlayerReceiveNameTagEvent;

public class PlayerListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[TF2]") && event.getPlayer().hasPermission("tf2.create")) {
            Location bl = event.getBlock().getLocation();
            if (event.getLine(1).equalsIgnoreCase("map")) {
                String map = event.getLine(2);
                final Player player = event.getPlayer();
                DataChecker dc = new DataChecker(map);
                if (!dc.allGood()) {
                    player.sendMessage("§e[TF2] This map has not yet been setup.");
                    player.sendMessage("§e[TF2] Type §6/tf2 checkdata " + map + " §eto see what else needs to be done.");
                    event.setLine(0, "");
                    event.setLine(1, "");
                    event.setLine(2, "");
                    event.setLine(3, "");
                    return;
                }
                LobbyWallUtilities.getUtilities().saveSignLocation(event.getLine(2), bl);
                MapConfiguration.getMaps().reloadMap(event.getLine(2));
                DataConfiguration.getData().reloadData();
                MapConfiguration.getMaps().saveMap(map);
                DataConfiguration.getData().saveData();
                TF2 plugin = GameUtilities.getUtilities().plugin;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        LobbyWall.getWall().update();
                        player.performCommand("tf2 reload");
                    }
                }, 60L);
                event.getPlayer().sendMessage("§e[TF2] Successfully created a join sign!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().isSneaking()) {
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (GameUtilities.getUtilities().makingclassbutton.containsKey(player.getName()) && event.getBlockPlaced().getType() == Material.STONE_BUTTON) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("classbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ() + "." + GameUtilities.getUtilities().makingclassbuttontype.get(player.getName()) + "." + GameUtilities.getUtilities().makingclassbutton.get(player.getName()));
            DataConfiguration.getData().saveData();
            DataConfiguration.getData().getDataFile().set("classbuttons", classbs);
            player.sendMessage("§e[TF2] Successfully made a " + GameUtilities.getUtilities().makingclassbuttontype.get(player.getName()) + " class button for the class §o" + GameUtilities.getUtilities().makingclassbutton.get(player.getName()) + "§r§e.");
            GameUtilities.getUtilities().makingclassbutton.remove(player.getName());
            GameUtilities.getUtilities().makingclassbuttontype.remove(player.getName());
        }

        if (GameUtilities.getUtilities().makingchangeclassbutton.contains(player.getName()) && event.getBlockPlaced().getType() == Material.STONE_BUTTON) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ());
            DataConfiguration.getData().getDataFile().set("changeclassbuttons", classbs);
            GameUtilities.getUtilities().makingchangeclassbutton.remove(player.getName());
            DataConfiguration.getData().saveData();
            player.sendMessage("§e[TF2] Successfully made a change class button.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.getEntityType() == EntityType.SPLASH_POTION && event.getEntity().getShooter() instanceof Player) {
            Player shooter = (Player) event.getEntity().getShooter();
            if (GameUtilities.getUtilities().isIngame(shooter)) {
                LocationStore.setAFKTime(shooter, null);
                LocationStore.unsetLastLocation(shooter);
            }

        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            if (GameUtilities.getUtilities().isIngame(damaged)) {

                // Set Armor Durability
                if (damaged.getInventory().getHelmet() != null) {
                    damaged.getInventory().getHelmet().setDurability((short) -100);
                }
                if (damaged.getInventory().getChestplate() != null) {
                    damaged.getInventory().getChestplate().setDurability((short) -100);
                }
                if (damaged.getInventory().getLeggings() != null) {
                    damaged.getInventory().getLeggings().setDurability((short) -100);
                }
                if (damaged.getInventory().getBoots() != null) {
                    damaged.getInventory().getBoots().setDurability((short) -100);
                }

                // AFK Timer
                LocationStore.setAFKTime(damaged, null);
                LocationStore.unsetLastLocation(damaged);

                // Check Game Status
                if (!GameUtilities.getUtilities().getGameStatus(GameUtilities.getUtilities().getCurrentMap(damaged)).equalsIgnoreCase("in-game")) {
                    event.setCancelled(true);
                    return;
                }

                if (event.getDamager() instanceof Player) {
                    Player damager = (Player) event.getDamager();
                    if (GameUtilities.getUtilities().teams.get(damaged.getName()).equalsIgnoreCase(GameUtilities.getUtilities().teams.get(damager.getName()))) {
                        event.setCancelled(true);
                        return;
                    }

                    if (damager.getItemInHand() != null && damager.getItemInHand().getType().getId() != 373) {
                        damager.getItemInHand().setDurability((short) -100);
                    }

                    if (damaged.getHealth() - event.getDamage() <= 0) {
                        Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                    }

                }

                if (event.getDamager() instanceof Projectile) {
                    Projectile pro = (Projectile) event.getDamager();
                    if (pro.getShooter() instanceof Player) {
                        Player damager = (Player) pro.getShooter();
                        if (GameUtilities.getUtilities().teams.get(damaged.getName()).equalsIgnoreCase(GameUtilities.getUtilities().teams.get(damager.getName()))) {
                            event.setCancelled(true);
                            return;
                        }

                        if (damager.getItemInHand() != null) {
                            damager.getItemInHand().setDurability((short) -100);
                        }

                        if (pro instanceof Arrow) {
                            damager.playSound(damager.getLocation(), Sound.ORB_PICKUP, 60f, 0f);
                        }

                        if (damaged.getHealth() - event.getDamage() <= 0) {
                            Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damager));
                            event.setCancelled(true);
                        }
                    }
                }
                damaged.damage(event.getDamage());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(final TF2DeathEvent event) {
        GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
            @Override
            public void run() {
                final Player player = event.getPlayer();
                final Player killer = event.getKiller();

                player.teleport(MapUtilities.getUtilities().loadTeamSpawn(GameUtilities.getUtilities().ingame.get(player.getName()), GameUtilities.getUtilities().teams.get(player.getName())));
                player.sendMessage("§e[TF2] You were killed by " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " §r§e(" + ClassUtilities.getUtilities().classes.get(killer.getName()) + ")!");
                killer.sendMessage("§e[TF2] You killed " + GameUtilities.getUtilities().getTeamColor(player) + player.getName() + " §r§e(" + ClassUtilities.getUtilities().classes.get(player.getName()) + ")!");
                killer.playSound(killer.getLocation(), Sound.valueOf(GameUtilities.getUtilities().plugin.getConfig().getString("killsound.sound")), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.volume"), GameUtilities.getUtilities().plugin.getConfig().getInt("killsound.pitch"));

                GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                    @Override
                    public void run() {
                        GameUtilities.getUtilities().justspawned.remove(player.getName());
                    }
                }, 40L);

                GameUtilities.getUtilities().kills.put(player.getName(), 0);
                Integer kills = GameUtilities.getUtilities().kills.get(killer.getName());
                Integer tkills = GameUtilities.getUtilities().totalkills.get(killer.getName());

                if (tkills == null) {
                    GameUtilities.getUtilities().totalkills.put(killer.getName(), 1);
                } else {
                    GameUtilities.getUtilities().totalkills.put(killer.getName(), tkills + 1);
                }

                if (kills != null && kills != 0) {
                    GameUtilities.getUtilities().kills.put(killer.getName(), kills + 1);
                    if ((kills + 1) % GameUtilities.getUtilities().plugin.getConfig().getInt("killstreaks") == 0) {
                        GameUtilities.getUtilities().broadcast(GameUtilities.getUtilities().getCurrentMap(killer), "§e[TF2] " + GameUtilities.getUtilities().getTeamColor(killer) + killer.getName() + " §r§eis on a §4§l" + (kills + 1) + " §r§ekill streak!");
                    } else {
                        killer.sendMessage("§e[TF2] You have made " + (kills + 1) + " kills!");
                    }

                } else {
                    GameUtilities.getUtilities().kills.put(killer.getName(), 1);
                }
                player.setHealth(20);
                player.updateInventory();
                ClassUtilities.getUtilities().changeClass(player, ClassUtilities.getUtilities().classes.get(player.getName()));
                GameUtilities.getUtilities().justspawned.add(player.getName());
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPotionSplash(PotionSplashEvent event) {
        try {
            for (LivingEntity e : event.getAffectedEntities()) {
                if (e instanceof Player) {
                    Player damaged = (Player) e;
                    if (event.getPotion().getShooter() != null && event.getPotion().getShooter() instanceof Player) {
                        Player throwee = (Player) event.getPotion().getShooter();
                        if (GameUtilities.getUtilities().isIngame(throwee) && GameUtilities.getUtilities().isIngame(damaged)) {
                            if (GameUtilities.getUtilities().getTeam(throwee).equalsIgnoreCase(GameUtilities.getUtilities().getTeam(damaged))) {
                                e.setNoDamageTicks(1);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLoseHunger(FoodLevelChangeEvent event) {
        if (GameUtilities.getUtilities().ingame.containsKey(event.getEntity().getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getPlayer())) {
            Player player = event.getPlayer();
            String name = player.getName();
            String map = GameUtilities.getUtilities().getCurrentMap(player);
            GameUtilities.getUtilities().leaveCurrentGame(player);
            Queue q = GameUtilities.getUtilities().plugin.getQueue(map);
            q.remove(name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event) {
        if (!(event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())) {
            return;
        }

        Player player = event.getPlayer();
        Block b = event.getTo().getBlock();
        TF2 plugin = GameUtilities.getUtilities().plugin;

        // Capture Points
        if ((b.getType() == Material.STONE_PLATE || b.getType() == Material.WOOD_PLATE) && GameUtilities.getUtilities().isIngame(player) && CapturePointUtilities.getUtilities().locationIsCapturePoint(b.getLocation())) {
            Map map = plugin.getMap(CapturePointUtilities.getUtilities().getMapFromLocation(b.getLocation()));
            Integer id = CapturePointUtilities.getUtilities().getIDFromLocation(b.getLocation());
            CapturePoint cp = map.getCapturePoint(id);
            if (GameUtilities.getUtilities().getTeam(player).equalsIgnoreCase("red")) {
                if (cp.getStatus().string().equalsIgnoreCase("uncaptured")) {
                    if (CapturePointUtilities.getUtilities().capturePointBeforeHasBeenCaptured(map, id)) {
                        if (cp.capturing == null) {
                            cp.startCapturing(player);
                        }
                    } else {
                        event.getPlayer().sendMessage("§e[TF2] You must capture point #" + (id - 1) + " first!");
                    }

                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage("§e[TF2] This point has already been captured! Head to #" + (id + 1) + "!");
                } else if (map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("capturing")) {
                    event.getPlayer().sendMessage("§e[TF2] This point is being captured by " + cp.capturing.getName() + "!");
                }
            } else {
                if (!map.getCapturePoint(id).getStatus().string().equalsIgnoreCase("captured")) {
                    event.getPlayer().sendMessage("§e[TF2] You must be on the §4§lred §r§eteam to capture points! §9§lBlue §r§eis for defending.");
                } else {
                    event.getPlayer().sendMessage("§e[TF2] The §4§lred §r§eteam has already captured this point!");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDrop(PlayerDropItemEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getPlayer())) {
            ItemStack[] armor = event.getPlayer().getInventory().getArmorContents();
            event.getPlayer().sendMessage("§e[TF2] You cannot drop items while in a game!");
            event.setCancelled(true);
            event.getPlayer().getInventory().setArmorContents(armor);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        final Player damaged = e.getEntity();
        if (GameUtilities.getUtilities().isIngame(damaged)) {
            damaged.setHealth(20);
            e.getDrops().clear();
            e.setDeathMessage(null);
            Bukkit.getServer().getPluginManager().callEvent(new TF2DeathEvent(damaged, damaged));
            GameUtilities.getUtilities().plugin.getServer().getScheduler().scheduleSyncDelayedTask(GameUtilities.getUtilities().plugin, new Runnable() {
                @Override
                public void run() {
                    ClassUtilities.getUtilities().changeClass(damaged, ClassUtilities.getUtilities().classes.get(damaged.getName()));
                    damaged.updateInventory();
                }
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getPlayer()) && !event.getMessage().startsWith("/tf2") && !event.getPlayer().hasPermission("tf2.create")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§e[TF2] You cannot use commands while in a game!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNameTag(PlayerReceiveNameTagEvent event) {
        if (GameUtilities.getUtilities().isIngame(event.getNamedPlayer())) {
            if (GameUtilities.getUtilities().getTeam(event.getNamedPlayer()).equalsIgnoreCase("red")) {
                event.setTag(ChatColor.RED + event.getNamedPlayer().getName());
            } else if (GameUtilities.getUtilities().getTeam(event.getNamedPlayer()).equalsIgnoreCase("blue")) {
                event.setTag(ChatColor.BLUE + event.getNamedPlayer().getName());
            }
        }
    }

    // Taste the rainbow.
    public String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([l-ok0-8k9a-f])", "\u00A7$1");
    }

}
