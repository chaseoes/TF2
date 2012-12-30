package me.chaseoes.tf2;

public enum Team {

    RED("red"),
    BLUE("blue");

    private final String name;

    private Team (String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static Team match(String str){
        for (Team team : Team.values()){
            if(team.getName().equalsIgnoreCase(str))
                return team;
        }
        return null;
    }
}
