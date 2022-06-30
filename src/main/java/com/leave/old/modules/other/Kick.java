/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.other;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Kick
extends Module {
    public Kick() {
        super("Kick", 0, Category.Other, false);
    }

    @Override
    public void enable() {
        this.toggle();
        Kick.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
        super.enable();
    }
}

