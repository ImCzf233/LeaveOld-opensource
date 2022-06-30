/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.other;

import com.leave.old.Category;
import com.leave.old.Utils.ReflectionUtil;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class SafeWalk
extends Module {
    public SafeWalk() {
        super("SafeWalk", 0, Category.Other, false);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        this.Eagle();
    }

    @Override
    public void enable() {
        super.enable();
    }

    @Override
    public void disable() {
        try {
            ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, false);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.disable();
    }

    void Eagle() {
        try {
            if (SafeWalk.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                if (!SafeWalk.mc.gameSettings.keyBindJump.isPressed()) {
                    BlockPos bp = new BlockPos(SafeWalk.mc.thePlayer.posX, SafeWalk.mc.thePlayer.posY - 1.0, SafeWalk.mc.thePlayer.posZ);
                    if (SafeWalk.mc.theWorld.getBlockState(bp).getBlock() == Blocks.air) {
                        ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, true);
                    } else {
                        ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, false);
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

