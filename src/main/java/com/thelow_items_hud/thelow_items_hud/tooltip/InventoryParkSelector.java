package com.thelow_items_hud.thelow_items_hud.tooltip;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class InventoryParkSelector extends InventoryBasic {
    public InventoryParkSelector(InventoryBasic inventoryBasic) {
        super(inventoryBasic.getDisplayName().getUnformattedText(), inventoryBasic.hasCustomName(), inventoryBasic.getSizeInventory());
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (stack != null) {
        	if (stack.getDisplayName().contains("Quick Talk Spell")) {
                int stage = generateParkStage(stack); 
                if(stage!=ConfigHandler.QuickTalkSpell&&ConfigHandler.getstatus){
                	ConfigHandler.QuickTalkSpell = stage;
                	ConfigHandler.save();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§a[thelow_item_hud]§7CT減少パークのレベルを更新しました("+stage+")"));
                }
            }
        }
        super.setInventorySlotContents(index, stack);
    }
    
    private static int generateParkStage(ItemStack stack) {
        try {
            return Integer.parseInt(stack.getDisplayName().replaceAll(".*\\(", "").replaceAll("/\\d+", "").replaceAll("\\)$", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}