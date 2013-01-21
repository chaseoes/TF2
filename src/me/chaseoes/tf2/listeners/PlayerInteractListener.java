package me.chaseoes.tf2.listeners;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.Game;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.commands.SpectateCommand;
import me.chaseoes.tf2.utilities.DataChecker;
import me.chaseoes.tf2.utilities.GeneralUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class PlayerInteractListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
            if (gp.isIngame()) {
                if ((player.getItemInHand().getType() == Material.getMaterial(373)) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
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
                    Game game = GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map));
                    Team team = game.decideTeam();
                    
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

                    if (gp.isIngame()) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You are already playing on a map!");
                        return;
                    }

                    if (SpectateCommand.getCommand().isSpectating(gp.getPlayer())) {
                        event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You are already spectating a game.");
                        return;
                    }

                    if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                        player.sendMessage(ChatColor.YELLOW + "[TF2] That map is disabled.");
                        return;
                    }

                    game.joinGame(GameUtilities.getUtilities().getGamePlayer(player), team);
                    event.setCancelled(true);
                }
            }

            if (event.hasBlock() && (event.getClickedBlock().getType() == Material.STONE_BUTTON || event.getClickedBlock().getType() == Material.WOOD_BUTTON)) {
                if (gp.isIngame()) {
                    for (String s : DataConfiguration.getData().getDataFile().getStringList("classbuttons")) {
                        if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(event.getClickedBlock().getLocation().toString())) {
                            if (player.hasPermission("tf2.button." + ClassUtilities.getUtilities().loadClassButtonTypeFromLocation(s))) {
                                TF2Class c = new TF2Class(ClassUtilities.getUtilities().loadClassFromLocation(s));
                                if (c.apply(gp)) {
                                    if (gp.isUsingChangeClassButton()) {
                                        player.teleport(MapUtilities.getUtilities().loadTeamSpawn(gp.getGame().getMapName(), gp.getTeam()));
                                        gp.setInLobby(false);
                                        gp.setUsingChangeClassButton(false);
                                        TF2Class classChosen = gp.getCurrentClass();
                                        classChosen.apply(gp);
                                    }
                                }
                                return;
                            }
                            event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] " + GeneralUtilities.colorize(TF2.getInstance().getConfig().getString("donor-button-noperm")));
                        }
                    }

                    for (String s : DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons")) {
                        if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(event.getClickedBlock().getLocation().toString())) {
                            gp.setInLobby(true);
                            gp.setUsingChangeClassButton(true);
                            event.getPlayer().teleport(MapUtilities.getUtilities().loadTeamLobby(GameUtilities.getUtilities().getGamePlayer(player).getCurrentMap(), gp.getTeam()));
                        }
                    }
                }
            }

            if (event.hasBlock() && event.getClickedBlock().getState() instanceof InventoryHolder && gp.isCreatingContainer()) {
                if (TF2.getInstance().getMap(gp.getMapCreatingItemFor()).isContainerRegistered(event.getClickedBlock().getLocation())) {
                    player.sendMessage(ChatColor.YELLOW + "[TF2] This container is already registered.");
                } else {
                    Map map = TF2.getInstance().getMap(gp.getMapCreatingItemFor());
                    map.addContainer(event.getClickedBlock().getLocation(), ((InventoryHolder) event.getClickedBlock().getState()).getInventory());
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully registered container.");
                }
                gp.setCreatingContainer(false);
                gp.setMapCreatingItemFor(null);
                event.setCancelled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
