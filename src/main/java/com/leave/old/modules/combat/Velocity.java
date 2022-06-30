/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.IntegerSetting;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Velocity
extends Module {
    private IntegerSetting Horizontal = new IntegerSetting("Horizontal", 100.0, 0.0, 100.0, 1);
    private IntegerSetting Vertical = new IntegerSetting("Vertical", 100.0, 0.0, 100.0, 1);

    public Velocity() {
        super("Velocity", 0, Category.Combat, false);
        this.getSetting().add(this.Horizontal);
        this.getSetting().add(this.Vertical);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent ev) {
        if (Tools.isPlayerInGame() && Velocity.mc.thePlayer.maxHurtTime > 0 && Velocity.mc.thePlayer.hurtTime == Velocity.mc.thePlayer.maxHurtTime) {
            if (this.Horizontal.getCurrent() != 100.0) {
                Velocity.mc.thePlayer.motionX *= this.Horizontal.getCurrent() / 100.0;
                Velocity.mc.thePlayer.motionZ *= this.Horizontal.getCurrent() / 100.0;
            }
            if (this.Vertical.getCurrent() != 100.0) {
                Velocity.mc.thePlayer.motionY *= this.Vertical.getCurrent() / 100.0;
            }
        }
    }
}

