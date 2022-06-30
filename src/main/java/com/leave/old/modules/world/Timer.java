/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Utils.Mappings;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.IntegerSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Timer
extends Module {
    private IntegerSetting TimerSpeed = new IntegerSetting("TimerSpeed", 2.0, 0.1, 5.0, 1);
    net.minecraft.util.Timer timer = (net.minecraft.util.Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);

    public Timer() {
        super("Timer", 0, Category.World, false);
        this.getSetting().add(this.TimerSpeed);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        this.timer.timerSpeed = (float)this.TimerSpeed.getCurrent();
    }

    @Override
    public void disable() {
        this.timer.timerSpeed = 1.0f;
        super.disable();
    }
}

