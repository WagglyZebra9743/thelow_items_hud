package com.thelow_items_hud.thelow_items_hud.config;

import java.util.List;

import com.thelow_items_hud.thelow_items_hud.skills.APIListener;

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
        APIListener.overStrength[0] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_sword", APIListener.overStrength[0]).getDouble();
        APIListener.overStrength[1] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_bow", APIListener.overStrength[1]).getDouble();
        APIListener.overStrength[2] = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "OS_magic", APIListener.overStrength[2]).getDouble();
        ConfigHandler.hudX = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudX", ConfigHandler.hudX).getInt();
        ConfigHandler.hudY = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudY", ConfigHandler.hudY).getInt();
        ConfigHandler.hudenable = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "hudenable", ConfigHandler.hudenable).getBoolean();
        ConfigHandler.tooltipenable = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "tooltipenable", ConfigHandler.tooltipenable).getBoolean();
        ConfigHandler.getstatus = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "getstatus", ConfigHandler.getstatus).getBoolean();
        ConfigHandler.tooltipKey = ConfigHandler.getConfig().get(ConfigHandler.CATEGORY_GENERAL, "tooltipKey", ConfigHandler.tooltipKey).getInt();

        // 反映された値をファイルに保存
        ConfigHandler.save();
        
    }
}
