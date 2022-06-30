/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.Utils;

import java.awt.Color;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class HUDUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static Field timerField = null;
    public static float delta;

    public static final ScaledResolution getScaledRes() {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        return scaledRes;
    }

    public static void drawImage(int x, int y, int width, int height, ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void glColor(int hex) {
        float alpha = (float)(hex >> 24 & 0xFF) / 255.0f;
        float red = (float)(hex >> 16 & 0xFF) / 255.0f;
        float green = (float)(hex >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hex & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glShadeModel((int)7425);
        GL11.glDisable((int)3553);
        GL11.glEnable((int)2848);
        GL11.glDisable((int)2929);
        GL11.glDisable((int)2896);
        GL11.glDepthMask((boolean)false);
        GL11.glHint((int)3154, (int)4354);
    }

    public static void post3D() {
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void drawBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer vertexBuffer = tessellator.getWorldRenderer();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        HUDUtils.drawImage(image, x, y, width, height, 1.0f);
    }

    public static void drawImage(ResourceLocation image, float x, float y, float width, float height) {
        HUDUtils.drawImage(image, (int)x, (int)y, (int)width, (int)height, 1.0f);
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, float alpha) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDepthMask((boolean)false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)alpha);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
    }

    public static void doGlScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;
        int k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int)(x * scaleFactor), (int)(mc.displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)(height * scaleFactor));
    }

    public static double getAnimationState(double animation, double finalState, double speed) {
        float add = (float)((double)delta * speed);
        animation = animation < finalState ? (animation + (double)add < finalState ? (animation += (double)add) : finalState) : (animation - (double)add > finalState ? (animation -= (double)add) : finalState);
        return animation;
    }

    public static void drawArc(float x1, float y1, double r, int color, int startPoint, double arc, int linewidth) {
        r *= 2.0;
        x1 *= 2.0f;
        y1 *= 2.0f;
        float f = (float)(color >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(color & 0xFF) / 255.0f;
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        GL11.glLineWidth((float)linewidth);
        GL11.glEnable((int)2848);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)3);
        int i = startPoint;
        while ((double)i <= arc) {
            double x = Math.sin((double)i * Math.PI / 180.0) * r;
            double y = Math.cos((double)i * Math.PI / 180.0) * r;
            GL11.glVertex2d((double)((double)x1 + x), (double)((double)y1 + y));
            ++i;
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
        x = (float)((double)x + ((double)(round / 2.0f) + 0.5));
        y = (float)((double)y + ((double)(round / 2.0f) + 0.5));
        x2 = (float)((double)x2 - ((double)(round / 2.0f) + 0.5));
        y2 = (float)((double)y2 - ((double)(round / 2.0f) + 0.5));
        HUDUtils.drawRect(x, y, x2, y2, color);
        HUDUtils.drawCircle((int)(x2 - round / 2.0f), (int)(y + round / 2.0f), round, color);
        HUDUtils.drawCircle((int)(x + round / 2.0f), (int)(y2 - round / 2.0f), round, color);
        HUDUtils.drawCircle((int)(x + round / 2.0f), (int)(y + round / 2.0f), round, color);
        HUDUtils.drawCircle((int)(x2 - round / 2.0f), (int)(y2 - round / 2.0f), round, color);
        HUDUtils.drawRect(x - round / 2.0f - 0.5f, y + round / 2.0f, x2, y2 - round / 2.0f, color);
        HUDUtils.drawRect(x, y + round / 2.0f, x2 + round / 2.0f + 0.5f, y2 - round / 2.0f, color);
        HUDUtils.drawRect(x + round / 2.0f, y - round / 2.0f - 0.5f, x2 - round / 2.0f, y2 - round / 2.0f, color);
        HUDUtils.drawRect(x + round / 2.0f, y, x2 - round / 2.0f, y2 + round / 2.0f + 0.5f, color);
    }

    public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
        HUDUtils.enableGL2D();
        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
        HUDUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
        HUDUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
        HUDUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
        HUDUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
        HUDUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
        HUDUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
        HUDUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
        HUDUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
        HUDUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
        HUDUtils.disableGL2D();
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        HUDUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        HUDUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static void enableGL2D() {
        GL11.glDisable((int)2929);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDepthMask((boolean)true);
        GL11.glEnable((int)2848);
        GL11.glHint((int)3154, (int)4354);
        GL11.glHint((int)3155, (int)4354);
    }

    public static void disableGL2D() {
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)2848);
        GL11.glHint((int)3154, (int)4352);
        GL11.glHint((int)3155, (int)4352);
    }

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }

    public static void drawBorderedCircle(int x, int y, float radius, int outsideC, int insideC) {
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        float scale = 0.1f;
        GL11.glScalef((float)scale, (float)scale, (float)scale);
        x = (int)((float)x * (1.0f / scale));
        y = (int)((float)y * (1.0f / scale));
        HUDUtils.drawCircle(x, y, radius *= 1.0f / scale, insideC);
        HUDUtils.drawUnfilledCircle(x, y, radius, 1.0f, outsideC);
        GL11.glScalef((float)(1.0f / scale), (float)(1.0f / scale), (float)(1.0f / scale));
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static void drawUnfilledCircle(int x, int y, float radius, float lineWidth, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glLineWidth((float)lineWidth);
        GL11.glEnable((int)2848);
        GL11.glBegin((int)2);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        }
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }

    public static void drawCircle(int x, int y, float radius, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)((double)x + Math.sin((double)i * 3.141526 / 180.0) * (double)radius), (double)((double)y + Math.cos((double)i * 3.141526 / 180.0) * (double)radius));
        }
        GL11.glEnd();
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        int j;
        if (left < right) {
            j = (int)left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            j = (int)top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0f;
        float f = (float)(color >> 16 & 0xFF) / 255.0f;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0f;
        float f2 = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0).endVertex();
        worldrenderer.pos(right, bottom, 0.0).endVertex();
        worldrenderer.pos(right, top, 0.0).endVertex();
        worldrenderer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void su() {
        try {
            timerField = Minecraft.class.getDeclaredField("field_71428_T");
        }
        catch (Exception var4) {
            try {
                timerField = Minecraft.class.getDeclaredField("timer");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (timerField != null) {
            timerField.setAccessible(true);
        }
    }

    public static void rect(float x1, float y1, float x2, float y2, int fill) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f((float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f);
        float f = (float)(fill >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(fill >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(fill >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(fill & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glPushMatrix();
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glBegin((int)7);
        GL11.glVertex2d((double)x2, (double)y1);
        GL11.glVertex2d((double)x1, (double)y1);
        GL11.glVertex2d((double)x1, (double)y2);
        GL11.glVertex2d((double)x2, (double)y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
    }

    public static Timer getTimer() {
        try {
            HUDUtils.su();
            return (Timer)timerField.get(mc);
        }
        catch (IllegalAccessException | IndexOutOfBoundsException var1) {
            return null;
        }
    }

    public static int rainbowDraw(long speed, long ... delay) {
        long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
        return Color.getHSBColor((float)(time % (15000L / speed)) / (15000.0f / (float)speed), 1.0f, 1.0f).getRGB();
    }

    public static int astolfoColorsDraw(int yOffset, int yTotal, float speed) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed)) + (float)((yTotal - yOffset) * 9); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.HSBtoRGB(hue += 0.5f, 0.5f, 1.0f);
    }

    public static int astolfoColorsDraw(int yOffset, int yTotal) {
        return HUDUtils.astolfoColorsDraw(yOffset, yTotal, 2900.0f);
    }

    public static void re(BlockPos bp, int color, boolean shade) {
        if (bp != null) {
            double x = (double)bp.getX() - HUDUtils.mc.getRenderManager().viewerPosX;
            double y = (double)bp.getY() - HUDUtils.mc.getRenderManager().viewerPosY;
            double z = (double)bp.getZ() - HUDUtils.mc.getRenderManager().viewerPosZ;
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)2.0f);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            float a = (float)(color >> 24 & 0xFF) / 255.0f;
            float r = (float)(color >> 16 & 0xFF) / 255.0f;
            float g = (float)(color >> 8 & 0xFF) / 255.0f;
            float b = (float)(color & 0xFF) / 255.0f;
            GL11.glColor4d((double)r, (double)g, (double)b, (double)a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
            if (shade) {
                HUDUtils.dbb(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), r, g, b);
            }
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
        }
    }

    public static void ee(Entity e, int type, double expand, double shift, int color, boolean damage) {
        if (e instanceof EntityLivingBase) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            float d = (float)expand / 40.0f;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer)e).hurtTime != 0) {
                color = Color.RED.getRGB();
            }
            GlStateManager.pushMatrix();
            if (type == 3) {
                GL11.glTranslated((double)x, (double)(y - 0.2), (double)z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), (double)0.0, (double)1.0, (double)0.0);
                GlStateManager.disableDepth();
                GL11.glScalef((float)(0.03f + d), (float)(0.03f + d), (float)(0.03f + d));
                int outline = Color.black.getRGB();
                Gui.drawRect(-20, -1, -26, 75, outline);
                Gui.drawRect(20, -1, 26, 75, outline);
                Gui.drawRect(-20, -1, 21, 5, outline);
                Gui.drawRect(-20, 70, 21, 75, outline);
                if (color != 0) {
                    Gui.drawRect(-21, 0, -25, 74, color);
                    Gui.drawRect(21, 0, 25, 74, color);
                    Gui.drawRect(-21, 0, 24, 4, color);
                    Gui.drawRect(-21, 71, 25, 74, color);
                } else {
                    int st = HUDUtils.rainbowDraw(2L, 0L);
                    int en = HUDUtils.rainbowDraw(2L, 1000L);
                    HUDUtils.dGR(-21, 0, -25, 74, st, en);
                    HUDUtils.dGR(21, 0, 25, 74, st, en);
                    Gui.drawRect(-21, 0, 21, 4, en);
                    Gui.drawRect(-21, 71, 21, 74, st);
                }
                GlStateManager.enableDepth();
            } else if (type == 4) {
                EntityLivingBase en = (EntityLivingBase)e;
                double r = en.getHealth() / en.getMaxHealth();
                int b = (int)(74.0 * r);
                int hc = r < 0.3 ? Color.red.getRGB() : (r < 0.5 ? Color.orange.getRGB() : (r < 0.7 ? Color.yellow.getRGB() : Color.green.getRGB()));
                GL11.glTranslated((double)x, (double)(y - 0.2), (double)z);
                GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), (double)0.0, (double)1.0, (double)0.0);
                GlStateManager.disableDepth();
                GL11.glScalef((float)(0.03f + d), (float)(0.03f + d), (float)(0.03f + d));
                int i = (int)(21.0 + shift * 2.0);
                Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                Gui.drawRect(i + 1, b, i + 4, 74, Color.DARK_GRAY.getRGB());
                Gui.drawRect(i + 1, 0, i + 4, b, hc);
                GlStateManager.enableDepth();
            } else if (type == 6) {
                HUDUtils.d3p(x, y, z, 0.7f, 45, 1.5f, color, color == 0);
            } else {
                if (color == 0) {
                    color = HUDUtils.rainbowDraw(2L, 0L);
                }
                float a = (float)(color >> 24 & 0xFF) / 255.0f;
                float r = (float)(color >> 16 & 0xFF) / 255.0f;
                float g = (float)(color >> 8 & 0xFF) / 255.0f;
                float b = (float)(color & 0xFF) / 255.0f;
                if (type == 5) {
                    int i;
                    GL11.glTranslated((double)x, (double)(y - 0.2), (double)z);
                    GL11.glRotated((double)(-HUDUtils.mc.getRenderManager().playerViewY), (double)0.0, (double)1.0, (double)0.0);
                    GlStateManager.disableDepth();
                    GL11.glScalef((float)(0.03f + d), (float)0.03f, (float)(0.03f + d));
                    boolean base = true;
                    HUDUtils.d2p(0.0, 95.0, 10, 3, Color.black.getRGB());
                    for (i = 0; i < 6; ++i) {
                        HUDUtils.d2p(0.0, 95 + (10 - i), 3, 4, Color.black.getRGB());
                    }
                    for (i = 0; i < 7; ++i) {
                        HUDUtils.d2p(0.0, 95 + (10 - i), 2, 4, color);
                    }
                    HUDUtils.d2p(0.0, 95.0, 8, 3, color);
                    GlStateManager.enableDepth();
                } else {
                    AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1 + expand, 0.1 + expand, 0.1 + expand);
                    AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glEnable((int)3042);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glLineWidth((float)2.0f);
                    GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
                    if (type == 1) {
                        RenderGlobal.drawSelectionBoundingBox(axis);
                    } else if (type == 2) {
                        HUDUtils.dbb(axis, r, g, b);
                    }
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glDisable((int)3042);
                }
            }
            GlStateManager.popMatrix();
        }
    }

    public static void dbb(AxisAlignedBB abb, float r, float g, float b) {
        float a = 0.25f;
        Tessellator ts = Tessellator.getInstance();
        WorldRenderer vb = ts.getWorldRenderer();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
    }

    public static void dtl(Entity e, int color, float lw) {
        if (e != null) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosX;
            double y = (double)e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)HUDUtils.getTimer().renderPartialTicks - HUDUtils.mc.getRenderManager().viewerPosZ;
            float a = (float)(color >> 24 & 0xFF) / 255.0f;
            float r = (float)(color >> 16 & 0xFF) / 255.0f;
            float g = (float)(color >> 8 & 0xFF) / 255.0f;
            float b = (float)(color & 0xFF) / 255.0f;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glLineWidth((float)lw);
            GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
            GL11.glBegin((int)2);
            GL11.glVertex3d((double)0.0, (double)HUDUtils.mc.thePlayer.getEyeHeight(), (double)0.0);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            GL11.glEnd();
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    public static void dGR(int left, int top, int right, int bottom, int startColor, int endColor) {
        int j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }
        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(startColor & 0xFF) / 255.0f;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0.0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0.0).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0.0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void db(int w, int h, int r) {
        int c = r == -1 ? -1089466352 : r;
        Gui.drawRect(0, 0, w, h, c);
    }

    public static void drawColouredText(String text, char lineSplit, int leftOffset, int topOffset, long colourParam1, long shift, boolean rect, FontRenderer fontRenderer) {
        int bX = leftOffset;
        int l = 0;
        long colourControl = 0L;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if (c == lineSplit) {
                leftOffset = bX;
                topOffset += fontRenderer.FONT_HEIGHT + 5;
                colourControl = shift * (long)(++l);
                continue;
            }
            fontRenderer.drawString(String.valueOf(c), leftOffset, topOffset, HUDUtils.astolfoColorsDraw((int)colourParam1, (int)colourControl), rect);
            leftOffset += fontRenderer.getCharWidth(c);
            if (c == ' ') continue;
            colourControl -= 90L;
        }
    }

    public static PositionMode getPostitionMode(int marginX, int marginY, double height, double width) {
        int halfHeight = (int)(height / 4.0);
        int halfWidth = (int)(width / 1.0);
        PositionMode positionMode = null;
        if (marginY < halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.UPLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.UPRIGHT;
            }
        }
        if (marginY > halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.DOWNLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.DOWNRIGHT;
            }
        }
        return positionMode;
    }

    public static void drawCircle(double x, double y, double radius, int c) {
        GL11.glEnable((int)32925);
        GL11.glEnable((int)2881);
        float alpha = (float)(c >> 24 & 0xFF) / 255.0f;
        float red = (float)(c >> 16 & 0xFF) / 255.0f;
        float green = (float)(c >> 8 & 0xFF) / 255.0f;
        float blue = (float)(c & 0xFF) / 255.0f;
        boolean blend = GL11.glIsEnabled((int)3042);
        boolean line = GL11.glIsEnabled((int)2848);
        boolean texture = GL11.glIsEnabled((int)3553);
        if (!blend) {
            GL11.glEnable((int)3042);
        }
        if (!line) {
            GL11.glEnable((int)2848);
        }
        if (texture) {
            GL11.glDisable((int)3553);
        }
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glColor4f((float)red, (float)green, (float)blue, (float)alpha);
        GL11.glBegin((int)9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d((double)(x + Math.sin((double)i * 3.141526 / 180.0) * radius), (double)(y + Math.cos((double)i * 3.141526 / 180.0) * radius));
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable((int)3553);
        }
        if (!line) {
            GL11.glDisable((int)2848);
        }
        if (!blend) {
            GL11.glDisable((int)3042);
        }
        GL11.glDisable((int)2881);
        GL11.glClear((int)0);
    }

    public static void d2p(double x, double y, int radius, int sides, int color) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);
        for (int i = 0; i < sides; ++i) {
            double angle = Math.PI * 2 * (double)i / (double)sides + Math.toRadians(180.0);
            worldrenderer.pos(x + Math.sin(angle) * (double)radius, y + Math.cos(angle) * (double)radius, 0.0).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void d3p(double x, double y, double z, double radius, int sides, float lineWidth, int color, boolean chroma) {
        float a = (float)(color >> 24 & 0xFF) / 255.0f;
        float r = (float)(color >> 16 & 0xFF) / 255.0f;
        float g = (float)(color >> 8 & 0xFF) / 255.0f;
        float b = (float)(color & 0xFF) / 255.0f;
        HUDUtils.mc.entityRenderer.disableLightmap();
        GL11.glDisable((int)3553);
        GL11.glEnable((int)3042);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glDisable((int)2929);
        GL11.glEnable((int)2848);
        GL11.glDepthMask((boolean)false);
        GL11.glLineWidth((float)lineWidth);
        if (!chroma) {
            GL11.glColor4f((float)r, (float)g, (float)b, (float)a);
        }
        GL11.glBegin((int)1);
        long d = 0L;
        long ed = 15000L / (long)sides;
        long hed = ed / 2L;
        for (int i = 0; i < sides * 2; ++i) {
            if (chroma) {
                if (i % 2 != 0) {
                    if (i == 47) {
                        d = hed;
                    }
                    d += ed;
                }
                int c = HUDUtils.rainbowDraw(2L, d);
                float r2 = (float)(c >> 16 & 0xFF) / 255.0f;
                float g2 = (float)(c >> 8 & 0xFF) / 255.0f;
                float b2 = (float)(c & 0xFF) / 255.0f;
                GL11.glColor3f((float)r2, (float)g2, (float)b2);
            }
            double angle = Math.PI * 2 * (double)i / (double)sides + Math.toRadians(180.0);
            GL11.glVertex3d((double)(x + Math.cos(angle) * radius), (double)y, (double)(z + Math.sin(angle) * radius));
        }
        GL11.glEnd();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        HUDUtils.mc.entityRenderer.enableLightmap();
    }

    public static void drawBoundingBox(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.color(red, green, blue, alpha);
        HUDUtils.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static enum PositionMode {
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT;

    }
}

