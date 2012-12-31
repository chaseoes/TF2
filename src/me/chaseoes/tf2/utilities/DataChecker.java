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

    public Boolean teamLobbyHasBeenSet(Team team) {
        switch (team) {
            case RED:
                return TF2.getInstance().getMap(map).getRedLobby() != null;
            case BLUE:
                return TF2.getInstance().getMap(map).getBlueLobby() != null;
        }
        return null;
    }

    public Boolean teamSpawnHasBeenSet(Team team) {
        switch (team){
            case RED:
                return TF2.getInstance().getMap(map).getRedSpawn() != null;
            case BLUE:
                return TF2.getInstance().getMap(map).getBlueSpawn() != null;
        }
        return null;
    }

    public Boolean playerLimitHasBeenSet() {
        return TF2.getInstance().getMap(map).getPlayerlimit() >= 2;
    }

    public Integer getPlayerLimit() {
        return TF2.getInstance().getMap(map).getPlayerlimit();
    }

    public Boolean timeLimitHasBeenSet() {
        return TF2.getInstance().getMap(map).getTimelimit() >= 1;
    }

    public Integer getTimeLimit() {
        return TF2.getInstance().getMap(map).getTimelimit();
    }

    public Boolean redTPHasBeenSet() {
        return TF2.getInstance().getMap(map).getRedTeamTeleportTime() >= 1;
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
        return DataConfiguration.getData().getDataFile().getString("lobbywall." + map + ".w") != null;
    }

    public Boolean allGood() {
        return globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet(Team.RED) && teamLobbyHasBeenSet(Team.BLUE) && teamSpawnHasBeenSet(Team.RED) && teamSpawnHasBeenSet(Team.BLUE) && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet() && lobbyWallHasBeenSet();
    }
    
    public Boolean allGoodExceptLobbyWall() {
        return globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet(Team.RED) && teamLobbyHasBeenSet(Team.BLUE) && teamSpawnHasBeenSet(Team.RED) && teamSpawnHasBeenSet(Team.BLUE) && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet();
    }

}
