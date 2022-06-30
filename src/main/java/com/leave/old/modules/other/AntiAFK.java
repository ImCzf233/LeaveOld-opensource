/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.other;

import com.leave.old.Category;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.IntegerSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiAFK
extends Module {
    private TimerUtils timerUtils = new TimerUtils();
    private IntegerSetting Delay = new IntegerSetting("Delay", 10.0, 1.0, 100.0, 0);

    public AntiAFK() {
        super("AntiAFK", 0, Category.Other, false);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (this.timerUtils.isDelayComplete(2000.0)) {
            AntiAFK.mc.thePlayer.jump();
            this.timerUtils.reset();
        }
    }
}

