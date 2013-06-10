package me.chaseoes.tf2.utilities;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import me.chaseoes.tf2.GamePlayer;
import me.chaseoes.tf2.TF2;

public class ScoreboardUtilities {
	
	public void display(GamePlayer player) {
		ScoreboardManager manager = TF2.getInstance().getServer().getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Team red = board.registerNewTeam(me.chaseoes.tf2.Team.RED.getName());
		Team blue = board.registerNewTeam(me.chaseoes.tf2.Team.BLUE.getName());
	}

}
