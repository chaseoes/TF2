package com.chaseoes.tf2.localization;

import com.chaseoes.tf2.TF2;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class Localizers {

    private static final Localizers INSTANCE = new Localizers();

    private Localizer defLoc;

    private Localizers() {

    }

    public static Localizers getInstance() {
        return INSTANCE;
    }

    public static Localizer getDefaultLoc() {
        return getInstance().defLoc;
    }

    public void reload() {
        String loc = TF2.getInstance().getConfig().getString("default-localization");
        try {
            defLoc = new Localizer(loc);
        } catch (Exception ex) {
            TF2.getInstance().getLogger().severe("Failed to load translation file, " + ex.getMessage());
            TF2.getInstance().getLogger().severe("Loading english instead");
            try {
                defLoc = new Localizer("english");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

}
