package com.thelow_items_hud.thelow_items_hud; // あなたのMODのパッケージ名に合わせてください

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * クライアントサイドのキー入力を処理するクラス
 * このアノテーションは、このクラスがクライアントサイドでのみロードされることを保証します。
 */
@SideOnly(Side.CLIENT)
public class ClientInputHandler {

    /**
     * キーが入力されたフレームで呼び出されるメソッド
     * @param event KeyInputEvent
     */
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        // Minecraftのインスタンスを取得
        Minecraft mc = Minecraft.getMinecraft();

        // 攻撃キーが「押された瞬間」かどうかを判定
        // isKeyDown()は、キーが押された最初のティックでのみtrueを返します。
        if (mc.gameSettings.keyBindAttack.isKeyDown()) {
            
            // GUI画面を開いていない時（ゲームプレイ中）のみ処理を実行
            if (mc.currentScreen == null) {
                // ここに実行したい処理を記述します
                // 例：プレイヤーにメッセージを送信する
                mc.thePlayer.addChatMessage(new ChatComponentText("攻撃キーが押されました！(空振り含む)"));

                // 例：コンソールにログを出力する
                System.out.println("Attack key pressed!");
            }
        }
    }
}