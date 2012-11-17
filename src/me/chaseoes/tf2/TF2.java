package me.chaseoes.tf2;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.chaseoes.tf2.capturepoints.CapturePointUtilities;
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
import me.chaseoes.tf2.listeners.InteractListener;
import me.chaseoes.tf2.lobbywall.LobbyWall;
import me.chaseoes.tf2.lobbywall.LobbyWallUtilities;
import me.chaseoes.tf2.lobbywall.WorldEditUtilities;

import org.bukkit.plugin.java.JavaPlugin;

public class TF2 extends JavaPlugin {

    public HashMap<String, Queue> queues = new HashMap<String, Queue>();
    public HashMap<String, Map> maps = new HashMap<String, Map>();

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
        getServer().getPluginManager().registerEvents(new PlayerListeners(), this);
        getServer().getPluginManager().registerEvents(new InteractListener(), this);
        getConfig().options().copyDefaults(true);
        saveConfig();
        Schedulers.getSchedulers().startAFKChecker();
        
        for (String map : DataConfiguration.getData().getDataFile().getStringList("enabled-maps")) {
            maps.put(map, new Map(map));
            queues.put(map, new Queue(map));
            LobbyWall.getWall().update();
            GameUtilities.getUtilities().redHasBeenTeleported.put(map, false);
        }
        
        String heybukkitdev = "Hey fellow BukkitDev staff! The lines below simply color names in the /list command (ListCommand.java). Don't worry. :)";
        GameUtilities.getUtilities().coolpeople.add("chaseoes");
        GameUtilities.getUtilities().coolpeople.add("skitscape");
        GameUtilities.getUtilities().coolpeople.add("AntVenom");
        GameUtilities.getUtilities().coolpeople.add("Fawdz");
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onDisable() {
        saveConfig();
        for (String map : MapUtilities.getUtilities().getEnabledMaps()) {
            GameUtilities.getUtilities().stopMatch(map);
        }
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
    }

    public Queue getQueue(String map) {
        return queues.get(map);
    }

    public Map getMap(String map) {
        return maps.get(map);
    }

}
