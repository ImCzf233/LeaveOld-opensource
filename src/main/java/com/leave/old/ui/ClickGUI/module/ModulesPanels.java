/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.leave.old.ui.ClickGUI.module;

import com.leave.old.Client;
import com.leave.old.Notifications.Notification;
import com.leave.old.Setting;
import com.leave.old.Utils.ClickGUIUtils;
import com.leave.old.Utils.FontUtil;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import com.leave.old.ui.ClickGUI.module.setting.BooleanValuePart;
import com.leave.old.ui.ClickGUI.module.setting.ModeValuesPart;
import com.leave.old.ui.ClickGUI.module.setting.NumberValuePart;
import com.leave.old.ui.ClickGUI.module.setting.SettingPanel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;

public class ModulesPanels {
    private boolean hovered;
    private Module module;
    public boolean showSetting;
    private int bik = -2;
    private int y;
    public final List<SettingPanel> settingPanelList;

    public ModulesPanels(Module module) {
        this.module = module;
        this.settingPanelList = new ArrayList<SettingPanel>();
        if (!module.getSetting().isEmpty()) {
            for (Setting it : module.getSetting()) {
                if (it instanceof EnableSetting) {
                    this.settingPanelList.add(new BooleanValuePart(it));
                    continue;
                }
                if (it instanceof IntegerSetting) {
                    this.settingPanelList.add(new NumberValuePart(it));
                    continue;
                }
                if (!(it instanceof ModeSetting)) continue;
                this.settingPanelList.add(new ModeValuesPart(it));
            }
        }
    }

    public int getSettingY() {
        return this.settingPanelList.size() * 16;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void drawScreen(int mouseX, int mouseY, float particalTicks, int x, int y, int width, int height, int modY, int CategoryY, int tot, Color rainbow) {
        this.y = y;
        this.hovered = ClickGui.Element.getCurrent() != ClickGui.Element.getMax() ? (tot <= (int)ClickGui.Element.getCurrent() * 16 && tot >= 0 ? ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height) : false) : ClickGUIUtils.isHovered(mouseX, mouseY, x, y, width, height);
        int color = new Color(41, 41, 41).getRGB();
        if (this.bik == -1) {
            if (this.module.getState()) {
                ClickGUIUtils.drawRect(x, y, width, height, this.hovered ? new Color(51, 51, 51).getRGB() : color);
                ClickGUIUtils.drawRect(x, y, width, height, this.hovered ? new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()).getRGB() : new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent(), 230).getRGB());
            } else {
                ClickGUIUtils.drawRect(x, y, width, height, this.hovered ? new Color(51, 51, 51).getRGB() : color);
            }
            if (ClickGui.useFont.getEnable()) {
                Client.cFontRenderer.drawCenteredString("Press a key", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
            } else {
                FontUtil.drawCenteredString("Press a key", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
            }
            if (Keyboard.getEventKey() != 42) {
                this.bik = Keyboard.getEventKey();
                if (this.bik != -2 && this.bik != 0) {
                    this.module.setKey(this.bik);
                    Client.notificationManager.add(new Notification(this.module.getName(), Notification.Type.KeyBind, this.bik));
                }
            }
        } else {
            this.bik = -2;
            if (this.module.getState()) {
                ClickGUIUtils.drawRect(x, y, width, height, this.hovered ? new Color(51, 51, 51).getRGB() : color);
                ClickGUIUtils.drawRect(x, y, width, height, rainbow.getRGB());
            } else {
                ClickGUIUtils.drawRect(x, y, width, height, this.hovered ? new Color(51, 51, 51).getRGB() : color);
            }
            if (ClickGui.useFont.getEnable()) {
                if (this.module.getState()) {
                    if ((int)ClickGui.red.getCurrent() > 127 && (int)ClickGui.green.getCurrent() > 127 && (int)ClickGui.blue.getCurrent() > 127) {
                        if (this.module.getKey() != 0) {
                            Client.cFontRenderer.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, color);
                        } else {
                            Client.cFontRenderer.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, color);
                        }
                    } else if (this.module.getKey() != 0) {
                        Client.cFontRenderer.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                    } else {
                        Client.cFontRenderer.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                    }
                } else if (this.module.getKey() != 0) {
                    Client.cFontRenderer.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                } else {
                    Client.cFontRenderer.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                }
            } else if (this.module.getState()) {
                if ((int)ClickGui.red.getCurrent() > 127 && (int)ClickGui.green.getCurrent() > 127 && (int)ClickGui.blue.getCurrent() > 127) {
                    if (this.module.getKey() != 0) {
                        FontUtil.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, color);
                    } else {
                        FontUtil.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, color);
                    }
                } else if (this.module.getKey() != 0) {
                    FontUtil.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                } else {
                    FontUtil.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
                }
            } else if ((int)ClickGui.red.getCurrent() > 127 && (int)ClickGui.green.getCurrent() > 127 && (int)ClickGui.blue.getCurrent() > 127) {
                if (this.module.getKey() != 0) {
                    FontUtil.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, color);
                } else {
                    FontUtil.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, color);
                }
            } else if (this.module.getKey() != 0) {
                FontUtil.drawCenteredString(this.module.getName() + " <" + Keyboard.getKeyName((int)this.module.getKey()) + ">", x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
            } else {
                FontUtil.drawCenteredString(this.module.getName(), x + width / 2 - 4, y + height / 2 - 4, Color.WHITE.getRGB());
            }
        }
        if (this.showSetting && !this.settingPanelList.isEmpty()) {
            int settingY = y;
            int cnt = 0;
            for (SettingPanel it : this.settingPanelList) {
                int finalSettingY = settingY;
                it.drawScreen(mouseX, mouseY, particalTicks, x, finalSettingY + 16, width, 16, tot + ++cnt * 16);
                if (it.isModedr()) {
                    settingY += it.getModeY();
                    cnt += it.getModes();
                }
                settingY += 16;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!this.hovered && this.bik == -1) {
            this.module.setKey(0);
            this.bik = -2;
            Client.notificationManager.add(new Notification(this.module.getName(), Notification.Type.KeyBind, 0));
        }
        if (this.hovered && mouseButton == 0) {
            if (Keyboard.isKeyDown((int)42)) {
                this.bik = -1;
                return;
            }
            this.module.toggle();
        }
        if (this.hovered && mouseButton == 1) {
            boolean bl = this.showSetting = !this.showSetting;
        }
        if (this.showSetting && !this.settingPanelList.isEmpty()) {
            this.settingPanelList.forEach(it -> it.mouseClicked(mouseX, mouseY, mouseButton));
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.showSetting && !this.settingPanelList.isEmpty()) {
            this.settingPanelList.forEach(it -> it.mouseReleased(mouseX, mouseY, state));
        }
    }
}

