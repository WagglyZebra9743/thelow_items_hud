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
        String msg = event.message.getUnformattedText();

        if (msg.startsWith("$api")) {
            String[] split = msg.split(" ", 2);
            if (split.length == 2) {
                try {
                	JsonObject json = new JsonParser().parse(split[1]).getAsJsonObject();
                    String apiType = json.get("apiType").getAsString();
                    if ("skill_cooltime".equals(apiType)) {
                        JsonObject response = json.getAsJsonObject("response");
                        System.out.println(response);
                        String skillname = response.get("name").getAsString();
                        System.out.println("skill:"+skillname);
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
                        skillname=null;
                        }
                    }
                    Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(apiType));
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
