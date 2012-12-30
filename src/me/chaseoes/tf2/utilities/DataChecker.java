package me.chaseoes.tf2.utilities;

import me.chaseoes.tf2.DataConfiguration;
import me.chaseoes.tf2.MapUtilities;
import me.chaseoes.tf2.TF2;
import me.chaseoes.tf2.Team;
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
        return MapUtilities.getUtilities().loadTeamLobby(map, Team.match(team)) != null;
    }

    public Boolean teamSpawnHasBeenSet(String team) {
        return MapUtilities.getUtilities().loadTeamSpawn(map, Team.match(team)) != null;
    }

    public Boolean playerLimitHasBeenSet() {
        if (TF2.getInstance().getMap(map).getPlayerlimit() >= 2) {
            return true;
        }
        return false;
    }

    public Integer getPlayerLimit() {
        return TF2.getInstance().getMap(map).getPlayerlimit();
    }

    public Boolean timeLimitHasBeenSet() {
        if (TF2.getInstance().getMap(map).getTimelimit() >= 1) {
            return true;
        }
        return false;
    }

    public Integer getTimeLimit() {
        return TF2.getInstance().getMap(map).getTimelimit();
    }

    public Boolean redTPHasBeenSet() {
        if (TF2.getInstance().getMap(map).getRedTeamTeleportTime() >= 1) {
            return true;
        }
        return false;
    }

    public Integer getRedTP() {
        return TF2.getInstance().getMap(map).getRedTeamTeleportTime();
    }

    public Integer totalNumberOfCapturePoints() {
        return TF2.getInstance().getMap(map).getCapturePoints().size();
    }

    public Boolean globalLobbySet() {
        try {
            MapUtilities.getUtilities().loadLobby();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public Boolean lobbyWallHasBeenSet() {
        if (DataConfiguration.getData().getDataFile().getString("lobbywall." + map + ".w") != null) {
            return true;
        }
        return false;
    }

    public Boolean allGood() {
        if (globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet("red") && teamLobbyHasBeenSet("blue") && teamSpawnHasBeenSet("red") && teamSpawnHasBeenSet("blue") && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet() && lobbyWallHasBeenSet()) {
            return true;
        }
        return false;
    }
    
    public Boolean allGoodExceptLobbyWall() {
        if (globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet("red") && teamLobbyHasBeenSet("blue") && teamSpawnHasBeenSet("red") && teamSpawnHasBeenSet("blue") && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet()) {
            return true;
        }
        return false;
    }

}
