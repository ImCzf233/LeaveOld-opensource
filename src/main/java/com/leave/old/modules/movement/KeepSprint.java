/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KeepSprint
extends Module {
    public KeepSprint() {
        super("KeepSprint", 0, Category.Movement, false);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (!KeepSprint.mc.thePlayer.isSprinting()) {
            KeepSprint.mc.thePlayer.setSprinting(true);
        }
    }
}

