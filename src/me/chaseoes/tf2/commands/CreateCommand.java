package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldedit.EmptyClipboardException;

public class CreateCommand {

    @SuppressWarnings("unused")
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
        if (strings[1].equalsIgnoreCase("map")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                try {
                    MapUtilities.getUtilities().createMap(strings[2], p);
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Successfully created the map " + ChatColor.ITALIC + strings[2] + "!");
                } catch (EmptyClipboardException e) {
                    cs.sendMessage(ChatColor.YELLOW + "[TF2] Please make a WorldEdit selection first.");
                }
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("classbutton")) {
            if (strings.length == 4 && (strings[2].equalsIgnoreCase("normal") || strings[2].equalsIgnoreCase("donator"))) {
                Player p = (Player) cs;
                String classname = strings[3];
                GameUtilities.getUtilities().makingclassbutton.put(p.getName(), classname);
                GameUtilities.getUtilities().makingclassbuttontype.put(p.getName(), strings[2]);
                p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Place the button to create a " + strings[2] + " class button for the class " + classname + ".");
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("changeclassbutton")) {
            if (strings.length == 2) {
                Player p = (Player) cs;
                GameUtilities.getUtilities().makingchangeclassbutton.add(p.getName());
                p.getInventory().addItem(new ItemStack(Material.STONE_BUTTON));
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Place the button to create a changeclass button.");
            } else {
                h.wrongArgs();
            }
        } else {
            h.unknownCommand();
        }
    }

}
