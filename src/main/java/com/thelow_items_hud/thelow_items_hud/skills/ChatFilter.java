package com.thelow_items_hud.thelow_items_hud.skills;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatFilter {
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText(); // 色コードや装飾を除去したテキスト
        
        // "$API"で始まるか判定
        if (message.startsWith("$API")) {
            event.setCanceled(true); // この行で表示をキャンセル
        }
    }
}