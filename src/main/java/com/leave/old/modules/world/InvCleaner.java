/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.modules.world.AutoArmor;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InvCleaner
extends Module {
    private IntegerSetting BlockCap = new IntegerSetting("MaxBlocks", 128.0, 0.0, 256.0, 1);
    private EnableSetting Food = new EnableSetting("Food", false);
    private EnableSetting Sort = new EnableSetting("Sort", false);
    private EnableSetting Bow = new EnableSetting("Bow", false);
    private EnableSetting Sword = new EnableSetting("Sword", true);
    private EnableSetting UHC = new EnableSetting("UHC", true);
    private IntegerSetting Delay = new IntegerSetting("Delay", 50.0, 0.0, 1000.0, 1);
    TimerUtils timer = new TimerUtils();
    public static int weaponSlot = 36;
    public static int pickaxeSlot = 37;
    public static int axeSlot = 38;
    public static int shovelSlot = 39;

    public InvCleaner() {
        super("InvCleaner", 0, Category.World, false);
        this.getSetting().add(this.BlockCap);
        this.getSetting().add(this.Food);
        this.getSetting().add(this.Sort);
        this.getSetting().add(this.Bow);
        this.getSetting().add(this.Sword);
        this.getSetting().add(this.UHC);
        this.getSetting().add(this.Delay);
    }

    @SubscribeEvent
    public void onPre(TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        double delay = this.Delay.getCurrent();
        if (InvCleaner.mc.currentScreen == null || InvCleaner.mc.currentScreen instanceof GuiInventory || InvCleaner.mc.currentScreen instanceof GuiChat) {
            if (this.timer.delay((float)delay) && weaponSlot >= 36) {
                if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
                    this.getBestWeapon(weaponSlot);
                } else if (!this.isBestWeapon(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
                    this.getBestWeapon(weaponSlot);
                }
            }
            if (this.Sort.getEnable()) {
                if (this.timer.delay((float)delay) && pickaxeSlot >= 36) {
                    this.getBestPickaxe(pickaxeSlot);
                }
                if (this.timer.delay((float)delay) && shovelSlot >= 36) {
                    this.getBestShovel(shovelSlot);
                }
                if (this.timer.delay((float)delay) && axeSlot >= 36) {
                    this.getBestAxe(axeSlot);
                }
            }
            if (this.timer.delay((float)delay)) {
                for (int i = 9; i < 45; ++i) {
                    ItemStack is;
                    if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.shouldDrop(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack(), i)) continue;
                    this.drop(i);
                    this.timer.reset();
                    if (delay > 0.0) break;
                }
            }
        }
    }

    public boolean shouldDrop(ItemStack stack, int slot) {
        if (stack.getDisplayName().toLowerCase().contains("(right click)")) {
            return false;
        }
        if (this.UHC.getEnable()) {
            if (stack.getDisplayName().toLowerCase().contains("apple")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("head")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("gold")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("crafting table")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("stick")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("and") && stack.getDisplayName().toLowerCase().contains("ril")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("axe of perun")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("barbarian")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("bloodlust")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("dragonchest")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("dragon sword")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("excalibur")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("exodus")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("fusion armor")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("hermes boots")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("hide of leviathan")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("scythe")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("seven-league boots")) {
                return false;
            }
            if (stack.getDisplayName().toLowerCase().contains("shoes of vidar")) {
                return false;
            }
        }
        if (slot == weaponSlot && this.isBestWeapon(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack()) || slot == pickaxeSlot && this.isBestPickaxe(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack()) && pickaxeSlot >= 0 || slot == axeSlot && this.isBestAxe(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack()) && axeSlot >= 0 || slot == shovelSlot && this.isBestShovel(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack()) && shovelSlot >= 0) {
            return false;
        }
        if (stack.getItem() instanceof ItemArmor) {
            for (int type = 1; type < 5; ++type) {
                ItemStack is;
                if (InvCleaner.mc.thePlayer.inventoryContainer.getSlot(4 + type).getHasStack() && AutoArmor.isBestArmor(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(4 + type).getStack(), type) || !AutoArmor.isBestArmor(stack, type)) continue;
                return false;
            }
        }
        if (stack.getItem() instanceof ItemBlock && this.getBlockCount() > (int)this.BlockCap.getCurrent()) {
            return true;
        }
        if (stack.getItem() instanceof ItemPotion && this.isBadPotion(stack)) {
            return true;
        }
        if (stack.getItem() instanceof ItemFood && this.Food.getEnable() && !(stack.getItem() instanceof ItemAppleGold)) {
            return true;
        }
        if (stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor) {
            return true;
        }
        if ((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && this.Bow.getEnable()) {
            return true;
        }
        return stack.getItem().getUnlocalizedName().contains("tnt") || stack.getItem().getUnlocalizedName().contains("stick") || stack.getItem().getUnlocalizedName().contains("egg") || stack.getItem().getUnlocalizedName().contains("string") || stack.getItem().getUnlocalizedName().contains("cake") || stack.getItem().getUnlocalizedName().contains("mushroom") || stack.getItem().getUnlocalizedName().contains("flint") || stack.getItem().getUnlocalizedName().contains("compass") || stack.getItem().getUnlocalizedName().contains("dyePowder") || stack.getItem().getUnlocalizedName().contains("feather") || stack.getItem().getUnlocalizedName().contains("bucket") || stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect") || stack.getItem().getUnlocalizedName().contains("snow") || stack.getItem().getUnlocalizedName().contains("fish") || stack.getItem().getUnlocalizedName().contains("enchant") || stack.getItem().getUnlocalizedName().contains("exp") || stack.getItem().getUnlocalizedName().contains("shears") || stack.getItem().getUnlocalizedName().contains("anvil") || stack.getItem().getUnlocalizedName().contains("torch") || stack.getItem().getUnlocalizedName().contains("seeds") || stack.getItem().getUnlocalizedName().contains("leather") || stack.getItem().getUnlocalizedName().contains("reeds") || stack.getItem().getUnlocalizedName().contains("skull") || stack.getItem().getUnlocalizedName().contains("record") || stack.getItem().getUnlocalizedName().contains("snowball") || stack.getItem() instanceof ItemGlassBottle || stack.getItem().getUnlocalizedName().contains("piston");
    }

    private int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock)) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }

    private void getBestShovel(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestShovel(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || shovelSlot == i || this.isBestWeapon(is)) continue;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getHasStack()) {
                this.swap(i, shovelSlot - 36);
                this.timer.reset();
                if (!(this.Delay.getCurrent() > 0.0)) continue;
                return;
            }
            if (this.isBestShovel(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(shovelSlot).getStack())) continue;
            this.swap(i, shovelSlot - 36);
            this.timer.reset();
            if (!(this.Delay.getCurrent() > 0.0)) continue;
            return;
        }
    }

    private boolean isBestShovel(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemSpade)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemSpade)) continue;
            return false;
        }
        return true;
    }

    private boolean isBestAxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemAxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemAxe) || this.isBestWeapon(stack)) continue;
            return false;
        }
        return true;
    }

    private void getBestAxe(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestAxe(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || axeSlot == i || this.isBestWeapon(is)) continue;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getHasStack()) {
                this.swap(i, axeSlot - 36);
                this.timer.reset();
                if (!(this.Delay.getCurrent() > 0.0)) continue;
                return;
            }
            if (this.isBestAxe(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(axeSlot).getStack())) continue;
            this.swap(i, axeSlot - 36);
            this.timer.reset();
            if (!(this.Delay.getCurrent() > 0.0)) continue;
            return;
        }
    }

    private void getBestPickaxe(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestPickaxe(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || pickaxeSlot == i || this.isBestWeapon(is)) continue;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getHasStack()) {
                this.swap(i, pickaxeSlot - 36);
                this.timer.reset();
                if (!(this.Delay.getCurrent() > 0.0)) continue;
                return;
            }
            if (this.isBestPickaxe(InvCleaner.mc.thePlayer.inventoryContainer.getSlot(pickaxeSlot).getStack())) continue;
            this.swap(i, pickaxeSlot - 36);
            this.timer.reset();
            if (!(this.Delay.getCurrent() > 0.0)) continue;
            return;
        }
    }

    private boolean isBestPickaxe(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemPickaxe)) {
            return false;
        }
        float value = this.getToolEffect(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getToolEffect(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > value) || !(is.getItem() instanceof ItemPickaxe)) continue;
            return false;
        }
        return true;
    }

    private float getToolEffect(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemTool)) {
            return 0.0f;
        }
        String name = item.getUnlocalizedName();
        ItemTool tool = (ItemTool)item;
        float value = 1.0f;
        if (item instanceof ItemPickaxe) {
            value = tool.getStrVsBlock(stack, Blocks.stone);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemSpade) {
            value = tool.getStrVsBlock(stack, Blocks.dirt);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else if (item instanceof ItemAxe) {
            value = tool.getStrVsBlock(stack, Blocks.log);
            if (name.toLowerCase().contains("gold")) {
                value -= 5.0f;
            }
        } else {
            return 1.0f;
        }
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack) * 0.0075);
        value = (float)((double)value + (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack) / 100.0);
        return value;
    }

    private boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion)stack.getItem();
            if (potion.getEffects(stack) == null) {
                return true;
            }
            for (PotionEffect o : potion.getEffects(stack)) {
                PotionEffect effect = o;
                if (effect.getPotionID() != Potion.poison.getId() && effect.getPotionID() != Potion.harm.getId() && effect.getPotionID() != Potion.moveSlowdown.getId() && effect.getPotionID() != Potion.weakness.getId()) continue;
                return true;
            }
        }
        return false;
    }

    public void shiftClick(int slot) {
        InvCleaner.mc.playerController.windowClick(InvCleaner.mc.thePlayer.inventoryContainer.windowId, slot, 0, 1, InvCleaner.mc.thePlayer);
    }

    public void swap(int slot1, int hotbarSlot) {
        InvCleaner.mc.playerController.windowClick(InvCleaner.mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, InvCleaner.mc.thePlayer);
    }

    public void drop(int slot) {
        InvCleaner.mc.playerController.windowClick(InvCleaner.mc.thePlayer.inventoryContainer.windowId, slot, 1, 4, InvCleaner.mc.thePlayer);
    }

    public boolean isBestWeapon(ItemStack stack) {
        float damage = this.getDamage(stack);
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !(this.getDamage(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) > damage) || !(is.getItem() instanceof ItemSword) && this.Sword.getEnable()) continue;
            return false;
        }
        return stack.getItem() instanceof ItemSword || !this.Sword.getEnable();
    }

    public void getBestWeapon(int slot) {
        for (int i = 9; i < 45; ++i) {
            ItemStack is;
            if (!InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !this.isBestWeapon(is = InvCleaner.mc.thePlayer.inventoryContainer.getSlot(i).getStack()) || !(this.getDamage(is) > 0.0f) || !(is.getItem() instanceof ItemSword) && this.Sword.getEnable()) continue;
            this.swap(i, slot - 36);
            this.timer.reset();
            break;
        }
    }

    private float getDamage(ItemStack stack) {
        float damage = 0.0f;
        Item item = stack.getItem();
        if (item instanceof ItemTool) {
            ItemTool tool = (ItemTool)item;
            damage += (float)tool.getDamage(stack);
        }
        if (item instanceof ItemSword) {
            ItemSword sword = (ItemSword)item;
            damage += sword.getDamageVsEntity();
        }
        return damage += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
    }
}

