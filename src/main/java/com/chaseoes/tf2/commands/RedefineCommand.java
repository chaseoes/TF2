package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.localization.Localizers;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.utilities.WorldEditUtilities;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class RedefineCommand {

    private TF2 plugin;
    static RedefineCommand instance = new RedefineCommand();

    private RedefineCommand() {
    }

    public static RedefineCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execRedefineCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 2) {
            if (!TF2.getInstance().mapExists(strings[1])) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, strings[1]);
                return;
            }
            Map map = plugin.getMap(strings[1]);
            Player player = (Player) cs;
            try {
                Selection sel = WorldEditUtilities.getWorldEdit().getSelection(player);
                Location b1 = new Location(player.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
                Location b2 = new Location(player.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
                map.setP1(b1);
                map.setP2(b2);
                Localizers.getDefaultLoc().MAP_SUCCESSFULLY_REDEFINED.sendPrefixed(player, strings[1]);
            } catch (Exception ex) {
                Localizers.getDefaultLoc().WORLDEDIT_NO_REGION.sendPrefixed(player);
            }

        } else {
            h.wrongArgs("/tf2 redefine <map>");
        }
    }
}
