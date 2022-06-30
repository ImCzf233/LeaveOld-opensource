/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.leave.old.Notifications;

import com.leave.old.Client;
import com.leave.old.Utils.AnimationUtils;
import com.leave.old.Utils.FontUtil;
import com.leave.old.modules.render.ClickGui;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

public class Notification {
    public String text;
    public double width = 30.0;
    public double height = 20.0;
    public float x;
    Type type;
    public float y;
    public float position;
    public boolean in = true;
    public AnimationUtils animationUtils = new AnimationUtils();
    AnimationUtils yAnimationUtils = new AnimationUtils();
    private int key;

    public Notification(String text, Type type, int key) {
        this.text = text;
        this.type = type;
        this.key = key;
        this.width = key != 0 ? (ClickGui.useFont.getEnable() ? (double)(Client.cFontRenderer.getStringWidth(text) + 90) : (double)(FontUtil.getStringWidth(text) + 90)) : (ClickGui.useFont.getEnable() ? (double)(Client.cFontRenderer.getStringWidth(text) + 80) : (double)(FontUtil.getStringWidth(text) + 80));
        this.x = (float)this.width;
    }

    public void onRender() {
        int i = 0;
        for (Notification notification : Client.notificationManager.notifications) {
            if (notification == this) break;
            ++i;
        }
        this.y = this.yAnimationUtils.animate((float)((double)i * (this.height + 5.0)), this.y, 0.1f);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        Gui.drawRect((int)((double)((float)sr.getScaledWidth() + this.x) - this.width), (int)((double)((float)(sr.getScaledHeight() - 55) - this.y) - this.height), (int)((float)sr.getScaledWidth() + this.x), (int)((float)(sr.getScaledHeight() - 55) - this.y), new Color(24, 24, 24, 200).getRGB());
        if (this.type.name().equalsIgnoreCase(Type.Enable.name())) {
            if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
                Client.cFontRenderer.drawStringWithShadow(" - Enabled", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(33, 248, 0, 255).getRGB());
            } else {
                FontUtil.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
                FontUtil.drawStringWithShadow(" - Enabled", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(33, 248, 0, 255).getRGB());
            }
        } else if (this.type.name().equalsIgnoreCase(Type.Disable.name())) {
            if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
                Client.cFontRenderer.drawStringWithShadow(" - Disabled", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(248, 0, 0, 255).getRGB());
            } else {
                FontUtil.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
                FontUtil.drawStringWithShadow(" - Disabled", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(248, 0, 0, 255).getRGB());
            }
        } else if (this.type.name().equalsIgnoreCase(Type.NONE.name())) {
            if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
            } else {
                FontUtil.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
            }
        } else if (ClickGui.useFont.getEnable()) {
            Client.cFontRenderer.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
            Client.cFontRenderer.drawStringWithShadow("- BindKey ", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 255).getRGB());
            Client.cFontRenderer.drawStringWithShadow(" " + Keyboard.getKeyName((int)this.key), (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + (double)FontUtil.getStringWidth("- BindKey ")), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(255, 224, 0, 255).getRGB());
        } else {
            FontUtil.drawStringWithShadow(this.text, (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 232).getRGB());
            FontUtil.drawStringWithShadow("- BindKey ", (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + 5.0), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(204, 204, 204, 255).getRGB());
            FontUtil.drawStringWithShadow(" " + Keyboard.getKeyName((int)this.key), (float)((double)((float)sr.getScaledWidth() + this.x) - this.width + 10.0 + (double)FontUtil.getStringWidth(this.text) + (double)FontUtil.getStringWidth("- BindKey ")), (float)sr.getScaledHeight() - 50.0f - this.y - 18.0f, new Color(255, 224, 0, 255).getRGB());
        }
    }

    public static enum Type {
        Enable,
        Disable,
        KeyBind,
        NONE;

    }
}

