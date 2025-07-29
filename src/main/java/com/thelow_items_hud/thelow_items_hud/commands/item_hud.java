package com.thelow_items_hud.thelow_items_hud.commands;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class item_hud extends CommandBase {

    public static void register(FMLServerStartingEvent event) {
        event.registerServerCommand(new item_hud());
    }

    @Override
    public String getCommandName() {
        return "item_hud";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/item_hud <x> <y> - HUD表示位置を変更します";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.addChatMessage(new ChatComponentText("§c使用方法: /item_hud <x> <y>"));
            return;
        }

        try {
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);

            ConfigHandler.hudX = x;
            ConfigHandler.hudY = y;
            ConfigHandler.save();

            sender.addChatMessage(new ChatComponentText("§a[thelow_item_hud]表示位置を §e(" + x + ", " + y + ") §aに変更しました。"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("§c[thelow_item_hud]数値が無効です。整数で指定してください。"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // 誰でも使用可能
    }
}