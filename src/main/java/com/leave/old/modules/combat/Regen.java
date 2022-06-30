/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.settings.IntegerSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Regen
extends Module {
    public IntegerSetting health = new IntegerSetting("Health", 16.0, 4.0, 20.0, 0);
    public IntegerSetting speed = new IntegerSetting("Speed", 100.0, 10.0, 1000.0, 0);

    public Regen() {
        super("Regen", 0, Category.Combat, false);
        this.getSetting().add(this.health);
        this.getSetting().add(this.speed);
    }

    public void sendPacket(Packet packet) {
        Regen.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Regen.mc.thePlayer.capabilities.isCreativeMode || Regen.mc.thePlayer.getHealth() == 0.0f) {
            return;
        }
        if (Regen.mc.thePlayer.getFoodStats().getFoodLevel() < 18) {
            return;
        }
        if (Regen.mc.thePlayer.getHealth() >= Regen.mc.thePlayer.getMaxHealth()) {
            return;
        }
        if ((double)Regen.mc.thePlayer.getHealth() <= this.health.getCurrent()) {
            for (int i = 0; i < (int)this.speed.getCurrent(); ++i) {
                this.sendPacket(new C03PacketPlayer());
            }
        }
    }
}

