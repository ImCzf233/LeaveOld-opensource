/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class InvMove
extends Module {
    public InvMove() {
        super("InvMove", 0, Category.Movement, false);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (InvMove.mc.currentScreen != null) {
            if (!(InvMove.mc.currentScreen instanceof GuiChat)) {
                KeyBinding[] key;
                KeyBinding[] keyBindingArray = new KeyBinding[6];
                keyBindingArray[0] = InvMove.mc.gameSettings.keyBindForward;
                keyBindingArray[1] = InvMove.mc.gameSettings.keyBindBack;
                keyBindingArray[2] = InvMove.mc.gameSettings.keyBindLeft;
                keyBindingArray[3] = InvMove.mc.gameSettings.keyBindRight;
                keyBindingArray[4] = InvMove.mc.gameSettings.keyBindSprint;
                keyBindingArray[5] = InvMove.mc.gameSettings.keyBindJump;
                KeyBinding[] array = key = keyBindingArray;
                int lengths = key.length;
                for (int i = 0; i < lengths; ++i) {
                    KeyBinding b = array[i];
                    KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown((int)b.getKeyCode()));
                }
            }
        }
    }
}

