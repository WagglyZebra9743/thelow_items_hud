package com.thelow_items_hud.thelow_items_hud.skills;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class APIListener {

    private static String latestData = null;
    public static double[] overStrength = {1.0,1.0,1.0};
    public static boolean status_getted = false;
    public static boolean tickenable = false;
    
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        
        String msg = event.message.getUnformattedText();
        
        if(msg.contains("職業「")&&msg.contains("」を選択しました。")) {
        	mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        }
        
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
                     if("detailed_status".equals(apiType)) {
                    	 if(!ConfigHandler.getstatus)return;
                    	 JsonObject response = json.getAsJsonObject("response");
                    	 overStrength[0] = response.get("overStrengthSword").getAsDouble();
                    	 overStrength[0]+=1;
                    	 overStrength[1] = response.get("overStrengthBow").getAsDouble();
                    	 overStrength[1]+=1;
                    	 overStrength[2] = response.get("overStrengthMagic").getAsDouble();
                    	 overStrength[2]+=1;
                    	 status_getted = true;
                    	 ConfigHandler.save();
                     }
                      
                      
                      
                      
                     
                } catch (Exception e) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§a[thelow_items_hud]§c 解析失敗: " + e.getMessage()));
                }
            }
        }
    }
    
    @SubscribeEvent
    public void APIcancel(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText(); // 色コードや装飾を除去したテキスト
        
        String colormsg = event.message.getFormattedText();
        if(colormsg.equals("§r§a正常にプレイヤーデータをロードしました。§r")) {
        	timer.Reconnected();
        	status_getted = false;
        	timer.tick=0;
        	tickenable = true;
        	
        	mc.thePlayer.sendChatMessage("/thelow_api subscribe SKILL_COOLTIME");
        }
        
        if(colormsg.startsWith("§r§a倉庫データを取得しました")){
        	mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        }
        
        if(colormsg.contains("§r§e[転生]")) {
        	mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        }
        
        // "$API"で始まるか判定
        if (message.startsWith("$API")) {
            event.setCanceled(true); // この行で表示をキャンセル
        }
    }
    


    public static String getother() {
        return latestData;
    }
}
