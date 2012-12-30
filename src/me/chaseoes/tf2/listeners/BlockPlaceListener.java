package me.chaseoes.tf2.listeners;

import java.util.List;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.GameUtilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (GameUtilities.getUtilities().makingclassbutton.containsKey(player.getName()) && event.getBlockPlaced().getType() == Material.STONE_BUTTON) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("classbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ() + "." + GameUtilities.getUtilities().makingclassbuttontype.get(player.getName()) + "." + GameUtilities.getUtilities().makingclassbutton.get(player.getName()));
            DataConfiguration.getData().saveData();
            DataConfiguration.getData().getDataFile().set("classbuttons", classbs);
            DataConfiguration.getData().saveData();
            player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully made a " + GameUtilities.getUtilities().makingclassbuttontype.get(player.getName()) + " class button for the class " + ChatColor.ITALIC + GameUtilities.getUtilities().makingclassbutton.get(player.getName()) + ChatColor.RESET + "" + ChatColor.YELLOW + ".");
            GameUtilities.getUtilities().makingclassbutton.remove(player.getName());
            GameUtilities.getUtilities().makingclassbuttontype.remove(player.getName());
        }

        if (GameUtilities.getUtilities().makingchangeclassbutton.contains(player.getName()) && event.getBlockPlaced().getType() == Material.STONE_BUTTON) {
            List<String> classbs = DataConfiguration.getData().getDataFile().getStringList("changeclassbuttons");
            classbs.add(event.getBlockPlaced().getWorld().getName() + "." + event.getBlockPlaced().getLocation().getBlockX() + "." + event.getBlockPlaced().getLocation().getBlockY() + "." + event.getBlockPlaced().getLocation().getBlockZ());
            DataConfiguration.getData().getDataFile().set("changeclassbuttons", classbs);
            GameUtilities.getUtilities().makingchangeclassbutton.remove(player.getName());
            DataConfiguration.getData().saveData();
            player.sendMessage(ChatColor.YELLOW + "[TF2] Successfully made a change class button.");
        }
    }

}
