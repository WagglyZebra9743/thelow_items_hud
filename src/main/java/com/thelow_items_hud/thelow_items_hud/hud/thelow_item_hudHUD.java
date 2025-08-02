package com.thelow_items_hud.thelow_items_hud.hud;

import java.util.ArrayList;
import java.util.List;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.skills.skill;
import com.thelow_items_hud.thelow_items_hud.skills.timer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class thelow_item_hudHUD extends Gui {

    private static final Minecraft mc = Minecraft.getMinecraft();
    
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new thelow_item_hudHUD());
    }

    private static String pskillname = null;
    private static Long itemseed = 0L;
    private static String[] stone_type = {"zombie_attackLEVEL","skeleton_attackLEVEL","living_attackLEVEL","reduce_cooltime_magic_stoneLEVEL","add_mp_magicstoneLEVEL"};
    private static double[] attak_par = {1.1,1.15,1.23,1.35,1.55};
    private static double[] cas_par = {0.95,0.9,0.84,0.77,0.6};
    private static double[] pos_ev = {0.05,0.1,0.15,0.3,0.5};
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;//プレイヤーまたはワールドがない場合は実行しない
        ItemStack holdingItems = mc.thePlayer.getHeldItem();
        
        
        
        if(holdingItems==null) {
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();
        	return;//何も持っていないから終わり
        }
        
        
        if(holdingItems.getTagCompound()==null||!holdingItems.hasTagCompound()) {
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();
        	return;//タグがないから終わり
        }
        
        
        NBTTagCompound nbt = holdingItems.getTagCompound();
        
        
        if (!nbt.hasKey("thelow_item_id")) {
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();
        	return;//idがないなら終わり
        }
        
        if (nbt.hasKey("thelow_item_seed_value", 4)) { // 4 = Long型
            long seed = nbt.getLong("thelow_item_seed_value");
            // seed の変化を検知してトリガー
            if(itemseed == 0L || itemseed != seed) {
            	itemseed = seed;
            	timer.EisyouReset();
            	timer.YamikaihouReset();
            	timer.EsuReset();
            }
        }else {
        	timer.EisyouReset();
        	timer.YamikaihouReset();
        	timer.EsuReset();
        }
        
        String itemName = null;
        
            if (nbt.hasKey("display", 10)) { // 10 = Compound
                NBTTagCompound display = nbt.getCompoundTag("display");
                if (display.hasKey("Name", 8)) { // 8 = String
                    itemName = display.getString("Name");
                
            }
        }
        if(itemName==null || itemName=="") {
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();
        	return;
        }
        
        
        
        List<String> lore = new ArrayList<>();
        
        if (nbt.hasKey("display", 10)) { // 10 = NBTTagCompound
            NBTTagCompound display = nbt.getCompoundTag("display");
            if (display.hasKey("Lore", 9)) { // 9 = NBTTagList
                NBTTagList loreList = display.getTagList("Lore", 8); // 8 = String tag
                for (int i = 0; i < loreList.tagCount(); i++) {
                    lore.add(loreList.getStringTagAt(i));
                }
            }
        }
        if(itemName!=null&&!itemName.isEmpty()) HUD_render(itemName,0);
        
        if(itemName!=null&&(itemName.contains("メモ")||itamName.contains("リスト"))) {
        	int di0 = 13;
        	for(int i=0;i<lore.size();i++) {
        		String loretext = lore.get(i).replaceAll("§.", "");;
        		
        		if(loretext!=null&&!loretext.equals(" ")&&!loretext.equals("")&&!loretext.contains("のみ使用可能です。")) {
        		HUD_render(lore.get(i),i*13+di0);
        		}else {
        			di0 -= 13;
        			continue;
        		}
        	}
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();return;
        }
        
        String item_type = "";
        
        for (String line : lore) {
            if (line.contains("以上")) {
            	if (line.contains("剣")) {
            		item_type = "剣";
            	}
            	if (line.contains("弓")) {
            		item_type = "弓";
            	}
            	if (line.contains("魔法")) {
            		item_type = "魔法";
            	}
            	
            }
        }
        if(item_type == ""||item_type == null || item_type.isEmpty()) {
        	timer.YamikaihouReset();
        	timer.EsuReset();
        	timer.EisyouReset();
        	return;
        }

        
        
        int level = 0;
        
        
        if (nbt.hasKey("thelow_avaliable_level")) {//レベルデータがあるかをチェック
        	level = (int) nbt.getShort("thelow_avaliable_level");//levelに格納
        }
        
        double[] stones = {1,1,1,100,0};
        if(nbt.hasKey("thelow_item_slot_list")) {//魔法石スロットがあるか
        	String slot_text_data = nbt.getString("thelow_item_slot_list");//それをStringで取得
        	String[] slots = slot_text_data.split(",");
        	if(slots != null && !slot_text_data.isEmpty()) {//データがあるかチェック
        		for (String slot : slots) {
        			//特攻魔石
            		for(int n = 0;n<=2;n++) {
            			for(int i = 1;i<=5;i++) {
            				if(slot.equals(stone_type[n]+i)){
            					stones[n] *= attak_par[i-1];
            				}
            			}
            			if(slot.equals(stone_type[n]+"4_5")) {
            				stones[n] *= 1.4;
            			}
            			if(slot.equals(stone_type[n]+"LEGEND")) {
            				for(int j = 0;j<=2;j++) {
            					stones[j] *= 0.06;
            				}
            				stones[n] /= 0.06;
            				stones[n] *= 1.55;
            			}
            		}
            		//キャスター
            		for(int i = 1;i<=5;i++) {
        				if(slot.equals(stone_type[3]+i)){
        					stones[3] *= cas_par[i-1];
        				}
        			}
            		if(slot.equals(stone_type[3]+"4_5")) {
            			stones[3] *= 0.72;
            		}
            		//ポーシング
            		for(int i = 1;i<=5;i++) {
        				if(slot.equals(stone_type[4]+i)){
        					stones[4] += pos_ev[i-1];
        				}
        			}
            		if(slot.equals(stone_type[4]+"4_5")) {
            			stones[4] += 0.4;
            		}
        		}
        		
        	}
        }
        

        String sskill = skill.getsp(nbt);
        String sskillname = skill.getname(sskill);
        
        if(sskillname!=null) {
        	if(sskillname.equals("覚醒")){
        		if(skill.kaihou) {
        			sskillname = "§a"+sskillname+"§r";
        		}else {
        			sskillname = "§8"+sskillname+"§r";
        			int time = timer.Kaihoutimer();
        			if(time!=0) {
        				sskillname = sskillname + time + "秒";
        			}
        		}
        	}
        }
        
        String nskill = skill.getno(nbt);
        String nskillname = skill.getname(nskill);
        if(nskillname!=null) {
        	if(nskillname.equals("開放")) {
        		if(skill.yochou) {
        			nskillname = "§a"+nskillname+"§r";
        		}else {
        			nskillname = "§8"+nskillname+"§r";
        			int time = timer.Yochoutimer();
        			if(time!=0) {
        				nskillname = nskillname + time +"秒";
        			}
        		}
        	}
        }
        
        
        String pskill = skill.getpa(nbt);
        pskillname = skill.getpskillname(pskill);
        String pskillnameshow = null;
        if(pskillname!=null) {
        	switch (pskillname) {
            case "闇の解放":
                if (skill.yamikaihou) {
                    pskillnameshow = "§a" + pskillname + "§r";
                } else {
                    pskillnameshow = "§8" + pskillname + "§r";
                    int time = timer.Yamikaihoutimer();
                    if (time != 0) {
                        pskillnameshow += time + "秒";
                    }
                }
                break;

            case "エース":
                if (skill.esu) {
                    pskillnameshow = "§a" + pskillname + "§r";
                } else {
                    pskillnameshow = "§8" + pskillname + "§r";
                    int time = timer.Esutimer();
                    if (time != 0) {
                        pskillnameshow += time + "秒";
                    }
                }
                break;

            case "詠唱":
                if (skill.eisyou) {
                    pskillnameshow = "§a" + pskillname + "§r";
                } else {
                    pskillnameshow = "§8" + pskillname + "§r";
                    int time = timer.Eisyoutimer();
                    if (time != 0) {
                        pskillnameshow += time + "秒";
                    }
                }
                break;

            default:
                pskillnameshow = pskillname;
                timer.EisyouReset();
                break;
        	}
        }
        
        
        StringBuilder text3Builder = new StringBuilder();
        if(sskillname!=null){
        	text3Builder.append(sskillname);
        }
        
        if(nskillname!=null) {
        	if(text3Builder.length() != 0) {
            	text3Builder.append(":");
            }
        	text3Builder.append(nskillname);
        }
        if(pskillnameshow!=null) {
        	if(text3Builder.length() != 0) {
            	text3Builder.append(":");
            }
        	text3Builder.append(pskillnameshow);
        }
        String text3 = null;
        if(text3Builder!=null) {
        	text3 = text3Builder.toString();
        }
        
        
        
        String text = item_type+"レベル"+level+"以上";
        
        StringBuilder text2Builder = new StringBuilder();

        // stones[x]をappendする際にString.format()で書式を指定
        if (stones[0] != 1) {
        	text2Builder.append("§5§lゾン×").append(String.format("%.3f", stones[0])).append("§r");
        }
        if (stones[1] != 1) {
        	if (text2Builder.length() > 0) text2Builder.append(",");
        	text2Builder.append("§f§lスケ×").append(String.format("%.3f", stones[1])).append("§r");
        }
        if (stones[2] != 1) {
        	if (text2Builder.length() > 0) text2Builder.append(",");
        	text2Builder.append("§a§l生物×").append(String.format("%.3f", stones[2])).append("§r");
        }
        if (stones[3] != 100) {
        	if (text2Builder.length() > 0) text2Builder.append(",");
        	text2Builder.append("§3§lCT").append(String.format("%.2f", stones[3])).append("%§r");
        }
        if (stones[4] != 0) {
        	if (text2Builder.length() > 0) text2Builder.append(",");
        	text2Builder.append("§d§lポシ+").append(String.format("%.2f", stones[4])).append("§r");
        }

        String text2 = text2Builder.toString();
    
        
        HUD_render(text,13);
     
        if(text2!=null&&!text2.isEmpty()) {
        	HUD_render(text2,26);
        }
        
        if(text3!=null&&!text3.isEmpty()) {
        	HUD_render(text3,39);
        }
    }
    private void HUD_render(String text,int dy) {
    	FontRenderer font = mc.fontRendererObj;
    	int x = ConfigHandler.hudX;//HUD表示位置指定
    	int y = ConfigHandler.hudY;//HUD表示位置指定
    	y += dy;
    	int textWidth = font.getStringWidth(text);
    	int textHeight = font.FONT_HEIGHT;
    	int padding = 2;
    	Gui.drawRect(x - padding, y - padding, x + textWidth + padding, y + textHeight + padding, 0x50000000);
    	
    	
    	GlStateManager.pushMatrix();
    	mc.fontRendererObj.drawStringWithShadow(text , x, y, 0xFFFFFF);
    	GlStateManager.popMatrix();
    }
    public static String Getpskillname() {
    	return pskillname;
    }
}
