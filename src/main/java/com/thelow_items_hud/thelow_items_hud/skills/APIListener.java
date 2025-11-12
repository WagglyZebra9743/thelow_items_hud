package com.thelow_items_hud.thelow_items_hud.skills;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thelow_items_hud.thelow_items_hud.thelow_items_hud;
import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class APIListener {

    private static String latestData = null;
    public static boolean status_getted = false;
    public static boolean tickenable = false;
    public static String itemID = "";
    private final int INTERVAL = 200;
    private static int cmd_ct = 0;
    private static boolean[] flags = {false,false,false,false};
    private static int subscribeitemCTdelay = 0;
    private static boolean version_Checked = false;
    private static int SendVersionTimer = 0;
    
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        
        String msg = event.message.getUnformattedText();
    	final String colormsg = event.message.getFormattedText();
    	if(!version_Checked&&ConfigHandler.AutoVersionCheck&&colormsg.startsWith("§r§a倉庫データを取得しました")) {
    		SendVersionText();
    		SendVersionTimer = 41;
        }
        
        if(flags[2]&&msg.contains("職業「")&&msg.endsWith("」を選択しました。")) {
        	mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        	flags[2] = false;
        }
        
        if (msg.startsWith("$api")) {
            String[] split = msg.split(" ", 2);
            if (split.length == 2) {
                try {
                	JsonObject json = new JsonParser().parse(split[1]).getAsJsonObject();
                	if(json==null||json.isJsonNull()||!json.has("apiType")||!json.has("response"))return;
                    String apiType = json.get("apiType").getAsString();
                    if(apiType==null||apiType.equals("")||apiType.isEmpty())return;
                    if ("skill_cooltime".equals(apiType)) {
                        JsonObject response = json.getAsJsonObject("response");
                        if(response==null||!response.has("name"))return;
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
                    	 if(!ConfigHandler.getstatus&&json!=null&&!json.has("response"))return;
                    	 JsonObject response = json.getAsJsonObject("response");
                    	 if(response==null||!response.has("overStrengthSword")||!response.has("overStrengthBow")||!response.has("overStrengthMagic"))return;
                    	 ConfigHandler.overStrength[0] = response.get("overStrengthSword").getAsDouble();
                    	 ConfigHandler.overStrength[0]+=1;
                    	 ConfigHandler.overStrength[1] = response.get("overStrengthBow").getAsDouble();
                    	 ConfigHandler.overStrength[1]+=1;
                    	 ConfigHandler.overStrength[2] = response.get("overStrengthMagic").getAsDouble();
                    	 ConfigHandler.overStrength[2]+=1;
                    	 status_getted = true;
                    	 ConfigHandler.save();
                     }
                     if("item_cooltime".equals(apiType)) {
                    	 JsonObject response = json.getAsJsonObject("response");
                         if(response==null||!response.has("itemId")||!response.has("cooltime"))return;
                         itemID = response.get("itemId").getAsString();
                         int cooltimetick = (int) response.get("cooltime").getAsDouble()*20;
                         timer.itemCTtimer = cooltimetick;
                     }
                     
                } catch (Exception e) {
                    mc.thePlayer.addChatMessage(new ChatComponentText("§a[thelow_items_hud]§c 解析失敗: " + e.getMessage()));
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void APIcancel(ClientChatReceivedEvent event) {
        String message = event.message.getUnformattedText(); // 色コードや装飾を除去したテキスト
        
        String colormsg = event.message.getFormattedText();
        if(!flags[0]&&colormsg.equals("§r§a正常にプレイヤーデータをロードしました。§r")) {
        	timer.Reconnected();
        	status_getted = false;
        	timer.tick=0;
        	tickenable = true;
            flags[0]=true;
        	
        	mc.thePlayer.sendChatMessage("/thelow_api subscribe SKILL_COOLTIME");
        }
        
        if(!flags[1]&&colormsg.startsWith("§r§a倉庫データを取得しました")){
        	if(ConfigHandler.overStrength==null||ConfigHandler.overStrength[0]==1.0||ConfigHandler.overStrength[1]==1.0||ConfigHandler.overStrength[2]==1.0) {
        		mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        	}else {
        		status_getted = true;
        	}
            flags[1]=true;
        }
        
        if(flags[2]&&colormsg.contains("§r§e[転生]")) {
        	mc.thePlayer.sendChatMessage("/thelow_api detailed_status");
        	flags[2]=false;
        }
        if(!flags[3]&&colormsg.contains("§r§aインベントリ")) {
        	flags[3]=true;
        }
        // "$api"で始まるか判定
        if (message.startsWith("$api")) {
            event.setCanceled(true); // この行で表示をキャンセル
        }
    }
    


    public static String getother() {
        return latestData;
    }
    
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;//TickEventはSTARTとENDの2回発火するので1回にする
        if(SendVersionTimer>0) {
        	SendVersionTimer--;
        	if(SendVersionTimer==0) {
        		SendVersionText();
        	}
        }
        if(flags[2]) {
        	cmd_ct=0;
        }else {
        	cmd_ct++;
        	if(cmd_ct%INTERVAL==0) {
        		flags[2]=true;
        		cmd_ct=0;
        		
        	}
        }
        if(flags[3]) {
        	subscribeitemCTdelay++;
        	if(subscribeitemCTdelay>=27) {
        		mc.thePlayer.sendChatMessage("/thelow_api subscribe ITEM_COOLTIME");
        		subscribeitemCTdelay = 0;
        		flags[3] = false;
        	}
        }
    }
    public static void ReSubscribe_itemCT() {
    	flags[3] = true;
    }
    
    private static void sendchat(final String text,final EntityPlayerSP thePlayer) {
    	if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
        }
    }
    
    private static void sendClickableLink(final String url) {
    	
        IChatComponent component = new ChatComponentText(url);

        ChatStyle style = new ChatStyle();
        
        ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
        style.setChatClickEvent(clickEvent);
        
        style.setColor(EnumChatFormatting.AQUA); // 水色にする
        style.setUnderlined(true); // 下線を引く

        component.setChatStyle(style);

        if (Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(component);
        }
    }
    
    private static void SendVersionText() {
		if(thelow_items_hud.latestver.equals(""))return;
		version_Checked = true;
		int status = thelow_items_hud.the_status;
		if(thelow_items_hud.CustomMsg!=null&&!thelow_items_hud.CustomMsg.equals("")&&!thelow_items_hud.CustomMsg.equals("OK")) {
			sendchat("§a[thelow_quest_helper]" + thelow_items_hud.CustomMsg,mc.thePlayer);
		}
		if(status==-1)return;
		switch (status){
			case 0:{//安定バージョン
				sendchat("§a[thelow_items_hud]§7新バージョンが利用可能です"+thelow_items_hud.VERSION_STRING+"→"+thelow_items_hud.latestver,mc.thePlayer);
				sendClickableLink("https://github.com/WagglyZebra9743/thelow_items_hud/releases/latest");
				return;
			}
			case 1:{//特殊な使い方をすると不具合が出る
				sendchat("§a[thelow_items_hud]§e軽微な不具合があるバージョンです",mc.thePlayer);
				sendchat("§e新バージョンが利用可能です"+thelow_items_hud.VERSION_STRING+"→"+thelow_items_hud.latestver,mc.thePlayer);
				sendClickableLink("https://github.com/WagglyZebra9743/thelow_items_hud/releases/latest");
				return;
			}
			case 2:{//人によっては表示が崩れる等の不具合が出る
				sendchat("§a[thelow_items_hud]§6中程度な不具合があるバージョンです",mc.thePlayer);
				sendchat("§6新バージョンが利用可能です"+thelow_items_hud.VERSION_STRING+"→"+thelow_items_hud.latestver,mc.thePlayer);
				sendClickableLink("https://github.com/WagglyZebra9743/thelow_items_hud/releases/latest");
				return;
			}
			case 3:{//不具合が出るしクラッシュ等も起きる
				sendchat("§a[thelow_items_hud]§c重大な不具合があるバージョンです",mc.thePlayer);
				sendchat("§c新バージョンに更新することを推奨します"+thelow_items_hud.VERSION_STRING+"→"+thelow_items_hud.latestver,mc.thePlayer);
				sendClickableLink("https://github.com/WagglyZebra9743/thelow_items_hud/releases/latest");
				return;
			}
		}
    }
}