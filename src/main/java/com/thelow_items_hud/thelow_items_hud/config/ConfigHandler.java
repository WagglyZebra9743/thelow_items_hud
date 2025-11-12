package com.thelow_items_hud.thelow_items_hud.config;

import java.io.File;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

    private static Configuration config;

    public static final String CATEGORY_GENERAL = "general";
    private static final String DEFAULT_VERSIONCHECK_TEXT = "バージョン情報を自動で確認するかどうか";
    private static final String DEFAULT_SENDMCID_TEXT = "バージョン確認時にmcidを送信するか";

    public static int hudX = 5;
    public static int hudY = 5;
    public static boolean hudenable = true;
    public static boolean tooltipenable = true;
    public static boolean getstatus = true;
    public static boolean itemCThudenable = true;
    public static int itemCThudX = 450;
    public static int itemCThudY = 5;
    public static int tooltipKey = Keyboard.KEY_LSHIFT;
    public static int QuickTalkSpell = 0;
    public static double[] overStrength = {1.0,1.0,1.0};
    public static boolean AutoVersionCheck = true;
    public static boolean SendMCID = false;

    public static void loadConfig(File configFile) {
        config = new Configuration(configFile);
        config.load();

        hudX = config.get(CATEGORY_GENERAL, "hudX", 5, "HUDのX座標").getInt();
        hudY = config.get(CATEGORY_GENERAL, "hudY", 5, "HUDのY座標").getInt();
        
        hudenable = config.get(CATEGORY_GENERAL, "hudenable", true, "HUDが有効かどうか").getBoolean();
        tooltipenable = config.get(CATEGORY_GENERAL, "tooltipenable", true, "tooltipが有効かどうか").getBoolean();
        getstatus = config.get(CATEGORY_GENERAL, "getstatus", true, "増加値等を自動取得するか").getBoolean();
        
        itemCThudenable = config.get(CATEGORY_GENERAL, "itemCThudenable", true, "アイテムCTを表示するか").getBoolean();
        itemCThudX = config.get(CATEGORY_GENERAL, "itemCThudX", 450, "アイテムCT表示のX座標").getInt();
        itemCThudY = config.get(CATEGORY_GENERAL, "itemCThudY", 5, "アイテムCT表示のY座標").getInt();
        
        overStrength[0] = config.get(CATEGORY_GENERAL, "OS_sword", 1.0, "剣OS等増加値").getDouble();
        overStrength[1] = config.get(CATEGORY_GENERAL, "OS_bow", 1.0, "弓OS等増加値").getDouble();
        overStrength[2] = config.get(CATEGORY_GENERAL, "OS_magic", 1.0, "魔法OS等増加値").getDouble();
        
        tooltipKey = config.get(CATEGORY_GENERAL, "tooltipKey", Keyboard.KEY_LSHIFT, "tooltip表示キー").getInt();
        QuickTalkSpell = Math.max(0, config.get(CATEGORY_GENERAL, "QuickTalkSpell", 0, "CT減少パークのレベル").getInt());
        
        AutoVersionCheck = config.get(CATEGORY_GENERAL, "AutoVersionCheck", true, DEFAULT_VERSIONCHECK_TEXT).getBoolean();
        SendMCID = config.get(CATEGORY_GENERAL, "SendMCID", false, DEFAULT_SENDMCID_TEXT).getBoolean();


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
        config.get(CATEGORY_GENERAL, "itemCThudenable", true,"アイテムCTを表示するか").set(itemCThudenable);
        config.get(CATEGORY_GENERAL, "itemCThudX", 450, "アイテムCTHUDのX座標").set(itemCThudX);
        config.get(CATEGORY_GENERAL, "itemCThudY", 5, "アイテムCTHUDのY座標").set(itemCThudY);
        config.get(CATEGORY_GENERAL, "OS_sword", 1.0, "剣OS等増加値").set(overStrength[0]);
        config.get(CATEGORY_GENERAL, "OS_bow", 1.0, "弓OS等増加値").set(overStrength[1]);
        config.get(CATEGORY_GENERAL, "OS_magic", 1.0, "魔法OS等増加値").set(overStrength[2]);
        config.get(CATEGORY_GENERAL, "tooltipKey", Keyboard.KEY_LSHIFT, "tooltip表示キー").set(tooltipKey);
        config.get(CATEGORY_GENERAL, "QuickTalkSpell", 0, "CT減少パークのレベル").set(QuickTalkSpell);
        config.get(CATEGORY_GENERAL, "AutoVersionCheck", true,DEFAULT_VERSIONCHECK_TEXT).set(AutoVersionCheck);
        config.get(CATEGORY_GENERAL, "SendMCID", false,DEFAULT_SENDMCID_TEXT).set(SendMCID);

        
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public static Configuration getConfig() {
        return config;
    }
}