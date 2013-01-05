package me.chaseoes.tf2;

import java.sql.ResultSet;

import me.chaseoes.tf2.utilities.SQLUtilities;

import org.bukkit.entity.Player;

public class StatCollector {

    String player;

    int kills = 0;
    int highest_killstreak = 0;
    int points_captured = 0;
    int games_played = 0;
    int red_team_count = 0;
    int blue_team_count = 0;
    int time_ingame = 0;
    int games_won = 0;
    int arrows_fired = 0;
    int deaths = 0;

    public StatCollector(Player p) {
        player = p.getName();
        load();
    }

    @SuppressWarnings("deprecation")
    public void load() {
        TF2.getInstance().getServer().getScheduler().scheduleAsyncDelayedTask(TF2.getInstance(), new Runnable() {
            @Override
            public void run() {
                ResultSet rs = SQLUtilities.getUtilities().getResultSet("SELECT * FROM players WHERE username='" + player + "'");
                boolean loaded = false;

                try {
                    while (rs.next()) {
                        loaded = true;
                        kills = Integer.parseInt(rs.getString("kills"));
                        highest_killstreak = Integer.parseInt(rs.getString("highest_killstreak"));
                        points_captured = Integer.parseInt(rs.getString("points_captured"));
                        games_played = Integer.parseInt(rs.getString("games_played"));
                        red_team_count = Integer.parseInt(rs.getString("red_team_count"));
                        blue_team_count = Integer.parseInt(rs.getString("blue_team_count"));
                        time_ingame = Integer.parseInt(rs.getString("time_ingame"));
                        games_won = Integer.parseInt(rs.getString("games_won"));
                        arrows_fired = Integer.parseInt(rs.getString("arrows_fired"));
                        deaths = Integer.parseInt(rs.getString("deaths"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!loaded) {
                    SQLUtilities.getUtilities().execUpdate("INSERT INTO players(username, kills, highest_killstreak, points_captured, games_played, red_team_count, blue_team_count, time_ingame, games_won, arrows_fired, deaths) VALUES ('" + player + "', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0')");
                }
            }
        }, 0L);
    }

    public void addStatsFromGame(int k, int h_k, int pc, Team team, int time, Team winningTeam, int af, int death) {
        kills = kills + k;

        if (h_k > highest_killstreak) {
            highest_killstreak = h_k;
        }

        points_captured = points_captured + pc;
        games_played++;
        if (team == Team.RED) {
            red_team_count++;
        } else {
            blue_team_count++;
        }

        if (team == winningTeam) {
            games_won++;
        }

        time_ingame = time_ingame + time;
        arrows_fired = arrows_fired + af;
        deaths = deaths + death;
    }

    @SuppressWarnings("deprecation")
    public void submit() {
        TF2.getInstance().getServer().getScheduler().scheduleAsyncDelayedTask(TF2.getInstance(), new Runnable() {
            @Override
            public void run() {
                ResultSet rs = SQLUtilities.getUtilities().getResultSet("SELECT * FROM players WHERE username='" + player + "'");
                try {
                    while (rs.next()) {
                        rs.updateString("kills", kills + "");
                        rs.updateString("highest_killstreak", highest_killstreak + "");
                        rs.updateString("points_captured", points_captured + "");
                        rs.updateString("games_played", games_played + "");
                        rs.updateString("red_team_count", red_team_count + "");
                        rs.updateString("blue_team_count", blue_team_count + "");
                        rs.updateString("time_ingame", time_ingame + "");
                        rs.updateString("games_won", games_won + "");
                        rs.updateString("arrows_fired", arrows_fired + "");
                        rs.updateString("deaths", deaths + "");
                        rs.updateRow();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0L);
    }

}
