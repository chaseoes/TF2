package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.*;
import com.chaseoes.tf2.localization.Localizers;
import com.chaseoes.tf2.utilities.SerializableLocation;
import com.chaseoes.tf2.utilities.WorldEditUtilities;
import com.sk89q.worldedit.EmptyClipboardException;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

public class CreateCommand {

    private TF2 plugin;
    static CreateCommand instance = new CreateCommand();

    private CreateCommand() {

    }

    public static CreateCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execCreateCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            h.wrongArgs("/tf2 create <map|classbutton|changeclassbutton|refillcontainer>");
        } else if (strings[1].equalsIgnoreCase("map")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                String map = strings[2];
                if (map.length() > 14) {
                    Localizers.getDefaultLoc().MAPNAME_TOO_LONG.sendPrefixed(cs, strings[2]);
                    return;
                }
                if (plugin.mapExists(map)) {
                    Localizers.getDefaultLoc().MAP_ALREADY_EXISTS.sendPrefixed(cs, strings[2]);
                    return;
                }
                try {
                    MapUtilities.getUtilities().createMap(map, p);
                    Localizers.getDefaultLoc().MAP_SUCCESSFULLY_CREATED.sendPrefixed(cs, strings[2]);
                } catch (EmptyClipboardException e) {
                    Localizers.getDefaultLoc().WORLDEDIT_NO_REGION.sendPrefixed(cs);
                }
            } else {
                h.wrongArgs("/tf2 create map <name>");
            }
        } else if (strings[1].equalsIgnoreCase("classbutton")) {
            if (strings.length == 4 && (strings[2].equalsIgnoreCase("normal") || strings[2].equalsIgnoreCase("donator"))) {
                Player p = (Player) cs;
                GamePlayer gp = GameUtilities.getUtilities().getGamePlayer(p);
                String classname = strings[3];
                gp.setMakingClassButton(true);
                gp.setClassButtonName(classname);
                gp.setClassButtonType(strings[2]);
                if (p.getGameMode() == GameMode.CREATIVE) {
                    if (!p.getInventory().contains(Material.STONE_BUTTON)) {
                        p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                    }
                } else {
                    p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                }
                Localizers.getDefaultLoc().BUTTON_CLASS_CREATE.sendPrefixed(cs, strings[2], classname);
            } else {
                h.wrongArgs("/tf2 create classbutton [normal|donator] <class>");
            }
        } else if (strings[1].equalsIgnoreCase("changeclassbutton")) {
            if (strings.length == 2) {
                Player p = (Player) cs;
                GameUtilities.getUtilities().getGamePlayer(p).setMakingChangeClassButton(true);
                if (p.getGameMode() == GameMode.CREATIVE) {
                    if (!p.getInventory().contains(Material.STONE_BUTTON)) {
                        p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                    }
                } else {
                    p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                }
                Localizers.getDefaultLoc().BUTTON_CHANGE_CLASS_CREATE.sendPrefixed(cs);
            } else {
                h.wrongArgs("/tf2 create changeclassbutton");
            }
        } else if (strings[1].equalsIgnoreCase("refillcontainer")) {
            Player p = (Player) cs;
            String map;
            if (strings.length == 2) {
                Map m = WorldEditUtilities.getWEUtilities().getMap(p.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(p);
                    h.wrongArgs("/tf2 create refillcontainer [map]");
                    return;
                }
                map = m.getName();
            } else {
                map = strings[2];
            }
            GameUtilities.getUtilities().getGamePlayer(p).setCreatingContainer(true);
            if (!plugin.mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(p, map);
                return;
            }
            GameUtilities.getUtilities().getGamePlayer(p).setMapCreatingItemFor(map);
            Localizers.getDefaultLoc().CONTAINER_CREATE.sendPrefixed(p);
        } else if (strings[1].equalsIgnoreCase("classchest")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                String className = strings[2];
                BlockIterator bi = new BlockIterator(p, 5);
                while (bi.hasNext()) {
                    Block b = bi.next();
                    if (b.getType() != Material.AIR) {
                        DataConfiguration.getData().getDataFile().set("class-chest-locations." + className, SerializableLocation.locationToString(b.getLocation()));
                        DataConfiguration.getData().saveData();
                        Localizers.getDefaultLoc().CLASS_CHEST_CREATE.sendPrefixed(cs);
                        return;
                    }
                }
                Localizers.getDefaultLoc().CLASS_CHEST_NO_CHEST.sendPrefixed(cs);
            } else {
                h.wrongArgs("/tf2 create classchest <class name>");
            }
        } else {
            h.unknownCommand();
        }
    }

}
