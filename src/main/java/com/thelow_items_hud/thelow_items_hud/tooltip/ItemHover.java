package com.thelow_items_hud.thelow_items_hud.tooltip;

import java.util.ArrayList;
import java.util.List;

import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemHover {
	
	
	@SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
		
		/*if(!APIListener.status_getted) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/thelow_api detailed_status");
		}*/
		
		if(!Keyclick.enable) {
			return;
		}
		
		
		
        ItemStack stack = event.itemStack;
        if (stack != null && stack.hasTagCompound()) {
            List<String> tooltip = event.toolTip;
            
            NBTTagCompound nbt = stack.getTagCompound();
            List<String> lore = thelow_item_hudHUD.getlore(nbt);
            int item_type = -1;
            
            
            for (String line : lore) {
                if (line.contains("以上")) {
                	if (line.contains("剣")) {
                		item_type = 0;
                	}
                	if (line.contains("弓")) {
                		item_type = 1;
                	}
                	if (line.contains("魔法")) {
                		item_type = 2;
                	}
                }
            }
            if(item_type==-1)return;
            
            double base_attack = 0;
            if(nbt.hasKey("thelow_item_damage")) {
            	base_attack = nbt.getDouble("thelow_item_damage");
            }
            List<String> thelow_item_special_attack_type = new ArrayList<>();
            for (int i=1; i<=10;i++) {
            	if(nbt.hasKey("thelow_item_special_attack_type"+i)) {
            		thelow_item_special_attack_type.add(nbt.getString("thelow_item_special_attack_type"+i));
            	}
            }
            
            List<Double> thelow_item_special_attack_value = new ArrayList<>();
            for (int i=1; i<=thelow_item_special_attack_type.size();i++) {
            	if(nbt.hasKey("thelow_item_special_attack_value"+i)) {
            		thelow_item_special_attack_value.add(nbt.getDouble("thelow_item_special_attack_value"+i));
            	}
            }
           
            List<Double> special_attack_damage = new ArrayList<>();
            for (int i=0; i<thelow_item_special_attack_type.size();i++) {
            	special_attack_damage.add((thelow_item_special_attack_value.get(i)+1) * base_attack);
            }
            
            
            
            for (int i=0;i<special_attack_damage.size();i++) {
            	special_attack_damage.set(i, special_attack_damage.get(i)*APIListener.overStrength[item_type]);
            }
            
            double base_attack_damage = base_attack * APIListener.overStrength[item_type];
            
            double[] stonepar = thelow_item_hudHUD.stonepars(nbt);
            
            List<String> mob_damage_name = new ArrayList<>();
            List<Double> mob_damage_value = new ArrayList<>();
            
            mob_damage_name.add("基礎ダメージ");
            mob_damage_value.add(base_attack_damage);
            
            for (int i=0; i<thelow_item_special_attack_type.size();i++) {
            	String type = thelow_item_special_attack_type.get(i);
            	switch (type){
            		case "ZOMBIE":{
            			mob_damage_name.add("ゾンビ");
            			mob_damage_value.add(stonepar[0]*special_attack_damage.get(i));
            			if(stonepar[0]!=1&&!thelow_item_special_attack_type.contains("PIG_ZOMBIE")) {
            				mob_damage_name.add("ピッグマン");
            				mob_damage_value.add(stonepar[0]*base_attack_damage);
            			}
            			break;
            		}
            		case "SKELETON":{
            			mob_damage_name.add("スケルトン");
            			mob_damage_value.add(stonepar[1]*special_attack_damage.get(i));
            			break;
            		}
            		case "UNDEAD":{
            			mob_damage_name.add("アンデット");
            			mob_damage_value.add(special_attack_damage.get(i));
            			if(stonepar[0]!=1) {
            				mob_damage_name.add("ゾンビ");
            				mob_damage_value.add(stonepar[0]*special_attack_damage.get(i));
            			}
            			if(stonepar[0]!=1) {
            				mob_damage_name.add("スケルトン");
            				mob_damage_value.add(stonepar[1]*special_attack_damage.get(i));
            			}
            			break;
            		}
            		case "PIG_ZOMBIE":{
            			mob_damage_name.add("ピッグマン");
            			mob_damage_value.add(stonepar[0]*special_attack_damage.get(i));
            			break;
            		}
            		case "SPIDER":{
            			mob_damage_name.add("クモ");
            			mob_damage_value.add(stonepar[2]*special_attack_damage.get(i));
            			if(stonepar[2]!=1) {
            				mob_damage_name.add("生物");
            				mob_damage_value.add(stonepar[2]*base_attack_damage);
            			}
            			break;
            		}
            		case "IRON_GOLEM":{
            			mob_damage_name.add("ゴーレム");
            			mob_damage_value.add(special_attack_damage.get(i));
            			break;
            		}
            		case "GIANT":{
            			mob_damage_name.add("ジャイアント");
            			mob_damage_value.add(special_attack_damage.get(i));
            			break;
            		}
            		case "WITHER":{
            			mob_damage_name.add("ウィザー");
            			mob_damage_value.add(special_attack_damage.get(i));
            		}
            	}
            }
            
            if(stonepar[0]!=1&&!mob_damage_name.contains("ゾンビ")) {
				mob_damage_name.add("ゾンビ");
				mob_damage_value.add((stonepar[0]*base_attack_damage));
            }
            if(stonepar[1]!=1&&!mob_damage_name.contains("スケルトン")) {
				mob_damage_name.add("スケルトン");
				mob_damage_value.add((stonepar[1]*base_attack_damage));
            }
            if(stonepar[2]!=1&&!mob_damage_name.contains("生物")) {
				mob_damage_name.add("生物");
				mob_damage_value.add((stonepar[2]*base_attack_damage));
            }
            
            int addplace = 0;
            
            for(int i=0;i<lore.size();i++) {
            	if(lore.get(i).contains("[SLOT]")){
            		addplace = i;
            		break;
            	}
            }
            
            for(int i=0;i<mob_damage_name.size();i++) {
            	if(i==0&&!APIListener.status_getted) {
            		tooltip.add(addplace,"まだステータスを読み込んでいません");
            		addplace++;
            	}
            	String text = mob_damage_name.get(i)+": "+String.format("%.2f",mob_damage_value.get(i))+"§4("+String.format("%.2f",mob_damage_value.get(i)*1.15)+")§r";
            	tooltip.add(addplace,text);
            	addplace++;
            }
            
            
            		
            }
        }
}
