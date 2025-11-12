package com.thelow_items_hud.thelow_items_hud.tooltip;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;
import com.thelow_items_hud.thelow_items_hud.skills.skill;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

class textvalues{
	List<String> name = new ArrayList<>();
	List<Double> value = new ArrayList<>();
}

public class ItemHover {
	
	private static String generalSkillName = "";
	
	private static final FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
	
	@SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
		
		generalSkillName = "";
		if(!Keyclick.enable)return;
		
        final ItemStack stack = event.itemStack;
        if (stack == null || !stack.hasTagCompound())return;
        List<String> tooltip = event.toolTip;
       
        final NBTTagCompound nbt = stack.getTagCompound();
        List<String> lore = thelow_item_hudHUD.getlore(nbt);
        if(lore==null||lore.isEmpty()) return;
        int item_type = -1;
        
        final String item_type_line = thelow_item_hudHUD.GetTextInlore(lore, "以上");
        if(item_type_line!=null) {
            if (item_type_line.contains("剣")) {
            	item_type = 0;
            }
            if (item_type_line.contains("弓")) {
            	item_type = 1;
            }
            if (item_type_line.contains("魔法")) {
            	item_type = 2;
            }
        }
        
        if(item_type==-1) {
            
            //スキルの文字列があるかどうかを判定してtooltipに表示させてみる
            //0.持ってるアイテムから取得したloreを一つのStringにすると同時にアイテムタイプの情報を持っているか確認する
            
            //手に持っているアイテムのnbt
            final NBTTagCompound handitemnbt = thelow_item_hudHUD.latestnbt;
            if(handitemnbt==null)return;//nbtがないなら終了
            //手に持っているアイテムのlore
            final List<String> handitemlore = thelow_item_hudHUD.getlore(handitemnbt);
            if(handitemlore==null||handitemlore.isEmpty()) return;//無いなら終了
           
            //クールタイムの変更を表示したい
            double skillcooltimestone = thelow_item_hudHUD.stonepars(handitemnbt)[3];
            double skillcooltime = 0.0;
            double minskillcooltime = 0.0;
            skillcooltimestone /=100;
            int CTplace = 0;
            String showCTtext="";
            for(int i=0;i<lore.size();i++) {
                String loretxt = lore.get(i);
                if(loretxt!=null&&loretxt.contains("§e CT: ： §6")) {
                	CTplace = i+1;
					showCTtext = loretxt;
                	Pattern p = Pattern.compile("§e CT: ： §6(\\d+(?:\\.\\d+)?)秒");
                    Matcher m = p.matcher(loretxt);
                    while (m.find()) {
                        skillcooltime = Double.parseDouble(m.group(1));
                    }
                    p = Pattern.compile("最小(\\d+(?:\\.\\d+)?)秒");
                    m = p.matcher(loretxt);
                    while (m.find()) {
                        minskillcooltime = Double.parseDouble(m.group(1));
                    }
               	}
            }
            //実行時間を検知する
            //スキルには最小クールタイム時間があることがある
            double activatetime = 0;
            for(String loreline : lore) {
                if(loreline!=null&&loreline.contains("§e 実行時間: ： §6")) {
                	Pattern p = Pattern.compile("§e 実行時間: ： §6(\\d+(?:\\.\\d+)?)秒");
                    Matcher m = p.matcher(loreline);
                    while (m.find()) {
                        activatetime = Double.parseDouble(m.group(1));
                    }
               	}
            }
            showCTtext = skill_cooltime_to_text(skillcooltime,activatetime,skillcooltimestone,minskillcooltime,showCTtext,"%s§f → §b%.2f秒");
            
            String skillsetid = null;
            if(handitemnbt.hasKey("thelow_item_weapon_skill_set_id")) {
        		skillsetid = handitemnbt.getString("thelow_item_weapon_skill_set_id");
        	}
            
            String skill_id = null;
            //一部のスキルは誤取得するのでここで除外しておく
            if(skillsetid!=null&&nbt.hasKey("view_weapon_skill_id")) {
            	 skill_id = skillsetid+nbt.getString("view_weapon_skill_id");
            	 //11wskill42→ストーンフィールド(敵体力の1%)、32wskill127→ホーリーブラッド(回復量1.4倍)、34wskill136→闇の加速(弓の速さ1.4倍)、1wskill2→剣舞(敵最大体力の1%,2%,4%..)
            	if(skill_id!=null&&(skill_id.equals("11wskill42")||skill_id.equals("32wskill127")||skill_id.equals("34wskill136")||skill_id.equals("1wskill2"))){
            		if(showCTtext!=null&&showCTtext!=""&&!showCTtext.isEmpty()) {
            			tooltip.set(CTplace,showCTtext);
            		}
            		return;
            	}
            }
            	
            if(skill_id!=null&&skill_id.equals("32wskill125")) {//恵みの泉
            	skillcooltime=132.0;
                showCTtext = skill_cooltime_to_text(skillcooltime,activatetime,skillcooltimestone,minskillcooltime,showCTtext,"%s§b | %.2f秒(プリ)");
            }
            	
            if(skill_id!=null&&skill_id.equals("40wskill158")) {//百花繚乱
            	skillcooltime=105.0;
                showCTtext = skill_cooltime_to_text(skillcooltime,activatetime,skillcooltimestone,minskillcooltime,showCTtext,"%s§b | %.2f秒(バタシ)");
                tooltip.set(CTplace,showCTtext);
            }
            if(CTplace!=0&&showCTtext!=null&&showCTtext!=""&&!showCTtext.isEmpty()) {
            	tooltip.set(CTplace,showCTtext);
            }
            int handitem_type = -1;
            final String handitem_type_line = thelow_item_hudHUD.GetTextInlore(handitemlore, "以上");
            if(handitem_type_line!=null) {
                if (handitem_type_line.contains("剣")) {
                	handitem_type = 0;
                }
                if (handitem_type_line.contains("弓")) {
                	handitem_type = 1;
                }
                if (handitem_type_line.contains("魔法")) {
                	handitem_type = 2;
                }
            }
            
            if(handitem_type==-1)return;
            
            String loretext = "";
            for(String line : lore) {
            	loretext = loretext+line;
            }
            loretext = loretext.replaceAll("§[0-9a-fk-or]", "");
            	
            	
            //1.それぞれの与える予定ダメージを配列に記録しておく
            	
            final textvalues base_mob_damages = create_damage(handitemnbt,handitem_type,"基礎");
            	
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
                
            if(values!=null&&!values.name.isEmpty()&&!values.value.isEmpty()&&skill_id!=null) {
               	if(skill_id.equals("6wskill22")) {
                   	values.name.add("詠唱付き");
                    values.value.add(8.0);
                }if(skill_id.equals("6wskill23")) {
                   	values.name.add("詠唱付き");
                   	values.value.add(6.0);
               	}if(skill_id.equals("6wskill24")) {
                   	values.name.add("詠唱付き");
                   	values.value.add(18.0);
               	}
               	if(skill_id.equals("36wskill142-2")) {
               		values.name.add("最大");
               		values.value.add(1.2);
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
               			damagetext ="§7"+mobdamagename+String.format("%.2f",mobdamagevalue)+"§4("+String.format("%.2f",mobdamagevalue*1.15)+")§r";
               			addplace++;
                   		tooltip.add(addplace,damagetext);
               		}
               	}
            }
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
            final String text = mob_damages.name.get(i)+": "+String.format("%.2f",mob_damages.value.get(i))+"§4("+String.format("%.2f",mob_damages.value.get(i)*1.15)+")§r";
            tooltip.add(addplace,text);
            addplace++;
            }
       //汎用スキルをtooltipとオーバーライドしたHUDに表示させたい
        if(!nbt.hasKey("thelow_nbttag_weaponskilllist"))return;
        final String SkillListText = nbt.getString("thelow_nbttag_weaponskilllist");
        if(SkillListText==null||SkillListText.isEmpty())return;
        final String[] skillList = SkillListText.split(",");
        if(skillList==null)return;
        String showskilllist = "";
        for(String SkillText : skillList) {
        	final String skillname = skill.getname(SkillText);
        	if(skillname!=null&&!skillname.isEmpty()) {
        		showskilllist = showskilllist + skillname + " ";
        	}
        }
        if(showskilllist.replace(" ", "").equals(""))return;
        for(int i=0;i<tooltip.size();i++) {
        	final String tooltipline = tooltip.get(i);
        	if(tooltipline.equals("§5§o§cShift+「Q」でスキル選択")) {
        		tooltip.add(i+1, "スキル名:"+showskilllist);
        		generalSkillName = showskilllist;
        	}
        }
    }
	
	private static textvalues create_damage(final NBTTagCompound nbt,final int item_type,final String base_attack_text){
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
        	special_attack_damage.set(i, special_attack_damage.get(i)*ConfigHandler.overStrength[item_type]);
        }
        
        final double base_attack_damage = base_attack * ConfigHandler.overStrength[item_type];
        
        final double[] stonepar = thelow_item_hudHUD.stonepars(nbt);
        
        
        damages.name.add(base_attack_text);
        damages.value.add(base_attack_damage);
        
        for (int i=0; i<thelow_item_special_attack_type.size();i++) {
        	final String type = thelow_item_special_attack_type.get(i);
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
	private static String skill_cooltime_to_text(final double skillcooltime,final double activatetime,final double skillcooltimestone,final double minskillcooltime,final String showCTtext,final String textformat) {
		if(showCTtext==null)return null;
		double currentskillcooltime = skillcooltime;
		double theskillcooltime = skillcooltime;
		theskillcooltime*= skillcooltimestone;
		theskillcooltime*=1.0 - (ConfigHandler.QuickTalkSpell * 5.0 / 100.0);
		if(theskillcooltime+activatetime<minskillcooltime) {
			theskillcooltime = minskillcooltime;
		}
		theskillcooltime+=activatetime;
		if(currentskillcooltime==theskillcooltime)return null;
		String CTtext = showCTtext;
		if(theskillcooltime+activatetime!=0) {
			CTtext = String.format(textformat,showCTtext,skillcooltime);
		}
		return CTtext;
	}
	
	@SubscribeEvent
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Pre event) {
		if(generalSkillName.equals(""))return;
		GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, 0.0F, 500.0F);//強制手前表示
        font.drawStringWithShadow("§fスキル名"+generalSkillName, ConfigHandler.generalSkillhudX,ConfigHandler.generalSkillhudY , 0xFFFFFF);
        GlStateManager.popMatrix();
	}
}
