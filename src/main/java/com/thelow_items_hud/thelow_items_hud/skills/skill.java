package com.thelow_items_hud.thelow_items_hud.skills;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class skill {
	private static Map<String, String> skillMap = new HashMap<>();
	static {
		//武器固有スキルby wiki
		skillMap.put("1","華麗なる剣技");
		skillMap.put("1wskill2","剣舞");
		skillMap.put("1wskill3","パリィ");
		skillMap.put("1wskill4","デスダンス");
		skillMap.put("2","禁忌の力");
		skillMap.put("2wskill6","予兆");
		skillMap.put("2wskill7","開放");
		skillMap.put("2wskill8","覚醒");
		skillMap.put("3","炎神の怒り");
		skillMap.put("3wskill10","焼却");
		skillMap.put("3wskill11","炎の舞");
		skillMap.put("3wskill12","ラヴァネス");
		skillMap.put("4","ヘッドショット");
		skillMap.put("4wskill14","トラップ");
		skillMap.put("4wskill15","ロックオン");
		skillMap.put("4wskill16","遠距離スナイプ");
		skillMap.put("5","闘争本能");
		skillMap.put("5wskill18","狂気");
		skillMap.put("5wskill19","レイジ");
		skillMap.put("5wskill20","バーサーク");
		skillMap.put("6","詠唱");
		skillMap.put("6wskill22","マジックボール");
		skillMap.put("6wskill23","ライトニングボルト");
		skillMap.put("6wskill24","メテオストライク");
		skillMap.put("6wskill24-2","ファイヤーボルケーノ");
		skillMap.put("7","フリーズ");
		skillMap.put("7wskill26","アイスショット");
		skillMap.put("7wskill27","ブラインドアイ");
		skillMap.put("7wskill28","フロストアロー");
		skillMap.put("8","鋼の心");
		skillMap.put("8wskill30","硬化");
		skillMap.put("8wskill31","挑発");
		skillMap.put("8wskill32","英雄伝");
		skillMap.put("9","平和の心");
		skillMap.put("9wskill34","みんながんばって！");
		skillMap.put("9wskill35","平和のために！");
		skillMap.put("9wskill36","戦姫の号令");
		skillMap.put("11","大地の加護");
		skillMap.put("11wskill42","ストーンフィールド");
		skillMap.put("11wskill43","トゥルーロック");
		skillMap.put("11wskill44","ショックストーン");
		skillMap.put("12","光ある場所に");
		skillMap.put("12wskill46","ライトスパーク");
		skillMap.put("12wskill47","触れられざるもの");
		skillMap.put("12wskill48","神の鉄槌");
		skillMap.put("13","死神の鎌");
		skillMap.put("13wskill50","死を操るもの");
		skillMap.put("13wskill51","迫りくる恐怖");
		skillMap.put("13wskill52","死の宣告");
		skillMap.put("19","ブラッドヒール");
		skillMap.put("19wskill74","血の渇望");
		skillMap.put("19wskill75","血の流動");
		skillMap.put("19wskill76","血の斬撃");
		skillMap.put("22wskill85","-黒竜- ヘイロン");
		skillMap.put("22","竜魔術");
		skillMap.put("23wskill89","-黒竜- ヘイロン -滅-");
		skillMap.put("23wskill90","龍の刻印");
		skillMap.put("23","竜の契約");
		skillMap.put("26wskill101","炎帝 ~バジリスクの炎息~");
		skillMap.put("26wskill102","猛火斬り");
		skillMap.put("26wskill103","鎮火");
		skillMap.put("26","火傷");
		skillMap.put("27wskill105","ブリザードストライク");
		skillMap.put("27wskill106","アイスヒール");
		skillMap.put("27wskill107","アイススロウ");
		skillMap.put("27","氷の加護");
		skillMap.put("28wskill109","カオスブリザード");
		skillMap.put("28wskill110","アイススタンプ");
		skillMap.put("28wskill111","雪柱");
		skillMap.put("28","氷の加護");
		skillMap.put("29","闇の解放");
		skillMap.put("29wskill114","血の代償");
		skillMap.put("29wskill116","冥府の審判");
		skillMap.put("30wskill117","オーバーシュート");
		skillMap.put("30wskill118","シャドウパワー");
		skillMap.put("30wskill119","ポイズンキラー");
		skillMap.put("30","エレメンタルパワー");
		skillMap.put("31wskill121","戦姫の号令");
		skillMap.put("31wskill122","氷の鎧");
		skillMap.put("31wskill123","ホーリーキュア");
		skillMap.put("31","力の加護");
		skillMap.put("32wskill125","恵みの泉");
		skillMap.put("32wskill126","隠密");
		skillMap.put("32wskill127","ホーリーブラッド");
		skillMap.put("32","闇の呪縛");
		skillMap.put("33wskill103","鎮火");
		skillMap.put("33","炎の極意");
		skillMap.put("33wskill131","大火炎");
		skillMap.put("33wskill132","炎の神殿");
		skillMap.put("34wskill117","オーバーシュート");
		skillMap.put("34wskill134","矢筒み");
		skillMap.put("34wskill135","自動射撃");
		skillMap.put("34","闇の加速");
		skillMap.put("35wskill137","サテライトキャノン");
		skillMap.put("35wskill138","ダークサイクロン");
		skillMap.put("35wskill139","インフェライズ");
		skillMap.put("35","グロウ");
		skillMap.put("36","下剋上");
		skillMap.put("36wskill142","居合斬り");
		skillMap.put("36wskill142-2","率舞");
		skillMap.put("36wskill143","集中");
		skillMap.put("36wskill144","天下無双");
		skillMap.put("37","準召喚術式　吸引");
		skillMap.put("37wskill149","召喚術式 ~魔~");
		skillMap.put("38","準召喚術式　吸引");
		skillMap.put("38wskill150","召喚術式 ~弓~");
		skillMap.put("39","準召喚術式　吸引");
		skillMap.put("39wskill151","召喚術式 ~剣~");
		skillMap.put("40wskill156","激昂乱舞");
		skillMap.put("40wskill157","才色兼備");
		skillMap.put("40wskill158","百火繚乱");
		skillMap.put("40","錦上添花");
		skillMap.put("43","エース");
		skillMap.put("45wskill3","攻撃破壊");
		skillMap.put("45wskill44","魔力解放");
		skillMap.put("45","覇気");
		skillMap.put("45wskill74","魔力吸収");
		skillMap.put("46","冥雷");
		skillMap.put("46wskill165","イモータルスフィア");
		skillMap.put("46wskill166","アタナイトキューブ");
		skillMap.put("46wskill167","咒力爆弾");
		skillMap.put("47","愛と美の女神");
		skillMap.put("47wskill169","獄陽炎");
		skillMap.put("47wskill170","ブラックホール");
		skillMap.put("47wskill171","グラビティエンド");
		skillMap.put("48wskill172","パニッシュメント");
		skillMap.put("48wskill173","士気高揚");
		skillMap.put("48wskill174","ステッドショック");
		skillMap.put("48","血の欲望");
		skillMap.put("50wskill48","神の鉄槌");
		skillMap.put("50wskill51","迫りくる恐怖");
		skillMap.put("50","闇の解放");
		skillMap.put("50wskill169","獄陽炎");
		skillMap.put("51wskill20","バーサーク");
		skillMap.put("51wskill30","硬化");
		skillMap.put("51wskill138","ダークサイクロン");
		skillMap.put("51","下剋上");
		skillMap.put("52","ブラッドヒール");
		skillMap.put("52wskill103","鎮火");
		skillMap.put("52wskill105","ブリザードストライク");
		skillMap.put("52wskill111","雪柱");
		skillMap.put("53wskill30","アイスアーマー");
		skillMap.put("53wskill105","ブリザードストライク");
		skillMap.put("53wskill111","雪柱");
		skillMap.put("53","黄泉の冷気");
		skillMap.put("55skill1","ライトニングオーダー");
		skillMap.put("55","古代の契約");
		skillMap.put("55wskill135","トリスタン");
		skillMap.put("55wskill55_1","覇者の残骸");
		skillMap.put("57wskill3","パリィ");
		skillMap.put("60wsikll54_1","勝者の制約");
		skillMap.put("60wskill54_2","crash");
		skillMap.put("60wskill54_3","Impaction");
		skillMap.put("64wskill18","龍の一閃");
		skillMap.put("64wskill75","龍の福音");
		skillMap.put("64wskill105","覇王の鉄槌");
		skillMap.put("64wskill141","威風堂々");
		skillMap.put("67wskill67_1","スペルダンス");
		skillMap.put("67wskill67_2","激情");
		skillMap.put("67wskill67_3","星輝神の歌声");
		skillMap.put("67wskill67_4","束縛のエクリプス");
		skillMap.put("68wskill68_1","ラータイブ：ザイン");
		skillMap.put("68wskill68_2","ラータイブ：アレフ");
		skillMap.put("68wskill68_3","ラータイブ：メギド");
		skillMap.put("68wskill68_4","セグヴァ");
		skillMap.put("70wskill111","雪柱");
		skillMap.put("70wskill134","矢筒み");
		skillMap.put("70wskill141","執行者");
		skillMap.put("74","霊魂");//勝手にパッシブと予想
		skillMap.put("74wskill177","醜悪の賽");
		skillMap.put("74wskill178","疾風怒濤");
		skillMap.put("74wskill179","朱炎ノ槍");
		skillMap.put("75wskill105","捨て鉢");
		skillMap.put("75wskill118","承認欲求");
		skillMap.put("75wskill134","矢筒");
		skillMap.put("75","冥妬の怨");
		skillMap.put("76wskill180","咒砲");
		skillMap.put("76wskill181","厄災降臨");
		skillMap.put("76wskill182","呑甦");
		skillMap.put("76","煌雷");//勝手にパッシブと予想
		skillMap.put("79wskill194","ゼータ・リュラエ");
		skillMap.put("79wskill195","アウトバースト");
		skillMap.put("79wskill196","シータ・リュラエ");
		skillMap.put("79","オービタリティ");
		skillMap.put("yhj-ws1wskill3","パリィ(改)");
		skillMap.put("yhj-ws2wskill20","バーサーク(改)");
		skillMap.put("yhj-ws3wskill52","死の宣告(改)");
		skillMap.put("yhj-ws4wskill76","血の斬撃(改)");

		//汎用スキルby wiki
		skillMap.put("n_s_NEAR_RANGE_THUNDER","ライゴウ");
		skillMap.put("n_s_NEAR_RANGE_ICE","アイスエイジ");
		skillMap.put("n_s_NEAR_RANGE_FIRE","陽炎 -かげろう");
		skillMap.put("n_s_NEAR_RANGE_DARKNESS","リベイション");
		skillMap.put("n_s_FAR_RANGE_THUNDER","エル・トール");
		skillMap.put("n_s_FAR_RANGE_ICE","フェザントアロー");
		skillMap.put("n_s_FAR_RANGE_FIRE","メイゴウ");
		skillMap.put("n_s_FAR_RANGE_DARKNESS","ブラッククラッシャー");
		skillMap.put("n_s_FAR_SINGLE_LEVEL1","ミラクルストーム");
		skillMap.put("n_s_FAR_SINGLE_LEVEL2","サイクロンスター");
		skillMap.put("n_s_FAR_SINGLE_LEVEL3","クリスタルエアー");
		skillMap.put("n_s_FAR_SINGLE_LEVEL4","エンジェルリバース");
		skillMap.put("n_skill_r_fire","天使の囁き");
		skillMap.put("n_skill_5","姫の応援");
		skillMap.put("n_skill_1","天狗の加護");
		skillMap.put("n_skill_11","鋼の鎧");
		skillMap.put("n_skill_22","羊飼いの一声");
		skillMap.put("n_skill_21","騎士の喚き声");

	}
	public static boolean yochou = false;
	public static boolean kaihou = false;
	public static boolean yamikaihou = false;
	public static boolean esu = false;

	
	public static String getsp(NBTTagCompound nbt) {
		if (nbt.hasKey("thelow_item_weapon_skill_special_skill")) {//スキルデータがあるかをチェック
        	String skill = nbt.getString("thelow_item_weapon_skill_special_skill");//格納
        	if(skill.equals("skill_cancel"))return null;
        	String skillset = nbt.getString("thelow_item_weapon_skill_set_id");
        	String skillid = skillset+skill;
        	return skillid;
        }
		return null;
	}
	public static String getno(NBTTagCompound nbt) {
		if (nbt.hasKey("thelow_item_weapon_skill_normal_skill")) {//スキルデータがあるかをチェック
        	String skill = nbt.getString("thelow_item_weapon_skill_normal_skill");//格納
        	if(skill.equals("skill_cancel"))return null;
        	if (nbt.hasKey("thelow_item_weapon_skill_set_id")) {
        	String skillset = nbt.getString("thelow_item_weapon_skill_set_id");
        	String skillid = skillset+skill;
        	return skillid;
        	}else return skill;
        }
		return null;
	}
	public static String getpa(NBTTagCompound nbt) {
		if(nbt.hasKey("thelow_item_weapon_skill_set_id")) {
			String skillid = nbt.getString("thelow_item_weapon_skill_set_id");
			return skillid;
		}
		return null;
	}
	public static String getname(String skill) {
		String skillname = skillMap.getOrDefault(skill,null);
		if(skillname==null)return null;
		if(skillname.equals("闇の解放")) {
			timer.Yamikaihou();
		}else {
			timer.YamikaihouReset();
		}
		if(skillname.equals("エース")) {
			timer.Esu();
		}else {
			timer.EsuReset();
		}
		return skillname;
	}
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent  event) {
	    if (event.entityPlayer == Minecraft.getMinecraft().thePlayer) {

				timer.YamikaihouReset();
				timer.EsuReset();
			System.out.println("attackd");
	    }
	}
	
	@SubscribeEvent
	public void onDamage(LivingHurtEvent event) {
	    EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
	    if (event.entity == player) {
	    	
	    			timer.EsuReset();
	    			
	    			System.out.println("damaged");
	    }
	}
	@SubscribeEvent
	public void onActionBar(ClientChatReceivedEvent event) {
		
    			// アクションバーかどうか判定
    			if (event.type == 2) { // 2 = アクションバー
    				String text = event.message.getUnformattedText();
	        
    				// ボス名の判定
    			if (text.contains("§c【")) {
    				timer.EsuReset();
    				System.out.println("VSboss");
    			
	        }
	    }
	}
	
}
