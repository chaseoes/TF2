package me.chaseoes.tf2;

import java.util.HashMap;
import java.util.logging.Level;

import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
import me.chaseoes.tf2.classes.ClassUtilities;
import me.chaseoes.tf2.commands.CommandManager;
import me.chaseoes.tf2.commands.CreateCommand;
import me.chaseoes.tf2.commands.DebugCommand;
import me.chaseoes.tf2.commands.DeleteCommand;
import me.chaseoes.tf2.commands.DisableCommand;
import me.chaseoes.tf2.commands.EnableCommand;
import me.chaseoes.tf2.commands.JoinCommand;
import me.chaseoes.tf2.commands.LeaveCommand;
import me.chaseoes.tf2.commands.ListCommand;
import me.chaseoes.tf2.commands.ReloadCommand;
import me.chaseoes.tf2.commands.SetCommand;
import me.chaseoes.tf2.listeners.BlockPlaceListener;
import me.chaseoes.tf2.listeners.FoodLevelChangeListener;
import me.chaseoes.tf2.listeners.InteractListener;
import me.chaseoes.tf2.listeners.PlayerCommandPreprocessListener;
import me.chaseoes.tf2.listeners.PlayerDamageByEntityListener;
import me.chaseoes.tf2.listeners.PlayerDeathListener;
import me.chaseoes.tf2.listeners.PlayerDropItemListener;
import me.chaseoes.tf2.listeners.PlayerJoinListener;
import me.chaseoes.tf2.listeners.PlayerMoveListener;
import me.chaseoes.tf2.listeners.PlayerQuitListener;
import me.chaseoes.tf2.listeners.PlayerReceiveNameTagListener;
import me.chaseoes.tf2.listeners.PotionSplashListener;
import me.chaseoes.tf2.listeners.ProjectileLaunchListener;
import me.chaseoes.tf2.listeners.SignChangeListener;
import me.chaseoes.tf2.listeners.TF2DeathListener;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.lobbywall.WorldEditUtilities;
import me.chaseoes.tf2.utilities.SerializableLocation;
import me.chaseoes.tf2.utilities.UpdateChecker;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TF2 extends JavaPlugin {

    public HashMap<String, Queue> queues = new HashMap<String, Queue>();
    public HashMap<String, Map> maps = new HashMap<String, Map>();
    public UpdateChecker uc;

    @Override
    public void onEnable() {
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

        for (String map : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
            maps.put(map, new Map(map));
            GameUtilities.getUtilities().addGame(maps.get(map));
            queues.put(map, new Queue(map));
            GameUtilities.getUtilities().setRedHasBeenTeleported(map, false);
        }

        GameUtilities.getUtilities().coolpeople.add("chaseoes");
        GameUtilities.getUtilities().coolpeople.add("skitscape");
        GameUtilities.getUtilities().coolpeople.add("AntVenom");
        GameUtilities.getUtilities().coolpeople.add("Fawdz");
        GameUtilities.getUtilities().coolpeople.add("Double0Negative");

        LobbyWall.getWall().startTask();

        uc = new UpdateChecker(this);
        uc.startTask();
    }

    @Override
    public void onDisable() {
        saveConfig();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            GameUtilities.getUtilities().stopMatch(map);
        }
        getServer().getScheduler().cancelTasks(this);
    }

    public void setupClasses() {
        MapConfiguration.getMaps().setup(this);
        MapUtilities.getUtilities().setup(this);
        WorldEditUtilities.getWEUtilities().setup(this);
        CreateCommand.getCommand().setup(this);
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
        SerializableLocation.getUtilities().setup(this);

        // Register Events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockPlaceListener(), this);
        pm.registerEvents(new FoodLevelChangeListener(), this);
        pm.registerEvents(new InteractListener(), this);
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
    }

    public Queue getQueue(String map) {
        return queues.get(map);
    }

    public Map getMap(String map) {
        return maps.get(map);
    }

}
