package me.chaseoes.tf2;

import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.commands.*;
import me.chaseoes.tf2.listeners.*;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.utilities.IconMenu;
import me.chaseoes.tf2.utilities.SerializableLocation;
import me.chaseoes.tf2.utilities.UpdateChecker;
import me.chaseoes.tf2.utilities.WorldEditUtilities;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

public class TF2 extends JavaPlugin {

    public HashMap<String, Queue> queues = new HashMap<String, Queue>();
    public HashMap<String, Map> maps = new HashMap<String, Map>();
    public HashMap<String, String> usingSetSpawnMenu = new HashMap<String, String>();
    public UpdateChecker uc;
    public IconMenu setSpawnMenu;
    private static TF2 instance;

    public static TF2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getServer().getScheduler().cancelTasks(this);
        setupClasses();
        if (getServer().getPluginManager().getPlugin("TagAPI") == null) {
            getLogger().log(Level.SEVERE, "The TagAPI plugin is required to run TF2. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
            getLogger().log(Level.SEVERE, "The WorldEdit plugin is required to run TF2. Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("tf2").setExecutor(new CommandManager());
        getConfig().options().copyDefaults(true);
        saveConfig();
        Schedulers.getSchedulers().startAFKChecker();

        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            addMap(map, GameStatus.WAITING);
        }

        for (String map : MapUtilities.getUtilities().getDisabledMaps()) {
            addMap(map, GameStatus.DISABLED);
        }

        LobbyWall.getWall().startTask();

        uc = new UpdateChecker(this);
        uc.startTask();

        setSpawnMenu = new IconMenu("Set Spawns", 9, new IconMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(IconMenu.OptionClickEvent event) {
                String map = usingSetSpawnMenu.get(event.getPlayer().getName());
                String name = ChatColor.stripColor(event.getName());
                if (name.equalsIgnoreCase("Blue Lobby")) {
                    MapUtilities.getUtilities().setTeamLobby(map, Team.BLUE, event.getPlayer().getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the blue team's lobby.");
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase("Red Lobby")) {
                    MapUtilities.getUtilities().setTeamLobby(map, Team.RED, event.getPlayer().getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the red team's lobby.");
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase("Blue Spawn")) {
                    MapUtilities.getUtilities().setTeamSpawn(map, Team.BLUE, event.getPlayer().getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the blue team's spawn.");
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                } else if (name.equalsIgnoreCase("Red Spawn")) {
                    MapUtilities.getUtilities().setTeamSpawn(map, Team.RED, event.getPlayer().getLocation());
                    event.getPlayer().sendMessage(ChatColor.YELLOW + "[TF2] Successfully set the red team's spawn.");
                    usingSetSpawnMenu.remove(event.getPlayer().getName());
                }
                event.setWillClose(true);
            }
        }, this).setOption(2, new ItemStack(Material.getMaterial(331), 1), ChatColor.DARK_RED + "" + ChatColor.BOLD + "Red Lobby" + ChatColor.RESET, ChatColor.WHITE + "Set the red team lobby.").setOption(3, new ItemStack(Material.getMaterial(351), 1, (short) 4), ChatColor.AQUA + "" + ChatColor.BOLD + "Blue Lobby" + ChatColor.RESET, ChatColor.WHITE + "Set the blue team lobby.").setOption(4, new ItemStack(Material.WOOL, 1, (short) 14), ChatColor.DARK_RED + "" + ChatColor.BOLD + "Red Spawn" + ChatColor.RESET, ChatColor.WHITE + "Set the red team's spawn.").setOption(5, new ItemStack(Material.WOOL, 1, (short) 11), ChatColor.AQUA + "" + ChatColor.BOLD + "Blue Spawn" + ChatColor.RESET, ChatColor.WHITE + "Set the blue team's spawn.").setOption(6, new ItemStack(Material.BEDROCK, 1), ChatColor.RED + "" + ChatColor.BOLD + "Exit" + ChatColor.RESET, ChatColor.RED + "Exit this menu.");
    }

    @Override
    public void onDisable() {
        reloadConfig();
        saveConfig();
        for (Map map : MapUtilities.getUtilities().getMaps()) {
            GameUtilities.getUtilities().getGame(map).stopMatch();
        }
        getServer().getScheduler().cancelTasks(this);
        instance = null;
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
        SerializableLocation.getUtilities().setup(this);

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
        pm.registerEvents(new PlayerReceiveNameTagListener(), this);
        pm.registerEvents(new PotionSplashListener(), this);
        pm.registerEvents(new ProjectileLaunchListener(), this);
        pm.registerEvents(new SignChangeListener(), this);
        pm.registerEvents(new TF2DeathListener(), this);
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new EntityDamageListener(), this);
    }

    public Queue getQueue(String map) {
        return queues.get(map);
    }

    public Map getMap(String map) {
        return maps.get(map);
    }

    public void addMap(String map, GameStatus status) {
        Map m = new Map(this, map);
        maps.put(map, m);
        GameUtilities.getUtilities().addGame(m);
        queues.put(map, new Queue(m.getName()));
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
        game.stopMatch();
        MapUtilities.getUtilities().destroyMap(m);
    }

    public boolean mapExists(String map) {
        return maps.containsKey(map);
    }
}
