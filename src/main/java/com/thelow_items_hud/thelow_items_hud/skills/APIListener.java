package com.thelow_items_hud.thelow_items_hud.skills;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class APIListener {

    private static String latestData = null;
    

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        String colormsg = event.message.getFormattedText();
        if(colormsg.equals(" §r§a正常にプレイヤーデータをロードしました。§r")) {
        	timer.Reconnected();
        }

        String msg = event.message.getUnformattedText();
        
        if (msg.startsWith("$api")) {
            String[] split = msg.split(" ", 2);
            if (split.length == 2) {
                try {
                	JsonObject json = new JsonParser().parse(split[1]).getAsJsonObject();
                    String apiType = json.get("apiType").getAsString();
                    if ("skill_cooltime".equals(apiType)) {
                        JsonObject response = json.getAsJsonObject("response");
                        String skillname = response.get("name").getAsString();
                        if(skillname!=null) {
                        if(skillname.equals("予兆")) {
                        	timer.Yochou();
                        }
                        if(skillname.equals("開放")) {
                        	timer.Kaihou();
                        }
                        if(skillname.equals("覚醒")) {
                        	skill.kaihou = false;
                        }
                        if(skillname.equals("マジックボール")||skillname.equals("ライトニングボルト")||skillname.equals("ファイヤ・ボルケーノ")||skillname.equals("メテオストライク")) {
                        	timer.EisyouReset();
                        }
                        skillname=null;
                        }
                    }
                } catch (Exception e) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a[thelow_items_hud]§c 解析失敗: " + e.getMessage()));
                }
            }
        }
    }

    public static String getother() {
        return latestData;
    }
}
