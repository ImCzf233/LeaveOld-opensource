/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.IntegerSetting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class HighJump
extends Module {
    TimerUtils timer = new TimerUtils();
    IntegerSetting horizontalValue = new IntegerSetting("Horizontal", 0.0, 0.0, 10.0, 1);
    IntegerSetting verticalValue = new IntegerSetting("Vertical", 5.0, 0.0, 10.0, 1);

    public HighJump() {
        super("HighJump", 0, Category.Movement, false);
        this.getSetting().add(this.horizontalValue);
        this.getSetting().add(this.verticalValue);
    }

    @Override
    public void enable() {
        Tools.nullCheck();
        this.Jump();
        this.toggle();
        super.enable();
    }

    public void sendPacket(Packet packet) {
        HighJump.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public void Jump() {
        double yaw = Math.toRadians(HighJump.mc.thePlayer.rotationYaw);
        double x = -Math.sin(yaw) * this.horizontalValue.getCurrent();
        double z = Math.cos(yaw) * this.horizontalValue.getCurrent();
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, true));
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ, true));
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX + x, HighJump.mc.thePlayer.posY + this.verticalValue.getCurrent(), HighJump.mc.thePlayer.posZ + z, true));
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(HighJump.mc.thePlayer.posX + 0.5, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ + 0.5, true));
        HighJump.mc.thePlayer.setPosition(HighJump.mc.thePlayer.posX + -Math.sin(yaw) * 0.04, HighJump.mc.thePlayer.posY, HighJump.mc.thePlayer.posZ + Math.cos(yaw) * 0.04);
    }
}

