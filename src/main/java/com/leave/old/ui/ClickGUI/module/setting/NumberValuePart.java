/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.ui.ClickGUI.module.setting;

import com.leave.old.Client;
import com.leave.old.Setting;
import com.leave.old.Utils.ClickGUIUtils;
import com.leave.old.Utils.FontLoaders;
import com.leave.old.Utils.FontUtil;
import com.leave.old.Utils.Render2DUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.ui.ClickGUI.module.setting.SettingPanel;
import java.awt.Color;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class NumberValuePart
extends SettingPanel {
    private boolean hovered = false;
    private boolean silderHovered = false;
    private boolean dragging = false;
    private double offsetX = 0.0;

    public NumberValuePart(Setting setting) {
        super(setting);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particalTicks, int x, int y, int width, int height, int tot) {
        if (ClickGui.Element.getCurrent() != ClickGui.Element.getMax()) {
            if (tot <= (int)ClickGui.Element.getCurrent() * 16 && tot >= 0) {
                this.hovered = ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height);
            }
        } else {
            this.hovered = ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height);
        }
        int color = new Color(55, 55, 55, 255).getRGB();
        if (this.hovered) {
            color = new Color(55, 55, 55, 255).getRGB();
        }
        Render2DUtils.drawRect(x, y, width, height, color);
        IntegerSetting setting1 = (IntegerSetting)this.setting;
        double percentBar = (setting1.getCurrent() - setting1.getMin()) / (setting1.getMax() - setting1.getMin());
        if (this.dragging) {
            for (Module m : Client.moduleManager.getModules()) {
                m.isSilder(this.setting);
            }
            double value = setting1.getMax() - setting1.getMin();
            double val = setting1.getMin() + MathHelper.clamp_double((double)(mouseX - x) / (double)width, 0.0, 1.0) * value;
            Double tmp = 1.0;
            if (((IntegerSetting)this.setting).getDou() == 1) {
                Double retn;
                DecimalFormat df = new DecimalFormat("#.0");
                String str = df.format(val);
                tmp = retn = new Double(str);
            } else if (((IntegerSetting)this.setting).getDou() == 0) {
                Double retn;
                DecimalFormat df = new DecimalFormat("#");
                String str = df.format(val);
                tmp = retn = new Double(str);
            }
            setting1.setCurrent(tmp);
        }
        Gui.drawRect(x, y + 9, x + (int)(percentBar * (double)width), y + 15, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 190).getRGB());
        Gui.drawRect(x + (int)(percentBar * (double)width) - 1, y + 9, x + (int)Math.min(percentBar * (double)width, (double)width), y + 15, new Color(0, 0, 0, 100).getRGB());
        GL11.glPushMatrix();
        if (ClickGui.useFont.getEnable()) {
            FontLoaders.F14.drawString(setting1.getName(), x + 3, y + height / 3 - 2, Color.WHITE.getRGB());
            if (((IntegerSetting)this.setting).getName().equalsIgnoreCase("MaxElement")) {
                if (ClickGui.Element.getCurrent() == ClickGui.Element.getMax()) {
                    FontLoaders.F14.drawString("NONE", x + width - Client.cFontRenderer.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent()) - 2, y + height / 3 - 2, Color.WHITE.getRGB());
                } else {
                    FontLoaders.F14.drawString("" + ((IntegerSetting)this.setting).getCurrent(), x + width - Client.cFontRenderer.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent()) - 2, y + height / 3 - 2, Color.WHITE.getRGB());
                }
            } else {
                FontLoaders.F14.drawString("" + ((IntegerSetting)this.setting).getCurrent(), x + width - Client.cFontRenderer.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent()) - 2, y + height / 3 - 2, Color.WHITE.getRGB());
            }
        } else {
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
            FontUtil.drawString(setting1.getName(), (float)(x + 4) * 1.3333334f, (float)(y + height / 3 - 3) * 1.3333334f, Color.WHITE.getRGB());
            if (((IntegerSetting)this.setting).getName().equalsIgnoreCase("MaxElement")) {
                if (ClickGui.Element.getCurrent() == ClickGui.Element.getMax()) {
                    FontUtil.drawString("NONE", (float)(x + width - FontUtil.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent())) * 1.3333334f, (float)(y + height / 3 - 3) * 1.3333334f, Color.WHITE.getRGB());
                } else {
                    FontUtil.drawString("" + ((IntegerSetting)this.setting).getCurrent(), (float)(x + width - FontUtil.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent())) * 1.3333334f, (float)(y + height / 3 - 3) * 1.3333334f, Color.WHITE.getRGB());
                }
            } else {
                FontUtil.drawString("" + ((IntegerSetting)this.setting).getCurrent(), (float)(x + width - FontUtil.getStringWidth("" + ((IntegerSetting)this.setting).getCurrent())) * 1.3333334f, (float)(y + height / 3 - 3) * 1.3333334f, Color.WHITE.getRGB());
            }
        }
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, particalTicks, x, y, width, height, tot);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.hovered && mouseButton == 0) {
            this.dragging = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }
}

