/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ItemESP
extends Module {
    public ItemESP() {
        super("ItemESP", 0, Category.Render, false);
    }

    public static List getEntityList() {
        return ItemESP.mc.theWorld.getLoadedEntityList();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        RenderHelper.enableGUIStandardItemLighting();
        for (Object object : ItemESP.getEntityList()) {
            if (!(object instanceof EntityItem) && !(object instanceof EntityArrow)) continue;
            Entity entity = (Entity)object;
            ItemESP.drawESP(entity, (float)ClickGui.red.getCurrent(), (float)ClickGui.green.getCurrent(), (float)ClickGui.blue.getCurrent(), 1.0f, event.partialTicks);
        }
        RenderHelper.disableStandardItemLighting();
    }

    public static void drawESP(Entity entity, float colorRed, float colorGreen, float colorBlue, float colorAlpha, float ticks) {
        try {
            double exception = ItemESP.mc.getRenderManager().viewerPosX;
            double renderPosY = ItemESP.mc.getRenderManager().viewerPosY;
            double renderPosZ = ItemESP.mc.getRenderManager().viewerPosZ;
            double xPos = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)ticks - exception;
            double yPos = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)ticks + (double)(entity.height / 2.0f) - renderPosY;
            double zPos = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)ticks - renderPosZ;
            float playerViewY = ItemESP.mc.getRenderManager().playerViewY;
            float playerViewX = ItemESP.mc.getRenderManager().playerViewX;
            boolean thirdPersonView = ItemESP.mc.getRenderManager().options.thirdPersonView == 2;
            GL11.glPushMatrix();
            GlStateManager.translate(xPos, yPos, zPos);
            GlStateManager.rotate(-playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate((float)(thirdPersonView ? -1 : 1) * playerViewX, 1.0f, 0.0f, 0.0f);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2896);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glLineWidth((float)1.0f);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glColor4f((float)colorRed, (float)colorGreen, (float)colorBlue, (float)colorAlpha);
            GL11.glBegin((int)1);
            GL11.glVertex3d((double)0.0, (double)1.0, (double)0.0);
            GL11.glVertex3d((double)-0.5, (double)0.5, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)1.0, (double)0.0);
            GL11.glVertex3d((double)0.5, (double)0.5, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)-0.5, (double)0.5, (double)0.0);
            GL11.glVertex3d((double)0.0, (double)0.0, (double)0.0);
            GL11.glVertex3d((double)0.5, (double)0.5, (double)0.0);
            GL11.glEnd();
            GL11.glDepthMask((boolean)true);
            GL11.glEnable((int)2929);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2896);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

