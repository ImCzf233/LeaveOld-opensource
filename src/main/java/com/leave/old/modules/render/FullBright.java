/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FullBright
extends Module {
    private float old;

    public FullBright() {
        super("FullBright", 0, Category.Render, false);
    }

    @Override
    public void enable() {
        this.old = FullBright.mc.gameSettings.gammaSetting;
        super.enable();
    }

    @Override
    public void disable() {
        super.enable();
        FullBright.mc.gameSettings.gammaSetting = this.old;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            this.disable();
            return;
        }
        if (FullBright.mc.gameSettings.gammaSetting != 10000.0f) {
            FullBright.mc.gameSettings.gammaSetting = 10000.0f;
        }
    }
}

