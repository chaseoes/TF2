package me.chaseoes.tf2;

import java.util.Collections;
import java.util.HashSet;

import me.chaseoes.tf2.classes.TF2Class;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;

public class GamePlayer {

    Player player;
    String map;
    Team team;
    int kills;
    int deaths;
    int totalKills;
    int totalDeaths;
    int currentKillstreak;
    int arrowsFired;
    int pointsCaptured;
    long timeEnteredGame;
    boolean inLobby;
    boolean usingChangeClassButton;
    boolean makingChangeClassButton;
    boolean makingClassButton;
    boolean justSpawned;
    boolean creatingContainer;
    boolean isDead;
    String mapCreatingItemFor;
    String classButtonType;
    String classButtonName;
    TF2Class currentClass;
    String playerLastDamagedBy;
    StatCollector stats;
    HashSet<Integer> killstreaks;

    ItemStack[] savedInventoryItems;
    ItemStack[] savedArmorItems;
    GameMode savedGameMode;
    float savedXPCount;
    int savedLevelCount;
    int savedFoodLevel;
    int savedHealth;

    public GamePlayer(Player p) {
        player = p;
        team = null;
        map = null;
        kills = 0;
        deaths = 0;
        inLobby = false;
        savedInventoryItems = null;
        savedArmorItems = null;
        savedXPCount = 0;
        savedLevelCount = 0;
        savedFoodLevel = 0;
        savedHealth = 0;
        currentKillstreak = 0;
        arrowsFired = 0;
        pointsCaptured = 0;
        timeEnteredGame = System.currentTimeMillis();
        savedGameMode = null;
        playerLastDamagedBy = this.getName();
        killstreaks = new HashSet<Integer>();
        stats = new StatCollector(p);
    }

    public Game getGame() {
        if (getCurrentMap() == null) {
            return null;
        }
        return GameUtilities.getUtilities().getGame(TF2.getInstance().getMap(getCurrentMap()));
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return player.getName();
    }

    public boolean isIngame() {
        return team != null;
    }

    public boolean isInLobby() {
        return inLobby;
    }

    public void setInLobby(boolean b) {
        inLobby = b;
    }

    public String getCurrentMap() {
        return map;
    }

    public void setMap(String m) {
        map = m;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team t) {
        team = t;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int i) {
        if (i == -1) {
            kills++;
        } else {
            kills = i;
        }
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int i) {
        if (i == -1) {
            deaths++;
        } else {
            deaths = i;
        }
    }

    public String getTeamColor() {
        if (!isIngame()) {
            return null;
        }

        String color = "" + ChatColor.BLUE + ChatColor.BOLD;
        if (getTeam() == Team.RED) {
            color = "" + ChatColor.DARK_RED + ChatColor.BOLD;
        }

        return color;
    }

    public void leaveCurrentGame() {
        Game game = getGame();
        game.map.getQueue().remove(player);
            TF2Class c = new TF2Class("NONE");
            c.clearInventory(player);
            loadInventory();
            if (game.getStatus() == GameStatus.STARTING && game.playersInGame.size() == 1) {
                game.stopMatch(true);
            }

            if (game.playersInGame.size() == 0) {
                game.stopMatch(true);
            }
        player.teleport(MapUtilities.getUtilities().loadLobby());
        TagAPI.refreshPlayer(player);
        game.map.getQueue().check();
        clear();
    }

    public void saveInventory() {
        savedInventoryItems = player.getInventory().getContents();
        savedArmorItems = player.getInventory().getArmorContents();
        savedXPCount = player.getExp();
        savedLevelCount = player.getLevel();
        savedFoodLevel = player.getFoodLevel();
        savedHealth = player.getHealth();
        savedGameMode = player.getGameMode();
    }

    @SuppressWarnings("deprecation")
    public void loadInventory() {
        player.getInventory().setContents(savedInventoryItems);
        player.getInventory().setArmorContents(savedArmorItems);
        player.setExp(savedXPCount);
        player.setLevel(savedLevelCount);
        player.setFoodLevel(savedFoodLevel);
        player.setHealth(savedHealth);
        player.setGameMode(savedGameMode);
        player.updateInventory();
    }

    public void setUsingChangeClassButton(Boolean b) {
        usingChangeClassButton = b;
    }

    public void setMakingChangeClassButton(Boolean b) {
        makingChangeClassButton = b;
    }

    public boolean isUsingChangeClassButton() {
        return usingChangeClassButton;
    }

    public boolean isMakingChangeClassButton() {
        return makingChangeClassButton;
    }

    public void setMakingClassButton(boolean b) {
        makingClassButton = b;
    }

    public boolean isMakingClassButton() {
        return makingClassButton;
    }

    public void setClassButtonType(String s) {
        classButtonType = s;
    }

    public String getClassButtonType() {
        return classButtonType;
    }

    public void setClassButtonName(String name) {
        classButtonName = name;
    }

    public String getClassButtonName() {
        return classButtonName;
    }

    public TF2Class getCurrentClass() {
        return currentClass;
    }

    public void setCurrentClass(TF2Class c) {
        currentClass = c;
    }

    public int getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(int i) {
        if (i == -1) {
            totalKills++;
        } else {
            totalKills = i;
        }
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public void settotalDeaths(int i) {
        if (i == -1) {
            totalDeaths++;
        } else {
            totalDeaths = i;
        }
    }

    public boolean justSpawned() {
        return justSpawned;
    }

    public void setJustSpawned(Boolean b) {
        justSpawned = b;
    }

    public GamePlayer getPlayerLastDamagedBy() {
        return GameUtilities.getUtilities().getGamePlayer(Bukkit.getPlayerExact(playerLastDamagedBy));
    }

    public void setPlayerLastDamagedBy(GamePlayer player) {
        if (player != null) {
            playerLastDamagedBy = player.getName();
        } else {
            playerLastDamagedBy = this.getName();
        }
    }

    public boolean isCreatingContainer() {
        return creatingContainer;
    }

    public void setCreatingContainer(boolean bool) {
        creatingContainer = bool;
    }

    public String getMapCreatingItemFor() {
        return mapCreatingItemFor;
    }

    public void setMapCreatingItemFor(String map) {
        mapCreatingItemFor = map;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setIsDead(boolean bool) {
        isDead = bool;
    }

    public StatCollector getStatCollector() {
        return stats;
    }

    public void setCurrentKillstreak(int i) {
        currentKillstreak = i;
    }

    public int getCurrentKillstreak() {
        return currentKillstreak;
    }

    public void setArrowsFired(int i) {
        if (i != -1) {
            arrowsFired = i;
            return;
        }
        arrowsFired++;
    }

    public int getArrowsFired() {
        return arrowsFired;
    }

    public void setPointsCaptured(int i) {
        if (i != -1) {
            pointsCaptured = i;
            return;
        }
        pointsCaptured++;
    }

    public int getPointsCaptured() {
        return pointsCaptured;
    }

    public void setTimeEnteredGame() {
        timeEnteredGame = System.currentTimeMillis();
    }

    public int getTotalTimeIngame() {
        return (int) ((System.currentTimeMillis() - timeEnteredGame) / 1000);
    }

    public int getHighestKillstreak() {
        if (!killstreaks.isEmpty()) {
            return Collections.max(killstreaks);
        }
        return 0;
    }

    public void addKillstreak(Integer i) {
        killstreaks.add(i);
    }

    public void clear() {
        map = null;
        team = null;
        kills = 0;
        deaths = 0;
        totalKills = 0;
        totalDeaths = 0;
        inLobby = false;
        usingChangeClassButton = false;
        makingChangeClassButton = false;
        makingClassButton = false;
        creatingContainer = false;
        isDead = false;
        justSpawned = false;
        classButtonType = null;
        classButtonName = null;
        currentClass = null;
        savedInventoryItems = null;
        savedArmorItems = null;
        savedGameMode = null;
        savedXPCount = 0f;
        savedLevelCount = 0;
        savedFoodLevel = 0;
        savedHealth = 0;
        currentKillstreak = 0;
        arrowsFired = 0;
        pointsCaptured = 0;
        timeEnteredGame = System.currentTimeMillis();
        playerLastDamagedBy = this.getName();
        mapCreatingItemFor = null;
        killstreaks.clear();
        stats = new StatCollector(player);
    }

}
