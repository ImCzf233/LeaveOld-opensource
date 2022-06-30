/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoJump
extends Module {
    private boolean c = false;
    private EnableSetting b = new EnableSetting("Shifting cancel", true);

    public AutoJump() {
        super("AutoJump", 0, Category.Movement, false);
        this.getSetting().add(this.b);
    }

    @Override
    public void disable() {
        super.disable();
        this.c = false;
        this.ju(false);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        if (Tools.isPlayerInGame()) {
            if (!(!AutoJump.mc.thePlayer.onGround || this.b.getEnable() && AutoJump.mc.thePlayer.isSneaking())) {
                if (AutoJump.mc.theWorld.getCollidingBoundingBoxes(AutoJump.mc.thePlayer, AutoJump.mc.thePlayer.getEntityBoundingBox().offset(AutoJump.mc.thePlayer.motionX / 3.0, -1.0, AutoJump.mc.thePlayer.motionZ / 3.0)).isEmpty()) {
                    this.c = true;
                    this.ju(true);
                } else if (this.c) {
                    this.c = false;
                    this.ju(false);
                }
            } else if (this.c) {
                this.c = false;
                this.ju(false);
            }
        }
    }

    private void ju(boolean ju) {
        KeyBinding.setKeyBindState(AutoJump.mc.gameSettings.keyBindJump.getKeyCode(), ju);
    }
}

