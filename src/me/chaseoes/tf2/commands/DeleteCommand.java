package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteCommand {

    @SuppressWarnings("unused")
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
        if (strings[1].equalsIgnoreCase("map")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                Map map = TF2.getInstance().getMap(strings[2]);
                if (map == null) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[2] + " is not a valid map name.");
                } else {
                    TF2.getInstance().removeMap(strings[2]);
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully deleted " + strings[2]);
                }
            } else {
                h.wrongArgs();
            }
        } else {
            h.unknownCommand();
        }
    }

}
