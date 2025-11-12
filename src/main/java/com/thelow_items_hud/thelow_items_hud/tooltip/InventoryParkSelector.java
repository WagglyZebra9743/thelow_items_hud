package com.thelow_items_hud.thelow_items_hud.tooltip;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.hud.thelow_item_hudHUD;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryParkSelector{
	private boolean Getpark = false;
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onGuiOpenTrigger(GuiOpenEvent event) {
    	Getpark = true;
	}
    
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiDraw(GuiScreenEvent.DrawScreenEvent.Pre event) {
		
		//有効じゃないなら何もしなくていい
		if(!ConfigHandler.getstatus)return;
		
		//guiが取れないなら動作する必要はない
		if (event.gui==null||!(event.gui instanceof GuiContainer)) {
			Getpark=false;
			return;
		}
		
		final GuiContainer gui = (GuiContainer) event.gui;
		final Container container = gui.inventorySlots;

		if(!ChecktheContainerIsPerkSelector(container)) {
			Getpark=false;
			return;
		}
		
		//パーク情報を取得する部分(ラグ対策で一度だけ動く)
		if(Getpark) {
            Getpark = false;
			final int chestSlotCount = ((ContainerChest) container).getLowerChestInventory().getSizeInventory();
	        for (int slotIndex = 0;slotIndex<chestSlotCount;slotIndex++) {

	            //スロットのデータを取得
	            final Slot slot = container.getSlot(slotIndex);
	            if (slot==null||!slot.getHasStack())continue;
	            
	            //ItemStack(いつものアイテムのデータ)にする
	            final ItemStack stack = slot.getStack();
	            if (stack==null||!stack.hasTagCompound())continue;
	            
	            //そのアイテムのnbtを取得する
	            final NBTTagCompound nbt = stack.getTagCompound();
	            if(nbt==null)continue;
	            
	            //アイテム名を取得
	            final String itemname = thelow_item_hudHUD.GetItemName(nbt);
	            if(itemname==null)continue;
	            if(!itemname.startsWith("§aQuick Talk Spell"))continue;
	            final Matcher matcher = Pattern.compile("§aQuick Talk Spell \\(([0-9]+)/10\\)").matcher(itemname);
	            if (!matcher.find())continue;
	            final int QuickSpellLevel = Integer.parseInt(matcher.group(1));
	            if(QuickSpellLevel==ConfigHandler.QuickTalkSpell)return;
                ConfigHandler.QuickTalkSpell = QuickSpellLevel;
                ConfigHandler.save();
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a[thelow_item_hud]§7CT減少パークのレベルを更新しました("+QuickSpellLevel+")"));
                break;
	    	}
        }
    }
    
	private static boolean ChecktheContainerIsPerkSelector(Container container) {
		if (!(container instanceof ContainerChest))return false;
		
		//開いたUIの名前を取得する
		final IInventory chestInventory = ((ContainerChest) container).getLowerChestInventory();
		final String guiTitle = StringUtils.stripControlCodes(chestInventory.getDisplayName().getUnformattedText());
		
		//パークメニューかを確認
		if (!(guiTitle.contains("Perk Selector")))return false;
		return true;
	}
}