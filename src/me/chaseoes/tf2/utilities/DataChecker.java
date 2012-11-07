package me.chaseoes.tf2.utilities;

import me.chaseoes.tf2.MapConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.capturepoints.CapturePointUtilities;

public class DataChecker {

    String map;

    public DataChecker(String m) {
        map = m;
    }

    public Boolean capturePointOneHasBeenSet() {
        try {
            CapturePointUtilities.getUtilities().getLocationFromID(map, 1);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean teamLobbyHasBeenSet(String team) {
        try {
            MapUtilities.getUtilities().loadTeamLobby(map, team);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean teamSpawnHasBeenSet(String team) {
        try {
            MapUtilities.getUtilities().loadTeamSpawn(map, team);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean playerLimitHasBeenSet() {
        if (MapConfiguration.getMaps().getMap(map).getInt("playerlimit") >= 0) {
            return true;
        }
        return false;
    }

    public Integer getPlayerLimit() {
        return MapConfiguration.getMaps().getMap(map).getInt("playerlimit");
    }

    public Boolean timeLimitHasBeenSet() {
        if (MapConfiguration.getMaps().getMap(map).getInt("timelimit") >= 0) {
            return true;
        }
        return false;
    }

    public Integer getTimeLimit() {
        return MapConfiguration.getMaps().getMap(map).getInt("timelimit");
    }

    public Boolean redTPHasBeenSet() {
        if (MapConfiguration.getMaps().getMap(map).getInt("teleport-red-team") >= 0) {
            return true;
        }
        return false;
    }

    public Integer getRedTP() {
        return MapConfiguration.getMaps().getMap(map).getInt("teleport-red-team");
    }

    public Integer totalNumberOfCapturePoints() {
        return MapConfiguration.getMaps().getMap(map).getConfigurationSection("capture-points").getKeys(false).size();
    }

    public Boolean globalLobbySet() {
        try {
            MapUtilities.getUtilities().loadLobby();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Boolean allGood() {
        if (globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet("red") && teamLobbyHasBeenSet("blue") && teamSpawnHasBeenSet("red") && teamSpawnHasBeenSet("blue") && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet()) {
            return true;
        }
        return false;
    }

}
