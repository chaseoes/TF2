package me.chaseoes.tf2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.lobbywall.LobbyWall;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.kitteh.tag.TagAPI;

public class GameUtilities {

    public TF2 plugin;
    static GameUtilities instance = new GameUtilities();
    public HashMap<String, String> ingame = new HashMap<String, String>(); // player,
                                                                           // map
    public HashMap<String, String> teams = new HashMap<String, String>(); // player,
                                                                          // team
    public HashMap<String, String> capturepoints = new HashMap<String, String>();
    public HashMap<String, Integer> afktimes = new HashMap<String, Integer>();
    public HashMap<String, Location> afklocations = new HashMap<String, Location>();
    public HashMap<String, String> makingclassbutton = new HashMap<String, String>();
    public HashMap<String, String> makingclassbuttontype = new HashMap<String, String>();
    public HashSet<String> makingchangeclassbutton = new HashSet<String>();
    public HashSet<String> usingchangeclassbutton = new HashSet<String>();
    public HashSet<String> gamestarted = new HashSet<String>(); // map
    public HashSet<String> gameinlobby = new HashSet<String>(); // map
    public HashSet<String> gamestarting = new HashSet<String>(); // map
    public HashSet<String> justspawned = new HashSet<String>(); // player
    public HashMap<String, Integer> gametimes = new HashMap<String, Integer>();
    public HashMap<String, Integer> arrows = new HashMap<String, Integer>();
    public HashMap<String, Integer> kills = new HashMap<String, Integer>();
    public HashMap<String, Integer> totalkills = new HashMap<String, Integer>();
    public HashMap<String, Integer> totaldeaths = new HashMap<String, Integer>();
    public HashMap<String, GameStatus> gamestatus = new HashMap<String, GameStatus>();
    public HashMap<String, Boolean> redHasBeenTeleported = new HashMap<String, Boolean>();
    public HashMap<String, ItemStack[]> inventories = new HashMap<String, ItemStack[]>();
    public HashMap<String, ItemStack[]> armorinventories = new HashMap<String, ItemStack[]>();
    public List<String> coolpeople = new ArrayList<String>();
    public Integer afkchecker;

    private GameUtilities() {

    }

    public static GameUtilities getUtilities() {
        return instance;
    }

    public void setup(TF2 p) {
        plugin = p;
    }

    public void joinGame(Player player, String map, String team) {
        inventories.put(player.getName(), player.getInventory().getContents());
        armorinventories.put(player.getName(), player.getInventory().getArmorContents());
        player.getInventory().clear();
        player.updateInventory();
        ingame.put(player.getName(), map);
        teams.put(player.getName(), team);
        gameinlobby.add(map);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(MapUtilities.getUtilities().loadTeamLobby(map, team));
        LobbyWall.getWall().update();
        TagAPI.refreshPlayer(player);
        kills.put(player.getName(), 0);
        totalkills.put(player.getName(), 0);
        totaldeaths.put(player.getName(), 0);
        System.out.println(getGameStatus(map));
        if (getGameStatus(map).equalsIgnoreCase("waiting")) {
            if ((double) getIngameList(map).size() / MapConfiguration.getMaps().getMap(map).getInt("playerlimit") * 100 >= plugin.getConfig().getInt("autostart-percent")) {
                Schedulers.getSchedulers().startCountdown(map);
            }
        }

        player.updateInventory();
    }

    public void leaveCurrentGame(Player player) {
        String map = getCurrentMap(player);
        kills.put(player.getName(), 0);
        player.teleport(MapUtilities.getUtilities().loadLobby());
        player.setGameMode(plugin.getServer().getDefaultGameMode());
        TagAPI.refreshPlayer(player);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        if (gamestarting.contains(ingame.get(player.getName())) && getIngameList(ingame.get(player.getName())).size() == 1) {
            stopMatch(ingame.get(player.getName()));
        }

        ingame.remove(player.getName());
        teams.remove(player.getName());
        checkQueue(map);
        if (getIngameList(ingame.get(player.getName())).size() == 0) {
            stopMatch(ingame.get(player.getName()));
        }
        LobbyWall.getWall().update();
        kills.put(player.getName(), 0);
        totalkills.put(player.getName(), 0);
        totaldeaths.put(player.getName(), 0);

        player.getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getInventory().setBoots(new ItemStack(Material.AIR));
        player.getInventory().clear();
        if (inventories.get(player.getName()) != null) {
            player.getInventory().setContents(inventories.get(player.getName()));
        }
        if (armorinventories.get(player.getName()) != null) {
            player.getInventory().setArmorContents(armorinventories.get(player.getName()));
        }
        player.updateInventory();
    }

    public void startMatch(final String map) {
        System.out.println("STARTING!");
        gamestarted.add(map);
        gameinlobby.remove(map);
        CapturePointUtilities.getUtilities().uncaptureAll(plugin.getMap(map));
        Schedulers.getSchedulers().startTimeLimitCounter(map);
        Schedulers.getSchedulers().startRedTeamCountdown(map);
        for (String player : getIngameList(map)) {
            Player p = plugin.getServer().getPlayerExact(player);
            if (getTeam(p).equalsIgnoreCase("blue")) {
                p.teleport(MapUtilities.getUtilities().loadTeamSpawn(map, getTeam(p)));
            } else {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        for (String playe : getIngameList(map)) {
                            if (getTeam(plugin.getServer().getPlayerExact(playe)).equalsIgnoreCase("red")) {
                                plugin.getServer().getPlayerExact(playe).teleport(MapUtilities.getUtilities().loadTeamSpawn(map, getTeam(plugin.getServer().getPlayerExact(playe))));
                            }
                        }
                        redHasBeenTeleported.put(map, true);
                        Schedulers.getSchedulers().stopRedTeamCountdown(map);
                    }
                }, MapConfiguration.getMaps().getMap(map).getInt("teleport-red-team") * 20L);
            }
        }
    }

    public void stopMatch(String map) {
        gamestarted.remove(map);
        gameinlobby.add(map);
        gamestatus.put(map, GameStatus.WAITING);
        Schedulers.getSchedulers().stopRedTeamCountdown(map);
        for (String player : getIngameList(map)) {
            Player p = plugin.getServer().getPlayerExact(player);
            leaveCurrentGame(p);
            p.sendMessage("§e[TF2] The game has ended.");
        }
        redHasBeenTeleported.put(map, false);
        Schedulers.getSchedulers().stopTimeLimitCounter(map);
    }

    public void winGame(final String map, String team) {
        String[] winlines = new String[4];
        winlines[0] = " ";
        winlines[1] = "§4§lRed Team";
        if (team.equalsIgnoreCase("blue")) {
            winlines[1] = "§9§lBlue Team";
        }
        winlines[2] = "§a§lWins!";
        winlines[3] = " ";
        String te = " §4§lred §r§e";
        if (team.equalsIgnoreCase("blue")) {
            te = " §9§lblue §r§e";
        }
        LobbyWall.getWall().setAllLines(map, null, winlines, false, true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                String[] creditlines = new String[4];
                creditlines[0] = " ";
                creditlines[1] = "§lTF2 Plugin By:";
                creditlines[2] = "§9chaseoes";
                creditlines[3] = " ";
                LobbyWall.getWall().unsetNoUpdate(map);
                LobbyWall.getWall().setAllLines(map, 4, creditlines, false, true);
            }
        }, 120L);
        plugin.getServer().broadcastMessage("§eThe" + te + "team has won on the map §l" + map + "§r§e!");
        Schedulers.getSchedulers().stopTimeLimitCounter(map);
        stopMatch(map);
    }

    public Integer getAmountOnTeam(String map, String team) {
        Integer red = 0;
        Integer blue = 0;
        for (String player : ingame.keySet()) {
            if (ingame.get(player).equalsIgnoreCase(map)) {
                if (teams.get(player).equalsIgnoreCase("red")) {
                    red++;
                }
                if (teams.get(player).equalsIgnoreCase("blue")) {
                    blue++;
                }
            }
        }

        if (team.equalsIgnoreCase("blue")) {
            return blue;
        }
        return red;
    }

    public String getTeamColor(Player player) {
        String color = "§9§l";
        if (GameUtilities.getUtilities().getTeam(player).equalsIgnoreCase("red")) {
            color = "§4§l";
        }
        return color;
    }

    public Integer getTimeLeftSeconds(String map) {
        return MapConfiguration.getMaps().getMap(map).getInt("timelimit") - gametimes.get(map);
    }

    public String decideTeam(String map) {
        Integer red = 0;
        Integer blue = 0;
        for (String player : ingame.keySet()) {
            if (ingame.get(player).equalsIgnoreCase(map)) {
                if (teams.get(player).equalsIgnoreCase("red")) {
                    red++;
                }
                if (teams.get(player).equalsIgnoreCase("blue")) {
                    blue++;
                }
            }
        }

        if (red > blue) {
            return "blue";
        }
        return "red";
    }

    public List<String> getIngameList(String map) {
        List<String> in = new ArrayList<String>();
        for (String p : ingame.keySet()) {
            if (ingame.get(p).equalsIgnoreCase(map)) {
                in.add(p);
            }
        }
        return in;
    }

    public String getTeam(Player player) {
        if (player != null && player.getName() != null) {
                return teams.get(player.getName());
        }
        return "red";
    }

    public Boolean isIngame(Player player) {
        if (ingame.containsKey(player.getName())) {
            return true;
        }
        return false;
    }

    public String getCurrentMap(Player player) {
        return ingame.get(player.getName());
    }

    public String getGameStatus(String map) {
        if (gamestatus.get(map) == GameStatus.INGAME) {
            return "In-Game";
        } else if (gamestatus.get(map) == GameStatus.STARTING) {
            return "Starting";
        } else if (gamestatus.get(map) == GameStatus.WAITING) {
            return "Waiting";
        } else if (gamestatus.get(map) == GameStatus.DISABLED) {
            return "Disabled";
        }
        return null;
    }

    public String getTimeLeft(String map) {
        if (getGameStatus(map).equalsIgnoreCase("Waiting") || getGameStatus(map).equalsIgnoreCase("Starting")) {
            return "Not Started";
        }
        Integer time = MapConfiguration.getMaps().getMap(map).getInt("timelimit");
        if (gametimes.get(map) != null) {
            time = getTimeLeftSeconds(map);
        }
        int hours = time / (60 * 60);
        time = time % (60 * 60);
        int minutes = time / 60;
        time = time % 60;

        if (hours == 0) {
            return minutes + "m " + time + "s";
        }
        return Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(time) + "s";
    }

    public String getTimeLeftPretty(String map) {
        if (getGameStatus(map).equalsIgnoreCase("Waiting") || getGameStatus(map).equalsIgnoreCase("Starting")) {
            return "Not Started";
        }
        Integer time = MapConfiguration.getMaps().getMap(map).getInt("timelimit");
        if (gametimes.get(map) != null) {
            time = getTimeLeftSeconds(map);
        }
        int hours = time / (60 * 60);
        time = time % (60 * 60);
        int minutes = time / 60;
        time = time % 60;

        if (hours == 0) {
            if (time == 0) {
                return minutes + " §9minutes";
            }
            if (minutes == 0) {
                return time + " §9seconds";
            }
            return minutes + " §9minutes §b" + time + " §9seconds";
        }
        return Math.abs(hours) + "h " + Math.abs(minutes) + "m " + Math.abs(time) + "s";
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveConfig();
    }

    public void broadcast(String map, String message) {
        if (getIngameList(map) != null) {
            for (String p : getIngameList(map)) {
                if (p != null) {
                    plugin.getServer().getPlayerExact(p).sendMessage(message);
                }
            }
        }
    }

    public void checkQueue(String map) {
        try {
            Queue q = plugin.getQueue(map);
            if (q != null) {
                String team = GameUtilities.getUtilities().decideTeam(map);
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
                            if (!(position <= MapConfiguration.getMaps().getMap(map).getInt("playerlimit"))) {
                                p.sendMessage("§e[TF2] You are #" + position + " in line for the map §l" + map + "§r§e.");
                            } else {
                                joinGame(p, map, team);
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

    public void getPlayerWithMostKills(String map) {

    }

}
