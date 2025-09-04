package com.thelow_items_hud.thelow_items_hud.tooltip;

import org.lwjgl.input.Keyboard;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Keyclick {

    // 左Shiftが押されている間だけ true
    public static boolean enable = false;
    
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
    	if(Minecraft.getMinecraft().currentScreen == null)return;
        // 「現在割り当てられているキー」を取得
        int keyCode = ConfigHandler.tooltipKey;

        // そのキーが押されているか確認（GUIを開いていても検出）
        enable = Keyboard.isKeyDown(keyCode);
    }
}