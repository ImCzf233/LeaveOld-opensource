/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.Utils.Rotation;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtil {
    static Minecraft mc = Minecraft.getMinecraft();
    public static Rotation serverRotation = new Rotation(0.0f, 0.0f);

    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getPredictedRotations(EntityLivingBase ent) {
        double x = ent.posX + (ent.posX - ent.lastTickPosX);
        double z = ent.posZ + (ent.posZ - ent.lastTickPosZ);
        double y = ent.posY + (double)(ent.getEyeHeight() / 2.0f);
        return RotationUtil.getRotationFromPosition(x, z, y);
    }

    public static float[] getAverageRotations(List<EntityLivingBase> targetList) {
        double posX = 0.0;
        double posY = 0.0;
        double posZ = 0.0;
        for (Entity entity : targetList) {
            posX += entity.posX;
            posY += entity.getEntityBoundingBox().maxY - 2.0;
            posZ += entity.posZ;
        }
        return new float[]{RotationUtil.getRotationFromPosition(posX /= (double)targetList.size(), posZ /= (double)targetList.size(), posY /= (double)targetList.size())[0], RotationUtil.getRotationFromPosition(posX, posZ, posY)[1]};
    }

    public static float getStraitYaw() {
        float YAW = MathHelper.wrapAngleTo180_float(RotationUtil.mc.thePlayer.rotationYaw);
        YAW = YAW < 45.0f && YAW > -45.0f ? 0.0f : (YAW > 45.0f && YAW < 135.0f ? 90.0f : (YAW > 135.0f || YAW < -135.0f ? 180.0f : -90.0f));
        return YAW;
    }

    public static float[] getBowAngles(Entity entity) {
        double xDelta = (entity.posX - entity.lastTickPosX) * 0.4;
        double zDelta = (entity.posZ - entity.lastTickPosZ) * 0.4;
        double d = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        d -= d % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        boolean sprint = entity.isSprinting();
        xMulti = d / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        double x = entity.posX + xMulti - Minecraft.getMinecraft().thePlayer.posX;
        double z = entity.posZ + zMulti - Minecraft.getMinecraft().thePlayer.posZ;
        double y = Minecraft.getMinecraft().thePlayer.posY + (double)Minecraft.getMinecraft().thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        double dist = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(entity);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        double d1 = MathHelper.sqrt_double(x * x + z * z);
        float pitch = (float)(-(Math.atan2(y, d1) * 180.0 / Math.PI)) + (float)dist * 0.11f;
        return new float[]{yaw, -pitch};
    }

    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - Minecraft.getMinecraft().thePlayer.posX;
        double zDiff = z - Minecraft.getMinecraft().thePlayer.posZ;
        double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 1.2;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / Math.PI));
        return new float[]{yaw, pitch};
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g * (g * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g * d3)));
    }

    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ < 0.0 && deltaX > 0.0) {
            if (deltaX != 0.0) {
                yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
            }
        } else if (deltaZ != 0.0) {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(yaw - (float)yawToEntity));
    }

    public static float getPitchChange(float pitch, Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = posY - 2.2 + (double)entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float)pitchToEntity) - 2.5f;
    }

    public static float getNewAngle(float angle) {
        if ((angle %= 360.0f) >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }

    public static boolean canEntityBeSeen(Entity e) {
        boolean see;
        Vec3 vec1 = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
        AxisAlignedBB box = e.getEntityBoundingBox();
        Vec3 vec2 = new Vec3(e.posX, e.posY + (double)(e.getEyeHeight() / 1.32f), e.posZ);
        double minx = e.posX - 0.25;
        double maxx = e.posX + 0.25;
        double miny = e.posY;
        double maxy = e.posY + Math.abs(e.posY - box.maxY);
        double minz = e.posZ - 0.25;
        double maxz = e.posZ + 0.25;
        boolean bl = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, minz);
        boolean bl2 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, minz);
        boolean bl3 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, miny, maxz);
        boolean bl4 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, miny, maxz);
        boolean bl5 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, minz);
        boolean bl6 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, minz);
        boolean bl7 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(minx, maxy, maxz - 0.1);
        boolean bl8 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        if (see) {
            return true;
        }
        vec2 = new Vec3(maxx, maxy, maxz);
        boolean bl9 = see = RotationUtil.mc.theWorld.rayTraceBlocks(vec1, vec2) == null;
        return see;
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle = Math.abs(angle1 - angle2) % 360.0f;
        if (angle > 180.0f) {
            angle = 360.0f - angle;
        }
        return angle;
    }

    public static float[] getNeededFacing(Vec3 target, Vec3 from) {
        double diffX = target.xCoord - from.xCoord;
        double diffY = target.yCoord - from.yCoord;
        double diffZ = target.zCoord - from.zCoord;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static float[] getNeededRotations(Vec3 vec) {
        Vec3 playerVector = new Vec3(RotationUtil.mc.thePlayer.posX, RotationUtil.mc.thePlayer.posY + (double)RotationUtil.mc.thePlayer.getEyeHeight(), RotationUtil.mc.thePlayer.posZ);
        double y = vec.yCoord - playerVector.yCoord;
        double x = vec.xCoord - playerVector.xCoord;
        double z = vec.zCoord - playerVector.zCoord;
        double dff = Math.sqrt(x * x + z * z);
        float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(y, dff)));
        return new float[]{MathHelper.wrapAngleTo180_float(yaw), MathHelper.wrapAngleTo180_float(pitch)};
    }

    public static float[] RotationChange(float[] currentRotation, float[] targetRotation, float turnSpeed) {
        float yawDifference = RotationUtil.getAngleDifference(targetRotation[0], currentRotation[0]);
        float pitchDifference = RotationUtil.getAngleDifference(targetRotation[1], currentRotation[1]);
        return new float[]{currentRotation[0] + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation[1] + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))};
    }

    public static Rotation limitAngleChange(Rotation currentRotation, Rotation targetRotation, float turnSpeed) {
        float yawDifference = RotationUtil.getAngleDifference(targetRotation.getYaw(), currentRotation.getYaw());
        float pitchDifference = RotationUtil.getAngleDifference(targetRotation.getPitch(), currentRotation.getPitch());
        return new Rotation(currentRotation.getYaw() + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation.getPitch() + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed)));
    }

    private static float getAngleDifference(float a, float b) {
        return ((a - b) % 360.0f + 540.0f) % 360.0f - 180.0f;
    }
}

