/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.event.EventMove;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class MoveUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static double defaultSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static void strafeHYT(float speed) {
        if (!MoveUtils.isMoving()) {
            return;
        }
        double yaw = MoveUtils.getDirection();
        MoveUtils.mc.thePlayer.motionX = -Math.sin(yaw) * (double)speed;
        MoveUtils.mc.thePlayer.motionZ = Math.cos(yaw) * (double)speed;
    }

    public static boolean isMoving() {
        return MoveUtils.mc.thePlayer != null && (MoveUtils.mc.thePlayer.movementInput.moveForward != 0.0f || MoveUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f);
    }

    public static void strafe(double speed) {
        float a = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180);
        float l = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - 4.712389f;
        float r = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) + 4.712389f;
        float rf = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) + 0.5969026f;
        float lf = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) + -0.5969026f;
        float lb = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - 2.3876104f;
        float rb = MoveUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180) - -2.3876104f;
        if (MoveUtils.mc.gameSettings.keyBindForward.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                MoveUtils.mc.thePlayer.motionX -= (double)MathHelper.sin(lf) * speed;
                MoveUtils.mc.thePlayer.motionZ += (double)MathHelper.cos(lf) * speed;
            } else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                MoveUtils.mc.thePlayer.motionX -= (double)MathHelper.sin(rf) * speed;
                MoveUtils.mc.thePlayer.motionZ += (double)MathHelper.cos(rf) * speed;
            } else {
                MoveUtils.mc.thePlayer.motionX -= (double)MathHelper.sin(a) * speed;
                MoveUtils.mc.thePlayer.motionZ += (double)MathHelper.cos(a) * speed;
            }
        } else if (MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed()) {
                MoveUtils.mc.thePlayer.motionX -= (double)MathHelper.sin(lb) * speed;
                MoveUtils.mc.thePlayer.motionZ += (double)MathHelper.cos(lb) * speed;
            } else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed()) {
                MoveUtils.mc.thePlayer.motionX -= (double)MathHelper.sin(rb) * speed;
                MoveUtils.mc.thePlayer.motionZ += (double)MathHelper.cos(rb) * speed;
            } else {
                MoveUtils.mc.thePlayer.motionX += (double)MathHelper.sin(a) * speed;
                MoveUtils.mc.thePlayer.motionZ -= (double)MathHelper.cos(a) * speed;
            }
        } else if (MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            MoveUtils.mc.thePlayer.motionX += (double)MathHelper.sin(l) * speed;
            MoveUtils.mc.thePlayer.motionZ -= (double)MathHelper.cos(l) * speed;
        } else if (MoveUtils.mc.gameSettings.keyBindRight.isPressed() && !MoveUtils.mc.gameSettings.keyBindLeft.isPressed() && !MoveUtils.mc.gameSettings.keyBindForward.isPressed() && !MoveUtils.mc.gameSettings.keyBindBack.isPressed()) {
            MoveUtils.mc.thePlayer.motionX += (double)MathHelper.sin(r) * speed;
            MoveUtils.mc.thePlayer.motionZ -= (double)MathHelper.cos(r) * speed;
        }
    }

    public static void setMotion(double speed) {
        double forward = MoveUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = MoveUtils.mc.thePlayer.movementInput.moveStrafe;
        float yaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MoveUtils.mc.thePlayer.motionX = 0.0;
            MoveUtils.mc.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            MoveUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            MoveUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static boolean checkTeleport(double x, double y, double z, double distBetweenPackets) {
        double distx = MoveUtils.mc.thePlayer.posX - x;
        double disty = MoveUtils.mc.thePlayer.posY - y;
        double distz = MoveUtils.mc.thePlayer.posZ - z;
        double dist = Math.sqrt(MoveUtils.mc.thePlayer.getDistanceSq(x, y, z));
        double distanceEntreLesPackets = distBetweenPackets;
        double nbPackets = Math.round(dist / distanceEntreLesPackets + 0.49999999999) - 1L;
        double xtp = MoveUtils.mc.thePlayer.posX;
        double ytp = MoveUtils.mc.thePlayer.posY;
        double ztp = MoveUtils.mc.thePlayer.posZ;
        int i = 1;
        while ((double)i < nbPackets) {
            AxisAlignedBB bb;
            double xdi = (x - MoveUtils.mc.thePlayer.posX) / nbPackets;
            double ydi = (y - MoveUtils.mc.thePlayer.posY) / nbPackets;
            double zdi = (z - MoveUtils.mc.thePlayer.posZ) / nbPackets;
            if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb = new AxisAlignedBB((xtp += xdi) - 0.3, ytp += ydi, (ztp += zdi) - 0.3, xtp + 0.3, ytp + 1.8, ztp + 0.3)).isEmpty()) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static boolean isOnGround(double height) {
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, MoveUtils.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    public static int getJumpEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.jump)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }

    public static int getSpeedEffect() {
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ)).getBlock();
    }

    public static Block getBlockAtPosC(double x, double y, double z) {
        EntityPlayerSP inPlayer = Minecraft.getMinecraft().thePlayer;
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(inPlayer.posX + x, inPlayer.posY + y, inPlayer.posZ + z)).getBlock();
    }

    public static float getDistanceToGround(Entity e) {
        if (MoveUtils.mc.thePlayer.isCollidedVertically && MoveUtils.mc.thePlayer.onGround) {
            return 0.0f;
        }
        for (float a = (float)e.posY; a > 0.0f; a -= 1.0f) {
            int id;
            int i;
            int[] stairs = new int[]{53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180};
            int[] exemptIds = new int[]{6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177};
            Block block = MoveUtils.mc.theWorld.getBlockState(new BlockPos(e.posX, a - 1.0f, e.posZ)).getBlock();
            if (block instanceof BlockAir) continue;
            if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                return (float)(e.posY - (double)a - 0.5) < 0.0f ? 0.0f : (float)(e.posY - (double)a - 0.5);
            }
            int[] arrayOfInt1 = stairs;
            int j = stairs.length;
            for (i = 0; i < j; ++i) {
                id = arrayOfInt1[i];
                if (Block.getIdFromBlock(block) != id) continue;
                return (float)(e.posY - (double)a - 1.0) < 0.0f ? 0.0f : (float)(e.posY - (double)a - 1.0);
            }
            arrayOfInt1 = exemptIds;
            j = exemptIds.length;
            for (i = 0; i < j; ++i) {
                id = arrayOfInt1[i];
                if (Block.getIdFromBlock(block) != id) continue;
                return (float)(e.posY - (double)a) < 0.0f ? 0.0f : (float)(e.posY - (double)a);
            }
            return (float)(e.posY - (double)a + block.getBlockBoundsMaxY() - 1.0);
        }
        return 0.0f;
    }

    public static float[] getRotationsBlock(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - MoveUtils.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - MoveUtils.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        double d1 = MoveUtils.mc.thePlayer.posY + (double)MoveUtils.mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    public static boolean isBlockAboveHead() {
        AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + (double)MoveUtils.mc.thePlayer.getEyeHeight(), MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 2.5, MoveUtils.mc.thePlayer.posZ - 0.3);
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb).isEmpty();
    }

    public static boolean isCollidedH(double dist) {
        AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + 2.0, MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 3.0, MoveUtils.mc.thePlayer.posZ - 0.3);
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty()) {
            return true;
        }
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }

    public static boolean isRealCollidedH(double dist) {
        AxisAlignedBB bb = new AxisAlignedBB(MoveUtils.mc.thePlayer.posX - 0.3, MoveUtils.mc.thePlayer.posY + 0.5, MoveUtils.mc.thePlayer.posZ + 0.3, MoveUtils.mc.thePlayer.posX + 0.3, MoveUtils.mc.thePlayer.posY + 1.9, MoveUtils.mc.thePlayer.posZ - 0.3);
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.3 + dist, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(-0.3 - dist, 0.0, 0.0)).isEmpty()) {
            return true;
        }
        if (!MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, 0.3 + dist)).isEmpty()) {
            return true;
        }
        return !MoveUtils.mc.theWorld.getCollidingBoundingBoxes(MoveUtils.mc.thePlayer, bb.offset(0.0, 0.0, -0.3 - dist)).isEmpty();
    }

    public static double getDirection() {
        float rotationYaw = MoveUtils.mc.thePlayer.rotationYaw;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (MoveUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (MoveUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }
        if (MoveUtils.mc.thePlayer.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        MoveUtils.mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        MoveUtils.mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }

    public static void setSpeed(double moveSpeed) {
        MoveUtils.setSpeed(moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }

    public static void setSpeed(EventMove moveEvent, double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0f));
        double mz = Math.sin(Math.toRadians(yaw + 90.0f));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }

    public static void setSpeed(EventMove moveEvent, double moveSpeed) {
        MoveUtils.setSpeed(moveEvent, moveSpeed, MoveUtils.mc.thePlayer.rotationYaw, MoveUtils.mc.thePlayer.movementInput.moveStrafe, MoveUtils.mc.thePlayer.movementInput.moveForward);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = (double)MoveUtils.mc.thePlayer.capabilities.getWalkSpeed() * 2.873;
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSlowdown)) {
            baseSpeed /= 1.0 + 0.2 * (double)(MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }
        if (MoveUtils.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)(MoveUtils.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }
}

