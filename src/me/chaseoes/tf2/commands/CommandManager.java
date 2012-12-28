package me.chaseoes.tf2.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;

import me.chaseoes.tf2.GameUtilities;
import me.chaseoes.tf2.utilities.IconMenu;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {

        CommandHelper h = new CommandHelper(cs, cmnd);

        if (strings.length == 0) {
            cs.sendMessage("§3Team Fortress 2 Plugin by chaseoes.");
            return true;
        }

        if (strings[0].equalsIgnoreCase("help")) {
            if (strings.length == 2 && strings[1].equalsIgnoreCase("3")) {
                cs.sendMessage("§b[§6-----------§b] §3TF2 Help §b3 §3(§7/tf2 help [page #]§3) §b[§6-----------§b]");
                cs.sendMessage("§c/tf2 set spawn lobby§7: Sets the global lobby spawn (no team).");
                cs.sendMessage("§b/tf2 join <map id> [team]§7: Join a map. If a team is not provided, you will be placed randomly.");
                cs.sendMessage("§b/tf2 leave§7: Leave the current game.");
                cs.sendMessage("§b/tf2 list [map]§7: List players currently in the game.");
                return true;
            } else if (strings.length == 2 && strings[1].equalsIgnoreCase("2")) {
                cs.sendMessage("§b[§6-----------§b] §3TF2 Help §b2 §3(§7/tf2 help [page #]§3) §b[§6-----------§b]");
                cs.sendMessage("§c/tf2 set timelimit <map id> <#>§7: Sets the time limit for a map (# must be in seconds).");
                cs.sendMessage("§c/tf2 set capturepoint <map id> <#>§7: Defines capture point number <#> on the pressure plate you're standing on.");
                cs.sendMessage("§c/tf2 set spawn <redlobby | bluelobby> <map id>§7: Sets the lobby spawn for the given team.");
                cs.sendMessage("§c/tf2 set spawn <redteam | blueteam> <map id>§7: Sets the spawn for the given team.");
                return true;
            } else {
                cs.sendMessage("§b[§6-----------§b] §3TF2 Help §b1 §3(§f/tf2 help [page #]§3) §b[§6-----------§b]");
                cs.sendMessage("§c/tf2 create map <id>§7: Creates a map, <id> being the name. A WorldEdit selection is required.");
                cs.sendMessage("§c/tf2 create classbutton <normal | donator> <class>§7: Gives you a button, which when placed, becomes a class button.");
                cs.sendMessage("§c/tf2 create changeclassbutton§7: Gives you a button, which when placed, becomes a change class button.");
                cs.sendMessage("§c/tf2 set playerlimit <map id> <#>§7: Sets the player limit for a map (# must be even).");
                return true;
            }
        }
        
        if (strings[0].equalsIgnoreCase("checkdata")) {
            if (cs.hasPermission("tf2.create")) {
                CheckDataCommand.getCommand().execCheckDataCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }
        
        if (strings[0].equalsIgnoreCase("debug")) {
            if (cs.hasPermission("tf2.create")) {
                DebugCommand.getCommand().execDebugCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("enable")) {
            if (cs.hasPermission("tf2.create")) {
                EnableCommand.getCommand().execEnableCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("disable")) {
            if (cs.hasPermission("tf2.create")) {
                DisableCommand.getCommand().execDisableCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("delete")) {
            if (cs.hasPermission("tf2.create")) {
                DeleteCommand.getCommand().execDeleteCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("reload")) {
            if (cs.hasPermission("tf2.create")) {
                ReloadCommand.getCommand().execReloadCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("join")) {
            if (cs instanceof org.bukkit.entity.Player) {
                if (cs.hasPermission("tf2.play")) {
                    JoinCommand.getCommand().execJoinCommand(cs, strings, cmnd);
                } else {
                    h.noPermission();
                }
            } else {
                h.noConsole();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("leave")) {
            if (cs instanceof org.bukkit.entity.Player) {
                if (cs.hasPermission("tf2.play")) {
                    LeaveCommand.getCommand().execLeaveCommand(cs, strings, cmnd);
                } else {
                    h.noPermission();
                }
            } else {
                h.noConsole();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("create")) {
            if (cs instanceof org.bukkit.entity.Player) {
                if (cs.hasPermission("tf2.create")) {
                    CreateCommand.getCommand().execCreateCommand(cs, strings, cmnd);
                } else {
                    h.noPermission();
                }
            } else {
                h.noConsole();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("set")) {
            if (cs.hasPermission("tf2.create")) {
                SetCommand.getCommand().execSetCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("list")) {
            if (cs.hasPermission("tf2.play")) {
                ListCommand.getCommand().execListCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        h.unknownCommand();
        return true;
    }

}
