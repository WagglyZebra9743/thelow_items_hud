package com.thelow_items_hud.thelow_items_hud.config;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ModConfigGui extends GuiConfig {
	
    public ModConfigGui(GuiScreen parentScreen) {
        super(parentScreen,
                getConfigElements(),
                "thelow_items_hud", // modid
                false,
                false,
                "thelow_items_hud Config");
    }

    private static List<IConfigElement> getConfigElements() {
        return new ConfigElement(ConfigHandler.getConfig().getCategory(ConfigHandler.CATEGORY_GENERAL))
                .getChildElements();
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();

        // GUIで変更された値を ConfigHandler のフィールドに反映
        ConfigHandler.overStrength[0] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_sword", 1.0).getDouble();
        ConfigHandler.overStrength[1] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_bow", 1.0).getDouble();
        ConfigHandler.overStrength[2] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_magic", 1.0).getDouble();
        ConfigHandler.QuickTalkSpell = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "QuickTalkSpell", 0).getInt();
        ConfigHandler.hudX = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudX", 5).getInt();
        ConfigHandler.hudY = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudY", 5).getInt();
        ConfigHandler.hudenable = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudenable", true).getBoolean();
        ConfigHandler.tooltipenable = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "tooltipenable", true).getBoolean();
        ConfigHandler.getstatus = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "getstatus", true).getBoolean();
        ConfigHandler.itemCThudenable = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL,"itemCThudenable",true).getBoolean();
        ConfigHandler.itemCThudX = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL,"itemCThudX",450).getInt();
        ConfigHandler.itemCThudY = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL,"itemCThudY",5).getInt();
        ConfigHandler.tooltipKey = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "tooltipKey", Keyboard.KEY_LSHIFT).getInt();
        ConfigHandler.AutoVersionCheck = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "AutoVersionCheck", true).getBoolean();
        ConfigHandler.SendMCID = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "SendMCID", false).getBoolean();

        // 反映された値をファイルに保存
        ConfigHandler.save();
        
    }
}
