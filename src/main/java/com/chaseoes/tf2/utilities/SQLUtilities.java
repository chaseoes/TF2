package com.chaseoes.tf2.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.chaseoes.tf2.TF2;

public class SQLUtilities {

    static SQLUtilities instance = new SQLUtilities();
    private TF2 plugin;
    Connection conn;
    boolean connected = false;

    private SQLUtilities() {

    }

    public static SQLUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                final TF2 p = plugin;
                String username = p.getConfig().getString("stats-database.username");
                String password = p.getConfig().getString("stats-database.password");
                String url = "jdbc:mysql://" + p.getConfig().getString("stats-database.hostname") + ":" + p.getConfig().getInt("stats-database.port") + "/" + p.getConfig().getString("stats-database.database_name");

                try {
                    conn = DriverManager.getConnection(url, username, password);
                    Statement st = conn.createStatement();
                    String table = "CREATE TABLE IF NOT EXISTS players(id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), username TEXT, kills TEXT, highest_killstreak TEXT, points_captured TEXT, games_played TEXT, red_team_count TEXT, blue_team_count TEXT, time_ingame TEXT, games_won TEXT, arrows_fired TEXT, deaths TEXT)";
                    st.executeUpdate(table);
                    connected = true;
                } catch (Exception e) {
                    connected = false;
                    plugin.getLogger().log(Level.SEVERE, "Could not connect to database! Verify your database details in the configuration are correct.");
                    plugin.getServer().getPluginManager().disablePlugin(plugin);
                }
            }
        });
    }

    public ResultSet getResultSet(String statement) {
        if (!connected) {
            return null;
        }
        ResultSet result = null;
        try {
            Statement st;
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (!plugin.getConfig().getBoolean("settings.sandbox")) {
                return st.executeQuery(statement);
            }
            return st.executeQuery(statement);
        } catch (SQLException e) {

        }
        return result;
    }

    public void execUpdate(String statement) {
        if (!connected) {
            return;
        }
        Statement st;
        try {
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            st.executeUpdate(statement);
        } catch (SQLException e) {

        }
    }

}
