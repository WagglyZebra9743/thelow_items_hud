package com.thelow_items_hud.thelow_items_hud.skills;

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
	private static BlockPos lastPos = null;
	private static final Minecraft mc = Minecraft.getMinecraft();
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
	        System.out.println("yami:"+yamikaihouTimer);
	        // 6秒経過で発動
	        if (yamikaihouTimer >= 6 * 20) {
	            skill.yamikaihou = true; // 使用可能
	            yamikaihouTimer = -1;    // タイマー停止
	        }
	    }
	    
	    
	    if (esuTimer >= 0) {
	        esuTimer++;
	        System.out.println("esu:"+esuTimer);

	        // 6秒経過で発動
	        if (esuTimer >= 6 * 20) {
	            skill.esu = true; // 使用可能
	            esuTimer = -1;    // タイマー停止
	        }
	    }
	}
	
	@SubscribeEvent
	public void Esuchek(TickEvent.ClientTickEvent event) {
		if (mc.thePlayer == null || mc.theWorld == null) return;
		EntityPlayerSP player = mc.thePlayer;
	    
	    BlockPos currentPos = player.getPosition();
	    if (lastPos == null) {
	        lastPos = currentPos;
	        return;
	    }
	    if (lastPos != null && !currentPos.equals(lastPos)) {
	    	EsuReset();
			System.out.println("moved");
			lastPos = currentPos;
			return;
	    }
	    
	    
	    if (player.getFoodStats().getFoodLevel() == 0) {
	    	EsuReset();
			System.out.println("hungerd");
	        return;
	    }
	    
	}
	// 呼び出しメソッド
	public static void Yochou() {
		if(yochouTimer==-1) {
	    yochouTimer = 0;  // カウント開始
	    skill.yochou = false; // リセット
		}else return;
	}
	
	public static void Kaihou() {
		if(kaihouTimer==-1) {
	    kaihouTimer = 0;  // カウント開始
	    skill.kaihou = false; // リセット
		}else return;
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

	public static void Esu() {
		if(esuTimer==-1&&!skill.esu) {
			esuTimer = 0;
		}
	}
	
	public static void EsuReset(){
		esuTimer = -1;
		skill.esu = false;
	}
	

}
