package me.chaseoes.tf2.commands;

import com.sk89q.worldedit.EmptyClipboardException;
import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Place the button to create a " + strings[2] + " class button for the class " + classname + ".");
            } else {
                h.wrongArgs();
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
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Place the button to create a changeclass button.");
            } else {
                h.wrongArgs();
            }
        } else if (strings[1].equalsIgnoreCase("refillcontainer")) {
            if (strings.length == 3) {
                Player p = (Player) cs;
                GameUtilities.getUtilities().getGamePlayer(p).setCreatingContainer(true);
                String map = strings[2];
                if (TF2.getInstance().getMap(map) == null) {
                    p.sendMessage(ChatColor.YELLOW + "[TF2] " + ChatColor.ITALIC + map + ChatColor.RESET + ChatColor.YELLOW + " is not a valid map name.");
                    return;
                }
                GameUtilities.getUtilities().getGamePlayer(p).setMapCreatingItemFor(map);
                cs.sendMessage(ChatColor.YELLOW + "[TF2] Right click a container to make it refill each game.");
            } else {
                h.wrongArgs();
            }
        }

        else {
            h.unknownCommand();
        }
    }

}
