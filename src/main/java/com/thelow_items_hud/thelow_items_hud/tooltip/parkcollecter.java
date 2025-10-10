package com.thelow_items_hud.thelow_items_hud.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryBasic;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class parkcollecter {
	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent event) {
	    // 1. 開かれたGUIがGuiChestかを確認
	    if (!(event.gui instanceof GuiChest)) {
	        return;
	    }

	    GuiChest gui = (GuiChest) event.gui;

	    // 2. GuiChestのインベントリコンテナがContainerChestかを確認
	    if (!(gui.inventorySlots instanceof ContainerChest)) {
	        return;
	    }

	    ContainerChest container = (ContainerChest) gui.inventorySlots;

	    // 3. 下段のインベントリがInventoryBasicかを確認
	    if (!(container.getLowerChestInventory() instanceof InventoryBasic)) {
	        return;
	    }

	    InventoryBasic inv = (InventoryBasic) container.getLowerChestInventory();

	    // 4. インベントリの名前に "Perk Selector" が含まれているかを確認
	    if (!inv.getDisplayName().getUnformattedText().contains("Perk Selector")) {
	        return;
	    }

	    // ここにパーク選択画面だった場合の処理を記述
	    gui.inventorySlots = new ContainerChest(
	            Minecraft.getMinecraft().thePlayer.inventory,
	            new InventoryParkSelector(inv),
	            Minecraft.getMinecraft().thePlayer
	    );
	}
}
