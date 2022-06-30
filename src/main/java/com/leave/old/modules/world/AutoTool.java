/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

public class AutoTool
extends Module {
    public static int previousSlot;
    public static boolean justFinishedMining;
    public static boolean mining;

    public AutoTool() {
        super("AutoTool", 0, Category.World, false);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (Mouse.isButtonDown((int)0)) {
            Block stateBlock;
            BlockPos lookingAtBlock = AutoTool.mc.objectMouseOver.getBlockPos();
            if (lookingAtBlock != null && (stateBlock = AutoTool.mc.theWorld.getBlockState(lookingAtBlock).getBlock()) != Blocks.air && !(stateBlock instanceof BlockLiquid) && stateBlock instanceof Block) {
                if (!mining) {
                    previousSlot = Tools.getCurrentPlayerSlot();
                    mining = true;
                }
                int index = -1;
                double speed = 1.0;
                for (int slot = 0; slot <= 8; ++slot) {
                    Block bl;
                    BlockPos p;
                    ItemStack itemInSlot = AutoTool.mc.thePlayer.inventory.getStackInSlot(slot);
                    if (itemInSlot != null && itemInSlot.getItem() instanceof ItemTool) {
                        p = AutoTool.mc.objectMouseOver.getBlockPos();
                        bl = AutoTool.mc.theWorld.getBlockState(p).getBlock();
                        if (!((double)itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed)) continue;
                        speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                        index = slot;
                        continue;
                    }
                    if (itemInSlot == null || !(itemInSlot.getItem() instanceof ItemShears)) continue;
                    p = AutoTool.mc.objectMouseOver.getBlockPos();
                    bl = AutoTool.mc.theWorld.getBlockState(p).getBlock();
                    if (!((double)itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState()) > speed)) continue;
                    speed = itemInSlot.getItem().getDigSpeed(itemInSlot, bl.getDefaultState());
                    index = slot;
                }
                if (index != -1 && !(speed <= 1.1) && speed != 0.0) {
                    Tools.hotkeyToSlot(index);
                }
            }
        } else {
            if (mining) {
                Tools.hotkeyToSlot(previousSlot);
            }
            justFinishedMining = false;
            mining = false;
        }
    }
}

