/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoBob
extends Module {
    public NoBob() {
        super("NoBob", 0, Category.Render, false);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        NoBob.mc.thePlayer.distanceWalkedModified = 0.0f;
    }
}

