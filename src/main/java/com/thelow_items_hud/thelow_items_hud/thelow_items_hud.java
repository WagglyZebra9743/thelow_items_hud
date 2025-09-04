package com.thelow_items_hud.thelow_items_hud;

import java.io.File;

import com.thelow_items_hud.thelow_items_hud.commands.item_hud;
import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;
import com.thelow_items_hud.thelow_items_hud.skills.ChatFilter;
import com.thelow_items_hud.thelow_items_hud.skills.skill;
import com.thelow_items_hud.thelow_items_hud.skills.timer;
import com.thelow_items_hud.thelow_items_hud.tooltip.ItemHover;
import com.thelow_items_hud.thelow_items_hud.tooltip.Keyclick;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "thelow_items_hud", version = "1.1",guiFactory = "com.thelow_items_hud.thelow_items_hud.config.GuiFactory")
public class thelow_items_hud {

	private APIListener apiListener = new APIListener();
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        ConfigHandler.loadConfig(configFile);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        thelow_item_hudHUD.register();
        ClientCommandHandler.instance.registerCommand(new item_hud());
        MinecraftForge.EVENT_BUS.register(apiListener);
        MinecraftForge.EVENT_BUS.register(new ChatFilter());
        MinecraftForge.EVENT_BUS.register(new timer());
        MinecraftForge.EVENT_BUS.register(new skill());
        MinecraftForge.EVENT_BUS.register(new Keyclick());
        
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new ItemHover());
        
        
    }
}
