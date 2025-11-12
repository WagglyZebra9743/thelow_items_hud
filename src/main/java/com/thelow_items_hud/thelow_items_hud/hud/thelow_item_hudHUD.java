package com.thelow_items_hud.thelow_items_hud.hud;

import java.util.ArrayList;
import java.util.List;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;
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
    private static final String[] stone_type = {"zombie_attackLEVEL","skeleton_attackLEVEL","living_attackLEVEL","reduce_cooltime_magic_stoneLEVEL","add_mp_magicstoneLEVEL"};
    private static final double[] attak_par = {1.1,1.15,1.23,1.35,1.55};
    private static final double[] cas_par = {0.95,0.9,0.84,0.77,0.6};
    private static final double[] pos_ev = {0.05,0.1,0.15,0.3,0.5};
    public static NBTTagCompound latestnbt = null;
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;//プレイヤーまたはワールドがない場合は実行しない
        if(!ConfigHandler.hudenable) return;//有効でない場合何もしない
        final ItemStack holdingItems = mc.thePlayer.getHeldItem();
        final int hudX = ConfigHandler.hudX;
    	final int hudY = ConfigHandler.hudY;
        
        if(ConfigHandler.itemCThudenable&&timer.itemCTtimer>=0) {
        	final int itemhudX =ConfigHandler.itemCThudX;
        	final int itemhudY =ConfigHandler.itemCThudY;
        	int time_tick = timer.itemCTtimer;
        	final int time_sec = time_tick/20;
        	time_tick%=20;
        	final int time_msec = time_tick*5;
        	final String itemCTtext = String.format("%d:%02d",time_sec,time_msec);
        	
        	HUD_render(APIListener.itemID,0,itemhudX,itemhudY);
        	HUD_render(itemCTtext,13,itemhudX,itemhudY);
        }
        
        
        if(holdingItems==null||holdingItems.getTagCompound()==null||!holdingItems.hasTagCompound()) {
        	ReturnProcess();
        	return;//タグがないから終わり
        }
        
        
        final NBTTagCompound nbt = holdingItems.getTagCompound();
        
        if(nbt==null)return;
        
        latestnbt = nbt;
        
        if (!nbt.hasKey("thelow_item_id")) {
        	final String itemName = GetItemName(nbt);
        	if(itemName!=null&&itemName.contains("§6§lおみくじ")) {
        		final List<String> lore = getlore(nbt);
            	ShowLores(lore,itemName);
        	}
        	if(itemName!=null&&itemName.startsWith("§7On CoolDown (§r")) {
        		HUD_render(itemName,0,hudX,hudY);
        	}
        	ReturnProcess();
        	return;//idがないなら終わり
        }
        final String itemid = nbt.getString("thelow_item_id");        
        if (nbt.hasKey("thelow_item_seed_value", 4)) { // 4 = Long型
            final long seed = nbt.getLong("thelow_item_seed_value");
            // seed の変化を検知してトリガー
            if(itemseed == 0L || itemseed != seed) {
            	itemseed = seed;
            	ReturnProcess();
            }
        }else {
        	ReturnProcess();
        }
        
        final String itemName = GetItemName(nbt);
        
        if(itemName==null || itemName=="" || itemName.isEmpty()) {
        	ReturnProcess();
        	return;
        }
        
        final List<String> lore = getlore(nbt);
        
        if(itemName.contains("メモ")||itemName.contains("リスト")) {
        	ShowLores(lore,itemName);
        	ReturnProcess();
        	return;
        }
        
        HUD_render(itemName,0,hudX,hudY);
        
        if(itemid.startsWith("normal_rod_")||nbt.hasKey("magic_pickaxe_exp")) {
        	String expline = GetTextInlore(lore,"釣り竿経験値");
        	if(expline==null) {
        		expline = GetTextInlore(lore,"ピッケルレベル");
        	}
        	if(expline!=null) {
        		HUD_render(expline,13,hudX,hudY);
        	}
        	ReturnProcess();
        	return;
        	
        }
        
        String item_type = "";
        
        final String item_type_line = GetTextInlore(lore,"以上");
        if(item_type_line==null) {
        	ReturnProcess();
        	return;
        }
        
        if(item_type_line.contains("剣"))item_type="剣";
        if(item_type_line.contains("弓"))item_type="弓";
        if(item_type_line.contains("魔法"))item_type="魔法";
        
        
        if(item_type == null||item_type.isEmpty()) {
        	ReturnProcess();
        	return;
        }
        if(item_type == "") {
        	final String may_posline = GetTextInlore(lore,"x:");
        	if(may_posline.contains("y:")&&may_posline.contains("z:")) {
        		HUD_render(may_posline,13,hudX,hudY);
        	}
        	ReturnProcess();
        	return;
        }

        
        
        int level = 0;
        
        
        if (nbt.hasKey("thelow_avaliable_level")) {//レベルデータがあるかをチェック
        	level = (int) nbt.getShort("thelow_avaliable_level");//levelに格納
        }
        
        final double[] stones = stonepars(nbt);
        

        final String sskill = skill.getsp(nbt);
        String sskillname = skill.getname(sskill);
        
        if(sskillname!=null&&sskillname.equals("覚醒")) {
       		if(skill.kaihou) {
       			sskillname = "§a"+sskillname+"§r";
       		}else {
        		sskillname = "§8"+sskillname+"§r";
        		final int time = timer.Kaihoutimer();
        		if(time!=0) {
        			sskillname = sskillname + time + "秒";
        		}
        	}
        }
        
        final String nskill = skill.getno(nbt);
        String nskillname = skill.getname(nskill);
        /*
        if(nskillname!=null) {
        	if(nskillname.equals("開放")) {
        		if(skill.yochou) {
        			nskillname = "§a"+nskillname+"§r";
        		}else {
        			nskillname = "§8"+nskillname+"§r";
        			final int time = timer.Yochoutimer();
        			if(time!=0) {
        				nskillname = nskillname + time +"秒";
        			}
        		}
        	}
        }
        */
        
        final String pskill = skill.getpa(nbt);
        pskillname = skill.getpskillname(pskill);
        String pskillnameshow = null;
        if(pskillname!=null) {
        	switch (pskillname) {
            case "闇の解放":
                if (skill.yamikaihou) {
                    pskillnameshow = "§a" + pskillname + "§r";
                } else {
                    pskillnameshow = "§8" + pskillname + "§r";
                    final int time = timer.Yamikaihoutimer();
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
                    final int time = timer.Esutimer();
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
                    final int time = timer.Eisyoutimer();
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
        
        
        
        final String text = item_type+"レベル"+level+"以上";
        
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
    
        
        HUD_render(text,13,hudX,hudY);
     
        if(text2!=null&&!text2.isEmpty()) {
        	HUD_render(text2,26,hudX,hudY);
        }
        
        if(text3!=null&&!text3.isEmpty()) {
        	HUD_render(text3,39,hudX,hudY);
        }
    }
    public static List<String> getlore(final NBTTagCompound nbt){
    	List<String> lore = new ArrayList<>();
    	if (nbt.hasKey("display", 10)) { // 10 = NBTTagCompound
            final NBTTagCompound display = nbt.getCompoundTag("display");
            if (display.hasKey("Lore", 9)) { // 9 = NBTTagList
                final NBTTagList loreList = display.getTagList("Lore", 8); // 8 = String tag
                for (int i = 0; i < loreList.tagCount(); i++) {
                    lore.add(loreList.getStringTagAt(i));
                }
            }
        }
    	return lore;
    }
    public static double[] stonepars(final NBTTagCompound nbt) {
    	double[] stones = {1,1,1,100,0};
        if(nbt.hasKey("thelow_item_slot_list")) {//魔法石スロットがあるか
        	final String slot_text_data = nbt.getString("thelow_item_slot_list");//それをStringで取得
        	final String[] slots = slot_text_data.split(",");
        	if(slots != null && !slot_text_data.isEmpty()) {//データがあるかチェック
        		for (final String slot : slots) {
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
        return stones;
    }
    
    private static void HUD_render(final String text,final int dy,final int x,int y) {
    	FontRenderer font = mc.fontRendererObj;
    	y += dy;
    	final int textWidth = font.getStringWidth(text);
    	final int textHeight = font.FONT_HEIGHT;
    	final int padding = 2;
    	Gui.drawRect(x - padding, y - padding, x + textWidth + padding, y + textHeight + padding, 0x50000000);
    	
    	
    	GlStateManager.pushMatrix();
    	mc.fontRendererObj.drawStringWithShadow(text , x, y, 0xFFFFFF);
    	GlStateManager.popMatrix();
    }
    public static String Getpskillname() {
    	return pskillname;
    }
    public static String GetItemName(NBTTagCompound nbt) {
    	String itemName = null;
        
        if (nbt.hasKey("display", 10)) { // 10 = Compound
            final NBTTagCompound display = nbt.getCompoundTag("display");
            if (display.hasKey("Name", 8)) { // 8 = String
                itemName = display.getString("Name");
            }
        }
        return itemName;
    }
    
    private static void ShowLores(final List<String> lore,final String name) {
    	final int hudX = ConfigHandler.hudX;
    	final int hudY = ConfigHandler.hudY;
    	if(name!=null&&!name.isEmpty()) {
    		HUD_render(name,0,hudX,hudY);
    	}else if(lore==null||lore.isEmpty())return;
    	
    	int di0 = 13;
    	for(int i=0;i<lore.size();i++) {
    		final String loretext = lore.get(i).replaceAll("§.", "");;
    		
    		if(loretext!=null&&!loretext.equals(" ")&&!loretext.equals("")&&!loretext.contains("のみ使用可能です。")) {
    		HUD_render(lore.get(i),i*13+di0,hudX,hudY);
    		}else {
    			di0 -= 13;
    			continue;
    		}
    	}
    	return;
    }
    
    private static void ReturnProcess() {
    	timer.YamikaihouReset();
    	timer.EsuReset();
    	timer.EisyouReset();
    }
    
    private static String GetTextInlore(final List<String> lore, final String Keyword) {
    	if(lore==null||Keyword==null)return null;
    	for(String line:lore) {
    		if(line!=null&&line.contains(Keyword)) {
    			return line;
    		}
    	}
    	return null;
    }
}
