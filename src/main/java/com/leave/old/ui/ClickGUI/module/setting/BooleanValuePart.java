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
import com.leave.old.Utils.FontUtil;
import com.leave.old.Utils.Render2DUtils;
import com.leave.old.Utils.RenderUtil;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.EnableSetting;
import com.leave.old.ui.ClickGUI.module.setting.SettingPanel;
import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class BooleanValuePart
extends SettingPanel {
    private boolean hovered = false;

    public BooleanValuePart(Setting setting) {
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
        Render2DUtils.drawRect(x, y, width, height, color);
        Render2DUtils.drawRect(x + 1, y + 2, height - 6, height - 6, new Color(15, 15, 15, 200).getRGB());
        if (((EnableSetting)this.setting).getEnable()) {
            RenderUtil.drawCheckmark(x + 2, y + 6, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 255).getRGB());
        }
        if (((EnableSetting)this.setting).getEnable()) {
            if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawString(this.setting.getName(), x + 14, y + height / 2 - 3, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 255).getRGB());
            } else {
                GL11.glPushMatrix();
                GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
                FontUtil.drawString(this.setting.getName(), (float)(x + 14) * 1.3333334f, (float)(y + height / 2 - 3) * 1.3333334f, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 255).getRGB());
                GL11.glPopMatrix();
            }
        } else if (ClickGui.useFont.getEnable()) {
            Client.cFontRenderer.drawString(this.setting.getName(), x + 14, y + height / 2 - 3, Color.WHITE.getRGB());
        } else {
            GL11.glPushMatrix();
            GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
            FontUtil.drawString(this.setting.getName(), (float)(x + 14) * 1.3333334f, (float)(y + height / 2 - 3) * 1.3333334f, Color.WHITE.getRGB());
            GL11.glPopMatrix();
        }
        super.drawScreen(mouseX, mouseY, particalTicks, x, y, width, height, tot);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.hovered && mouseButton == 0) {
            ((EnableSetting)this.setting).setEnable(!((EnableSetting)this.setting).getEnable());
            for (Module module : Client.moduleManager.getModules()) {
                module.onCheck(this.setting);
            }
        }
    }
}

