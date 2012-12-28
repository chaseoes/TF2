package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand {

    private TF2 plugin;
    static ListCommand instance = new ListCommand();

    private ListCommand() {

    }

    public static ListCommand getCommand() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void execListCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        if (strings.length == 2) {
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            for (String pl : GameUtilities.getUtilities().getIngameList(strings[1])) {

                if (!GameUtilities.getUtilities().coolpeople.contains(pl)) {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(pl).append(", ");
                    } else {
                        blue.append(pl).append(", ");
                    }
                } else {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append("§4§l").append(pl).append("§r§f, ");
                    } else {
                        blue.append("§4§l").append(pl).append("§r§f, ");
                    }
                }

            }
            
            cs.sendMessage("§e[TF2] Displaying players in the map §l" + strings[1] + "§r§e:");
            cs.sendMessage("§4§lRed team: §r\n" + red.toString());
            cs.sendMessage("§9§lBlue team: §r\n" + blue.toString());
        } else if (GameUtilities.getUtilities().isIngame((Player) cs)) {
            String map = GameUtilities.getUtilities().getCurrentMap((Player) cs);
            StringBuilder red = new StringBuilder();
            StringBuilder blue = new StringBuilder();
            Integer redc = 0;
            Integer bluec = 0;
            for (String pl : GameUtilities.getUtilities().getIngameList(map)) {
                if (!GameUtilities.getUtilities().coolpeople.contains(pl)) {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append(pl).append(", ");
                        redc++;
                    } else {
                        blue.append(pl).append(", ");
                        bluec++;
                    }
                } else {
                    if (GameUtilities.getUtilities().getTeam(plugin.getServer().getPlayerExact(pl)).equalsIgnoreCase("red")) {
                        red.append("§4§l").append(pl).append("§r§f, ");
                        redc++;
                    } else {
                        blue.append("§4§l").append(pl).append("§r§f, ");
                        bluec++;
                    }
                }

            }
            
            cs.sendMessage("§e[TF2] Displaying players in the map §l" + map + "§r§e:");
            cs.sendMessage("§4§lRed team (" + redc + "): §r\n" + red.toString());
            cs.sendMessage("§9§lBlue team (" + bluec + "): §r\n" + blue.toString());
        } else {
            h.wrongArgs();
        }
    }

}
