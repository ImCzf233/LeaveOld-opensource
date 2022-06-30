/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.util.internal.ThreadLocalRandom
 *  org.lwjgl.input.Mouse
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoClicker
extends Module {
    public static EnableSetting Left_Click = new EnableSetting("Left Click", true);
    private IntegerSetting LeftMinCPS = new IntegerSetting("MinCPS", 9.0, 1.0, 30.0, 1);
    private IntegerSetting LeftMaxCPS = new IntegerSetting("MaxCPS", 13.0, 1.0, 30.0, 1);
    private EnableSetting Right_Click = new EnableSetting("Right Click", false);
    private IntegerSetting RightMinCPS = new IntegerSetting("RightMinCPS", 12.0, 1.0, 30.0, 1);
    private IntegerSetting RightMaxCPS = new IntegerSetting("RightMaxCPS", 15.0, 1.0, 30.0, 1);
    private EnableSetting Break_Blocks = new EnableSetting("Break Blocks", true);
    private IntegerSetting BreakDelay = new IntegerSetting("Break Delay", 25.0, 0.0, 1000.0, 1);
    private EnableSetting Weapon_only = new EnableSetting("Weapon Only", false);
    private EnableSetting noBlockSword = new EnableSetting("noBlockSword", false);
    private EnableSetting onlyBlocks = new EnableSetting("onlyBlocks", false);
    private IntegerSetting JitterLeft = new IntegerSetting("JitterLeft", 0.0, 0.0, 3.0, 1);
    private IntegerSetting JitterRight = new IntegerSetting("JitterRight", 0.0, 0.0, 3.0, 1);
    private IntegerSetting rightClickDelay = new IntegerSetting("rightClickDelay", 85.0, 0.0, 500.0, 1);
    private Random rand = null;
    private Method playerMouseInput;
    private long leftDownTime;
    private long righti;
    private long leftUpTime;
    private long rightj;
    private long leftk;
    private long rightk;
    private long leftl;
    private long rightl;
    private double leftm;
    private double rightm;
    private boolean leftn;
    private boolean rightn;
    private boolean breakHeld;
    private boolean watingForBreakTimeout;
    private double breakBlockFinishWaitTime;
    private long lastClick;
    private long leftHold;
    private long rightHold;
    private boolean rightClickWaiting;
    private double rightClickWaitStartTime;
    private boolean allowedClick;
    public static boolean autoClickerEnabled;
    public static boolean breakTimeDone;
    public static int clickFinder;
    public static int clickCount;

    public AutoClicker() {
        super("AutoClicker", 0, Category.Combat, false);
        this.getSetting().add(Left_Click);
        this.getSetting().add(this.LeftMinCPS);
        this.getSetting().add(this.LeftMaxCPS);
        this.getSetting().add(this.Right_Click);
        this.getSetting().add(this.RightMinCPS);
        this.getSetting().add(this.RightMaxCPS);
        this.getSetting().add(this.Break_Blocks);
        this.getSetting().add(this.BreakDelay);
        this.getSetting().add(this.Weapon_only);
        this.getSetting().add(this.noBlockSword);
        this.getSetting().add(this.onlyBlocks);
        this.getSetting().add(this.JitterLeft);
        this.getSetting().add(this.JitterRight);
        try {
            this.playerMouseInput = GuiScreen.class.getDeclaredMethod("func_73864_a", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        }
        catch (Exception var4) {
            try {
                this.playerMouseInput = GuiScreen.class.getDeclaredMethod("mouseClicked", Integer.TYPE, Integer.TYPE, Integer.TYPE);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (this.playerMouseInput != null) {
            this.playerMouseInput.setAccessible(true);
        }
        this.rightClickWaiting = false;
        autoClickerEnabled = false;
        clickFinder = 2;
        clickCount = 1;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent ev) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        this.Click(ev, null);
    }

    @Override
    public void enable() {
        if (this.playerMouseInput == null) {
            this.disable();
        }
        this.rightClickWaiting = false;
        this.allowedClick = false;
        this.rand = new Random();
        autoClickerEnabled = true;
        super.enable();
    }

    @Override
    public void disable() {
        this.leftDownTime = 0L;
        this.leftUpTime = 0L;
        boolean leftHeld = false;
        this.rightClickWaiting = false;
        autoClickerEnabled = false;
        super.disable();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent ev) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        this.Click(null, ev);
    }

    private void Click(TickEvent.RenderTickEvent er, TickEvent.PlayerTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        double speedLeft1 = 1.0 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.LeftMinCPS.getCurrent() - 0.2, this.LeftMaxCPS.getCurrent());
        double leftHoldLength = speedLeft1 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.LeftMinCPS.getCurrent() - 0.02, this.LeftMaxCPS.getCurrent());
        double speedRight = 1.0 / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.RightMinCPS.getCurrent() - 0.2, this.RightMaxCPS.getCurrent());
        double rightHoldLength = speedRight / io.netty.util.internal.ThreadLocalRandom.current().nextDouble(this.RightMinCPS.getCurrent() - 0.02, this.RightMaxCPS.getCurrent());
        if (Mouse.isButtonDown((int)0) && Left_Click.getEnable()) {
            if (this.breakBlock()) {
                return;
            }
            if (this.Weapon_only.getEnable() && !Tools.isPlayerHoldingWeapon()) {
                return;
            }
            if (this.JitterLeft.getCurrent() > 0.0) {
                EntityPlayerSP entityPlayer;
                double a = this.JitterLeft.getCurrent() * 0.45;
                if (this.rand.nextBoolean()) {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw + (double)this.rand.nextFloat() * a);
                } else {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw - (double)this.rand.nextFloat() * a);
                }
                if (this.rand.nextBoolean()) {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch + (double)this.rand.nextFloat() * a * 0.45);
                } else {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch - (double)this.rand.nextFloat() * a * 0.45);
                }
            }
            double speedLeft = 1.0 / ThreadLocalRandom.current().nextDouble(this.LeftMinCPS.getCurrent() - 0.2, this.LeftMaxCPS.getCurrent());
            if ((double)(System.currentTimeMillis() - this.lastClick) > speedLeft * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (this.leftHold < this.lastClick) {
                    this.leftHold = this.lastClick;
                }
                int key = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                Tools.setMouseButtonState(0, true);
            } else if ((double)(System.currentTimeMillis() - this.leftHold) > leftHoldLength * 1000.0) {
                KeyBinding.setKeyBindState(AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode(), false);
                Tools.setMouseButtonState(0, false);
            }
        }
        if (Mouse.isButtonDown((int)1) && this.Right_Click.getEnable()) {
            if (!this.rightClickAllowed()) {
                return;
            }
            if (this.JitterRight.getCurrent() > 0.0) {
                EntityPlayerSP entityPlayer;
                double jitterMultiplier = this.JitterRight.getCurrent() * 0.45;
                if (this.rand.nextBoolean()) {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw + (double)this.rand.nextFloat() * jitterMultiplier);
                } else {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationYaw = (float)((double)entityPlayer.rotationYaw - (double)this.rand.nextFloat() * jitterMultiplier);
                }
                if (this.rand.nextBoolean()) {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch + (double)this.rand.nextFloat() * jitterMultiplier * 0.45);
                } else {
                    entityPlayer = AutoClicker.mc.thePlayer;
                    entityPlayer.rotationPitch = (float)((double)entityPlayer.rotationPitch - (double)this.rand.nextFloat() * jitterMultiplier * 0.45);
                }
            }
            if ((double)(System.currentTimeMillis() - this.lastClick) > speedRight * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (this.rightHold < this.lastClick) {
                    this.rightHold = this.lastClick;
                }
                int key = AutoClicker.mc.gameSettings.keyBindUseItem.getKeyCode();
                KeyBinding.setKeyBindState(key, true);
                if (clickCount / clickFinder == 0) {
                    // empty if block
                }
                ++clickCount;
                KeyBinding.onTick(key);
            } else if ((double)(System.currentTimeMillis() - this.rightHold) > rightHoldLength * 1000.0) {
                KeyBinding.setKeyBindState(AutoClicker.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            }
        } else if (!Mouse.isButtonDown((int)1)) {
            this.rightClickWaiting = false;
            this.allowedClick = false;
        }
    }

    public boolean breakBlock() {
        BlockPos p;
        if (this.Break_Blocks.getEnable() && AutoClicker.mc.objectMouseOver != null && (p = AutoClicker.mc.objectMouseOver.getBlockPos()) != null) {
            Block bl = AutoClicker.mc.theWorld.getBlockState(p).getBlock();
            if (bl != Blocks.air && !(bl instanceof BlockLiquid)) {
                if (this.BreakDelay.getCurrent() == 0.0) {
                    if (!this.breakHeld) {
                        int e = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        this.breakHeld = true;
                    }
                    return true;
                }
                if (!breakTimeDone && !this.watingForBreakTimeout) {
                    this.watingForBreakTimeout = true;
                    this.breakBlockFinishWaitTime = this.BreakDelay.getCurrent();
                    return false;
                }
                if (!breakTimeDone && this.watingForBreakTimeout && (double)System.currentTimeMillis() > this.breakBlockFinishWaitTime) {
                    breakTimeDone = true;
                    this.watingForBreakTimeout = false;
                }
                if (breakTimeDone && !this.watingForBreakTimeout) {
                    if (!this.breakHeld) {
                        int e = AutoClicker.mc.gameSettings.keyBindAttack.getKeyCode();
                        KeyBinding.setKeyBindState(e, true);
                        KeyBinding.onTick(e);
                        this.breakHeld = true;
                    }
                    return true;
                }
            }
            if (this.breakHeld) {
                this.breakHeld = false;
                breakTimeDone = false;
                this.watingForBreakTimeout = false;
            }
        }
        return false;
    }

    public boolean rightClickAllowed() {
        ItemStack item = AutoClicker.mc.thePlayer.getHeldItem();
        if (item != null) {
            if (this.onlyBlocks.getEnable() && !(item.getItem() instanceof ItemBlock)) {
                return false;
            }
            if (this.noBlockSword.getEnable() && item.getItem() instanceof ItemSword) {
                return false;
            }
        }
        if (this.rightClickDelay.getCurrent() != 0.0) {
            if (!this.rightClickWaiting && !this.allowedClick) {
                this.rightClickWaitStartTime = System.currentTimeMillis();
                this.rightClickWaiting = true;
                return false;
            }
            if (this.rightClickWaiting && !this.allowedClick) {
                double passedTime = (double)System.currentTimeMillis() - this.rightClickWaitStartTime;
                if (passedTime >= this.rightClickDelay.getCurrent()) {
                    this.allowedClick = true;
                    this.rightClickWaiting = false;
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}

