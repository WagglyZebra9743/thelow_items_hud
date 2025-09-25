package com.thelow_items_hud.thelow_items_hud.tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class textvalues{
	List<String> name = new ArrayList<>();
	List<Double> value = new ArrayList<>();
}

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
            if(lore==null||lore.isEmpty()) return;
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
            
            if(item_type==-1) {
            	//一部のスキルは誤取得するのでここで除外しておく
            	if(nbt.hasKey("view_weapon_skill_id")) {
            		String skill_id = nbt.getString("view_weapon_skill_id");
            		if(skill_id!=null&&(skill_id.equals("wskill42")||skill_id.equals("wskill127")||skill_id.equals("wskill136")||skill_id.equals("wskill2"))){
            			return;
            		}
            	}else return;
            	
            	//スキルの文字列があるかどうかを判定してtooltipに表示させてみる
            	//0.持ってるアイテムから取得したloreを一つのStringにすると同時にアイテムタイプの情報を持っているか確認する
            	
            	//手に持っているアイテムのnbt
            	NBTTagCompound nbt1 = thelow_item_hudHUD.latestnbt;
            	if(nbt1==null)return;//nbtがないなら終了
            	//手に持っているアイテムのlore
            	List<String> lore1 = thelow_item_hudHUD.getlore(nbt1);
                if(lore1==null||lore1.isEmpty()) return;//無いなら終了
                
                
            	int item_type1 = -1;
            	for (String line : lore1) {
                    if (line.contains("以上")) {
                    	if (line.contains("剣")) {
                    		item_type1 = 0;
                    	}
                    	if (line.contains("弓")) {
                    		item_type1 = 1;
                    	}
                    	if (line.contains("魔法")) {
                    		item_type1 = 2;
                    	}
                    }
                }
            	if(item_type1==-1)return;
            	
            	String loretext = "";
            	for(String line : lore) {
            		loretext = loretext+line;
            	}
            	loretext = loretext.replaceAll("§[0-9a-fk-or]", "");
            	
            	
            	//1.それぞれの与える予定ダメージを配列に記録しておく
            	
            	textvalues base_mob_damages = create_damage(nbt1,item_type1,"基礎");
            	
            	if(base_mob_damages==null)return;
                
                //この時点でmob_damages.nameには名前が、mob_damages.valueには対応したダメージが記録されている
            	
            	textvalues values = new textvalues();
                
            	// パターン1.1: 「攻撃力のn%」
                Pattern p1 = Pattern.compile("攻撃力の(\\d+(?:\\.\\d+)?)%");
                Matcher m1 = p1.matcher(loretext);
                while (m1.find()) {
                    double n = Double.parseDouble(m1.group(1));
                    values.value.add(n / 100.0 + 1.0);
                    values.name.add(n + "%");
                }
                
                // パターン1.2: 「n%のダメージ」
                Pattern p2 = Pattern.compile("(\\d+(?:\\.\\d+)?)%のダメージ");
                Matcher m2 = p2.matcher(loretext);
                while (m2.find()) {
                    double n = Double.parseDouble(m2.group(1));
                    values.value.add(n / 100.0 + 1.0);
                    values.name.add(n + "%");
                }
                
                // パターン1.3: 「攻撃力がn%に」
                Pattern p3 = Pattern.compile("攻撃力が(\\d+(?:\\.\\d+)?)%に");
                Matcher m3 = p3.matcher(loretext);
                while (m3.find()) {
                    double n = Double.parseDouble(m3.group(1));
                    values.value.add(n / 100.0 + 1.0);
                    values.name.add(n + "%");
                }
                
                // パターン1.4: 「攻撃力がn%上昇」
                Pattern p4 = Pattern.compile("攻撃力が(\\d+(?:\\.\\d+)?)%上昇");
                Matcher m4 = p4.matcher(loretext);
                while (m4.find()) {
                    double n = Double.parseDouble(m4.group(1));
                    values.value.add(n / 100.0 + 1.0);
                    values.name.add(n + "%");
                }

                // パターン2: 「n倍」
                // "倍" の前の数値を抽出します
                Pattern multiplierPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)(倍)");
                Matcher multiplierMatcher = multiplierPattern.matcher(loretext);
                while (multiplierMatcher.find()) {
                    // group(1)で数値部分を取得
                    double n = Double.parseDouble(multiplierMatcher.group(1));
                    values.value.add(n);
                    String nname = String.valueOf(n);
                    values.name.add(nname+"倍");
                }
                
                if(values==null||values.name.isEmpty()||values.value.isEmpty()) {
                	return;
                }else {
                	
                	String skillid = null;
                	if(nbt1.hasKey("thelow_item_weapon_skill_set_id")) {
            			skillid = nbt1.getString("thelow_item_weapon_skill_set_id");
            		}
                	
                	String itemName = null;
                	if (nbt.hasKey("display", 10)) { // 10 = Compound
                        NBTTagCompound display = nbt.getCompoundTag("display");
                        if (display.hasKey("Name", 8)) { // 8 = String
                            itemName = display.getString("Name");
                        }
                    	}
                	
                	if(skillid!=null&&skillid.equals("6")) {
                		if(itemName!=null&&itemName.equals("§bマジックボール")) {
                    		values.name.add("詠唱付き");
                    		values.value.add(8.0);
                		}if(itemName!=null&&itemName.equals("§bライトニングボルト")) {
                    		values.name.add("詠唱付き");
                    		values.value.add(6.0);
                		}if(itemName!=null&&itemName.equals("§bメテオストライク")) {
                    		values.name.add("詠唱付き");
                    		values.value.add(18.0);
                		}
                	}else if(skillid!=null&&skillid.equals("36")) {
                			if(itemName!=null&&itemName.equals("§b率舞")) {
                				values.name.add("最大");
                				values.value.add(1.2);
                			}
                	}
                	
                	int addplace = lore.size();
                	for(int i=0; i<values.name.size();i++) {
                		addplace++;
                		tooltip.add(addplace,values.name.get(i));
                		String damagetext="";
                		for(int j=0;j<base_mob_damages.name.size();j++) {
                			String mobdamagename = base_mob_damages.name.get(j);
                			double mobdamagevalue = base_mob_damages.value.get(j);
                			mobdamagevalue*=values.value.get(i);
                			damagetext =damagetext+mobdamagename+"§7"+String.format("%.2f",mobdamagevalue)+"§4("+String.format("%.2f",mobdamagevalue*1.15)+")§r";
                		}
                		addplace++;
                		tooltip.add(addplace,damagetext);
                	}
                }
                
                
            	//2. テキストを分析していく
            	//例 攻撃力のn% → double n+1
            	//n%のダメージを与える → double n+1
            	//○○に +n% → double n+1
            	//n倍 → double n
            	return;
            }
            
            textvalues mob_damages =create_damage(nbt,item_type,"基礎ダメージ");
            
            int addplace = 0;
            
            for(int i=0;i<lore.size();i++) {
            	if(lore.get(i).contains("[SLOT]")){
            		addplace = i;
            		break;
            	}
            }
            
            for(int i=0;i<mob_damages.name.size();i++) {
            	if(i==0&&!APIListener.status_getted) {
            		tooltip.add(addplace,"まだステータスを読み込んでいません");
            		addplace++;
            	}
            	String text = mob_damages.name.get(i)+": "+String.format("%.2f",mob_damages.value.get(i))+"§4("+String.format("%.2f",mob_damages.value.get(i)*1.15)+")§r";
            	tooltip.add(addplace,text);
            	addplace++;
            }
            
            }
        }
	
	private static textvalues create_damage(NBTTagCompound nbt,int item_type,String base_attack_text){
		textvalues damages = new textvalues();
    	
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
        
        
        damages.name.add(base_attack_text);
        damages.value.add(base_attack_damage);
        
        for (int i=0; i<thelow_item_special_attack_type.size();i++) {
        	String type = thelow_item_special_attack_type.get(i);
        	switch (type){
        		case "ZOMBIE":{
        			damages.name.add("ゾンビ");
        			damages.value.add(stonepar[0]*special_attack_damage.get(i));
        			if(stonepar[0]!=1&&!thelow_item_special_attack_type.contains("PIG_ZOMBIE")) {
        				damages.name.add("ピッグマン");
        				damages.value.add(stonepar[0]*base_attack_damage);
        			}
        			break;
        		}
        		case "SKELETON":{
        			damages.name.add("スケルトン");
        			damages.value.add(stonepar[1]*special_attack_damage.get(i));
        			break;
        		}
        		case "UNDEAD":{
        			damages.name.add("アンデット");
        			damages.value.add(special_attack_damage.get(i));
        			if(stonepar[0]!=1) {
        				damages.name.add("ゾンビ");
        				damages.value.add(stonepar[0]*special_attack_damage.get(i));
        			}
        			if(stonepar[0]!=1) {
        				damages.name.add("スケルトン");
        				damages.value.add(stonepar[1]*special_attack_damage.get(i));
        			}
        			break;
        		}
        		case "PIG_ZOMBIE":{
        			damages.name.add("ピッグマン");
        			damages.value.add(stonepar[0]*special_attack_damage.get(i));
        			break;
        		}
        		case "SPIDER":{
        			damages.name.add("クモ");
        			damages.value.add(stonepar[2]*special_attack_damage.get(i));
        			if(stonepar[2]!=1) {
        				damages.name.add("生物");
        				damages.value.add(stonepar[2]*base_attack_damage);
        			}
        			break;
        		}
        		case "IRON_GOLEM":{
        			damages.name.add("ゴーレム");
        			damages.value.add(special_attack_damage.get(i));
        			break;
        		}
        		case "GIANT":{
        			damages.name.add("ジャイアント");
        			damages.value.add(special_attack_damage.get(i));
        			break;
        		}
        		case "WITHER":{
        			damages.name.add("ウィザー");
        			damages.value.add(special_attack_damage.get(i));
        			break;
        		}
        	}
        }
        
        if(stonepar[0]!=1&&!damages.name.contains("ゾンビ")) {
        	damages.name.add("ゾンビ");
        	damages.value.add((stonepar[0]*base_attack_damage));
        }
        if(stonepar[1]!=1&&!damages.name.contains("スケルトン")) {
        	damages.name.add("スケルトン");
        	damages.value.add((stonepar[1]*base_attack_damage));
        }
        if(stonepar[2]!=1&&!damages.name.contains("生物")) {
        	damages.name.add("生物");
        	damages.value.add((stonepar[2]*base_attack_damage));
        }
		
		return damages;
	}
}
