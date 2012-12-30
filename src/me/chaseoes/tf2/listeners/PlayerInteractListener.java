package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.Queue;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.utilities.DataChecker;
import me.chaseoes.tf2.utilities.GeneralUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            if (GameUtilities.getUtilities().isIngame(player)) {
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
                if (player.getItemInHand().getType() == Material.getMaterial(373) || player.getItemInHand().getType() == Material.BOW) {
                    if (gp.justSpawned()) {
                        event.setCancelled(true);
                        player.updateInventory();
                    }
                    if (gp.isInLobby()) {
                        event.setCancelled(true);
                        player.updateInventory();
                    }
                }
            }

            if (event.getPlayer().isSneaking()) {
                return;
            }

            if (event.hasBlock() && (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST)) {
                Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase("Team Fortress 2") && s.getLine(2).equalsIgnoreCase("to join:")) {
                    String map = ChatColor.stripColor(s.getLine(3));
                    String team = GameUtilities.getUtilities().decideTeam(map);
                    DataChecker dc = new DataChecker(map);
                    if (!dc.allGood()) {
                        player.sendMessage(ChatColor.YELLOW + "[TF2] This map has not yet been setup.");
                        if (player.hasPermission("tf2.create")) {
                            player.sendMessage(ChatColor.YELLOW + "[TF2] Type " + ChatColor.GOLD + "/tf2 checkdata " + map + " " + ChatColor.YELLOW + "to see what else needs to be done.");
                        }
                        return;
                    }
                    if (!player.hasPermission("tf2.play")) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You do not have permission.");
                        return;
                    }

                    if (GameUtilities.getUtilities().isIngame(player)) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You are already playing on a map!");
                        return;
                    }

                    if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                        player.sendMessage(ChatColor.YELLOW + "[TF2] That map is disabled.");
                        return;
                    }

                    Queue q = GameUtilities.getUtilities().plugin.getQueue(map);
                    if (!player.hasPermission("tf2.create")) {
                        if (q.contains(player)) {
                            player.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + q.getPosition(player.getName()) + " in line for this map.");
                            return;
                        }
                        q.add(player);
                        Integer position = q.getPosition(player.getName());

                        if (GameUtilities.getUtilities().getIngameList(map).size() + 1 <= TF2.getInstance().getMap(map).getPlayerlimit()) {
                            q.remove(position);
                            GameUtilities.getUtilities().joinGame(player, map, team);
                        } else {
                            player.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + position + " in line for this map.");
                        }
                    } else {
                        GameUtilities.getUtilities().joinGame(player, map, team);
                    }

                    event.setCancelled(true);
                }
            }

            if (event.hasBlock() && (event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
                if (GameUtilities.getUtilities().isIngame(player)) {
                    GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
                    for (String s : DataConfiguration.getData().getDataFile().getStringList("classbuttons")) {
                        if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(event.getClickedBlock().getLocation().toString())) {
                            if (player.hasPermission("tf2.button." + ClassUtilities.getUtilities().loadClassButtonTypeFromLocation(s))) {
                                TF2Class c = new TF2Class(ClassUtilities.getUtilities().loadClassFromLocation(s));
                                if (c.apply(player)) {
                                    if (gp.isUsingChangeClassButton()) {
                                        player.teleport(MapUtilities.getUtilities().loadTeamSpawn(gp.getGame().getName(), Team.match(GameUtilities.getUtilities().getTeam(player))));
                                        gp.setInLobby(false);
                                        gp.setUsingChangeClassButton(false);
                                    }
                                }
                                return;
                            }
                            event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] " + GeneralUtilities.colorize(GameUtilities.getUtilities().plugin.getConfig().getString("donor-button-noperm")));
                        }
                    }

                    for (String s : DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons")) {
                        if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(event.getClickedBlock().getLocation().toString())) {
                            gp.setInLobby(true);
                            gp.setUsingChangeClassButton(true);
                            event.getPlayer().teleport(MapUtilities.getUtilities().loadTeamLobby(GameUtilities.getUtilities().getCurrentMap(player), Team.match(GameUtilities.getUtilities().getTeam(player))));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
