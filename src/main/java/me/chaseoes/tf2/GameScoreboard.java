package me.chaseoes.tf2;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class GameScoreboard {

	Game game;
	ScoreboardManager manager;
	Scoreboard board;
	Team red;
	Team blue;
	Objective objective;

	public GameScoreboard(Game g) {
		game = g;
		manager = TF2.getInstance().getServer().getScoreboardManager();
		board = manager.getNewScoreboard();
		red = board.registerNewTeam(me.chaseoes.tf2.Team.RED.getName());
		blue = board.registerNewTeam(me.chaseoes.tf2.Team.BLUE.getName());
		red.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + me.chaseoes.tf2.Team.RED.getName());
		blue.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + me.chaseoes.tf2.Team.BLUE.getName());
		red.setCanSeeFriendlyInvisibles(true);
		blue.setCanSeeFriendlyInvisibles(true);
		objective = board.registerNewObjective("TF2;" + g.getMapName(), "dummy");
        red.setPrefix(ChatColor.RED + "");
        blue.setPrefix(ChatColor.BLUE + "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "TF2 Kills");
	}

	public void addPlayer(GamePlayer gp) {
		if (gp.getTeam() == me.chaseoes.tf2.Team.RED) {
			red.addPlayer(getPlayer(gp));
		} else {
			blue.addPlayer(getPlayer(gp));
		}
        updateBoard();
	}

    public void removePlayer(GamePlayer gp) {
        if (gp.getTeam() == me.chaseoes.tf2.Team.RED) {
            red.removePlayer(getPlayer(gp));
        } else {
            blue.removePlayer(getPlayer(gp));
        }
        board.resetScores(getPlayer(gp));
        if (TF2.getInstance().getConfig().getBoolean("scoreboard")) {
            gp.getPlayer().setScoreboard(manager.getMainScoreboard());
        }
        updateBoard();
    }

    public void updateBoard() {
        resetScores();
        Collection<GamePlayer> players = game.playersInGame.values();
        if (TF2.getInstance().getConfig().getBoolean("scoreboard")) {
            for (GamePlayer gp : players) {
                if (!gp.getPlayer().getScoreboard().equals(board)) {
                    gp.getPlayer().setScoreboard(board);
                }
            }
        }
        ArrayList<GamePlayer> arrayPlayers = new ArrayList<GamePlayer>(players);
        Collections.sort(arrayPlayers, new GameplayerKillComparator());
        for (int i = 0; i < 10 && i < arrayPlayers.size(); i++) {
            GamePlayer gp = arrayPlayers.get(i);
            Score score = objective.getScore(getPlayer(gp));
            if (gp.getTotalKills() == 0) {
                score.setScore(1);
            }
            score.setScore(gp.getTotalKills());
        }
    }

	public void resetScores() {
		for (GamePlayer gp : game.playersInGame.values()) {
			board.resetScores(getPlayer(gp));
		}
	}

	public void remove() {
		resetScores();
		for (GamePlayer gp : game.playersInGame.values()) {
				red.removePlayer(getPlayer(gp));
				blue.removePlayer(getPlayer(gp));
            if (TF2.getInstance().getConfig().getBoolean("scoreboard")) {
			    gp.getPlayer().setScoreboard(manager.getMainScoreboard());
            }
		}
	}

	private OfflinePlayer getPlayer(GamePlayer gp) {
		return TF2.getInstance().getServer().getOfflinePlayer(gp.getPlayer().getName());
	}

    class GameplayerKillComparator implements Comparator<GamePlayer> {

        @Override
        public int compare(GamePlayer o1, GamePlayer o2) {
            return o2.getTotalKills() - o1.getTotalKills();
        }
    }

}
