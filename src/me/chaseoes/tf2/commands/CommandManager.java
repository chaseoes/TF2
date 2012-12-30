package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.GameUtilities;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {

        CommandHelper h = new CommandHelper(cs, cmnd);

        if (strings.length == 0) {
            cs.sendMessage(ChatColor.DARK_AQUA + "Team Fortress 2 Plugin by chaseoes.");
            return true;
        }

        if (strings[0].equalsIgnoreCase("help")) {
            if (strings.length == 2 && strings[1].equalsIgnoreCase("3")) {
                cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + "TF2 Help " + ChatColor.AQUA + "3 " + ChatColor.DARK_AQUA + "(" + ChatColor.GRAY + "/tf2 help [page #]" + ChatColor.DARK_AQUA + ") " + ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "]");
                cs.sendMessage(ChatColor.RED + "/tf2 set spawn lobby" + ChatColor.GRAY + ": Sets the global lobby spawn (no team).");
                cs.sendMessage(ChatColor.AQUA + "/tf2 join <map id> [team]" + ChatColor.GRAY + ": Join a map. If a team is not provided, you will be placed randomly.");
                cs.sendMessage(ChatColor.AQUA + "/tf2 leave" + ChatColor.GRAY + ": Leave the current game.");
                cs.sendMessage(ChatColor.AQUA + "/tf2 list [map]" + ChatColor.GRAY + ": List players currently in the game.");
                return true;
            } else if (strings.length == 2 && strings[1].equalsIgnoreCase("2")) {
                cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + "TF2 Help " + ChatColor.AQUA + "2 " + ChatColor.DARK_AQUA + "(" + ChatColor.GRAY + "/tf2 help [page #]" + ChatColor.DARK_AQUA + ") " + ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "]");
                cs.sendMessage(ChatColor.RED + "/tf2 set timelimit <map id> <#>" + ChatColor.GRAY + ": Sets the time limit for a map (# must be in seconds).");
                cs.sendMessage(ChatColor.RED + "/tf2 set capturepoint <map id> <#>" + ChatColor.GRAY + ": Defines capture point number <#> on the pressure plate you're standing on.");
                cs.sendMessage(ChatColor.RED + "/tf2 set spawn <redlobby | bluelobby> <map id>" + ChatColor.GRAY + ": Sets the lobby spawn for the given team.");
                cs.sendMessage(ChatColor.RED + "/tf2 set spawn <redteam | blueteam> <map id>" + ChatColor.GRAY + ": Sets the spawn for the given team.");
                return true;
            } else {
                cs.sendMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "] " + ChatColor.DARK_AQUA + "TF2 Help " + ChatColor.AQUA + "1 " + ChatColor.DARK_AQUA + "(" + ChatColor.WHITE + "/tf2 help [page #]" + ChatColor.DARK_AQUA + ") " + ChatColor.AQUA + "[" + ChatColor.GOLD + "-----------" + ChatColor.AQUA + "]");
                cs.sendMessage(ChatColor.RED + "/tf2 create map <id>" + ChatColor.GRAY + ": Creates a map, <id> being the name. A WorldEdit selection is required.");
                cs.sendMessage(ChatColor.RED + "/tf2 create classbutton <normal | donator> <class>" + ChatColor.GRAY + ": Gives you a button, which when placed, becomes a class button.");
                cs.sendMessage(ChatColor.RED + "/tf2 create changeclassbutton" + ChatColor.GRAY + ": Gives you a button, which when placed, becomes a change class button.");
                cs.sendMessage(ChatColor.RED + "/tf2 set playerlimit <map id> <#>" + ChatColor.GRAY + ": Sets the player limit for a map (# must be even).");
                return true;
            }
        }
        
        if (strings[0].equalsIgnoreCase("test")) {
            GameUtilities.getUtilities().winGame("default", "red");
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
