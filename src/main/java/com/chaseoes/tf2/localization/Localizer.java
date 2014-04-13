package com.chaseoes.tf2.localization;

import com.chaseoes.tf2.TF2;
import com.chaseoes.tf2.localization.replacer.StringReplacer;
import com.google.common.io.Files;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;

public class Localizer {

    public final I18N NO_PERMISSION = new I18N(this, "NO-PERMISSION");
    public final I18N NO_CONSOLE = new I18N(this, "NO-CONSOLE");
    public final I18N WRONG_ARGS = new I18N(this, "WRONG-ARGS");
    public final I18N WRONG_ARGS_USAGE = new I18N(this, "WRONG-ARGS-USAGE", new StringReplacer("usage"));
    public final I18N UNKNOWN_COMMAND = new I18N(this, "UNKNOWN-COMMAND");
    public final I18N UNKNOWN_COMMAND_HELP = new I18N(this, "UNKNOWN-COMMAND-HELP");

    public final I18N MAPNAME_TOO_LONG = new I18N(this, "MAPNAME-TOO-LONG", new StringReplacer("map"));
    public final I18N MAP_ALREADY_EXISTS = new I18N(this, "MAP-ALREADY-EXISTS", new StringReplacer("map"));
    public final I18N MAP_DOES_NOT_EXIST = new I18N(this, "MAP-DOES-NOT-EXIST", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_CREATED = new I18N(this, "MAP-SUCCESSFULLY-CREATED", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_DELETED = new I18N(this, "MAP-SUCCESSFULLY-DELETED", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_DISABLED_ALL = new I18N(this, "MAP-SUCCESSFULLY-DISABLED-ALL");
    public final I18N MAP_SUCCESSFULLY_ENABLED_ALL = new I18N(this, "MAP-SUCCESSFULLY-ENABLED-ALL");
    public final I18N MAP_SUCCESSFULLY_DISABLED_SINGLE = new I18N(this, "MAP-SUCCESSFULLY-DISABLED-SINGLE", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_ENABLED_SINGLE = new I18N(this, "MAP-SUCCESSFULLY-ENABLED-SINGLE", new StringReplacer("map"));
    public final I18N MAP_ALREADY_DISABLED = new I18N(this, "MAP-ALREADY-DISABLED", new StringReplacer("map"));
    public final I18N MAP_ALREADY_ENABLED = new I18N(this, "MAP-ALREADY-ENABLED", new StringReplacer("map"));
    public final I18N MAP_ALREADY_STARTING = new I18N(this, "MAP-ALREADY-STARTING");
    public final I18N MAP_ALREADY_INGAME = new I18N(this, "MAP-ALREADY-INGAME");
    public final I18N MAP_NOT_SETUP = new I18N(this, "MAP-NOT-SETUP");
    public final I18N MAP_NOT_SETUP_COMMAND_HELP = new I18N(this, "MAP-NOT-SETUP-COMMAND-HELP", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_REDEFINED = new I18N(this, "MAP-SUCCESSFULLY-REDEFINED", new StringReplacer("map"));
    public final I18N MAP_SUCCESSFULLY_STARTED = new I18N(this, "MAP-SUCCESSFULLY-STARTED");
    public final I18N MAP_SUCCESSFULLY_STOPPED_ALL = new I18N(this, "MAP-SUCCESSFULLY-STOPPED-ALL");
    public final I18N MAP_SUCCESSFULLY_STOPPED_SINGLE = new I18N(this, "MAP-SUCCESSFULLY-STOPPED-SINGLE");
    public final I18N MAP_SUCCESSFULLY_SET_PLAYERLIMIT = new I18N(this, "MAP-SUCCESSFULLY-SET-PLAYERLIMIT", new StringReplacer("map"), new IntReplacer("playerlimit"));
    public final I18N MAP_SUCCESSFULLY_SET_CAPTUREPOINT = new I18N(this, "MAP-SUCCESSFULLY-SET-CAPTUREPOINT", new StringReplacer("map"), new IntReplacer("id"));
    public final I18N MAP_SUCCESSFULLY_SET_TIMELIMIT = new I18N(this, "MAP-SUCCESSFULLY-SET-TIMELIMIT", new StringReplacer("map"), new IntReplacer("time"));
    public final I18N MAP_SUCCESSFULLY_SET_REDTP = new I18N(this, "MAP-SUCCESSFULLY-SET-REDTP", new StringReplacer("map"), new IntReplacer("time"));


    public final I18N MAP_INFO_DISABLED = new I18N(this, "MAP-INFO-DISABLED");
    public final I18N MAP_INFO_FULL = new I18N(this, "MAP-INFO-FULL");
    public final I18N MAP_INFO_TEAM_FULL = new I18N(this, "MAP-INFO-TEAM-FULL");
    public final I18N MAP_INFO_NOT_INGAME = new I18N(this, "MAP-INFO-NOT-INGAME");

    public final I18N WORLDEDIT_NO_REGION = new I18N(this, "WORLDEDIT-NO-REGION");

    public final I18N BUTTON_CLASS_CREATE = new I18N(this, "BUTTON-CLASS-CREATE", new StringReplacer("type"), new StringReplacer("class"));
    public final I18N BUTTON_CLASS_CREATED = new I18N(this, "BUTTON-CLASS-CREATED", new StringReplacer("type"), new StringReplacer("class"));
    public final I18N BUTTON_CLASS_REMOVE = new I18N(this, "BUTTON-CLASS-REMOVE");
    public final I18N BUTTON_CHANGE_CLASS_CREATE = new I18N(this, "BUTTON-CHANGE-CLASS-CREATE");
    public final I18N BUTTON_CHANGE_CLASS_CREATED = new I18N(this, "BUTTON-CHANGE-CLASS-CREATED");
    public final I18N BUTTON_CHANGE_CLASS_REMOVE = new I18N(this, "BUTTON-CHANGE-CLASS-REMOVE");
    public final I18N CONTAINER_CREATE = new I18N(this, "CONTAINER-CREATE");
    public final I18N CONTAINER_CREATED = new I18N(this, "CONTAINER-CREATED");
    public final I18N CONTAINER_ALREADY_REGSITERED = new I18N(this, "CONTAINER-ALREADY-REGISTERED");
    public final I18N CONTAINER_REMOVE = new I18N(this, "CONTAINER-REMOVE");
    public final I18N CLASS_CHEST_CREATE = new I18N(this, "CLASS-CHEST-CREATE");
    public final I18N CLASS_CHEST_NO_CHEST = new I18N(this, "CLASS-CHEST-NO-CHEST");

    public final I18N DEBUG_UPLOADING = new I18N(this, "DEBUG-UPLOADING");
    public final I18N DEBUG_SUCCESS = new I18N(this, "DEBUG-SUCCESS");
    public final I18N DEBUG_ERROR = new I18N(this, "DEBUG-ERROR");

    public final I18N HELP_JOIN = new I18N(this, "HELP-JOIN");
    public final I18N HELP_LEAVE = new I18N(this, "HELP-LEAVE");
    public final I18N HELP_LIST = new I18N(this, "HELP-LIST");
    public final I18N HELP_START = new I18N(this, "HELP-START");
    public final I18N HELP_STOP = new I18N(this, "HELP-STOP");
    public final I18N HELP_CREATE = new I18N(this, "HELP-CREATE");

    public final I18N PLAYER_LEAVE_GAME = new I18N(this, "PLAYER-LEAVE-GAME");
    public final I18N PLAYER_NOT_PLAYING = new I18N(this, "PLAYER-LEAVE-GAME");
    public final I18N PLAYER_JOIN_MAP = new I18N(this, "PLAYER-JOIN-MAP", new StringReplacer("map"));
    public final I18N PLAYER_JOIN_FULL_MAP = new I18N(this, "PLAYER-JOIN-FULL-MAP");
    public final I18N PLAYER_ALREADY_PLAYING = new I18N(this, "PLAYER-ALREADY-PLAYING");
    public final I18N PLAYER_ALREADY_SPECTATING = new I18N(this, "PLAYER-ALREADY-SPECTATING");
    public final I18N PLAYER_NOW_SPECTATING = new I18N(this, "PLAYER-NOW-SPECTATING");
    public final I18N PLAYER_NO_LONGER_SPECTATING = new I18N(this, "PLAYER-NO-LONGER-SPECTATING");
    public final I18N PLAYER_NOT_SPECTATING = new I18N(this, "PLAYER-NOT-SPECTATING");
    public final I18N PLAYER_TELEPORT_GLOBAL_LOBBY = new I18N(this, "PLAYER-TELEPORT-GLOBAL-LOBBY");
    public final I18N PLAYER_KICKED_FOR_AFK = new I18N(this, "PLAYER-KICKED-FOR-AFK");
    public final I18N PLAYER_INVENTORY_MOVING_BLOCKED = new I18N(this, "PLAYER-INVENTORY-MOVING-BLOCKED");
    public final I18N PLAYER_KILLED_BY = new I18N(this, "PLAYER-KILLED-BY", new StringReplacer("teamcolor"), new StringReplacer("player"), new StringReplacer("class"));
    public final I18N PLAYER_KILLED = new I18N(this, "PLAYER-KILLED", new StringReplacer("teamcolor"), new StringReplacer("player"), new StringReplacer("class"));
    public final I18N PLAYER_NOT_IN_MAP = new I18N(this, "PLAYER-NOT-IN-MAP");

    public final I18N GLOBAL_LOBBY_NOT_SET = new I18N(this, "GLOBAL-LOBBY-NOT-SET");
    public final I18N GLOBAL_LOBBY_SET = new I18N(this, "GLOBAL-LOBBY-SET");

    public final I18N LIST_DISPLAYING_MAP = new I18N(this, "LIST-DISPLAYING-MAP", new StringReplacer("map"));

    public final I18N RED_TEAM = new I18N(this, "RED-TEAM");
    public final I18N RED = new I18N(this, "RED");
    public final I18N BLUE_TEAM = new I18N(this, "BLUE-TEAM");
    public final I18N BLUE = new I18N(this, "BLUE");

    public final I18N CONFIG_RELOADED = new I18N(this, "CONFIG-RELOADED");

    public final I18N ERROR_PLAYERLIMIT_ODD = new I18N(this, "ERROR-PLAYERLIMIT-ODD");
    public final I18N ERROR_NOT_INTEGER = new I18N(this, "ERROR-NOT-INTEGER", new StringReplacer("int"));
    public final I18N ERROR_CHANGE_CLASS = new I18N(this, "ERROR-CHANGE-CLASS");

    public final I18N GAMESTATUS_INGAME = new I18N(this, "GAMESTATUS-INGAME");
    public final I18N GAMESTATUS_STARTING = new I18N(this, "GAMESTATUS-STARTING");
    public final I18N GAMESTATUS_WAITING = new I18N(this, "GAMESTATUS-WAITING");
    public final I18N GAMESTATUS_DISABLED = new I18N(this, "GAMESTATUS-DISABLED");
    public final I18N GAMESTATUS_NOT_STARTED = new I18N(this, "GAMESTATUS-NOT-STARTED");

    public final I18N TELEPORT_AFTER_CHOOSE_CLASS = new I18N(this, "TELEPORT-AFTER-CHOOSE-CLASS");
    public final I18N GAME_END = new I18N(this, "GAME-END");
    public final I18N GAME_WIN = new I18N(this, "GAME-WIN", new StringReplacer("team"), new StringReplacer("map"));
    public final I18N WINS = new I18N(this, "WINS");
    public final I18N IN_LINE = new I18N(this, "IN-LINE", new IntReplacer("position"));
    public final I18N PERCENT_JOIN = new I18N(this, "PERCENT-JOIN", new IntReplacer("percent"));
    public final I18N RED_TEAM_TELEPORTED_IN = new I18N(this, "RED-TEAM-TELEPORTED-IN", new IntReplacer("time"));
    public final I18N GAME_STARTING_IN = new I18N(this, "GAME-STARTING-IN", new IntReplacer("time"));
    public final I18N GAME_ENDING_IN = new I18N(this, "GAME-ENDING-IN", new StringReplacer("time"));

    public final I18N SETSPAWN_TITLE = new I18N(this, "SETSPAWN-TITLE");
    public final I18N SETSPAWN_BLUE_LOBBY = new I18N(this, "SETSPAWN-BLUE-LOBBY");
    public final I18N SETSPAWN_BLUE_LOBBY_DESC = new I18N(this, "SETSPAWN-BLUE-LOBBY-DESC");
    public final I18N SETSPAWN_RED_LOBBY = new I18N(this, "SETSPAWN-RED-LOBBY");
    public final I18N SETSPAWN_RED_LOBBY_DESC = new I18N(this, "SETSPAWN-RED-LOBBY-DESC");
    public final I18N SETSPAWN_BLUE_SPAWN = new I18N(this, "SETSPAWN-BLUE-SPAWN");
    public final I18N SETSPAWN_BLUE_SPAWN_DESC = new I18N(this, "SETSPAWN-BLUE-SPAWN-DESC");
    public final I18N SETSPAWN_RED_SPAWN = new I18N(this, "SETSPAWN-RED-SPAWN");
    public final I18N SETSPAWN_RED_SPAWN_DESC = new I18N(this, "SETSPAWN-RED-SPAWN-DESC");
    public final I18N SETSPAWN_EXIT = new I18N(this, "SETSPAWN-EXIT");
    public final I18N SETSPAWN_EXIT_DESC = new I18N(this, "SETSPAWN-EXIT-DESC");

    public final I18N CP_MUST_CAPTURE_PREVIOUS = new I18N(this, "CP-MUST-CAPTURE-PREVIOUS", new IntReplacer("id"));
    public final I18N CP_BEING_CAPTURED = new I18N(this, "CP-BEING-CAPTURED", new IntReplacer("id"));
    public final I18N CP_ALREADY_CAPTURED_RED = new I18N(this, "CP-ALREADY-CAPTURED-RED", new IntReplacer("id"));
    public final I18N CP_ALREADY_CAPTURED_BLUE = new I18N(this, "CP-ALREADY-CAPTURED-BLUE");
    public final I18N CP_ALREADY_CAPTURING = new I18N(this, "CP-ALREADY-CAPTURING", new StringReplacer("player"));
    public final I18N CP_CAPTURED = new I18N(this, "CP-CAPTURED", new IntReplacer("id"), new StringReplacer("player"));
    public final I18N CP_REMOVE = new I18N(this, "CP-REMOVE");
    public final I18N CP_WRONG_TEAM = new I18N(this, "CP-WRONG-TEAM");

    public final I18N DOES_NOT_EXIST_CLASS = new I18N(this, "DOES-NOT-EXIST-CLASS", new StringReplacer("class"));
    public final I18N CANT_USE_COMMANDS_INGAME = new I18N(this, "CANT-USE-COMMANDS-INGAME");
    public final I18N CANT_DROP_ITEMS_INGAME = new I18N(this, "CANT-DROP-ITEMS-INGAME");

    public final I18N LOBBYWALL_CREATED = new I18N(this, "LOBBYWALL-CREATED");
    public final I18N LOBBYWALL_REMOVE = new I18N(this, "LOBBYWALL-REMOVE");
    public final I18N LOBBYWALL_MAP_DISABLED = new I18N(this, "LOBBYWALL-MAP-DISABLED");
    public final I18N LOBBYWALL_JOIN_1 = new I18N(this, "LOBBYWALL-JOIN-1");
    public final I18N LOBBYWALL_JOIN_2 = new I18N(this, "LOBBYWALL-JOIN-2");
    public final I18N LOBBYWALL_JOIN_3 = new I18N(this, "LOBBYWALL-JOIN-3");
    public final I18N LOBBYWALL_STATUS_1 = new I18N(this, "LOBBYWALL-STATUS-1");
    public final I18N LOBBYWALL_STATUS_2 = new I18N(this, "LOBBYWALL-STATUS-2");
    public final I18N LOBBYWALL_STATUS_4 = new I18N(this, "LOBBYWALL-STATUS-4");
    public final I18N LOBBYWALL_TEAMS_1 = new I18N(this, "LOBBYWALL-TEAMS-1");
    public final I18N LOBBYWALL_TEAMS_2 = new I18N(this, "LOBBYWALL-TEAMS-2", new StringReplacer("players"));
    public final I18N LOBBYWALL_TEAMS_3 = new I18N(this, "LOBBYWALL-TEAMS-3");
    public final I18N LOBBYWALL_TEAMS_4 = new I18N(this, "LOBBYWALL-TEAMS-4", new StringReplacer("players"));
    public final I18N LOBBYWALL_TIME_1 = new I18N(this, "LOBBYWALL-TIME-1");
    public final I18N LOBBYWALL_TIME_2 = new I18N(this, "LOBBYWALL-TIME-2");
    public final I18N LOBBYWALL_TIME_4 = new I18N(this, "LOBBYWALL-TIME-4");
    public final I18N LOBBYWALL_CP_1 = new I18N(this, "LOBBYWALL-CP-1");
    public final I18N LOBBYWALL_CP_3 = new I18N(this, "LOBBYWALL-CP-3");

    public final I18N CP_CAPTURE_STATUS_CAP_UNCAPTURED = new I18N(this, "CP-CAPTURE-STATUS-CAP-UNCAPTURED");
    public final I18N CP_CAPTURE_STATUS_CAP_CAPTURING = new I18N(this, "CP-CAPTURE-STATUS-CAP-CAPTURING");
    public final I18N CP_CAPTURE_STATUS_CAP_CAPTURED = new I18N(this, "CP-CAPTURE-STATUS-CAP-CAPTURED");
    public final I18N CP_CAPTURE_STATUS_CAPTURED = new I18N(this, "CP-CAPTURE-STATUS-CAPTURED");

    private transient final File file;
    private transient final String localization;
    private transient YamlConfiguration config;

    public Localizer(String localization) throws IOException, InvalidConfigurationException {
        this.localization = localization + ".yml";
        this.file = new File(TF2.getInstance().getDataFolder(), "localization/" + this.localization);
        reload();
    }

    public void reload() throws IOException, InvalidConfigurationException {
        config = new YamlConfiguration();
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        config.loadFromString(Files.toString(file, Charset.forName("UTF-8")));
        InputStream in = TF2.getInstance().getResource("localization/" + localization);
        if (in != null) {
            YamlConfiguration def = new YamlConfiguration();
            def.load(in);
            in.close();
            config.setDefaults(def);
            config.options().copyDefaults(true);
            saveMessages();
        }
        test();
    }

    private void test() {
        for (Field f : this.getClass().getDeclaredFields()) {
            if (Modifier.isTransient(f.getModifiers())) {
                continue;
            }
            if (f.getType().equals(I18N.class)) {
                I18N i18n = null;
                try {
                    i18n = (I18N) f.get(this);
                    getConfigString(i18n.configNode);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (RuntimeException ex) {
                    System.err.println("Localization file does not contain '" + i18n.configNode + "'");
                }
            }
        }
    }

    public String getConfigString(String configNode) {
        if (!config.contains(configNode) || !config.isString(configNode)) {
            throw new RuntimeException("This localization file does not contain the localization for " + configNode);
        }
        return config.getString(configNode);
    }

    private void saveMessages() {
        try {
            String data = config.saveToString();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            out.write(data, 0, data.length());
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
