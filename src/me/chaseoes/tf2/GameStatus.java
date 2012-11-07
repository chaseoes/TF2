package me.chaseoes.tf2;

public class GameStatus {
    
    String status;
    public static GameStatus WAITING = new GameStatus("WAITING");
    public static GameStatus INGAME = new GameStatus("INGAME");
    public static GameStatus STARTING = new GameStatus("STARTING");
    public static GameStatus DISABLED = new GameStatus("DISABLED");
    
    private GameStatus(String s) {
        status = s;
    }

}
