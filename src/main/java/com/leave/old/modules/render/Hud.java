/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.ModeSetting;
import com.leave.old.ui.ClickGUI.CategorysPanels;
import com.leave.old.ui.ClickGUI.ClickGUI;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Hud
extends Module {
    public static EnableSetting Title = new EnableSetting("Title", true);
    public static EnableSetting ArrayList = new EnableSetting("ArrayList", true);
    public static EnableSetting RainBow = new EnableSetting("RainBow", false);
    public static EnableSetting Shadow = new EnableSetting("Shadow", false);
    public static EnableSetting notification = new EnableSetting("Notification", true);
    public ModeSetting RainbowMode = new ModeSetting("RainBowMode", "Mode1", Arrays.asList("Mode1", "Mode2"), this);
    float hue2;

    public Hud() {
        super("HUD", 0, Category.Render, true);
        this.getSetting().add(Title);
        this.getSetting().add(ArrayList);
        this.getSetting().add(RainBow);
        this.getSetting().add(notification);
        this.getSetting().add(this.RainbowMode);
    }

    @SubscribeEvent
    public void Notification(RenderGameOverlayEvent.Text event) {
        if (!notification.getEnable()) {
            return;
        }
        Client.notificationManager.draw();
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (float)(col1 >> 24 & 0xFF) / 255.0f;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(col1 & 0xFF) / 255.0f;
        float f4 = (float)(col2 >> 24 & 0xFF) / 255.0f;
        float f5 = (float)(col2 >> 16 & 0xFF) / 255.0f;
        float f6 = (float)(col2 >> 8 & 0xFF) / 255.0f;
        float f7 = (float)(col2 & 0xFF) / 255.0f;
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)2848);
        GL11.glShadeModel((int)7425);
        GL11.glPushMatrix();
        GL11.glBegin((int)7);
        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
        GL11.glVertex2d((double)left, (double)top);
        GL11.glVertex2d((double)left, (double)bottom);
        GL11.glColor4f((float)f5, (float)f6, (float)f7, (float)f4);
        GL11.glVertex2d((double)right, (double)bottom);
        GL11.glVertex2d((double)right, (double)top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glDisable((int)2848);
        GL11.glShadeModel((int)7424);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (!ArrayList.getEnable()) {
            return;
        }
        int rainbowTickc = 0;
        ScaledResolution s = new ScaledResolution(mc);
        int width = new ScaledResolution(mc).getScaledWidth();
        int height = new ScaledResolution(mc).getScaledHeight();
        int y = 3;
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module m : Client.moduleManager.getModules()) {
            if (!m.state) continue;
            enabledModules.add(m);
        }
        enabledModules.sort(new Comparator<Module>(){

            @Override
            public int compare(Module o1, Module o2) {
                if (ClickGui.useFont.getEnable()) {
                    return Client.cFontRenderer.getStringWidth(o2.getName()) - Client.cFontRenderer.getStringWidth(o1.getName());
                }
                return Module.mc.fontRendererObj.getStringWidth(o2.getName()) - Module.mc.fontRendererObj.getStringWidth(o1.getName());
            }
        });
        for (Module m : enabledModules) {
            int moduleWidth;
            int x1 = 0;
            int y1 = 0;
            if (m == null || !m.getState()) continue;
            if (++rainbowTickc > 100) {
                rainbowTickc = 0;
            }
            Color rainbow = this.RainbowMode.getCurrent().equalsIgnoreCase("Mode1") ? new Color(Color.HSBtoRGB((float)((double)Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 - Math.sin((double)rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f)) : new Color(Color.HSBtoRGB((float)((double)Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 - Math.sin((double)rainbowTickc / 50.0 * 1.6)) % 1.0f, 0.5f, 1.0f));
            int y2 = 0;
            int n = moduleWidth = ClickGui.useFont.getEnable() ? Client.cFontRenderer.getStringWidth(m.name) : Hud.mc.fontRendererObj.getStringWidth(m.name);
            if (ClickGui.screen != null) {
                for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
                    if (!categorysPanels.category.name().equalsIgnoreCase("TextGUI")) continue;
                    y2 = categorysPanels.getY();
                    if (categorysPanels.getX() > width / 2) {
                        x1 = categorysPanels.getX() - moduleWidth + categorysPanels.width;
                        y1 = categorysPanels.getY() + y + 18;
                        continue;
                    }
                    x1 = categorysPanels.getX();
                    y1 = categorysPanels.getY() + y + 18;
                }
            }
            if (!(Hud.mc.currentScreen instanceof ClickGUI)) {
                y1 -= 20;
            }
            if (RainBow.getEnable()) {
                if (ClickGui.useFont.getEnable()) {
                    Client.cFontRenderer.drawStringWithShadow(m.name, x1, y1, rainbow.getRGB());
                } else {
                    Hud.mc.fontRendererObj.drawStringWithShadow(m.name, x1, y1, rainbow.getRGB());
                }
            } else if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawStringWithShadow(m.name, x1, y1, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()).getRGB());
            } else {
                Hud.mc.fontRendererObj.drawStringWithShadow(m.name, x1, y1, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()).getRGB());
            }
            y += Hud.mc.fontRendererObj.FONT_HEIGHT;
        }
    }

    @SubscribeEvent
    public void Title(RenderGameOverlayEvent.Text event) {
        if (!Title.getEnable()) {
            return;
        }
        int rainbowTickc = 0;
        Color rainbow = new Color(Color.HSBtoRGB((float)((double)Minecraft.getMinecraft().thePlayer.ticksExisted / 50.0 - Math.sin((double)rainbowTickc / 40.0 * 1.4)) % 1.0f, 1.0f, 1.0f));
        if (Hud.mc.currentScreen instanceof ClickGUI && Hud.mc.currentScreen != null) {
            return;
        }
        float h_ = this.hue2;
        float h2 = this.hue2 + 85.0f;
        float h3 = this.hue2 + 170.0f;
        Color color33 = Color.getHSBColor(h_ / 255.0f, 0.9f, 1.0f);
        Color color332 = Color.getHSBColor(h2 / 255.0f, 0.9f, 1.0f);
        Color color333 = Color.getHSBColor(h3 / 255.0f, 0.9f, 1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        this.hue2 = (float)((double)this.hue2 + 0.5);
        Hud.drawGradientSideways(4.0, 3.0, 4 + Hud.mc.fontRendererObj.getStringWidth("LeaveOld") / 2, 4.0, color1, color2);
        Hud.drawGradientSideways(4 + Hud.mc.fontRendererObj.getStringWidth("LeaveOld") / 2, 3.0, 5 + Hud.mc.fontRendererObj.getStringWidth("LeaveOld"), 4.0, color2, color3);
        Hud.mc.fontRendererObj.drawStringWithShadow(" ", 3.0f, 5.0f, rainbow.getRGB());
        Hud.mc.fontRendererObj.drawStringWithShadow("LeaveOld", 4.0f, 5.0f, Color.WHITE.getRGB());
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        ScaledResolution sr = new ScaledResolution(mc);
        this.drewArmor(sr);
    }

    void drewArmor(ScaledResolution sr) {
        boolean currentItem = true;
        GL11.glPushMatrix();
        ArrayList<ItemStack> stuff = new ArrayList<ItemStack>();
        boolean onwater = Hud.mc.thePlayer.isEntityAlive() && Hud.mc.thePlayer.isInsideOfMaterial(Material.water);
        int split = -3;
        for (int index = 3; index >= 0; --index) {
            ItemStack armer = Hud.mc.thePlayer.inventory.armorInventory[index];
            if (armer == null) continue;
            stuff.add(armer);
        }
        if (Hud.mc.thePlayer.getCurrentEquippedItem() != null) {
            stuff.add(Hud.mc.thePlayer.getCurrentEquippedItem());
        }
        for (ItemStack errything : stuff) {
            if (Hud.mc.theWorld != null) {
                RenderHelper.enableGUIStandardItemLighting();
                split += 16;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            GlStateManager.clear(256);
            GlStateManager.enableBlend();
            Hud.mc.getRenderItem().zLevel = -150.0f;
            mc.getRenderItem().renderItemAndEffectIntoGUI(errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            mc.getRenderItem().renderItemOverlays(Hud.mc.fontRendererObj, errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
            Hud.mc.getRenderItem().zLevel = 0.0f;
            GlStateManager.disableBlend();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
            GlStateManager.enableDepth();
            GlStateManager.scale(2.0f, 2.0f, 2.0f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            errything.getEnchantmentTagList();
        }
        GL11.glPopMatrix();
    }
}

