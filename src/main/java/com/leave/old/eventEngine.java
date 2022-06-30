/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.leave.old;

import com.leave.old.Client;
import com.leave.old.Utils.Connection;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.eventapi.EventManager;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class eventEngine {
    private boolean init;
    private TimerUtils timerUtils = new TimerUtils();

    public eventEngine() {
        EventManager.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Tools.nullCheck()) {
            this.init = false;
            return;
        }
        if (!this.init) {
            new Connection(this);
            this.init = true;
        }
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        boolean suc = true;
        for (Module module : Client.moduleManager.getModules()) {
            if (!module.getState() || Minecraft.getMinecraft().theWorld == null) continue;
            suc &= module.onPacket(packet, side);
        }
        return suc;
    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            return;
        }
        int key = Keyboard.getEventKey();
        if (key == 0) {
            return;
        }
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        if (!this.timerUtils.isDelayComplete(10.0)) {
            this.timerUtils.reset();
            return;
        }
        for (Module m : Client.moduleManager.getModules()) {
            if (m.getKey() != Keyboard.getEventKey()) continue;
            m.toggle();
        }
    }
}

