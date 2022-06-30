/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoSoup
extends Module {
    IntegerSetting health = new IntegerSetting("Health", 6.5, 0.5, 9.5, 1);
    IntegerSetting delay = new IntegerSetting("Delay", 350.0, 50.0, 1000.0, 0);
    EnableSetting drop = new EnableSetting("Drop", false);
    private int oldSlot = -1;

    public AutoSoup() {
        super("AutoSoup", 0, Category.World, false);
        this.getSetting().add(this.health);
        this.getSetting().add(this.delay);
        this.getSetting().add(this.drop);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        int soupSlot = AutoSoup.getSoupFromInventory();
        if (soupSlot != -1 && (double)AutoSoup.mc.thePlayer.getHealth() < this.health.getCurrent()) {
            this.swap(AutoSoup.getSoupFromInventory(), 6);
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
            mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(AutoSoup.mc.thePlayer.inventory.getCurrentItem()));
            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(AutoSoup.mc.thePlayer.inventory.currentItem));
        }
    }

    private int findSoup(int startSlot, int endSlot) {
        for (int i = startSlot; i < endSlot; ++i) {
            ItemStack stack = AutoSoup.mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemSoup)) continue;
            return i;
        }
        return -1;
    }

    private boolean shouldEatSoup() {
        return !((double)AutoSoup.mc.thePlayer.getHealth() > this.health.getCurrent() * 2.0);
    }

    private void stopIfEating() {
        if (this.oldSlot == -1) {
            return;
        }
        KeyBinding.setKeyBindState(AutoSoup.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        AutoSoup.mc.thePlayer.inventory.currentItem = this.oldSlot;
        this.oldSlot = -1;
    }

    protected void swap(int slot, int hotbarNum) {
        AutoSoup.mc.playerController.windowClick(AutoSoup.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, AutoSoup.mc.thePlayer);
    }

    public static int getSoupFromInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        int soup = -1;
        int counter = 0;
        for (int i = 1; i < 45; ++i) {
            ItemStack is;
            Item item;
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || Item.getIdFromItem(item = (is = mc.thePlayer.inventoryContainer.getSlot(i).getStack()).getItem()) != 282) continue;
            ++counter;
            soup = i;
        }
        return soup;
    }
}

