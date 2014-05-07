package com.chaseoes.tf2.utilities;

import java.util.List;

import org.bukkit.entity.Player;

import com.chaseoes.tf2.Map;
import com.chaseoes.tf2.TF2;

public class GeneralUtilities {

    public static String colorize(String s) {
        if (s == null) {
            return null;
        }
        return s.replaceAll("&([l-ok0-8k9a-f])", "\u00A7$1");
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static void runCommands(String option, Player player, Player killer, Map map) {
        List<String> commands = TF2.getInstance().getConfig().getStringList("run-commands." + option);
        if (!commands.contains("null")) {
            for (String command : commands) {
                TF2.getInstance().getServer().dispatchCommand(TF2.getInstance().getServer().getConsoleSender(), replaceVariables(command, player, killer, map));
            }
        }
    }

    private static String replaceVariables(String message, Player player, Player killer, Map map) {
        return message.replace("%player", player.getName()).replace("%killer", killer.getName()).replace("%map_name", map.getName());
    }

}
