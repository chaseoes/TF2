package com.chaseoes.tf2.commands;


import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.GamePlayer;
import com.chaseoes.tf2.GameUtilities;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.Localizer;
import com.chaseoes.tf2.utilities.SerializableLocation;
import com.sk89q.worldedit.EmptyClipboardException;

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
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAPNAME-TOO-LONG").replace("%map", strings[2]));
                    return;
                }
                if (plugin.mapExists(map)) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("ALREADY-EXISTS-MAP").replace("%map", strings[2]));
                    return;
                }
                try {
                    MapUtilities.getUtilities().createMap(map, p);
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-SUCCESSFULLY-CREATED").replace("%map", strings[2]));
                } catch (EmptyClipboardException e) {
                    cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("WORLDEDIT-NO-REGION"));
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
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CLASS-CREATE").replace("%type", strings[2]).replace("%class", classname));
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
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("BUTTON-CHANGE-CLASS-CREATE"));
            } else {
                h.wrongArgs("/tf2 create changeclassbutton");
            }
        } else if (strings[1].equalsIgnoreCase("refillcontainer")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                GameUtilities.getUtilities().getGamePlayer(p).setCreatingContainer(true);
                String map = strings[2];
                if (!plugin.mapExists(map)) {
                    p.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                    return;
                }
                GameUtilities.getUtilities().getGamePlayer(p).setMapCreatingItemFor(map);
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CONTAINER-CREATE"));
            } else {
                h.wrongArgs("/tf2 create refillcontainer <map>");
            }
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
                        cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CLASSCHEST-CREATE"));
                        return;
                    }
                }
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("CLASSCHEST-NO-CHEST"));
            } else {
                h.wrongArgs("/tf2 create classchest <class name>");
            }
        }

        else {
            h.unknownCommand();
        }
    }

}
