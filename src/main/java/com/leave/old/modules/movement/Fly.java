/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.util.Arrays;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Fly
extends Module {
    private final ModeSetting Mode = new ModeSetting("Mode", "Dynamic", Arrays.asList("Simple", "Dynamic"), this);
    private IntegerSetting speed = new IntegerSetting("percent", 1.0, 0.1, 2.0, 1);

    public Fly() {
        super("Fly", 0, Category.Movement, false);
        this.getSetting().add(this.Mode);
        this.getSetting().add(this.speed);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.Mode.getCurrent() == "Dynamic") {
            if (!Tools.currentScreenMinecraft()) {
                return;
            }
            EntityPlayerSP player = Fly.mc.thePlayer;
            float flyspeed = (float)this.speed.getCurrent();
            player.jumpMovementFactor = 0.4f;
            player.motionX = 0.0;
            player.motionY = 0.0;
            player.motionZ = 0.0;
            player.jumpMovementFactor *= flyspeed * 3.0f;
            if (Fly.mc.gameSettings.keyBindJump.isKeyDown()) {
                player.motionY += (double)flyspeed;
            }
            if (Fly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                player.motionY -= (double)flyspeed;
            }
        } else if (this.Mode.getCurrent() == "Simple") {
            EntityPlayerSP player = Fly.mc.thePlayer;
            player.capabilities.isFlying = true;
        }
    }

    @Override
    public void disable() {
        if (Fly.mc.thePlayer.capabilities.isFlying) {
            Fly.mc.thePlayer.capabilities.isFlying = false;
        }
        super.disable();
    }
}

