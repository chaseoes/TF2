package me.chaseoes.tf2.commands;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.chaseoes.tf2.*;

import me.chaseoes.tf2.utilities.Localizer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand {

    private HashMap<String, SpectatePlayer> spectating = new HashMap<String, SpectatePlayer>();
    static SpectateCommand instance = new SpectateCommand();

    public static SpectateCommand getCommand() {
        return instance;
    }

    public void execSpectateCommand(CommandSender cs, String[] strings, Command cmnd) {
        CommandHelper h = new CommandHelper(cs, cmnd);
        Player player = (Player) cs;
        if (strings.length == 1) {
            if (isSpectating(player)) {
                stopSpectating(player);
            } else {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-NOT-SPECTATING"));
            }
        } else if (strings.length == 2) {
            String map = strings[1];
            if (!TF2.getInstance().mapExists(map)) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("DOES-NOT-EXIST-MAP").replace("%map", map));
                return;
            }

            Game game = GameUtilities.getUtilities().getGamePlayer((Player) cs).getGame();
            if (game != null) {
                cs.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-ALREADY-PLAYING"));
                return;
            }

            if (DataConfiguration.getData().getDataFile().getStringList("disabled-maps").contains(map)) {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-DISABLED"));
                return;
            }

            if (!spectating.containsKey(cs.getName())) {
                spectating.put(cs.getName(), new SpectatePlayer(player));
            }
            SpectatePlayer sc = spectating.get(cs.getName());
            Game ngame = GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map));
            if (ngame.getStatus() == GameStatus.INGAME || ngame.getStatus() == GameStatus.STARTING) {
                sc.toggleSpectating(GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(map)));
            } else {
                player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("MAP-INFO-NOT-INGAME"));
            }
        } else {
            h.wrongArgs();
        }
    }

    public boolean isSpectating(Player player) {
        if (spectating.containsKey(player.getName())) {
            return spectating.get(player.getName()).isSpectating;
        }
        return false;
    }

    public void stopSpectating(Player player) {
        if (isSpectating(player)) {
            SpectatePlayer sp = spectating.get(player.getName());
            sp.toggleSpectating(GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(sp.gameSpectating)));
        }
    }

    public void stopSpectating(Game game) {
        for (SpectatePlayer sps : spectating.values()) {
            if (sps.isSpectating && sps.gameSpectating.equalsIgnoreCase(game.getMapName())) {
                sps.toggleSpectating(game);
            }
        }
    }

    public void playerLogout(Player player) {
        spectating.remove(player.getName());
    }

    public Set<SpectatePlayer> getSpectators(Game game) {
        Set<SpectatePlayer> sps = new HashSet<SpectatePlayer>();
        for (SpectatePlayer sp : spectating.values()) {
            if (sp.isSpectating && sp.gameSpectating.equalsIgnoreCase(game.getMapName())) {
                sps.add(sp);
            }
        }
        return sps;
    }
}
