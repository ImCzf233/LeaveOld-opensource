/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.ui.ClickGUI.module.setting;

import com.leave.old.Client;
import com.leave.old.Setting;
import com.leave.old.Utils.FontUtil;
import com.leave.old.modules.Module;
import java.util.ArrayList;

public class SettingPanel {
    public final Setting setting;
    private Module module;
    public boolean aBinding;
    public static boolean a;
    public static int Code;
    public static Setting set;
    public int modeY;
    public int modes;
    public boolean modedr;

    public SettingPanel(Setting setting) {
        this.setting = setting;
    }

    public boolean isModedr() {
        return this.modedr;
    }

    public void setModedr(boolean modedr) {
        this.modedr = modedr;
    }

    public int getModeY() {
        return this.modeY;
    }

    public int getModes() {
        return this.modes;
    }

    public void drawScreen(int mouseX, int mouseY, float particalTicks, int x, int y, int width, int height, int tot) {
    }

    public void mouseClicked1(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public int getMaxSetting() {
        ArrayList<Setting> settings = new ArrayList<Setting>();
        for (Module it : Client.moduleManager.getModules()) {
            for (Setting s : it.getSetting()) {
                settings.addAll(it.getSetting());
            }
        }
        settings.sort((o1, o2) -> FontUtil.getStringWidth(o2.getName()) - FontUtil.getStringWidth(o1.getName()));
        return FontUtil.getStringWidth(((Setting)settings.get(0)).getName()) + 50;
    }
}

