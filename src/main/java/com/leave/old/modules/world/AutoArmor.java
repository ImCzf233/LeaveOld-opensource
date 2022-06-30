/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoArmor
extends Module {
    private TimerUtils timer = new TimerUtils();
    private EnableSetting isOngui = new EnableSetting("Inventory", false);

    public AutoArmor() {
        super("AutoArmor", 0, Category.World, false);
        this.getSetting().add(this.isOngui);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        long delay = 250L;
        if (this.isOngui.getEnable()) {
            if (AutoArmor.mc.currentScreen instanceof GuiInventory && (AutoArmor.mc.currentScreen == null || AutoArmor.mc.currentScreen instanceof GuiInventory || AutoArmor.mc.currentScreen instanceof GuiChat) && this.timer.delay(delay)) {
                this.getBestArmor();
            }
        } else {
            this.getBestArmor();
        }
    }

    public void getBestArmor() {
        for (int type = 1; type < 5; ++type) {
            if (AutoArmor.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack()) {
                ItemStack i = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack();
                if (AutoArmor.isBestArmor(i, type)) continue;
                C16PacketClientStatus is = new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT);
                AutoArmor.mc.thePlayer.sendQueue.addToSendQueue(is);
                this.drop(4 + type);
            }
            for (int var4 = 9; var4 < 45; ++var4) {
                ItemStack var5;
                if (!AutoArmor.mc.thePlayer.inventoryContainer.getSlot(var4).getHasStack() || !AutoArmor.isBestArmor(var5 = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(var4).getStack(), type) || !(AutoArmor.getProtection(var5) > 0.0f)) continue;
                this.shiftClick(var4);
                this.timer.reset();
                return;
            }
        }
    }

    public static boolean isBestArmor(ItemStack stack, int type) {
        float prot = AutoArmor.getProtection(stack);
        String strType = "";
        if (type == 1) {
            strType = "helmet";
        } else if (type == 2) {
            strType = "chestplate";
        } else if (type == 3) {
            strType = "leggings";
        } else if (type == 4) {
            strType = "boots";
        }
        if (!stack.getUnlocalizedName().contains(strType)) {
            return false;
        }
        for (int i = 5; i < 45; ++i) {
            ItemStack is;
            if (!AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(AutoArmor.getProtection(is = AutoArmor.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > prot) || !is.getUnlocalizedName().contains(strType)) continue;
            return false;
        }
        return true;
    }

    public void shiftClick(int slot) {
        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, AutoArmor.mc.thePlayer);
    }

    public void drop(int slot) {
        AutoArmor.mc.playerController.windowClick(AutoArmor.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, AutoArmor.mc.thePlayer);
    }

    public static float getProtection(ItemStack stack) {
        float prot = 0.0f;
        if (stack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor)stack.getItem();
            prot = (float)((double)prot + (double)armor.damageReduceAmount + (double)((100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack) / 100.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 50.0);
            prot = (float)((double)prot + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, stack) / 100.0);
        }
        return prot;
    }
}

