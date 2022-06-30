/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Setting;
import com.leave.old.config.configs.ClickGuiConfig;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.ui.ClickGUI.ClickGUI;
import java.lang.reflect.Field;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGui
extends Module {
    public static GuiScreen screen;
    public static IntegerSetting red;
    public static IntegerSetting green;
    public static IntegerSetting blue;
    public static IntegerSetting Element;
    public static IntegerSetting Scale;
    public static EnableSetting blur;
    public static EnableSetting rainBow;
    public static IntegerSetting rainBowSpeed;
    public static EnableSetting useFont;

    public ClickGui() {
        super("ClickGui", 54, Category.Render, false);
        this.getSetting().add(red);
        this.getSetting().add(green);
        this.getSetting().add(blue);
        this.getSetting().add(Element);
        this.getSetting().add(blur);
        this.getSetting().add(useFont);
        this.getSetting().add(rainBow);
        this.getSetting().add(rainBowSpeed);
    }

    public void displayGuiScreenBypass(GuiScreen screen) {
        ClickGui.mc.currentScreen = screen;
        mc.setIngameNotInFocus();
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        try {
            Field screenMCObject = GuiScreen.class.getField("field_146297_k");
            screenMCObject.setAccessible(true);
            screenMCObject.set(screen, mc);
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        ClickGui.mc.currentScreen.height = j;
        ClickGui.mc.currentScreen.width = i;
        ClickGui.mc.currentScreen.initGui();
    }

    @Override
    public void onCheck(Setting setting) {
        if (setting.getName().equalsIgnoreCase("Blur")) {
            if (screen == null) {
                screen = new ClickGUI();
            }
            mc.displayGuiScreen(null);
            if (Tools.isSingleplayer()) {
                mc.displayGuiScreen(screen);
            } else {
                this.displayGuiScreenBypass(screen);
            }
        }
        super.onCheck(setting);
    }

    @Override
    public void enable() {
        super.enable();
        if (screen == null) {
            screen = new ClickGUI();
        }
        if (Tools.isSingleplayer()) {
            mc.displayGuiScreen(screen);
        } else {
            this.displayGuiScreenBypass(screen);
        }
        ClickGuiConfig.loadClickGui();
        this.toggle();
    }

    static {
        red = new IntegerSetting("Red", 0.0, 0.0, 255.0, 0);
        green = new IntegerSetting("Green", 255.0, 0.0, 255.0, 0);
        blue = new IntegerSetting("Blue", 148.0, 0.0, 255.0, 0);
        Element = new IntegerSetting("MaxElement", 7.0, 1.0, 13.0, 0);
        Scale = new IntegerSetting("Scale", 1.0, 0.5, 2.0, 1);
        blur = new EnableSetting("Blur", true);
        rainBow = new EnableSetting("RainBow", false);
        rainBowSpeed = new IntegerSetting("RainBowSpeed", 50.0, 10.5, 100.0, 1);
        useFont = new EnableSetting("Smooth Font", false);
    }
}

