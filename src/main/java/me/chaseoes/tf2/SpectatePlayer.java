package me.chaseoes.tf2;

import me.chaseoes.tf2.classes.TF2Class;
import me.chaseoes.tf2.utilities.Localizer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpectatePlayer {

    Player player;
    public boolean isSpectating;
    public String gameSpectating;

    ItemStack[] savedInventoryItems;
    ItemStack[] savedArmorItems;
    GameMode savedGameMode;
    float savedXPCount;
    int savedLevelCount;
    int savedFoodLevel;
    int savedHealth;

    public SpectatePlayer(Player p) {
        player = p;
        savedInventoryItems = null;
        savedArmorItems = null;
        savedGameMode = null;
        savedXPCount = 0;
        savedFoodLevel = 0;
        savedHealth = 0;
        isSpectating = false;
    }

    public void saveInventory() {
        savedInventoryItems = player.getInventory().getContents();
        savedArmorItems = player.getInventory().getArmorContents();
        savedXPCount = player.getExp();
        savedLevelCount = player.getLevel();
        savedFoodLevel = player.getFoodLevel();
        savedHealth = player.getHealth();
        savedGameMode = player.getGameMode();
    }

    @SuppressWarnings("deprecation")
    public void loadInventory() {
        player.getInventory().setContents(savedInventoryItems);
        player.getInventory().setArmorContents(savedArmorItems);
        player.setExp(savedXPCount);
        player.setLevel(savedLevelCount);
        player.setFoodLevel(savedFoodLevel);
        player.setHealth(savedHealth);
        player.setGameMode(savedGameMode);
        player.updateInventory();
    }

    public void toggleSpectating(Game game) {
        if (!isSpectating) {
            gameSpectating = game.getMapName();
            for (GamePlayer gp : game.playersInGame.values()) {
                gp.getPlayer().hidePlayer(player);
            }
            saveInventory();
            new TF2Class("NONE").clearInventory(player);
            player.setLevel(0);
            player.setExhaustion(0);
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setExp(0);
            player.setGameMode(GameMode.ADVENTURE);
            player.setAllowFlight(true);
            player.setFlying(true);
            player.teleport(game.map.getRedSpawn());
            player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-NOW-SPECTATING"));
            isSpectating = true;
        } else {
            gameSpectating = null;
            for (GamePlayer gp : game.playersInGame.values()) {
                gp.getPlayer().showPlayer(player);
            }
            player.setFlying(false);
            player.setAllowFlight(false);
            loadInventory();
            player.teleport(MapUtilities.getUtilities().loadLobby());
            player.sendMessage(Localizer.getLocalizer().loadPrefixedMessage("PLAYER-NO-LONGER-SPECTATING"));
            clear();
        }
    }

    public void clear() {
        savedInventoryItems = null;
        savedArmorItems = null;
        savedGameMode = null;
        savedXPCount = 0;
        savedFoodLevel = 0;
        savedHealth = 0;
        isSpectating = false;
    }

}
