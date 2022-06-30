/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.Utils.ChatUtils;
import com.leave.old.modules.Module;
import com.leave.old.settings.IntegerSetting;
import java.lang.reflect.Field;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SuperKnockBack
extends Module {
    Field c = null;
    private IntegerSetting hurtTime = new IntegerSetting("HurtTime", 10.0, 0.0, 10.0, 1);

    public SuperKnockBack() {
        super("SuperKnockBack", 0, Category.Combat, false);
        this.getSetting().add(this.hurtTime);
    }

    @SubscribeEvent
    public void onAttackEntity(AttackEntityEvent event) {
        if (event.entity instanceof EntityLivingBase) {
            if ((double)event.entityLiving.hurtTime > this.hurtTime.getCurrent()) {
                return;
            }
            if (SuperKnockBack.mc.thePlayer.isSprinting()) {
                SuperKnockBack.mc.thePlayer.setSprinting(true);
            }
            SuperKnockBack.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(SuperKnockBack.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            SuperKnockBack.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(SuperKnockBack.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            try {
                Class<?> ca = SuperKnockBack.mc.thePlayer.getClass();
                Field field = ca.getDeclaredField("serverSprintState");
                field.setAccessible(true);
                field.setBoolean(SuperKnockBack.mc.thePlayer, true);
            }
            catch (Exception d) {
                ChatUtils.error(d);
            }
        }
    }
}

