package me.chaseoes.tf2;

import me.chaseoes.tf2.capturepoints.CapturePoint;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.utilities.Container;
import me.chaseoes.tf2.utilities.WorldEditUtilities;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.kitteh.tag.TagAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Game {

    TF2 plugin;
    Map map;
    GameStatus status = GameStatus.WAITING;
    public boolean redHasBeenTeleported = false;
    public int time = 0;

    public HashMap<String, GamePlayer> playersInGame = new HashMap<String, GamePlayer>();

    public Game(Map m, TF2 plugin) {
        map = m;
        this.plugin = plugin;
    }

    public GamePlayer getPlayer(Player player) {
        return playersInGame.get(player.getName());
    }

    public void setStatus(GameStatus s) {
        status = s;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getMapName() {
        return map.getName();
    }

    public void updateTime(int time) {
        this.time = time;
    }

    public Integer getTimeLeftSeconds() {
        return map.getTimelimit() - time;
    }

    public void setExpOfPlayers(double expOfPlayers) {
        for (GamePlayer gp : playersInGame.values()) {
            gp.getPlayer().setExp((float) expOfPlayers);
        }
    }

    public List<String> getPlayersIngame() {
        List<String> l = new ArrayList<String>();
        for (GamePlayer gp : playersInGame.values()) {
            l.add(gp.getName());
        }
        return l;
    }

    public void startMatch() {
        setStatus(GameStatus.INGAME);
        CapturePointUtilities.getUtilities().uncaptureAll(map);
        for (Container container : map.getContainers()) {
            container.applyItems();
        }
        Schedulers.getSchedulers().startTimeLimitCounter(map);
        Schedulers.getSchedulers().startRedTeamCountdown(map);

        for (GamePlayer gp : playersInGame.values()) {
            Player player = gp.getPlayer();
            if (gp.getTeam() == Team.BLUE) {
                if (gp.getCurrentClass() != null) {
                    gp.setInLobby(false);
                    player.teleport(map.getBlueSpawn());
                    gp.getCurrentClass().apply(player);
                    gp.setUsingChangeClassButton(false);
                } else {
                    gp.setUsingChangeClassButton(true);
                    player.sendMessage(ChatColor.YELLOW + "[TF2] You will be teleported when you choose a class.");
                }
            }
        }

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (GamePlayer gp : playersInGame.values()) {
                    Player player = gp.getPlayer();
                    if (gp.getTeam() == Team.RED) {
                        if (gp.getCurrentClass() != null) {
                            gp.setInLobby(false);
                            player.teleport(map.getRedSpawn());
                            gp.getCurrentClass().apply(player);
                            gp.setUsingChangeClassButton(false);
                        } else {
                            gp.setUsingChangeClassButton(true);
                            player.sendMessage(ChatColor.YELLOW + "[TF2] You will be teleported when you choose a class.");
                        }
                    }
                }

                redHasBeenTeleported = true;
                Schedulers.getSchedulers().stopRedTeamCountdown(map.getName());
            }
        }, map.getRedTeamTeleportTime() * 20L);
    }

    public void stopMatch() {
        setStatus(GameStatus.WAITING);
        Schedulers.getSchedulers().stopRedTeamCountdown(map.getName());
        Schedulers.getSchedulers().stopTimeLimitCounter(map.getName());
        Schedulers.getSchedulers().stopCountdown(map.getName());
        for (Container container : map.getContainers()) {
            container.applyItems();
        }
        HashMap<String, GamePlayer> gamePlayers = new HashMap<String, GamePlayer>(playersInGame);
        for (GamePlayer gp : gamePlayers.values()) {
            Player player = gp.getPlayer();
            leaveGame(player);
            gp.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] The game has ended.");
        }

        CapturePointUtilities.getUtilities().uncaptureAll(map);

        for (Entity e : map.getP1().getWorld().getEntities()) {
            if (e instanceof Arrow) {
                if (WorldEditUtilities.getWEUtilities().isInMap(e, map)) {
                    e.remove();
                }
            }
        }

        for (CapturePoint cp : map.getCapturePoints()) {
            cp.stopCapturing();
        }

        redHasBeenTeleported = false;
        playersInGame.clear();
    }

    public void winMatch(Team team) {
        String[] winlines = new String[4];
        winlines[0] = " ";
        winlines[1] = "" + ChatColor.DARK_RED + ChatColor.BOLD + "Red Team";

        if (team == Team.BLUE) {
            winlines[1] = ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team";
        }

        winlines[2] = ChatColor.GREEN + "" + ChatColor.BOLD + "Wins!";
        winlines[3] = " ";
        String te = " " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "red " + ChatColor.RESET + ChatColor.YELLOW;

        if (team == Team.BLUE) {
            te = " " + ChatColor.BLUE + "" + ChatColor.BOLD + "blue " + ChatColor.RESET + ChatColor.YELLOW;
        }

        LobbyWall.getWall().setAllLines(map.getName(), null, winlines, false, true);

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                String[] creditlines = new String[4];
                creditlines[0] = " ";
                creditlines[1] = ChatColor.BOLD + "TF2 Plugin By:";
                creditlines[2] = ChatColor.BLUE + "chaseoes";
                creditlines[3] = " ";
                LobbyWall.getWall().setAllLines(map.getName(), 4, creditlines, false, true);
            }
        }, 120L);

        CapturePointUtilities.getUtilities().uncaptureAll(map);
        plugin.getServer().broadcastMessage(ChatColor.YELLOW + "The" + te + "team has won on the map " + ChatColor.BOLD + map.getName() + ChatColor.RESET + ChatColor.YELLOW + "!");
        stopMatch();
    }

    @SuppressWarnings("deprecation")
    public void joinGame(GamePlayer player, Team team) {
        playersInGame.put(player.getName(), player);
        player.setMap(getMapName());
        player.setInLobby(true);
        player.setTeam(team);
        TF2Class c = new TF2Class("NONE");

        player.saveInventory();
        c.clearInventory(player.getPlayer());
        player.getPlayer().setHealth(20);
        player.getPlayer().setFoodLevel(20);
        player.getPlayer().setGameMode(GameMode.valueOf(plugin.getConfig().getString("gamemode").toUpperCase()));
        player.getPlayer().setLevel(0);
        player.getPlayer().setExp(0);
        switch (team) {
            case BLUE:
                player.getPlayer().teleport(map.getBlueLobby());
                break;
            case RED:
                player.getPlayer().teleport(map.getRedLobby());
                break;
        }

        TagAPI.refreshPlayer(player.getPlayer());

        double currentpercent = (double) playersInGame.size() / map.getPlayerlimit() * 100;
        if (getStatus().equals(GameStatus.WAITING)) {
            if (currentpercent >= plugin.getConfig().getInt("autostart-percent")) {
                Schedulers.getSchedulers().startCountdown(map);
                setStatus(GameStatus.STARTING);
            }
        }

        player.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You joined the map " + map.getName() + ChatColor.RESET + ChatColor.YELLOW + "!");

        if (getStatus() == GameStatus.WAITING) {
            player.getPlayer().sendMessage(ChatColor.YELLOW + "The game will start when " + plugin.getConfig().getInt("autostart-percent") + "% of players have joined.");
        } else if (getStatus() == GameStatus.INGAME) {
            switch (player.getTeam()) {
                case RED:
                    if (redHasBeenTeleported) {
                        player.setUsingChangeClassButton(true);
                    }
                    break;
                case BLUE:
                    player.setUsingChangeClassButton(true);
                    break;
            }
        }

        player.getPlayer().updateInventory();
    }

    public void leaveGame(Player player) {
        GamePlayer gp = getPlayer(player);
        gp.leaveCurrentGame();
    }

    public Team decideTeam() {
        int red = getSizeOfTeam(Team.RED);
        int blue = getSizeOfTeam(Team.BLUE);

        if (red > blue) {
            return Team.BLUE;
        }

        return Team.RED;
    }

    public Integer getSizeOfTeam(Team team) {
        int red = 0;
        int blue = 0;
        for (GamePlayer player : playersInGame.values()) {
            if (player.getTeam() == Team.RED) {
                red++;
            }

            if (player.getTeam() == Team.BLUE) {
                blue++;
            }
        }

        if (team == Team.BLUE) {
            return blue;
        }

        return red;
    }

    public String getTimeLeft() {
        if (getStatus().equals(GameStatus.WAITING) || getStatus().equals(GameStatus.STARTING)) {
            return "Not Started";
        }

        int time = getTimeLeftSeconds();
        int hours = time / (60 * 60);
        time = time % (60 * 60);
        int minutes = time / 60;
        time = time % 60;

        if (hours == 0) {
            return minutes + "m " + time + "s";
        }

        return Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(time) + "s";
    }

    public String getTimeLeftPretty() {
        if (getStatus().equals(GameStatus.WAITING) || getStatus().equals(GameStatus.STARTING)) {
            return "Not Started";
        }
        Integer time = getTimeLeftSeconds();
        int hours = time / (60 * 60);
        time = time % (60 * 60);
        int minutes = time / 60;
        time = time % 60;

        if (hours == 0) {
            if (time == 0) {
                return minutes + " " + ChatColor.BLUE + "minutes";
            }
            if (minutes == 0) {
                return time + " " + ChatColor.BLUE + "seconds";
            }
            return minutes + " " + ChatColor.BLUE + "minutes " + ChatColor.AQUA + time + " " + ChatColor.BLUE + "seconds";
        }
        return Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(time) + "s";
    }

    public void checkQueue(boolean b) {
        try {
            Queue q = plugin.getQueue(map.getName());
            if (q != null) {
                Team team = decideTeam();
                Iterator<String> it = q.getQueue().iterator();
                while (it.hasNext()) {
                    String pl = it.next();
                    Player p = plugin.getServer().getPlayerExact(pl);
                    if (p != null) {
                        Integer position = q.getPosition(p.getName());
                        if (position != null) {
                            if (!(playersInGame.size() + 1 <= map.getPlayerlimit())) {
                                if (b) {
                                    p.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + position + " in line for the map " + ChatColor.BOLD + map.getName() + ChatColor.RESET + ChatColor.YELLOW + ".");
                                }
                            } else {
                                joinGame(GameUtilities.getUtilities().getGamePlayer(p), team);
                                q.remove(p.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPrettyStatus() {
        GameStatus status = getStatus();
        if (status == GameStatus.INGAME) {
            return "In-Game";
        } else if (status == GameStatus.STARTING) {
            return "Starting";
        } else if (status == GameStatus.WAITING) {
            return "Waiting";
        } else if (status == GameStatus.DISABLED) {
            return "Disabled";
        }
        return "ERROR";
    }

    public void broadcast(String message) {
        for (GamePlayer player : playersInGame.values()) {
            player.getPlayer().sendMessage(message);
        }
    }

}