package me.chaseoes.tf2;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class GameScoreboard {

	Game game;
	ScoreboardManager manager;
	Scoreboard board;
	Team red;
	Team blue;
	Objective objective;

	public GameScoreboard(Game g) {
		g = game;
		manager = TF2.getInstance().getServer().getScoreboardManager();
		board = manager.getNewScoreboard();
		red = board.registerNewTeam(me.chaseoes.tf2.Team.RED.getName());
		blue = board.registerNewTeam(me.chaseoes.tf2.Team.BLUE.getName());
		red.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + me.chaseoes.tf2.Team.RED.getName());
		blue.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + me.chaseoes.tf2.Team.BLUE.getName());
		red.setCanSeeFriendlyInvisibles(true);
		blue.setCanSeeFriendlyInvisibles(true);
		objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("TF2 Kills");
	}

	public void addPlayer(GamePlayer p) {
		if (p.getTeam() == me.chaseoes.tf2.Team.RED) {
			red.addPlayer(p.getPlayer());
		} else {
			blue.addPlayer(p.getPlayer());
		}
	}

	public void updateBoard() {
		for (GamePlayer gp : game.playersInGame.values()) {
			Score score = objective.getScore(gp.getPlayer());
			score.setScore(gp.getKills());
		}
	}

	public void resetScores() {
		for (GamePlayer gp : game.playersInGame.values()) {
			board.resetScores(gp.getPlayer());
		}
	}

	public void remove() {
		resetScores();
		for (GamePlayer gp : game.playersInGame.values()) {
			if (gp.getTeam() == me.chaseoes.tf2.Team.RED) {
				red.removePlayer(gp.getPlayer());
			} else {
				blue.removePlayer(gp.getPlayer());
			}
		}
	}

}
