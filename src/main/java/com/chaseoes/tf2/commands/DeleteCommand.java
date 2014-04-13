package com.chaseoes.tf2.commands;

import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.localization.Localizers;
import com.chaseoes.tf2.utilities.WorldEditUtilities;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand {

    private TF2 plugin;
    static DeleteCommand instance = new DeleteCommand();

    private DeleteCommand() {

    }

    public static DeleteCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execDeleteCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 1) {
            if (cs instanceof Player) {
                h.wrongArgs("/tf2 delete map [map]");
            } else {
                h.wrongArgs("/tf2 delete map <map>");
            }
            return;
        }
        if (strings[1].equalsIgnoreCase("map")) {
            String map;
            if (strings.length == 2 && cs instanceof Player) {
                Player p = (Player) cs;
                Map m = WorldEditUtilities.getWEUtilities().getMap(p.getLocation());
                if (m == null) {
                    Localizers.getDefaultLoc().PLAYER_NOT_IN_MAP.sendPrefixed(p);
                    h.wrongArgs("/tf2 delete map [map]");
                    return;
                }
                map = m.getName();
            } else if (strings.length == 2) {
                h.wrongArgs("/tf2 delete map <map>");
                return;
            } else {
                map = strings[2];
            }
            if (!plugin.mapExists(map)) {
                Localizers.getDefaultLoc().MAP_DOES_NOT_EXIST.sendPrefixed(cs, map);
                return;
            }
            plugin.removeMap(map);
            Localizers.getDefaultLoc().MAP_SUCCESSFULLY_DELETED.sendPrefixed(cs, map);
        } else {
            h.wrongArgs("/tf2 delete map [map]");
        }
    }

}
