package me.chaseoes.tf2;

import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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

    public void startMatch() {
        setStatus(GameStatus.INGAME);
        CapturePointUtilities.getUtilities().uncaptureAll(map);
        Schedulers.getSchedulers().startTimeLimitCounter(map.getName());
        Schedulers.getSchedulers().startRedTeamCountdown(map.getName());

        for (GamePlayer gp : playersInGame.values()) {
            Player player = gp.getPlayer();
            if (gp.getTeam() == Team.BLUE) {
                if (gp.getCurrentClass() != null) {
                    gp.setInLobby(false);
                    player.teleport(MapUtilities.getUtilities().loadTeamSpawn(map.getName(), Team.BLUE));
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
                            player.teleport(MapUtilities.getUtilities().loadTeamSpawn(map.getName(), Team.RED));
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

        for (GamePlayer gp : playersInGame.values()) {
            Player player = gp.getPlayer();
            leaveCurrentGame(player);
            gp.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] The game has ended.");
        }

        CapturePointUtilities.getUtilities().uncaptureAll(map);
        redHasBeenTeleported = false;
        playersInGame.clear();
    }

    public void winGame(String team) {
        String[] winlines = new String[4];
        winlines[0] = " ";
        winlines[1] = "" + ChatColor.DARK_RED + ChatColor.BOLD + "Red Team";

        if (team.equalsIgnoreCase("blue")) {
            winlines[1] = ChatColor.BLUE + "" + ChatColor.BOLD + "Blue Team";
        }

        winlines[2] = ChatColor.GREEN + "" + ChatColor.BOLD + "Wins!";
        winlines[3] = " ";
        String te = " " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "red " + ChatColor.RESET + ChatColor.YELLOW;

        if (team.equalsIgnoreCase("blue")) {
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
        player.setMap(getName());
        player.setInLobby(true);
        player.setTeam(team);
        TF2Class c = new TF2Class("NONE");

        player.saveInventory();
        c.clearInventory(player.getPlayer());
        player.getPlayer().setHealth(20);
        player.getPlayer().setFoodLevel(20);
        player.getPlayer().setGameMode(GameMode.SURVIVAL);
        player.getPlayer().teleport(MapUtilities.getUtilities().loadTeamLobby(map.getName(), team));
        TagAPI.refreshPlayer(player.getPlayer());

        double currentpercent = (double) playersInGame.size() / map.getPlayerlimit() * 100;
        if (getStatus().equals(GameStatus.WAITING)) {
            if (currentpercent >= plugin.getConfig().getInt("autostart-percent")) {
                Schedulers.getSchedulers().startCountdown(map.getName());
                setStatus(GameStatus.STARTING);
            }
        }

        player.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] You joined the map " + map.getName() + ChatColor.RESET + ChatColor.YELLOW + "!");
        
        if (getStatus() == GameStatus.WAITING || getStatus() == GameStatus.STARTING) {
            player.getPlayer().sendMessage(ChatColor.YELLOW + "The game will start when " + (map.getPlayerlimit() * 100 / plugin.getConfig().getInt("autostart-percent") - playersInGame.size() * map.getPlayerlimit()) + " players have joined.");
        } else {
            player.setUsingChangeClassButton(true);
        }
        
        player.getPlayer().updateInventory();
    }

    public void leaveCurrentGame(Player player) {
        GamePlayer gp = getPlayer(player);
        playersInGame.remove(gp.getName());
        gp.leaveCurrentGame();
        player.teleport(MapUtilities.getUtilities().loadLobby());
        TagAPI.refreshPlayer(player);

        if (getStatus() == GameStatus.STARTING && playersInGame.size() == 1) {
            stopMatch();
        }

        checkQueue();
        if (playersInGame.size() == 0) {
            stopMatch();
        }
    }

    public Integer getAmountOnTeam(Team team) {
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

    public Team decideTeam() {
        int red = getAmountOnTeam(Team.RED);
        int blue = getAmountOnTeam(Team.BLUE);

        if (red > blue) {
            return Team.BLUE;
        }
        return Team.RED;
    }

    public void setStatus(GameStatus s) {
        status = s;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getName() {
        return map.getName();
    }

    public List<String> getIngameList() {
        List<String> l = new ArrayList<String>();
        for (GamePlayer gp : playersInGame.values()) {
            l.add(gp.getName());
        }
        return l;
    }

    public void updateTime(int time) {
        this.time = time;
    }

    public Integer getTimeLeftSeconds() {
        return map.getTimelimit() - time;
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

    public void broadcast(String message) {
        for (GamePlayer player : playersInGame.values()) {
            player.getPlayer().sendMessage(message);
        }
    }

    public void checkQueue() {
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
                            if (q.contains(p)) {
                                q.remove(p.getName());
                            }
                            if (!(position <= map.getPlayerlimit())) {
                                p.sendMessage(ChatColor.YELLOW + "[TF2] You are #" + position + " in line for the map " + ChatColor.BOLD + map + ChatColor.RESET + ChatColor.YELLOW + ".");
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

    public GamePlayer getPlayer(Player player) {
        for (GamePlayer gp : playersInGame.values()) {
            if (player.getName().equalsIgnoreCase(gp.getName())) {
                return gp;
            }
        }
        return GameUtilities.getUtilities().getGamePlayer(player);
    }

    public void setExpOfPlayers(double expOfPlayers) {
        for (GamePlayer gp : playersInGame.values()){
            gp.getPlayer().setExp((float) expOfPlayers);
        }
    }
}