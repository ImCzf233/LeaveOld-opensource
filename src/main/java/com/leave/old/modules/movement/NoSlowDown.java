/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.Utils.MoveUtils;
import com.leave.old.Utils.NoSlowDownUtil;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.combat.Killaura;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.util.Arrays;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class NoSlowDown
extends Module {
    MovementInput origmi;
    TimerUtils timer = new TimerUtils();
    private final ModeSetting Mode = new ModeSetting("Mode", "Vanilla", Arrays.asList("Basic", "Vanilla", "NCP", "AAC5", "AAC", "Custom"), this);
    private IntegerSetting percent = new IntegerSetting("percent", 0.0, 0.0, 100.0, 1);

    public NoSlowDown() {
        super("NoSlowDown", 0, Category.Movement, false);
        this.getSetting().add(this.Mode);
    }

    @Override
    public void enable() {
        this.origmi = NoSlowDown.mc.thePlayer.movementInput;
        if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
            NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
        }
        super.enable();
    }

    @SubscribeEvent
    public void onPlayerTicks(TickEvent.PlayerTickEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
            if (NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && MoveUtils.isMoving()) {
                this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
            }
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Hypixel")) {
            if (NoSlowDown.mc.thePlayer.isBlocking() && NoSlowDown.isMoving() && MoveUtils.isOnGround(0.42)) {
                NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        } else if (this.Mode.getCurrent().equalsIgnoreCase("AAC5") && (NoSlowDown.mc.thePlayer.isUsingItem() || NoSlowDown.mc.thePlayer.isBlocking())) {
            BlockPos pos = new BlockPos(-1, -1, -1);
            this.sendPacket(new C08PacketPlayerBlockPlacement(pos, 255, NoSlowDown.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
        }
    }

    @Override
    public void disable() {
        if (this.Mode.getCurrent().equalsIgnoreCase("Basic")) {
            if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
                NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
            }
            NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
            move.setNSD(false);
        }
        NoSlowDown.mc.thePlayer.movementInput = this.origmi;
        super.disable();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        double z;
        double y;
        if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
            this.origmi = NoSlowDown.mc.thePlayer.movementInput;
            NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
        }
        if (NoSlowDown.mc.thePlayer.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown() || NoSlowDown.mc.gameSettings.keyBindSneak.isKeyDown() && NoSlowDown.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
            move.setNSD(true);
        }
        if (NoSlowDown.mc.thePlayer.isUsingItem() && NoSlowDown.isMoving() && MoveUtils.isOnGround(0.42) && Killaura.target == null && this.Mode.getCurrent().equalsIgnoreCase("NCP")) {
            double x = NoSlowDown.mc.thePlayer.posX;
            y = NoSlowDown.mc.thePlayer.posY;
            z = NoSlowDown.mc.thePlayer.posZ;
            NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("Custom")) {
            try {
                if (NoSlowDown.mc.thePlayer.getItemInUse().getItem() instanceof ItemSword) {
                    NoSlowDown.mc.thePlayer.motionX *= 0.5;
                    NoSlowDown.mc.thePlayer.motionZ *= 0.5;
                }
                if (NoSlowDown.mc.thePlayer.isUsingItem()) {
                    NoSlowDown.mc.thePlayer.motionX *= this.percent.getCurrent() / 100.0;
                    NoSlowDown.mc.thePlayer.motionZ *= this.percent.getCurrent() / 100.0;
                }
            }
            catch (NullPointerException x) {
                // empty catch block
            }
        }
        if (NoSlowDown.mc.thePlayer.isUsingItem() && NoSlowDown.isMoving() && MoveUtils.isOnGround(0.42) && Killaura.target == null && this.Mode.getCurrent().equalsIgnoreCase("NCP")) {
            double x = NoSlowDown.mc.thePlayer.posX;
            y = NoSlowDown.mc.thePlayer.posY;
            z = NoSlowDown.mc.thePlayer.posZ;
            NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("Basic")) {
            if (NoSlowDown.mc.thePlayer.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown() || NoSlowDown.mc.gameSettings.keyBindSneak.isKeyDown() && NoSlowDown.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                if (!(NoSlowDown.mc.thePlayer.movementInput instanceof NoSlowDownUtil)) {
                    NoSlowDown.mc.thePlayer.movementInput = new NoSlowDownUtil(NoSlowDown.mc.gameSettings);
                }
                NoSlowDownUtil move = (NoSlowDownUtil)NoSlowDown.mc.thePlayer.movementInput;
                move.setNSD(true);
                if (event.phase == TickEvent.Phase.START) {
                    if (NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && MoveUtils.isMoving()) {
                        this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                } else if (event.phase == TickEvent.Phase.END && NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && MoveUtils.isMoving()) {
                    this.sendPacket(new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
                }
            }
            if (this.Mode.getCurrent().equalsIgnoreCase("AAC") && NoSlowDown.mc.thePlayer.isBlocking() && !NoSlowDown.mc.thePlayer.isRiding() && MoveUtils.isMoving()) {
                if (event.phase == TickEvent.Phase.START) {
                    if (NoSlowDown.mc.thePlayer.onGround || MoveUtils.isOnGround(0.5)) {
                        this.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(0, 0, 0), EnumFacing.DOWN));
                    }
                } else if (event.phase == TickEvent.Phase.END && this.timer.isDelayComplete(65.0)) {
                    this.sendPacket(new C08PacketPlayerBlockPlacement(NoSlowDown.mc.thePlayer.inventory.getCurrentItem()));
                    this.timer.reset();
                }
            }
        }
    }

    public static boolean isMoving() {
        if (!NoSlowDown.mc.thePlayer.isCollidedHorizontally && !NoSlowDown.mc.thePlayer.isSneaking()) {
            return NoSlowDown.mc.thePlayer.movementInput.moveForward != 0.0f || NoSlowDown.mc.thePlayer.movementInput.moveStrafe != 0.0f;
        }
        return false;
    }

    public void sendPacket(Packet packet) {
        NoSlowDown.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}

