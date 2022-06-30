/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiRain
extends Module {
    public AntiRain() {
        super("AntiRain", 0, Category.World, false);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        AntiRain.mc.theWorld.setRainStrength(0.0f);
    }
}

