package com.chaseoes.tf2.utilities;

import com.chaseoes.tf2.DataConfiguration;
import com.chaseoes.tf2.MapUtilities;
import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.Team;
import com.chaseoes.tf2.capturepoints.CapturePointUtilities;

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
        switch (team) {
            case RED:
                return TF2.getInstance().getMap(map).getRedSpawn() != null;
            case BLUE:
                return TF2.getInstance().getMap(map).getBlueSpawn() != null;
        }
        return null;
    }

    public Boolean playerLimitHasBeenSet() {
        if (getPlayerLimit() != null) {
            return getPlayerLimit() >= 2;
        }
        return false;
    }

    public Integer getPlayerLimit() {
        return TF2.getInstance().getMap(map).getPlayerlimit();
    }

    public Boolean timeLimitHasBeenSet() {
        if (getTimeLimit() != null) {
            return getTimeLimit() >= 1;
        }
        return false;
    }

    public Integer getTimeLimit() {
        return TF2.getInstance().getMap(map).getTimelimit();
    }

    public Boolean redTPHasBeenSet() {
        if (getRedTP() != null) {
            return getRedTP() >= 1;
        }
        return false;
    }

    public Boolean atLeastOneClassChestDefined() {
        return DataConfiguration.getData().getDataFile().getConfigurationSection("class-chest-locations") != null;
    }

    public Integer getRedTP() {
        return TF2.getInstance().getMap(map).getRedTeamTeleportTime();
    }

    public Integer totalNumberOfCapturePoints() {
        return TF2.getInstance().getMap(map).getCapturePoints().size();
    }

    public Integer getClassChests() {
        return DataConfiguration.getData().getDataFile().getConfigurationSection("class-chest-locations").getKeys(false).size();
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
        return atLeastOneClassChestDefined() && globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet(Team.RED) && teamLobbyHasBeenSet(Team.BLUE) && teamSpawnHasBeenSet(Team.RED) && teamSpawnHasBeenSet(Team.BLUE) && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet() && lobbyWallHasBeenSet();
    }

    public Boolean allGoodExceptLobbyWall() {
        return atLeastOneClassChestDefined() && globalLobbySet() && capturePointOneHasBeenSet() && teamLobbyHasBeenSet(Team.RED) && teamLobbyHasBeenSet(Team.BLUE) && teamSpawnHasBeenSet(Team.RED) && teamSpawnHasBeenSet(Team.BLUE) && playerLimitHasBeenSet() && timeLimitHasBeenSet() && redTPHasBeenSet();
    }

}
