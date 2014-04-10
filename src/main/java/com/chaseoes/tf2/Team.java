package com.chaseoes.tf2;

import org.bukkit.Color;

public enum Team {

    RED("red"), BLUE("blue");

    private final String name;

    private Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Team match(String str) {
        for (Team team : Team.values()) {
            if (team.getName().equalsIgnoreCase(str)) {
                return team;
            }
        }
        return null;
    }

    public Color getColor() {
        if (this == Team.RED) {
            return Color.RED;
        }
        return Color.BLUE;
    }
}
