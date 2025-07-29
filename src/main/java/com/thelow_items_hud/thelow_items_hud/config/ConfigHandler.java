package com.thelow_items_hud.thelow_items_hud.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration config;

    private static final String CATEGORY_GENERAL = "general";

    public static int hudX = 5;
    public static int hudY = 5;

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();

        hudX = config.get(CATEGORY_GENERAL, "hudX", 5, "HUDのX座標").getInt();
        hudY = config.get(CATEGORY_GENERAL, "hudY", 5, "HUDのY座標").getInt();

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void save() {
        config.get(CATEGORY_GENERAL, "hudX", hudX).set(hudX);
        config.get(CATEGORY_GENERAL, "hudY", hudY).set(hudY);
        if (config.hasChanged()) {
            config.save();
        }
    }
}