/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.Utils.Wrapper;
import java.util.LinkedList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public final class BlockUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i = BlockUtils.mc.thePlayer.posY; i > 0.0; i -= 1.0) {
            Block block = BlockUtils.getBlock(new BlockPos(BlockUtils.mc.thePlayer.posX, i, BlockUtils.mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
                distance = i;
                break;
            }
            if (i < 0.0) break;
        }
        double distancetofall = BlockUtils.mc.thePlayer.posY - distance - 1.0;
        return distancetofall;
    }

    public static IBlockState getState(BlockPos pos) {
        return Wrapper.INSTANCE.world().getBlockState(pos);
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtils.getState(pos).getBlock();
    }

    public static Material getMaterial(BlockPos pos) {
        return BlockUtils.getState(pos).getBlock().getMaterial();
    }

    public static boolean canBeClicked(BlockPos pos) {
        return BlockUtils.getBlock(pos).canCollideCheck(BlockUtils.getState(pos), false);
    }

    public static float getHardness(BlockPos pos) {
        return BlockUtils.getState(pos).getBlock().getPlayerRelativeBlockHardness(BlockUtils.mc.thePlayer, Wrapper.INSTANCE.world(), pos);
    }

    public static boolean isBlockMaterial(BlockPos blockPos, Block block) {
        return BlockUtils.getBlock(blockPos) == Blocks.air;
    }

    public static boolean isBlockMaterial(BlockPos blockPos, Material material) {
        return BlockUtils.getState(blockPos).getBlock().getMaterial() == material;
    }

    public static boolean placeBlockLegit(BlockPos pos) {
        Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3 hitVec;
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (eyesPos.squareDistanceTo(new Vec3(pos).addVector(0.5, 0.5, 0.5)) >= eyesPos.squareDistanceTo(new Vec3(neighbor).addVector(0.5, 0.5, 0.5)) || !BlockUtils.getBlock(neighbor).canCollideCheck(Wrapper.INSTANCE.world().getBlockState(neighbor), false) || eyesPos.squareDistanceTo(hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()))) > 18.0625) continue;
            BlockUtils.faceVectorPacket(hitVec);
        }
        BlockUtils.sendPacket(new C08PacketPlayerBlockPlacement());
        BlockUtils.swingMainHand();
        return true;
    }

    public static void swingMainHand() {
        BlockUtils.mc.thePlayer.swingItem();
    }

    public static void sendPacket(Packet packet) {
        BlockUtils.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static boolean placeBlockSimple(BlockPos pos) {
        Vec3 eyesPos = new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
        for (EnumFacing side : EnumFacing.values()) {
            Vec3 hitVec;
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockUtils.getBlock(neighbor).canCollideCheck(BlockUtils.getState(neighbor), false) || eyesPos.squareDistanceTo(hitVec = new Vec3(neighbor).addVector(0.5, 0.5, 0.5).add(new Vec3(side2.getDirectionVec()))) > 36.0) continue;
            return true;
        }
        return false;
    }

    public static void faceVectorPacket(Vec3 vec) {
        double diffX = vec.xCoord - BlockUtils.mc.thePlayer.posX;
        double diffY = vec.yCoord - (BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - BlockUtils.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, dist)));
        BlockUtils.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }

    public static void faceBlockClient(BlockPos blockPos) {
        double diffX = (double)blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        double diffY = (double)blockPos.getY() + 0.0 - (BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight());
        double diffZ = (double)blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
        BlockUtils.mc.thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch);
    }

    public static void faceBlockPacket(BlockPos blockPos) {
        double diffX = (double)blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        double diffY = (double)blockPos.getY() + 0.0 - (BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight());
        double diffZ = (double)blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        BlockUtils.sendPacket(new C03PacketPlayer.C05PacketPlayerLook(BlockUtils.mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw), BlockUtils.mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - BlockUtils.mc.thePlayer.rotationPitch), BlockUtils.mc.thePlayer.onGround));
    }

    public static void faceBlockClientHorizontally(BlockPos blockPos) {
        double diffX = (double)blockPos.getX() + 0.5 - BlockUtils.mc.thePlayer.posX;
        double diffZ = (double)blockPos.getZ() + 0.5 - BlockUtils.mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f;
        BlockUtils.mc.thePlayer.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - BlockUtils.mc.thePlayer.rotationYaw);
    }

    public static float getPlayerBlockDistance(BlockPos blockPos) {
        return BlockUtils.getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
        float xDiff = (float)(BlockUtils.mc.thePlayer.posX - posX);
        float yDiff = (float)(BlockUtils.mc.thePlayer.posY - posY);
        float zDiff = (float)(BlockUtils.mc.thePlayer.posZ - posZ);
        return BlockUtils.getBlockDistance(xDiff, yDiff, zDiff);
    }

    public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
        return MathHelper.sqrt_double((xDiff - 0.5f) * (xDiff - 0.5f) + (yDiff - 0.5f) * (yDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }

    public static float getHorizontalPlayerBlockDistance(BlockPos blockPos) {
        float xDiff = (float)(BlockUtils.mc.thePlayer.posX - (double)blockPos.getX());
        float zDiff = (float)(BlockUtils.mc.thePlayer.posZ - (double)blockPos.getZ());
        return MathHelper.sqrt_double((xDiff - 0.5f) * (xDiff - 0.5f) + (zDiff - 0.5f) * (zDiff - 0.5f));
    }

    public static Vec3 getEyesPos() {
        return new Vec3(BlockUtils.mc.thePlayer.posX, BlockUtils.mc.thePlayer.posY + (double)BlockUtils.mc.thePlayer.getEyeHeight(), BlockUtils.mc.thePlayer.posZ);
    }

    public static void breakBlocksPacketSpam(Iterable<BlockPos> blocks) {
        Vec3 eyesPos = BlockUtils.getEyesPos();
        NetHandlerPlayClient connection = BlockUtils.mc.thePlayer.sendQueue;
        block0: for (BlockPos pos : blocks) {
            Vec3 posVec = new Vec3(pos).addVector(0.5, 0.5, 0.5);
            double distanceSqPosVec = eyesPos.squareDistanceTo(posVec);
            for (EnumFacing side : EnumFacing.values()) {
                Vec3 hitVec = posVec.add(new Vec3(side.getDirectionVec()));
                if (eyesPos.squareDistanceTo(hitVec) >= distanceSqPosVec) continue;
                connection.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, side));
                connection.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
                continue block0;
            }
        }
    }

    public static LinkedList<BlockPos> findBlocksNearEntity(EntityLivingBase entity, int blockId, int blockMeta, int distance) {
        LinkedList<BlockPos> blocks = new LinkedList<BlockPos>();
        for (int x = (int)BlockUtils.mc.thePlayer.posX - distance; x <= (int)BlockUtils.mc.thePlayer.posX + distance; ++x) {
            for (int z = (int)BlockUtils.mc.thePlayer.posZ - distance; z <= (int)BlockUtils.mc.thePlayer.posZ + distance; ++z) {
                int height = Wrapper.INSTANCE.world().getHeight();
                for (int y = 0; y <= height; ++y) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    IBlockState blockState = Wrapper.INSTANCE.world().getBlockState(blockPos);
                    if (blockId == -1 || blockMeta == -1) {
                        blocks.add(blockPos);
                        continue;
                    }
                    int id = Block.getIdFromBlock(blockState.getBlock());
                    int meta = blockState.getBlock().getMetaFromState(blockState);
                    if (id != blockId || meta != blockMeta) continue;
                    blocks.add(blockPos);
                }
            }
        }
        return blocks;
    }

    public static boolean isInLiquid() {
        if (BlockUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        int y = (int)BlockUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(BlockUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = BlockUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block == Blocks.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static BlockPos getHypixelBlockpos(String str) {
        int val = 89;
        if (str != null && str.length() > 1) {
            char[] chs = str.toCharArray();
            int lenght = chs.length;
            for (int i = 0; i < lenght; ++i) {
                val += chs[i] * str.length() * str.length() + str.charAt(0) + str.charAt(1);
            }
            val /= str.length();
        }
        return new BlockPos(val, -val % 255, val);
    }
}

