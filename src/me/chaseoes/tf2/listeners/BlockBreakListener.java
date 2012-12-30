package me.chaseoes.tf2.listeners;

import java.util.List;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        Player player = event.getPlayer();
        if (b.getType() == Material.STONE_BUTTON && player.hasPermission("tf2.create")) {
            for (String s : DataConfiguration.getData().getDataFile().getStringList("classbuttons")) {
                if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(b.getLocation().toString())) {
                    List<String> buttons = DataConfiguration.getData().getDataFile().getStringList("classbuttons");
                    buttons.remove(s);
                    DataConfiguration.getData().getDataFile().set("classbuttons", buttons);
                    DataConfiguration.getData().saveData();
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully removed class button.");
                }
            }
            
            for (String s : DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons")) {
                if (ClassUtilities.getUtilities().loadClassButtonLocation(s).toString().equalsIgnoreCase(b.getLocation().toString())) {
                    List<String> buttons = DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons");
                    buttons.remove(s);
                    DataConfiguration.getData().getDataFile().set("changeclassbuttons", buttons);
                    DataConfiguration.getData().saveData();
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully removed changeclass button.");
                }
            }
        }
        
        if (b.getType() == Material.WALL_SIGN) {
            Sign s = (Sign) b.getState();
            if (s.getLine(0).equalsIgnoreCase("Team Fortress 2") && s.getLine(1).equalsIgnoreCase("Click here")) {
                String map = ChatColor.stripColor(s.getLine(3));
                if (b.getLocation().toString().equalsIgnoreCase(LobbyWallUtilities.getUtilities().loadSignLocation(map).toString())) {
                    DataConfiguration.getData().getDataFile().set("lobbywall." + map, null);
                    DataConfiguration.getData().saveData();
                    player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully removed lobby wall.");
                }
            }
        }
    }

}
