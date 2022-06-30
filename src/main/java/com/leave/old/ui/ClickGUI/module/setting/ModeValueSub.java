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
import com.leave.old.settings.ModeSetting;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class ModeValueSub {
    private boolean hovered = false;
    public String modes;
    private Setting setting;

    public ModeValueSub(String modes, Setting setting) {
        this.modes = modes;
        this.setting = setting;
    }

    public void drawScreen(int mouseX, int mouseY, float particalTicks, int x, int y, int width, int height, int tot) {
        this.reload(mouseX, mouseY, particalTicks, x + width, y, width, height, tot);
    }

    public void reload(int mouseX, int mouseY, float particalTicks, int x, int y, int width, int height, int tot) {
        if (ClickGui.Element.getCurrent() != ClickGui.Element.getMax()) {
            if (tot <= (int)ClickGui.Element.getCurrent() * 16 && tot >= 0) {
                this.hovered = ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height);
            }
        } else {
            this.hovered = ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height);
        }
        int color = new Color(47, 47, 47, 233).getRGB();
        if (this.hovered) {
            color = new Color(56, 56, 56, 255).getRGB();
        }
        Render2DUtils.drawRect(x, y, width, height, color);
        Render2DUtils.drawRect(x, y, 2, height, color);
        if (((ModeSetting)this.setting).getCurrent().equalsIgnoreCase(this.modes)) {
            if (((ModeSetting)this.setting).getCurrent() == "" || ((ModeSetting)this.setting).getCurrent() != this.modes) {
                ((ModeSetting)this.setting).setCurrent(this.modes);
            }
            if (ClickGui.useFont.getEnable()) {
                FontLoaders.F14.drawCenteredString(this.modes, x + width / 2, y + height / 2 - 3, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 255).getRGB());
            } else {
                GL11.glPushMatrix();
                GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
                FontUtil.drawCenteredString(this.modes, (float)(x + width / 2) * 1.3333334f, (float)(y + height / 2 - 3) * 1.3333334f, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 255).getRGB());
                GL11.glPopMatrix();
            }
        } else if (ClickGui.useFont.getEnable()) {
            FontLoaders.F14.drawCenteredString(this.modes, x + width / 2, y + height / 2 - 3, Color.WHITE.getRGB());
        } else {
            GL11.glPushMatrix();
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
            FontUtil.drawCenteredString(this.modes, (float)(x + width / 2) * 1.3333334f, (float)(y + height / 2 - 3) * 1.3333334f, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.hovered && mouseButton == 0) {
            ((ModeSetting)this.setting).setCurrent(this.modes);
            for (Module m : Client.moduleManager.getModules()) {
                m.setMode(this.setting);
            }
        }
    }
}

