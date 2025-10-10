package com.thelow_items_hud.thelow_items_hud.config;

import java.io.File;

import org.lwjgl.input.Keyboard;

import com.thelow_items_hud.thelow_items_hud.skills.APIListener;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration config;

    public static final String CATEGORY_GENERAL = "general";

    public static int hudX = 5;
    public static int hudY = 5;
    public static boolean hudenable = true;
    public static boolean tooltipenable = true;
    public static boolean getstatus = true;
    public static int tooltipKey = Keyboard.KEY_LSHIFT;
    public static int QuickTalkSpell = 0;

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();

        hudX = config.get(CATEGORY_GENERAL, "hudX", 5, "HUDのX座標").getInt();
        hudY = config.get(CATEGORY_GENERAL, "hudY", 5, "HUDのY座標").getInt();
        
        hudenable = config.get(CATEGORY_GENERAL, "hudenable", true, "HUDが有効かどうか").getBoolean();
        tooltipenable = config.get(CATEGORY_GENERAL, "tooltipenable", true, "tooltipが有効かどうか").getBoolean();
        getstatus = config.get(CATEGORY_GENERAL, "getstatus", true, "増加値等を自動取得するか").getBoolean();
        
        APIListener.overStrength[0] = config.get(CATEGORY_GENERAL, "OS_sword", 1.0, "剣OS等増加値").getDouble();
        APIListener.overStrength[1] = config.get(CATEGORY_GENERAL, "OS_bow", 1.0, "弓OS等増加値").getDouble();
        APIListener.overStrength[2] = config.get(CATEGORY_GENERAL, "OS_magic", 1.0, "魔法OS等増加値").getDouble();
        
        tooltipKey = config.get(CATEGORY_GENERAL, "tooltipKey", Keyboard.KEY_LSHIFT, "tooltip表示キー").getInt();
        QuickTalkSpell = config.get(CATEGORY_GENERAL, "QuickTalkSpell", 0, "CT減少パークのレベル").getInt();


        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void save() {
        config.get(CATEGORY_GENERAL, "hudX", 5, "HUDのX座標").set(hudX);
        config.get(CATEGORY_GENERAL, "hudY", 5, "HUDのY座標").set(hudY);
        config.get(CATEGORY_GENERAL, "hudenable", true,"HUDが有効かどうか").set(hudenable);
        config.get(CATEGORY_GENERAL, "tooltipenable", true,"tooltipが有効かどうか").set(tooltipenable);
        config.get(CATEGORY_GENERAL, "getstatus", true,"増加値等を自動取得するか").set(getstatus);
        config.get(CATEGORY_GENERAL, "OS_sword", 1.0, "剣OS等増加値").set(APIListener.overStrength[0]);
        config.get(CATEGORY_GENERAL, "OS_bow", 1.0, "弓OS等増加値").set(APIListener.overStrength[1]);
        config.get(CATEGORY_GENERAL, "OS_magic", 1.0, "魔法OS等増加値").set(APIListener.overStrength[2]);
        config.get(CATEGORY_GENERAL, "tooltipKey", Keyboard.KEY_LSHIFT, "tooltip表示キー").set(tooltipKey);
        config.get(CATEGORY_GENERAL, "QuickTalkSpell", 0, "CT減少パークのレベル").set(QuickTalkSpell);

        
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public static Configuration getConfig() {
        return config;
    }
}