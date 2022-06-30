/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Utils.HUDUtils;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class FuckBed
extends Module {
    public static BlockPos blockBreaking;
    TimerUtils timer = new TimerUtils();
    List<BlockPos> beds = new ArrayList<BlockPos>();
    private EnableSetting Instant = new EnableSetting("Instant", false);

    public FuckBed() {
        super("FuckBed", 0, Category.World, false);
        this.getSetting().add(this.Instant);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (blockBreaking != null) {
            if (this.Instant.getEnable()) {
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockBreaking, EnumFacing.DOWN));
                FuckBed.mc.thePlayer.swingItem();
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockBreaking, EnumFacing.DOWN));
            } else {
                Field field = ReflectionHelper.findField(PlayerControllerMP.class, "curBlockDamageMP", "field_78770_f");
                Field blockdelay = ReflectionHelper.findField(PlayerControllerMP.class, "blockHitDelay", "field_78781_i");
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    if (!blockdelay.isAccessible()) {
                        blockdelay.setAccessible(true);
                    }
                    if (field.getFloat(FuckBed.mc.playerController) > 1.0f) {
                        blockdelay.setInt(FuckBed.mc.playerController, 1);
                    }
                    FuckBed.mc.thePlayer.swingItem();
                    EnumFacing direction = this.getClosestEnum(blockBreaking);
                    if (direction != null) {
                        FuckBed.mc.playerController.onPlayerDamageBlock(blockBreaking, direction);
                    }
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        int reach;
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        for (int y = reach = 6; y >= -reach; --y) {
            for (int x = -reach; x <= reach; ++x) {
                for (int z = -reach; z <= reach; ++z) {
                    if (FuckBed.mc.thePlayer.isSneaking()) {
                        return;
                    }
                    BlockPos pos = new BlockPos(FuckBed.mc.thePlayer.posX + (double)x, FuckBed.mc.thePlayer.posY + (double)y, FuckBed.mc.thePlayer.posZ + (double)z);
                    if (!this.blockChecks(FuckBed.mc.theWorld.getBlockState(pos).getBlock()) || !(FuckBed.mc.thePlayer.getDistance(FuckBed.mc.thePlayer.posX + (double)x, FuckBed.mc.thePlayer.posY + (double)y, FuckBed.mc.thePlayer.posZ + (double)z) < (double)FuckBed.mc.playerController.getBlockReachDistance() - 0.2) || this.beds.contains(pos)) continue;
                    this.beds.add(pos);
                }
            }
        }
        BlockPos closest = null;
        if (!this.beds.isEmpty()) {
            for (int i = 0; i < this.beds.size(); ++i) {
                BlockPos bed = this.beds.get(i);
                if (FuckBed.mc.thePlayer.getDistance(bed.getX(), bed.getY(), bed.getZ()) > (double)FuckBed.mc.playerController.getBlockReachDistance() - 0.2 || FuckBed.mc.theWorld.getBlockState(bed).getBlock() != Blocks.bed) {
                    this.beds.remove(i);
                }
                if (closest != null && (!(FuckBed.mc.thePlayer.getDistance(bed.getX(), bed.getY(), bed.getZ()) < FuckBed.mc.thePlayer.getDistance(closest.getX(), closest.getY(), closest.getZ())) || FuckBed.mc.thePlayer.ticksExisted % 50 != 0)) continue;
                closest = bed;
            }
        }
        if (closest != null) {
            float[] rot = this.getRotations(closest, this.getClosestEnum(closest));
            blockBreaking = closest;
            return;
        }
        blockBreaking = null;
    }

    private boolean blockChecks(Block block) {
        return block == Blocks.bed;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!Tools.currentScreenMinecraft()) {
            return;
        }
        if (blockBreaking != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            HUDUtils.drawBoundingBox((double)blockBreaking.getX() - FuckBed.mc.getRenderManager().viewerPosX + 0.5, (double)blockBreaking.getY() - FuckBed.mc.getRenderManager().viewerPosY, (double)blockBreaking.getZ() - FuckBed.mc.getRenderManager().viewerPosZ + 0.5, 0.5, 0.5625, 1.0f, 0.0f, 0.0f, 0.25f);
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
    }

    private EnumFacing getClosestEnum(BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        float rotations = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations >= 45.0f && rotations <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        } else if (rotations >= 135.0f && rotations <= 180.0f || rotations <= -135.0f && rotations >= -180.0f) {
            closestEnum = EnumFacing.SOUTH;
        } else if (rotations <= -45.0f && rotations >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        } else if (rotations >= -45.0f && rotations <= 0.0f || rotations <= 45.0f && rotations >= 0.0f) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - FuckBed.mc.thePlayer.posX;
        double z = (double)block.getZ() + 0.5 - FuckBed.mc.thePlayer.posZ;
        double d1 = FuckBed.mc.thePlayer.posY + (double)FuckBed.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }
}

