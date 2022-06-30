/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.BlockUtils;
import com.leave.old.Utils.Connection;
import com.leave.old.modules.Module;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class NoFall
extends Module {
    private boolean aac5doFlag = false;
    private boolean aac5Check = false;
    private int aac5Timer = 0;
    private boolean isDmgFalling = false;
    private EnableSetting VOID = new EnableSetting("VOID", true);
    private IntegerSetting delay = new IntegerSetting("MLGDelay", 100.0, 1.0, 1000.0, 0);
    private final ModeSetting Mode = new ModeSetting("Mode", "AAC", Arrays.asList("AAC", "Simple", "Hypixel", "AAC2", "AAC5", "AAC5.0.4", "MLG"), this);

    public NoFall() {
        super("NoFall", 0, Category.World, false);
        this.getSetting().add(this.VOID);
        this.getSetting().add(this.delay);
        this.getSetting().add(this.Mode);
    }

    @Override
    public void enable() {
        this.aac5Check = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.isDmgFalling = false;
        super.enable();
    }

    @Override
    public void disable() {
        this.aac5Check = false;
        this.aac5doFlag = false;
        this.aac5Timer = 0;
        this.isDmgFalling = false;
        super.disable();
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("MLG")) {
            if (NoFall.mc.thePlayer.fallDistance > 4.0f && this.getSlotWaterBucket() != -1 && this.isMLGNeeded()) {
                NoFall.mc.thePlayer.rotationPitch = 90.0f;
                this.swapToWaterBucket(this.getSlotWaterBucket());
            }
            if (NoFall.mc.thePlayer.fallDistance > 4.0f && this.isMLGNeeded() && !NoFall.mc.thePlayer.isOnLadder() && NoFall.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY - BlockUtils.getDistanceToFall() - 1.0, NoFall.mc.thePlayer.posZ);
                this.placeWater(pos, EnumFacing.UP);
                if (NoFall.mc.thePlayer.getHeldItem().getItem() == Items.bucket) {
                    Thread thr = new Thread(() -> {
                        try {
                            Thread.sleep((long)this.delay.getCurrent());
                        }
                        catch (Exception exception) {
                            // empty catch block
                        }
                        NoFall.rightClickMouse(mc);
                    });
                    thr.start();
                }
                NoFall.mc.thePlayer.fallDistance = 0.0f;
            }
        }
    }

    public void sendPacket(Packet packet) {
        NoFall.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("Simple") && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            this.sendPacket(new C03PacketPlayer(true));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("Hypixel") && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            this.sendPacket(new C03PacketPlayer(true));
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC2") && NoFall.mc.thePlayer.ticksExisted == 1 && NoFall.mc.thePlayer.fallDistance > 2.0f) {
            C03PacketPlayer.C04PacketPlayerPosition p = new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, Double.NaN, NoFall.mc.thePlayer.posZ, true);
            NoFall.mc.thePlayer.sendQueue.addToSendQueue(p);
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC5")) {
            this.aac5Check = false;
            for (double offsetYs = 0.0; NoFall.mc.thePlayer.motionY - 1.5 < offsetYs; offsetYs -= 0.5) {
                BlockPos blockPos = new BlockPos(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + offsetYs, NoFall.mc.thePlayer.posZ);
                Block block = BlockUtils.getBlock(blockPos);
                AxisAlignedBB axisAlignedBB = block.getCollisionBoundingBox(NoFall.mc.theWorld, blockPos, BlockUtils.getState(blockPos));
                if (axisAlignedBB == null) continue;
                offsetYs = -999.9;
                this.aac5Check = true;
            }
            if (NoFall.mc.thePlayer.onGround) {
                NoFall.mc.thePlayer.fallDistance = -2.0f;
                this.aac5Check = false;
            }
            if (this.aac5Timer > 0) {
                --this.aac5Timer;
            }
            if (this.aac5Check && (double)NoFall.mc.thePlayer.fallDistance > 2.5 && !NoFall.mc.thePlayer.onGround) {
                this.aac5doFlag = true;
                this.aac5Timer = 18;
            } else if (this.aac5Timer < 2) {
                this.aac5doFlag = false;
            }
            if (this.aac5doFlag) {
                if (NoFall.mc.thePlayer.onGround) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 0.5, NoFall.mc.thePlayer.posZ, true));
                } else {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(NoFall.mc.thePlayer.posX, NoFall.mc.thePlayer.posY + 0.42, NoFall.mc.thePlayer.posZ, true));
                }
            }
        }
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC5.0.4") && NoFall.mc.thePlayer.fallDistance > 3.0f) {
            this.isDmgFalling = true;
        }
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (side == Connection.Side.OUT && packet instanceof C03PacketPlayer) {
            C03PacketPlayer p = (C03PacketPlayer)packet;
            if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
                Field field = ReflectionHelper.findField(C03PacketPlayer.class, "onGround", "field_149474_g");
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    field.setBoolean(p, true);
                }
                catch (Exception exception) {}
            } else if (this.Mode.getCurrent().equalsIgnoreCase("AAC5.0.4") && this.isDmgFalling) {
                Field field = ReflectionHelper.findField(C03PacketPlayer.class, "onGround", "field_149474_g");
                Field fx = ReflectionHelper.findField(C03PacketPlayer.class, "x", "field_149479_a");
                Field fy = ReflectionHelper.findField(C03PacketPlayer.class, "y", "field_149477_b");
                Field fz = ReflectionHelper.findField(C03PacketPlayer.class, "z", "field_149478_c");
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!fx.isAccessible()) {
                        fx.setAccessible(true);
                    }
                    if (!fy.isAccessible()) {
                        fy.setAccessible(true);
                    }
                    if (!fz.isAccessible()) {
                        fz.setAccessible(true);
                    }
                    if (field.getBoolean(p) && NoFall.mc.thePlayer.onGround) {
                        this.isDmgFalling = false;
                        field.setBoolean(p, true);
                        NoFall.mc.thePlayer.onGround = false;
                        double y = fy.getDouble(p);
                        double x = fx.getDouble(p);
                        double z = fx.getDouble(p);
                        fy.setDouble(p, y + 1.0);
                        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 1.0784, z, false));
                        this.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.5, z, true));
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
        return true;
    }

    public static void rightClickMouse(Minecraft mc) {
        NoFall.invoke(mc, "rightClickMouse", "func_147121_ag", new Class[0], new Object[0]);
    }

    public static Object invoke(Object target, String methodName, String obfName, Class[] methodArgs, Object[] args) {
        String[] stringArray;
        Class clazz = target.getClass();
        if (Client.isObfuscate) {
            String[] stringArray2 = new String[1];
            stringArray = stringArray2;
            stringArray2[0] = obfName;
        } else {
            String[] stringArray3 = new String[1];
            stringArray = stringArray3;
            stringArray3[0] = methodName;
        }
        Method method = ReflectionHelper.findMethod(clazz, target, stringArray, methodArgs);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isBlockUnderJudge() {
        int offset = 0;
        while ((double)offset < NoFall.mc.thePlayer.posY + (double)NoFall.mc.thePlayer.getEyeHeight()) {
            AxisAlignedBB boundingBox = NoFall.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!NoFall.mc.theWorld.getCollidingBoundingBoxes(NoFall.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
            offset += 2;
        }
        return false;
    }

    private void swapToWaterBucket(int blockSlot) {
        NoFall.mc.thePlayer.inventory.currentItem = blockSlot;
        NoFall.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C09PacketHeldItemChange(blockSlot));
    }

    private int getSlotWaterBucket() {
        for (int i = 0; i < 8; ++i) {
            if (NoFall.mc.thePlayer.inventory.mainInventory[i] == null || !NoFall.mc.thePlayer.inventory.mainInventory[i].getItem().getUnlocalizedName().contains("bucketWater")) continue;
            return i;
        }
        return -1;
    }

    private void placeWater(BlockPos pos, EnumFacing facing) {
        ItemStack heldItem = NoFall.mc.thePlayer.inventory.getCurrentItem();
        NoFall.mc.playerController.onPlayerRightClick(NoFall.mc.thePlayer, NoFall.mc.theWorld, NoFall.mc.thePlayer.inventory.getCurrentItem(), pos, facing, new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 1.0, (double)pos.getZ() + 0.5));
        if (heldItem != null) {
            NoFall.mc.playerController.sendUseItem(NoFall.mc.thePlayer, NoFall.mc.theWorld, heldItem);
            NoFall.mc.entityRenderer.itemRenderer.resetEquippedProgress2();
        }
    }

    private boolean isMLGNeeded() {
        if (NoFall.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE || NoFall.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR || NoFall.mc.thePlayer.capabilities.isFlying || NoFall.mc.thePlayer.capabilities.allowFlying) {
            return false;
        }
        for (double y = NoFall.mc.getMinecraft().thePlayer.posY; y > 0.0; y -= 1.0) {
            Block block = BlockUtils.getBlock(new BlockPos(NoFall.mc.getMinecraft().thePlayer.posX, y, NoFall.mc.getMinecraft().thePlayer.posZ));
            if (block.getMaterial() == Material.water) {
                return false;
            }
            if (block.getMaterial() != Material.air) {
                return true;
            }
            if (y < 0.0) break;
        }
        return true;
    }
}

