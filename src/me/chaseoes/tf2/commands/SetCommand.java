package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.utilities.IconMenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetCommand {

    @SuppressWarnings("unused")
    private TF2 plugin;
    static SetCommand instance = new SetCommand();

    private SetCommand() {

    }

    public static SetCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execSetCommand(final CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        final Player player = (Player) cs;
        if (strings[1].equalsIgnoreCase("spawn")) {
            if (strings.length == 3) {
                if (strings[2].equalsIgnoreCase("lobby")) {
                    MapUtilities.getUtilities().setLobby(player.getLocation());
                    cs.sendMessage("§e[TF2] Successfully set the global lobby.");
                } else {
                    final String map = strings[2];
                    IconMenu menu = new IconMenu("Map: §l" + map, 9, new IconMenu.OptionClickEventHandler() {
                        @Override
                        public void onOptionClick(IconMenu.OptionClickEvent event) {
                            String name = ChatColor.stripColor(event.getName());
                            if (name.equalsIgnoreCase("Blue Lobby")) {
                                MapUtilities.getUtilities().setTeamLobby(map, "blue", player.getLocation());
                                cs.sendMessage("§e[TF2] Successfully set the blue team's lobby.");
                            } else if (name.equalsIgnoreCase("Red Lobby")) {
                                MapUtilities.getUtilities().setTeamLobby(map, "red", player.getLocation());
                                cs.sendMessage("§e[TF2] Successfully set the red team's lobby.");
                            } else if (name.equalsIgnoreCase("Blue Spawn")) {
                                MapUtilities.getUtilities().setTeamSpawn(map, "blue", player.getLocation());
                                cs.sendMessage("§e[TF2] Successfully set the blue team's spawn.");
                            } else if (name.equalsIgnoreCase("Red Spawn")) {
                                MapUtilities.getUtilities().setTeamSpawn(map, "red", player.getLocation());
                                cs.sendMessage("§e[TF2] Successfully set the red team's spawn.");
                            }
                            event.setWillClose(true);
                        }
                    }, GameUtilities.getUtilities().plugin).setOption(2, new ItemStack(Material.getMaterial(331), 1), "§4§lRed Lobby§r", "§fSet the red team lobby.").setOption(3, new ItemStack(Material.getMaterial(351), 1, (short) 4), "§b§lBlue Lobby§r", "§fSet the blue team lobby.").setOption(4, new ItemStack(Material.WOOL, 1, (short) 14), "§4§lRed Spawn§r", "§fSet the red team's spawn.").setOption(5, new ItemStack(Material.WOOL, 1, (short) 11), "§b§lBlue Spawn§r", "§fSet the blue team's spawn.").setOption(6, new ItemStack(Material.BEDROCK, 1), "§c§lExit§r", "§cExit this menu.");
                    menu.open((Player) cs);
                }
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("playerlimit")) {
            if (strings.length == 4) {
                if (Integer.parseInt(strings[3]) % 2 == 0) {
                    MapUtilities.getUtilities().setPlayerLimit(strings[2], Integer.parseInt(strings[3]));
                    LobbyWall.getWall().update();
                    cs.sendMessage("§e[TF2] Successfully set the playerlimit for " + strings[2] + " to " + strings[3] + ".");
                } else {
                    cs.sendMessage("§e[TF2] Yo! Don't use no odd numbers for dat shit, mofo.");
                }
            }
        } else if (strings[1].equalsIgnoreCase("capturepoint")) {
            if (strings.length == 4) {
                CapturePointUtilities.getUtilities().defineCapturePoint(strings[2], Integer.parseInt(strings[3]), player.getLocation());
                cs.sendMessage("§e[TF2] Successfully defined capturepoint ID #" + strings[3] + " for the map " + strings[2] + ".");
            }
        } else if (strings[1].equalsIgnoreCase("timelimit")) {
            if (strings.length == 4) {
                MapUtilities.getUtilities().setTimeLimit(strings[2], Integer.parseInt(strings[3]));
                cs.sendMessage("§e[TF2] Successfully set the timelimit for " + strings[2] + " to " + strings[3] + ".");
            }
        } else if (strings[1].equalsIgnoreCase("redtp")) {
            if (strings.length == 4) {
                MapConfiguration.getMaps().getMap(strings[2]).set("teleport-red-team", Integer.parseInt(strings[3]));
                MapConfiguration.getMaps().saveMap(strings[2]);
                cs.sendMessage("§e[TF2] Successfully set the red teleport time for " + strings[2] + " to " + strings[3] + ".");
            }
        } else {
            h.unknownCommand();
        }
    }

}
