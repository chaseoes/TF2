package com.chaseoes.tf2;

import com.chaseoes.tf2.capturepoints.CapturePointUtilities;
import com.chaseoes.tf2.classes.ClassUtilities;
import com.chaseoes.tf2.commands.*;
import com.chaseoes.tf2.listeners.*;
import com.chaseoes.tf2.lobbywall.LobbyWall;
import com.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import com.chaseoes.tf2.localization.Localizers;
import com.chaseoes.tf2.utilities.*;
import net.gravitydevelopment.updater.Updater;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

public class TF2 extends JavaPlugin {

    public HashMap<String, Map> maps = new HashMap<String, Map>();
    public HashMap<String, String> usingSetSpawnMenu = new HashMap<String, String>();
    public HashMap<String, StatCollector> stats = new HashMap<String, StatCollector>();
    public IconMenu setSpawnMenu;
    private static TF2 instance;
    public boolean enabled;
    public boolean isDisabling;

    public static TF2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        isDisabling = false;
        getServer().getScheduler().cancelTasks(this);

        setupClasses();

        if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            getLogger().log(Level.SEVERE, pluginRequiredMessage("WorldEdit"));
            getServer().getPluginManager().disablePlugin(this);
            enabled = false;
            return;
        }

        getCommand("tf2").setExecutor(new CommandManager());
        getConfig().options().copyDefaults(true);
        saveConfig();
        DataConfiguration.getData().reloadData();
        Localizers.getInstance().reload();

        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            addMap(map, GameStatus.WAITING);
        }

        for (String map : MapUtilities.getUtilities().getDisabledMaps()) {
            addMap(map, GameStatus.DISABLED);
        }

        Schedulers.getSchedulers().startAFKChecker();

        LobbyWall.getWall().startTask();

        setSpawnMenu = new IconMenu(Localizers.getDefaultLoc().SETSPAWN_TITLE.getString(), 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                String map = usingSetSpawnMenu.get(event.getPlayer().getName());
                String name = ChatColor.stripColor(event.getName());
                if (name.equalsIgnoreCase(Localizers.getDefaultLoc().SETSPAWN_BLUE_LOBBY.getString())) {
                    MapUtilities.getUtilities().setTeamLobby(map, Team.BLUE, event.getPlayer().getLocation());
                    Localizers.getDefaultLoc().SETSPAWN_BLUE_LOBBY_DESC.sendPrefixed(event.getPlayer());
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase(Localizers.getDefaultLoc().SETSPAWN_RED_LOBBY.getString())) {
                    MapUtilities.getUtilities().setTeamLobby(map, Team.RED, event.getPlayer().getLocation());
                    Localizers.getDefaultLoc().SETSPAWN_RED_LOBBY_DESC.sendPrefixed(event.getPlayer());
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase(Localizers.getDefaultLoc().SETSPAWN_BLUE_SPAWN.getString())) {
                    MapUtilities.getUtilities().setTeamSpawn(map, Team.BLUE, event.getPlayer().getLocation());
                    Localizers.getDefaultLoc().SETSPAWN_BLUE_SPAWN_DESC.sendPrefixed(event.getPlayer());
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase(Localizers.getDefaultLoc().SETSPAWN_RED_SPAWN.getString())) {
                    MapUtilities.getUtilities().setTeamSpawn(map, Team.RED, event.getPlayer().getLocation());
                    Localizers.getDefaultLoc().SETSPAWN_RED_SPAWN_DESC.sendPrefixed(event.getPlayer());
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                }
                event.setWillClose(true);
            }
        }, this).setOption(2, new ItemStack(Material.REDSTONE, 1), ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizers.getDefaultLoc().SETSPAWN_RED_LOBBY.getString() + ChatColor.RESET, ChatColor.WHITE + Localizers.getDefaultLoc().SETSPAWN_RED_LOBBY_DESC.getString()).setOption(3, new ItemStack(Material.INK_SACK, 1, (short) 4), ChatColor.AQUA + "" + ChatColor.BOLD + Localizers.getDefaultLoc().SETSPAWN_BLUE_LOBBY.getString() + ChatColor.RESET, ChatColor.WHITE + Localizers.getDefaultLoc().SETSPAWN_BLUE_LOBBY_DESC.getString()).setOption(4, new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.DARK_RED + "" + ChatColor.BOLD + Localizers.getDefaultLoc().SETSPAWN_RED_SPAWN.getString() + ChatColor.RESET, ChatColor.WHITE + Localizers.getDefaultLoc().SETSPAWN_RED_SPAWN_DESC.getString()).setOption(5, new ItemStack(Material.WOOL, 1, (short) 11), ChatColor.AQUA + "" + ChatColor.BOLD + Localizers.getDefaultLoc().SETSPAWN_BLUE_SPAWN.getString() + ChatColor.RESET, ChatColor.WHITE + Localizers.getDefaultLoc().SETSPAWN_BLUE_SPAWN_DESC.getString()).setOption(6, new ItemStack(Material.BEDROCK, 1), ChatColor.RED + "" + ChatColor.BOLD + Localizers.getDefaultLoc().SETSPAWN_EXIT.getString() + ChatColor.RESET, ChatColor.RED + Localizers.getDefaultLoc().SETSPAWN_EXIT_DESC.getString());

        if (getConfig().getBoolean("auto-update")) {
            if (!getDescription().getVersion().contains("SNAPSHOT")) {
                new Updater(this, 46264, this.getFile(), Updater.UpdateType.DEFAULT, false);
            }
        }

        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit Metrics!
        }

        // Connect to database after everything else has loaded.
        if (getConfig().getBoolean("stats-database.enabled")) {
            SQLUtilities.getUtilities().setup(this);
        }

        enabled = true;
    }

    @Override
    public void onDisable() {
        isDisabling = true;
        if (enabled) {
            reloadConfig();
            saveConfig();
            for (Map map : MapUtilities.getUtilities().getMaps()) {
                if (GameUtilities.getUtilities().getGame(map).getStatus() != GameStatus.DISABLED) {
                    GameUtilities.getUtilities().getGame(map).stopMatch(false);
                }
            }
            instance = null;
        }
        getServer().getScheduler().cancelTasks(this);
        enabled = false;
    }

    public void setupClasses() {
        MapUtilities.getUtilities().setup(this);
        WorldEditUtilities.getWEUtilities().setup(this);
        CreateCommand.getCommand().setup(this);
        RedefineCommand.getCommand().setup(this);
        LobbyWall.getWall().setup(this);
        DataConfiguration.getData().setup(this);
        LobbyWallUtilities.getUtilities().setup(this);
        WorldEditUtilities.getWEUtilities().setupWorldEdit(getServer().getPluginManager());
        ClassUtilities.getUtilities().setup(this);
        GameUtilities.getUtilities().setup(this);
        CapturePointUtilities.getUtilities().setup(this);
        Schedulers.getSchedulers().setup(this);
        CreateCommand.getCommand().setup(this);
        DeleteCommand.getCommand().setup(this);
        DisableCommand.getCommand().setup(this);
        EnableCommand.getCommand().setup(this);
        JoinCommand.getCommand().setup(this);
        LeaveCommand.getCommand().setup(this);
        ListCommand.getCommand().setup(this);
        ReloadCommand.getCommand().setup(this);
        SetCommand.getCommand().setup(this);
        DebugCommand.getCommand().setup(this);
        StartCommand.getCommand().setup(this);
        StopCommand.getCommand().setup(this);

        // Register Events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new FoodLevelChangeListener(), this);
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new PlayerCommandPreprocessListener(), this);
        pm.registerEvents(new PlayerDamageByEntityListener(), this);
        pm.registerEvents(new PlayerDeathListener(), this);
        pm.registerEvents(new PlayerDropItemListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
        pm.registerEvents(new PlayerQuitListener(), this);
        pm.registerEvents(new PotionSplashListener(), this);
        pm.registerEvents(new ProjectileLaunchListener(), this);
        pm.registerEvents(new SignChangeListener(), this);
        pm.registerEvents(new TF2DeathListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new EntityDamageListener(), this);
        pm.registerEvents(new EntityShootBowListener(), this);
        pm.registerEvents(new InventoryClickListener(this), this);
        pm.registerEvents(new PlayerRespawnListener(), this);
    }

    public Map getMap(String map) {
        return maps.get(map);
    }

    public void addMap(String map, GameStatus status) {
        Map m = new Map(this, map);
        Game g = new Game(m, this);
        maps.put(map, m);
        GameUtilities.getUtilities().addGame(m, g);
        m.load();
        GameUtilities.getUtilities().getGame(m).redHasBeenTeleported = false;
        GameUtilities.getUtilities().getGame(m).setStatus(status);
        if (status == GameStatus.DISABLED) {
            String[] creditlines = new String[4];
            creditlines[0] = " ";
            creditlines[1] = "--------------------------";
            creditlines[2] = "--------------------------";
            creditlines[3] = " ";
            LobbyWall.getWall().setAllLines(map, null, creditlines, false, false);
        }
    }

    public Collection<Map> getMaps() {
        return maps.values();
    }

    public void removeMap(String map) {
        Map m = maps.remove(map);
        Game game = GameUtilities.getUtilities().removeGame(m);
        game.stopMatch(false);
        LobbyWall.getWall().unloadCacheInfo(map);
        MapUtilities.getUtilities().destroyMap(m);
    }

    public boolean mapExists(String map) {
        return maps.containsKey(map);
    }

    public String pluginRequiredMessage(String plugin) {
        return "\n------------------------------ [ ERROR ] ------------------------------\n-----------------------------------------------------------------------\n\n" + plugin + " is REQUIRED to run TF2!\nPlease download " + plugin + ", or TF2 will NOT work!\nDownload at: " + getPluginURL(plugin) + "\nTF2 is now being disabled...\n\n-----------------------------------------------------------------------\n-----------------------------------------------------------------------";
    }

    public String getPluginURL(String plugin) {
        if (plugin.equalsIgnoreCase("WorldEdit")) {
            return "http://dev.bukkit.org/server-mods/worldedit/";
        }
        return "";
    }
}
