package com.chaseoes.tf2.listeners;

import com.chaseoes.tf2.localization.Localizers;
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

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.Game;
import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.Team;
import com.chaseoes.tf2.classes.ClassUtilities;
import com.chaseoes.tf2.classes.TF2Class;
import com.chaseoes.tf2.commands.SpectateCommand;
import com.chaseoes.tf2.utilities.DataChecker;
import com.chaseoes.tf2.utilities.GeneralUtilities;

public class PlayerInteractListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        try {
            Player player = event.getPlayer();
            GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(player);
            if (gp.isIngame()) {
                if (player.getItemInHand().getType() == Material.getMaterial(373) && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
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
                if (s.getLine(0).equalsIgnoreCase(Localizers.getDefaultLoc().LOBBYWALL_JOIN_1.getString()) && s.getLine(2).equalsIgnoreCase(Localizers.getDefaultLoc().LOBBYWALL_JOIN_3.getString())) {
                    String map = ChatColor.stripColor(s.getLine(3));
                    Game game = GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map));
                    Team team = game.decideTeam();

                    DataChecker dc = new DataChecker(map);
                    if (!dc.allGood()) {
                        Localizers.getDefaultLoc().MAP_NOT_SETUP.sendPrefixed(player);
                        if (player.hasPermission("tf2.create")) {
                            Localizers.getDefaultLoc().MAP_NOT_SETUP_COMMAND_HELP.sendPrefixed(player, map);
                        }
                        return;
                    }

                    if (!player.hasPermission("tf2.play")) {
                        Localizers.getDefaultLoc().NO_PERMISSION.sendPrefixed(event.getPlayer());
                        return;
                    }

                    if (gp.isIngame()) {
                        Localizers.getDefaultLoc().PLAYER_ALREADY_PLAYING.sendPrefixed(event.getPlayer());
                        return;
                    }

                    if (SpectateCommand.getCommand().isSpectating(gp.getPlayer())) {
                        Localizers.getDefaultLoc().PLAYER_ALREADY_SPECTATING.sendPrefixed(event.getPlayer());
                        return;
                    }

                    if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                        Localizers.getDefaultLoc().MAP_INFO_DISABLED.sendPrefixed(player);
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
                    Localizers.getDefaultLoc().CONTAINER_ALREADY_REGSITERED.sendPrefixed(player);
                } else {
                    Map map = TF2.getInstance().getMap(gp.getMapCreatingItemFor());
                    map.addContainer(event.getClickedBlock().getLocation(), ((InventoryHolder) event.getClickedBlock().getState()).getInventory());
                    Localizers.getDefaultLoc().CONTAINER_CREATED.sendPrefixed(player);
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
