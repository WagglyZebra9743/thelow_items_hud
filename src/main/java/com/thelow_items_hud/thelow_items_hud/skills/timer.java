package com.thelow_items_hud.thelow_items_hud.skills;

import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class timer {
	private static int yochouTimer = -1; // -1 = 停止中
	private static int kaihouTimer = -1;
	private static int yamikaihouTimer = -1;
	private static int esuTimer = -1;
	private static int eisyouTimer = -1;
	public static int itemCTtimer = -1;
	private static BlockPos lastPos = null;
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static int tick = 0;
	@SubscribeEvent
	public void TickTimer(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.START) return;
	    if (yochouTimer >= 0) {
	        yochouTimer++;
	        // 30秒経過で発動
	        if (yochouTimer >= 30 * 20) {
	            skill.yochou = true; // 発動
	            yochouTimer = -1;    // タイマー停止
	        }
	    }
	    if (kaihouTimer >= 0) {
	        kaihouTimer++;

	        // 30秒経過で発動
	        if (kaihouTimer >= 30 * 20) {
	            skill.kaihou = true; // 発動
	            kaihouTimer = -1;    // タイマー停止
	        }
	    }
	    if (yamikaihouTimer >= 0) {
	        yamikaihouTimer++;
	        // 6秒経過で発動
	        if (yamikaihouTimer >= 6 * 20) {
	            skill.yamikaihou = true; // 使用可能
	            yamikaihouTimer = -1;    // タイマー停止
	        }
	    }
	    
	    
	    if (esuTimer >= 0) {
	        esuTimer++;

	        // 6秒経過で発動
	        if (esuTimer >= 6 * 20) {
	            skill.esu = true; // 使用可能
	            esuTimer = -1;    // タイマー停止
	        }
	    }
	    if(eisyouTimer >= 0) {
	    	eisyouTimer++;
	    	//10秒経過で実行
	    	if(eisyouTimer >=10*20) {
	    		skill.eisyou = true;
	    		eisyouTimer = -1;
	    	}
	    }
	    
	    //アイテムCT用
	    if(itemCTtimer >= 0) {
	    	itemCTtimer--;
	    	if(itemCTtimer < 0) {
	    		itemCTtimer = -1;
	    	}
	    }
	}
	
	@SubscribeEvent
	public void Esuchek(TickEvent.ClientTickEvent event) {
		if (mc.thePlayer == null || mc.theWorld == null) return;
		if(thelow_item_hudHUD.Getpskillname()!=null&&thelow_item_hudHUD.Getpskillname().equals("エース")) {
			final EntityPlayerSP player = mc.thePlayer;
	    
	    	final BlockPos currentPos = player.getPosition();
	    	if (lastPos == null) {
	        	lastPos = currentPos;
	        	return;
	    	}
	    	if (lastPos != null && !currentPos.equals(lastPos)) {
	    		EsuReset();
				lastPos = currentPos;
				return;
	    	}
	    
	    
	    	if (player.getFoodStats().getFoodLevel() == 0) {
	    		EsuReset();
	        	return;
	    	}
	    }
	    
	}
	// 呼び出しメソッド
	public static void Yochou() {
		if(yochouTimer==-1) {
	    yochouTimer = 0;  // カウント開始
	    skill.yochou = false; // リセット
		}else return;
	}
	
	public static int Yochoutimer() {
		int timer = yochouTimer;
		if(timer==-1)return 0;
		timer/=20;
		timer = 30 - timer;
		return timer;
	}
	
	public static void Kaihou() {
		if(kaihouTimer==-1) {
	    kaihouTimer = 0;  // カウント開始
	    skill.kaihou = false; // リセット
		}else return;
	}
	
	public static int Kaihoutimer() {
		int timer = kaihouTimer;
		if(timer==-1)return 0;
		timer/=20;
		timer = 30 - timer;
		return timer;
	}
	
	public static void Reconnected() {
		skill.yochou = false;
		skill.kaihou = false;
		yochouTimer = -1;
		kaihouTimer = -1;
		YamikaihouReset();
		EsuReset();
		EisyouReset();
		final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		 if (player != null) {
	            player.sendChatMessage("/thelow_api subscribe SKILL_COOLTIME");
	        }
	}
	
	public static void Yamikaihou() {
		if(yamikaihouTimer==-1&&!skill.yamikaihou) {
		yamikaihouTimer = 0;
		}
	}
	public static void YamikaihouReset() {
		yamikaihouTimer = -1;
		skill.yamikaihou = false;
	}
	
	public static int Yamikaihoutimer() {
		int timer = yamikaihouTimer;
		if(timer==-1)return 0;
		timer/=20;
		timer = 6 - timer;
		return timer;
	}

	public static void Esu() {
		if(esuTimer==-1&&!skill.esu) {
			esuTimer = 0;
		}
	}
	
	public static void EsuReset(){
		esuTimer = -1;
		skill.esu = false;
	}
	
	public static int Esutimer() {
		int timer = esuTimer;
		if(timer==-1)return 0;
		timer/=20;
		timer = 6 - timer;
		return timer;
	}
	
	public static void Eisyou() {
		if(eisyouTimer==-1&&!skill.eisyou) {
			eisyouTimer = 0;
		}
	}
	
	public static void EisyouReset() {
		eisyouTimer = -1;
		skill.eisyou = false;
	}
	
	public static int Eisyoutimer() {
		int timer = eisyouTimer;
		if(timer==-1)return 0;
		timer/=20;
		timer = 10 - timer;
		return timer;
	}

}
