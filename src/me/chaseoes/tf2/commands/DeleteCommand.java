package me.chaseoes.tf2.commands;

import me.chaseoes.tf2.TF2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        // CommandHelper h = new CommandHelper(cs, cmnd);
    }

}
