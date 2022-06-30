/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Utils.ReflectUtil;
import com.leave.old.Utils.RenderUtils;
import com.leave.old.modules.Module;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Projectiles
extends Module {
    public Projectiles() {
        super("Projectiles", 0, Category.Render, false);
    }

    public static Object getFieldValue(Object obj, String ... fields) {
        Class<?> clazz = obj.getClass();
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null) continue;
                f.setAccessible(true);
                try {
                    return f.get(obj);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            catch (NoSuchFieldException e) {
                // empty catch block
            }
        }
        return null;
    }

    public static Object getFieldValue(Class<?> clazz, String ... fields) {
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null) continue;
                f.setAccessible(true);
                try {
                    return f.get(null);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            catch (NoSuchFieldException e) {
                // empty catch block
            }
        }
        return null;
    }

    public static void setFieldValue(Object obj, Object value, String ... fields) {
        Class<?> clazz = obj.getClass();
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null) continue;
                f.setAccessible(true);
                try {
                    f.set(obj, value);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                    return;
                }
            }
            catch (NoSuchFieldException e) {
                // empty catch block
            }
        }
    }

    public static void setFieldValue(Class<?> clazz, Object value, String ... fields) {
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null) continue;
                f.setAccessible(true);
                try {
                    f.set(null, value);
                }
                catch (IllegalAccessException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            catch (NoSuchFieldException e) {
                // empty catch block
            }
        }
    }

    public static <T> T copy(T src, T dst) {
        for (Field f : Projectiles.getAllFields(src.getClass())) {
            if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) continue;
            f.setAccessible(true);
            try {
                f.set(dst, f.get(src));
            }
            catch (IllegalAccessException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return dst;
    }

    public static Field[] getAllFields(Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<Field>();
        do {
            for (Field f : clazz.getDeclaredFields()) {
                fields.add(f);
            }
        } while ((clazz = clazz.getSuperclass()) != Object.class && clazz != null);
        return fields.toArray(new Field[0]);
    }

    public static double getRenderPosX() {
        return (Double)ReflectUtil.getField("renderPosX", "field_78725_b", Minecraft.getMinecraft().getRenderManager());
    }

    public static double getRenderPosY() {
        return (Double)ReflectUtil.getField("renderPosY", "field_78726_c", Minecraft.getMinecraft().getRenderManager());
    }

    public static double getRenderPosZ() {
        return (Double)ReflectUtil.getField("renderPosZ", "field_78723_d", Minecraft.getMinecraft().getRenderManager());
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        RenderManager renderManager = mc.getRenderManager();
        try {
            Object posBefore;
            double renderPosX = Projectiles.getRenderPosX();
            double renderPosY = Projectiles.getRenderPosY();
            double renderPosZ = Projectiles.getRenderPosZ();
            if (Module.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            Item item = Module.mc.thePlayer.getHeldItem().getItem();
            boolean isBow = false;
            float motionFactor = 1.5f;
            float motionSlowdown = 0.99f;
            float gravity = 0.0f;
            float size = 0.0f;
            if (item instanceof ItemBow) {
                if (!Module.mc.thePlayer.isUsingItem()) {
                    return;
                }
                isBow = true;
                gravity = 0.05f;
                size = 0.3f;
                float power = (float)Module.mc.thePlayer.getItemInUseDuration() / 20.0f;
                power = (power * power + power * 2.0f) / 3.0f;
                if (power > 1.0f) {
                    power = 1.0f;
                }
                motionFactor = power * 3.0f;
            } else if (item instanceof ItemFishingRod) {
                gravity = 0.04f;
                size = 0.25f;
                motionSlowdown = 0.92f;
            } else if (item instanceof ItemPotion && ItemPotion.isSplash(Module.mc.thePlayer.getHeldItem().getItemDamage())) {
                gravity = 0.05f;
                size = 0.25f;
                motionFactor = 0.5f;
            } else {
                if (!(item instanceof ItemSnowball || item instanceof ItemEnderPearl || item instanceof ItemEgg)) {
                    return;
                }
                gravity = 0.03f;
                size = 0.25f;
            }
            float yaw = Module.mc.thePlayer.rotationYaw;
            float pitch = Module.mc.thePlayer.rotationPitch;
            double posX = renderPosX - (double)(MathHelper.cos(yaw / 180.0f * (float)Math.PI) * 0.16f);
            double posY = renderPosY + (double)Module.mc.thePlayer.getEyeHeight() - (double)0.1f;
            double posZ = renderPosZ - (double)(MathHelper.sin(yaw / 180.0f * (float)Math.PI) * 0.16f);
            double motionX = (double)(-MathHelper.sin(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(pitch / 180.0f * (float)Math.PI)) * (isBow ? 1.0 : 0.4);
            double motionY = (double)(-MathHelper.sin((pitch + (float)(item instanceof ItemPotion && ItemPotion.isSplash(Module.mc.thePlayer.getHeldItem().getItemDamage()) ? -20 : 0)) / 180.0f * (float)Math.PI)) * (isBow ? 1.0 : 0.4);
            double motionZ = (double)(MathHelper.cos(yaw / 180.0f * (float)Math.PI) * MathHelper.cos(pitch / 180.0f * (float)Math.PI)) * (isBow ? 1.0 : 0.4);
            float distance = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            motionX /= (double)distance;
            motionY /= (double)distance;
            motionZ /= (double)distance;
            motionX *= (double)motionFactor;
            motionY *= (double)motionFactor;
            motionZ *= (double)motionFactor;
            MovingObjectPosition landingPosition = null;
            boolean hasLanded = false;
            boolean hitEntity = false;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            ArrayList<Vec3> pos = new ArrayList<Vec3>();
            while (!hasLanded && posY > 0.0) {
                posBefore = new Vec3(posX, posY, posZ);
                Vec3 posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                landingPosition = Module.mc.theWorld.rayTraceBlocks((Vec3)posBefore, posAfter, false, true, false);
                posBefore = new Vec3(posX, posY, posZ);
                posAfter = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
                if (landingPosition != null) {
                    hasLanded = true;
                    posAfter = new Vec3(landingPosition.hitVec.xCoord, landingPosition.hitVec.yCoord, landingPosition.hitVec.zCoord);
                }
                AxisAlignedBB arrowBox = new AxisAlignedBB(posX - (double)size, posY - (double)size, posZ - (double)size, posX + (double)size, posY + (double)size, posZ + (double)size).addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0);
                int chunkMinX = MathHelper.floor_double((arrowBox.minX - 2.0) / 16.0);
                int n = MathHelper.floor_double((arrowBox.maxX + 2.0) / 16.0);
                int chunkMinZ = MathHelper.floor_double((arrowBox.minZ - 2.0) / 16.0);
                int chunkMaxZ = MathHelper.floor_double((arrowBox.maxZ + 2.0) / 16.0);
                ArrayList<Entity> collidedEntities = new ArrayList<Entity>();
                int n2 = chunkMinX;
                if (n2 <= n) {
                    int x;
                    do {
                        int z;
                        x = n2++;
                        int n22 = chunkMinZ;
                        if (n22 > chunkMaxZ) continue;
                        do {
                            z = n22++;
                            Module.mc.theWorld.getChunkFromChunkCoords(x, z).getEntitiesWithinAABBForEntity(Module.mc.thePlayer, arrowBox, collidedEntities, null);
                        } while (z != chunkMaxZ);
                    } while (x != n);
                }
                for (Entity possibleEntity : collidedEntities) {
                    AxisAlignedBB possibleEntityBoundingBox;
                    MovingObjectPosition movingObjectPosition;
                    if (!possibleEntity.canBeCollidedWith() || possibleEntity == Module.mc.thePlayer || (movingObjectPosition = (possibleEntityBoundingBox = possibleEntity.getEntityBoundingBox().expand(size, size, size)).calculateIntercept((Vec3)posBefore, posAfter)) == null) continue;
                    MovingObjectPosition possibleEntityLanding = movingObjectPosition;
                    hitEntity = true;
                    hasLanded = true;
                    landingPosition = possibleEntityLanding;
                }
                if (Module.mc.theWorld.getBlockState(new BlockPos(posX += motionX, posY += motionY, posZ += motionZ)).getBlock().getMaterial() == Material.water) {
                    motionX *= 0.6;
                    motionY *= 0.6;
                    motionZ *= 0.6;
                } else {
                    motionX *= (double)motionSlowdown;
                    motionY *= (double)motionSlowdown;
                    motionZ *= (double)motionSlowdown;
                }
                motionY -= (double)gravity;
                pos.add(new Vec3(posX - renderPosX, posY - renderPosY, posZ - renderPosZ));
            }
            GL11.glDepthMask((boolean)false);
            posBefore = new int[]{3042, 2848};
            RenderUtils.enableGlCap((int[])posBefore);
            posBefore = new int[]{2929, 3008, 3553};
            RenderUtils.disableGlCap((int[])posBefore);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glHint((int)3154, (int)4354);
            Color color = hitEntity ? new Color(255, 140, 140) : new Color(140, 255, 140);
            RenderUtils.glColor(color);
            GL11.glLineWidth((float)2.0f);
            worldRenderer.begin(3, DefaultVertexFormats.POSITION);
            boolean $i$f$forEach = false;
            for (Object e : pos) {
                Vec3 it = (Vec3)e;
                boolean bl = false;
                worldRenderer.pos(it.xCoord, it.yCoord, it.zCoord).endVertex();
            }
            tessellator.draw();
            GL11.glPushMatrix();
            GL11.glTranslated((double)(posX - renderPosX), (double)(posY - renderPosY), (double)(posZ - renderPosZ));
            if (landingPosition != null) {
                switch (landingPosition.sideHit.getAxis().ordinal()) {
                    case 0: {
                        GL11.glRotatef((float)90.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                        break;
                    }
                    case 2: {
                        GL11.glRotatef((float)90.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                    }
                }
                RenderUtils.drawAxisAlignedBB(new AxisAlignedBB(-0.5, 0.0, -0.5, 0.5, 0.1, 0.5), color, true, true, 3.0f);
            }
            GL11.glPopMatrix();
            GL11.glDepthMask((boolean)true);
            RenderUtils.resetCaps();
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
    }
}

