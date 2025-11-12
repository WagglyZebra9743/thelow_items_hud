package com.thelow_items_hud.thelow_items_hud;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.thelow_items_hud.thelow_items_hud.commands.item_hud;
import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;
import com.thelow_items_hud.thelow_items_hud.skills.skill;
import com.thelow_items_hud.thelow_items_hud.skills.timer;
import com.thelow_items_hud.thelow_items_hud.tooltip.InventoryParkSelector;
import com.thelow_items_hud.thelow_items_hud.tooltip.ItemHover;
import com.thelow_items_hud.thelow_items_hud.tooltip.Keyclick;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "thelow_items_hud", version = "1.6",guiFactory = "com.thelow_items_hud.thelow_items_hud.config.GuiFactory")
public class thelow_items_hud {
	
	public static String MOD_ID;
    public static String VERSION_STRING;
    private static String key = "";
    private static String API_URL = "https://script.google.com/macros/s/"+key+"/exec";
    public static String latestver = "";
    public static int the_status = -1;
    public static String CustomMsg = "";
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        ConfigHandler.loadConfig(configFile);
        
    //  イベントから ModMetadata を取得
        ModMetadata meta = event.getModMetadata();
        
        // modid を取得
        MOD_ID = meta.modId;
        
        //  version を文字列として取得
        VERSION_STRING = meta.version;
        List<String> keys = meta.authorList;
        if (keys != null && !keys.isEmpty()) {
            key = keys.get(0);
        }
        checkModVersion();
    }
    
	@Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        thelow_item_hudHUD.register();
        ClientCommandHandler.instance.registerCommand(new item_hud());
        MinecraftForge.EVENT_BUS.register(new APIListener());
        MinecraftForge.EVENT_BUS.register(new timer());
        MinecraftForge.EVENT_BUS.register(new skill());
        MinecraftForge.EVENT_BUS.register(new Keyclick());
        MinecraftForge.EVENT_BUS.register(new InventoryParkSelector());
        
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new ItemHover());
    }
	
    private void checkModVersion() {
    	if(!ConfigHandler.AutoVersionCheck)return;
    	key = APIkey.getKey(key);
        // ネットワーク処理は重たいため、必ず別スレッドで実行する
        new Thread(() -> {
            
            System.out.println("[thelow_items_hud]最新バージョンの確認します...");
            VersionResponse apiResponse = getVersionData();

            if (apiResponse == null) {
                System.out.println("[thelow_items_hud]バージョンのチェックに失敗しました");
                return;
            }

            latestver = apiResponse.latestVersion;
            the_status = apiResponse.currentStatus;
            CustomMsg = apiResponse.CustomMsg;

            System.out.println("[thelow_items_hud]this_version: "+ VERSION_STRING+",status: " + the_status +",latest: " + latestver);
            
        }).start();
		
	}
    private static VersionResponse getVersionData() {
        HttpURLConnection conn = null; // finallyで閉じるために外で宣言
        String mcid = "no";
        if(ConfigHandler.SendMCID&&Minecraft.getMinecraft().getSession().getUsername()!=null) {
        	mcid = Minecraft.getMinecraft().getSession().getUsername();
        }
        
        try {
        	API_URL = "https://script.google.com/macros/s/"+key+"/exec";
            final String urlStr = API_URL + "?action=get&modid=" + thelow_items_hud.MOD_ID + "&version=" + thelow_items_hud.VERSION_STRING + "&mcid=" + mcid;
            final URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            // レスポンスコードを確認 (200 OK 以外はエラーとして扱う)
            int responseCode = conn.getResponseCode();
            String responsemsg = conn.getResponseMessage();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("[thelow_items_hud]レスポンス: " + responseCode+":"+responsemsg);
                return null; // 失敗
            }

            // --- レスポンスの読み取り ---
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

            // --- ★ ここからがJSONパース処理 ---
            String jsonString = result.toString();
            if (jsonString.isEmpty()) {
                System.out.println("[thelow_quest_helper]APIレスポンスが空でした。");
                return null;
            }

            // Gsonを使ってJSON文字列を VersionResponse オブジェクトに変換
            Gson gson = new Gson();
            VersionResponse response = gson.fromJson(jsonString, VersionResponse.class);

            return response; // ★ パースしたオブジェクトを返す

        } catch (JsonSyntaxException e) {
            // JSONの形式が間違っていた場合 (例: GASがエラーHTMLを返した)
            System.out.println("[thelow_items_hud]JSONのパースに失敗しました。");
            e.printStackTrace();
            return null; // 失敗
            
        } catch (Exception e) {
            // タイムアウト、URL間違い、ネットワーク接続なし など
            System.out.println("[thelow_items_hud]バージョンチェック中に例外が発生しました。");
            e.printStackTrace();
            return null; // 失敗
            
        } finally {
            // 成功しても失敗しても、必ず接続を閉じる
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}

class VersionResponse {
    public String latestVersion;
    public int currentStatus;
    public int nextStatus;
    public String CustomMsg;
}
