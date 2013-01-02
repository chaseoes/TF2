package me.chaseoes.tf2.commands;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.chaseoes.tf2.Map;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.lobbywall.WorldEditUtilities;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if(strings.length == 2) {
            Map map = plugin.getMap(strings[1]);
            if(map == null){
                cs.sendMessage(ChatColor.YELLOW + "[TF2] " + strings[1] + " is not a valid map.");
                return;
            }
            Player player = (Player) cs;
            try {
                Selection sel = WorldEditUtilities.getWorldEdit().getSelection(player);
                Location b1 = new Location(player.getWorld(), sel.getNativeMinimumPoint().getBlockX(), sel.getNativeMinimumPoint().getBlockY(), sel.getNativeMinimumPoint().getBlockZ());
                Location b2 = new Location(player.getWorld(), sel.getNativeMaximumPoint().getBlockX(), sel.getNativeMaximumPoint().getBlockY(), sel.getNativeMaximumPoint().getBlockZ());
                map.setP1(b1);
                map.setP2(b2);
                player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully redefined " + map.getName() + "'s region.");
            } catch (Exception ex) {
                player.sendMessage(ChatColor.YELLOW + "[TF2] You need to select a region.");
            }

        } else {
            h.wrongArgs();
        }
    }
}
