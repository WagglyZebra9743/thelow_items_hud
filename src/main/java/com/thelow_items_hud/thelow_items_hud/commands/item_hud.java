package com.thelow_items_hud.thelow_items_hud.commands;

import java.util.ArrayList;
import java.util.List;

import com.thelow_items_hud.thelow_items_hud.config.ConfigHandler;
import com.thelow_items_hud.thelow_items_hud.skills.APIListener;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
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
        return "/item_hud <place/hud/tooltip/reload/help>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
        	sendmsg("§c使用方法: /item_hud <place/hud/tooltip/reload/help> ",sender);
            return;
        }
        String sub = args[0];

        switch (sub.toLowerCase()) {
            case "place":{
            	try {
                    int x = Integer.parseInt(args[1]);
                    int y = Integer.parseInt(args[2]);

                    sendmsg("§a[thelow_item_hud]§7表示位置を("+ConfigHandler.hudX+","+ConfigHandler.hudY+")から §e(" + x + ", " + y + ") §7に変更しました。",sender);
                    ConfigHandler.hudX = x;
                    ConfigHandler.hudY = y;
                    ConfigHandler.save();

                    if(!ConfigHandler.hudenable) {
                    	sendmsg("§c[thelow_item_hud]使用方法:/item_hud place <x> <y>",sender);
                    	}
                } catch (Exception  e) {
                    sendmsg("§c[thelow_item_hud]使用方法:/item_hud place <x> <y>",sender);
                }
            	break;
            }
            case "hud":{
            	if(args.length>=2&&args[1].equals("on")&&!args[1].equals("off")||!ConfigHandler.hudenable) {
            		ConfigHandler.hudenable=true;
        			sendmsg("§a[thelow_item_hud]§7HUD表示を有効にしました",sender);
            	}else if(args.length>=2&&args[1].equals("off")||ConfigHandler.hudenable){
        			ConfigHandler.hudenable=false;
        			sendmsg("§a[thelow_item_hud]§7HUD表示を無効にしました",sender);
            	}
            	ConfigHandler.save();
            	break;
            }
            case "tooltip":{
            	if(args.length>=2&&args[1].equals("on")&&!args[1].equals("off")||!ConfigHandler.tooltipenable) {
            		ConfigHandler.tooltipenable=true;
            		sendmsg("§a[thelow_item_hud]§7tooltip表示を有効にしました",sender);
            	}else if(args.length>=2&&args[1].equals("off")||ConfigHandler.tooltipenable) {
            		ConfigHandler.tooltipenable=false;
            		sendmsg("§a[thelow_item_hud]§7tooltip表示を無効にしました",sender);
            	}
            	ConfigHandler.save();
            	break;
            }
            case "reload":{
            	Minecraft.getMinecraft().thePlayer.sendChatMessage("/thelow_api detailed_status");
            	APIListener.ReSubscribe_itemCT();
            	sendmsg("§a[thelow_item_hud]§7APIコマンドを送信しました",sender);
            	break;
            }
            case "help":{
            	sendmsg("§a===thelow_item_hudコマンド一覧===" , sender);
            	sendmsg("§7/item_hud place <x> <y> - HUDが表示される場所を設定",sender);
            	sendmsg("§7/item_hud hud <on/off> - HUDの表示を切り替え",sender);
            	sendmsg("§7/item_hud tooltip <on/off> - tooltip(カーソルを合わせた時)の表示を切り替え",sender);
            	sendmsg("§7/item_hud reload - APIコマンドを送信して現在のステータスを取得します",sender);
            	sendmsg("§7/item_hud help - この画面を表示",sender);
            	break;
            }
            default:
                sendmsg("§c不明なコマンドです。/item_hud help でヘルプを表示します。",sender);
                break;
        }

        
    }
    
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("place");
            options.add("hud");
            options.add("tooltip");
            options.add("reload");
            options.add("help");
            return getListOfStringsMatchingLastWord(args, options.toArray(new String[0]));
        }
        if(args.length == 2) {
        	List<String> options = new ArrayList<>();
        	options.add("on");
        	options.add("off");
        	return getListOfStringsMatchingLastWord(args,options.toArray(new String[0]));
        }
        
        return null;
    }
    
    private static void sendmsg(String msg , ICommandSender sender) {
    	sender.addChatMessage(new ChatComponentText(msg));
    }
}