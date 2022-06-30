/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", 0, Category.Movement, false);
    }

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if (!Sprint.mc.thePlayer.isCollidedHorizontally && Sprint.mc.thePlayer.moveForward > 0.0f) {
            Sprint.mc.thePlayer.setSprinting(true);
        }
    }
}

