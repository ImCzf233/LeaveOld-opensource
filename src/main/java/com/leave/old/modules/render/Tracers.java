/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Utils.HUDUtils;
import com.leave.old.Utils.ReflectUtil;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.EnableSetting;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Timer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Tracers
extends Module {
    private EnableSetting Players = new EnableSetting("Players", true);
    private EnableSetting Mobs = new EnableSetting("Mobs", false);
    private EnableSetting Animals = new EnableSetting("Animals", false);
    private EnableSetting Inv = new EnableSetting("Invisible", false);
    private float width = 1.0f;

    public Tracers() {
        super("Tracers", 0, Category.Render, false);
        this.getSetting().add(this.Players);
        this.getSetting().add(this.Mobs);
        this.getSetting().add(this.Animals);
        this.getSetting().add(this.Inv);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        try {
            for (Entity entity : Tracers.mc.theWorld.getLoadedEntityList()) {
                if (!(entity instanceof EntityLivingBase) || !this.isValid((EntityLivingBase)entity)) continue;
                this.trace(entity, this.width, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()), Tracers.getTimer((Minecraft)Tracers.mc).renderPartialTicks);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static Timer getTimer(Minecraft mc) {
        return (Timer)ReflectUtil.getField("timer", "field_71428_T", mc);
    }

    private boolean isValid(EntityLivingBase entity) {
        return Minecraft.getMinecraft().thePlayer != entity && this.isValidType(entity) && entity.isEntityAlive() && !entity.isInvisible();
    }

    private void trace(Entity entity, float width, Color color, float partialTicks) {
        float r = 0.003921569f * (float)color.getRed();
        float g = 0.003921569f * (float)color.getGreen();
        float b = 0.003921569f * (float)color.getBlue();
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        Tracers.orientCamera(partialTicks);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        double x = HUDUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks) - Tracers.mc.getRenderManager().viewerPosX;
        double y = HUDUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks) - Tracers.mc.getRenderManager().viewerPosY;
        double z = HUDUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks) - Tracers.mc.getRenderManager().viewerPosZ;
        GL11.glLineWidth((float)width);
        GL11.glBegin((int)3);
        GL11.glColor3d((double)r, (double)g, (double)b);
        GL11.glVertex3d((double)x, (double)y, (double)z);
        GL11.glVertex3d((double)0.0, (double)Tracers.mc.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glEnable((int)2929);
        GL11.glPopMatrix();
    }

    public static void orientCamera(float pass) {
        ReflectUtil.invoke(Minecraft.getMinecraft().entityRenderer, "orientCamera", "func_78467_g", new Class[]{Float.TYPE}, new Object[]{Float.valueOf(pass)});
    }

    private boolean isValidType(EntityLivingBase entity) {
        return this.Players.getEnable() && entity instanceof EntityPlayer || this.Mobs.getEnable() && (entity instanceof EntityMob || entity instanceof EntitySlime) || this.Animals.getEnable() && (entity instanceof EntityVillager || entity instanceof EntityGolem) || this.Animals.getEnable() && entity instanceof EntityAnimal;
    }
}

