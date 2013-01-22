package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.TF2;

import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {

        CommandHelper h = new CommandHelper(cs, cmnd);

        if (strings.length == 0) {
            cs.sendMessage(Localizer.getLocalizer().loadMessage("ABOUT-HEADER"));
            cs.sendMessage(Localizer.getLocalizer().loadMessage("ABOUT-AUTHOR").replace("%version", TF2.getInstance().getDescription().getVersion()));
            cs.sendMessage(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/team-fortress-2/");
            return true;
        }
        
        if (strings[0].equalsIgnoreCase("help")) {
            HelpCommand.getCommand().execHelpCommand(cs, strings, cmnd);
            return true;
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

        if (strings[0].equalsIgnoreCase("start")) {
            if (cs.hasPermission("tf2.start") || cs.hasPermission("tf2.create")) {
                StartCommand.getCommand().execStartCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        if (strings[0].equalsIgnoreCase("stop")) {
            if (cs.hasPermission("tf2.stop") || cs.hasPermission("tf2.create")) {
                StopCommand.getCommand().execStopCommand(cs, strings, cmnd);
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
        if (strings[0].equalsIgnoreCase("redefine")) {
            if (cs.hasPermission("tf2.create")) {
                RedefineCommand.getCommand().execRedefineCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }
        
        if (strings[0].equalsIgnoreCase("spectate")) {
            if (cs.hasPermission("tf2.spectate")) {
                SpectateCommand.getCommand().execSpectateCommand(cs, strings, cmnd);
            } else {
                h.noPermission();
            }
            return true;
        }

        h.unknownCommand();
        return true;
    }

}
