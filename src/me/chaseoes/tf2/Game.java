package me.chaseoes.tf2;

public class Game {
    
//    TF2 plugin;
//    static Game instance = new Game();
//    Map map;
//    GameStatus status;
//    
//    public Game(Map m) {
//        map = m;
//    }
//    
//    public static GameUtilities getGame() {
//        return instance;
//    }
//
//    public void setup(TF2 p) {
//        plugin = p;
//    }
//    
//    public void start() {
//        setStatus(GameStatus.INGAME);
//        // CapturePointUtilities.getUtilities().setAllUncaptured(map);
//        final int limit = MapConfiguration.getMaps().getMap(map).getInt("timelimit");
//        timelimitcounter.put(map, plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
//            int current = 0;
//
//            @Override
//            public void run() {
//                LobbyWall.getWall().update();
//                current++;
//                if (current >= limit) {
//                    winGame(map, "blue");
//                    stopTimeLimitCounter(map);
//                    gametimes.remove(map);
//                }
//                gametimes.put(map, current);
//            }
//        }, 0L, 20L));
//        
//        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
//            public void run() {
//                for (String playe : getIngameList(map)) {
//                    if (getTeam(plugin.getServer().getPlayerExact(playe)).equalsIgnoreCase("red")) {
//                        plugin.getServer().getPlayerExact(playe).teleport(MapUtilities.getUtilities().loadTeamSpawn(map, getTeam(plugin.getServer().getPlayerExact(playe))));
//                    }
//                }
//            }
//        }, 200L);
//        for (Player player : getIngameList(map)) {
//            if (getTeam(p).equalsIgnoreCase("blue")) {
//            p.teleport(MapUtilities.getUtilities().loadTeamSpawn(map, getTeam(p)));
//            } else {
//                    if (getTeam(p).equalsIgnoreCase("red")) {
//                        p.sendMessage("§e[TF2] §4§lRed §r§eteam, you will be teleported in 10 seconds.");
//                    }
//            }
//        }
//    }
//    
//    public void stop() {
//        
//    }
//    
//    public void add(Player player) {
//        
//    }
//    
//    public void remove(Player player) {
//        
//    }
//    
//    public void setStatus(GameStatus s) {
//        status = s;
//    }
//    
//    public void getStatus() {
//        
//    }

}
