/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.ui.ClickGUI;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.ClickGUIUtils;
import com.leave.old.Utils.FontUtil;
import com.leave.old.Utils.TranslateUtil;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.ui.ClickGUI.ClickGUI;
import com.leave.old.ui.ClickGUI.module.ModulesPanels;
import com.leave.old.ui.ClickGUI.module.setting.SettingPanel;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class CategorysPanels {
    public Category category;
    private boolean Move;
    public int x;
    public int y;
    public int width;
    public int height;
    public int tempX;
    public int tempY;
    public int wheel;
    private boolean hovered;
    private boolean open;
    public ArrayList<ModulesPanels> modulesPanels;
    private boolean show;
    private TranslateUtil translate = new TranslateUtil(0.0f, 0.0f);
    private TranslateUtil translate_ = new TranslateUtil(0.0f, 0.0f);

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public CategorysPanels(Category category, int x, int y, int width, int height) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.modulesPanels = new ArrayList();
        ArrayList<Module> modules = new ArrayList<Module>();
        modules.addAll(Client.moduleManager.getModulesForCategory(this.category));
        for (Module module : modules) {
            this.modulesPanels.add(new ModulesPanels(module));
            System.out.println(this.modulesPanels.size());
        }
        this.translate.setX(0.0f);
        this.translate.setY(0.0f);
        this.translate_.setX(0.0f);
        this.translate_.setY(0.0f);
    }

    public boolean isShow() {
        return this.show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /*
     * WARNING - void declaration
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.hovered = ClickGUIUtils.isHovered(mouseX, mouseY, this.x, this.y, this.width, this.height);
        this.width = this.show ? 89 : 85;
        if (this.Move && ClickGUI.categoryName.equalsIgnoreCase(this.category.name())) {
            this.x = this.tempX + mouseX;
            this.y = this.tempY + mouseY;
        }
        int color = new Color(25, 25, 25).getRGB();
        ClickGUIUtils.drawRect(this.x, this.y, this.width, this.height, this.hovered ? new Color(29, 29, 29).getRGB() : color);
        if (ClickGui.useFont.getEnable()) {
            Client.cFontRenderer.drawString(this.category.name(), this.x + 5, this.y + this.height / 2 - 3, Color.WHITE.getRGB());
        } else {
            FontUtil.drawString(this.category.name(), this.x + 5, this.y + this.height / 2 - 4, Color.WHITE.getRGB());
        }
        if (this.open) {
            int var7_11 = 0;
            int modulePanelY = 0;
            for (ModulesPanels modulesPanels : this.modulesPanels) {
                if (modulesPanels.showSetting) {
                    modulePanelY += modulesPanels.getSettingY();
                }
                for (SettingPanel it : modulesPanels.settingPanelList) {
                    if (!it.isModedr() || !modulesPanels.showSetting) continue;
                    modulePanelY += it.getModeY();
                }
                modulePanelY += 16;
            }
            if (ClickGui.Element.getCurrent() != ClickGui.Element.getMax()) {
                CategorysPanels.startGlScissor(this.x, this.y + this.height + 14, this.width, (int)ClickGui.Element.getCurrent() * 16 - 14);
            } else {
                CategorysPanels.startGlScissor(this.x, this.y + this.height + 14, this.width, modulePanelY);
            }
            modulePanelY = ClickGui.Element.getCurrent() != ClickGui.Element.getMax() ? this.y + this.height + (int)this.translate.getY() : this.y + this.height;
            int rainbowTickc = 0;
            for (ModulesPanels modulesPanels : this.modulesPanels) {
                if (++rainbowTickc > 100) {
                    rainbowTickc = 0;
                }
                Color rainbow = new Color(Color.HSBtoRGB((float)((double)Minecraft.getMinecraft().thePlayer.ticksExisted / ClickGui.rainBowSpeed.getCurrent() - Math.sin((double)rainbowTickc / 50.0 * 1.4)) % 1.0f, 0.5f, 1.0f));
                modulesPanels.drawScreen(mouseX, mouseY, partialTicks, this.x, modulePanelY, this.width, 16, modulePanelY + this.wheel, this.y, modulePanelY - this.y - 16, ClickGui.rainBow.getEnable() ? rainbow : new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()));
                if (modulesPanels.showSetting) {
                    modulePanelY += modulesPanels.getSettingY();
                }
                for (SettingPanel it : modulesPanels.settingPanelList) {
                    if (!it.isModedr() || !modulesPanels.showSetting) continue;
                    modulePanelY += it.getModeY();
                }
                modulePanelY += 16;
            }
            CategorysPanels.stopGlScissor();
            boolean bl = false;
            for (ModulesPanels modulesPanels : this.modulesPanels) {
                var7_11 += 16;
                if (modulesPanels.showSetting) {
                    var7_11 += modulesPanels.getSettingY();
                }
                for (SettingPanel it : modulesPanels.settingPanelList) {
                    if (!it.isModedr() || !modulesPanels.showSetting) continue;
                    var7_11 += it.getModeY();
                }
            }
            this.show = false;
            int n = (int)(ClickGui.Element.getCurrent() / (double)(var7_11 / 16) * (ClickGui.Element.getCurrent() * 16.0));
            if (var7_11 > (int)ClickGui.Element.getCurrent() * 16 && ClickGui.Element.getCurrent() != ClickGui.Element.getMax()) {
                this.show = true;
                ClickGUIUtils.drawRect(this.x + this.width - 2, this.y + 19 - (int)this.translate.getY() * (int)ClickGui.Element.getCurrent() / (var7_11 / 16), 2, n, new Color(77, 77, 77, 255).getRGB());
            }
            if (ClickGui.Element.getCurrent() != ClickGui.Element.getMax() && ClickGUIUtils.isHovered(mouseX, mouseY, this.x, this.y + 16, this.width - 4, (int)ClickGui.Element.getCurrent() * 16) && this.open && Mouse.hasWheel()) {
                int wheel_ = Mouse.getDWheel();
                if (wheel_ < 0) {
                    if (this.wheel < (int)ClickGui.Element.getCurrent() * 16 - var7_11 + 16) {
                        return;
                    }
                    this.wheel -= 16;
                } else if (wheel_ > 0) {
                    if (this.wheel > -16) {
                        return;
                    }
                    this.wheel += 16;
                }
            }
            if (ClickGui.Element.getCurrent() != ClickGui.Element.getMax()) {
                this.translate.interpolate(0.0f, this.wheel, 0.15f);
            }
        }
    }

    public static void startGlScissor(int x, int y, int width, int height) {
        int scaleFactor = new ScaledResolution(Minecraft.getMinecraft()).getScaleFactor();
        GL11.glPushMatrix();
        GL11.glEnable((int)3089);
        GL11.glScissor((int)(x * scaleFactor), (int)(Minecraft.getMinecraft().displayHeight - (y + height) * scaleFactor), (int)(width * scaleFactor), (int)((height += 14) * scaleFactor));
    }

    public static void stopGlScissor() {
        GL11.glDisable((int)3089);
        GL11.glPopMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.hovered && mouseButton == 0) {
            ClickGUI.categoryName = this.category.name();
            this.Move = true;
            this.tempX = this.x - mouseX;
            this.tempY = this.y - mouseY;
        }
        if (this.hovered && mouseButton == 1) {
            ClickGUI.categoryNames = this.category.name();
            this.open = !this.open;
        } else if (mouseButton == 1) {
            boolean bl = false;
        }
        for (ModulesPanels modulesPanels : this.modulesPanels) {
            for (SettingPanel it : modulesPanels.settingPanelList) {
                it.mouseClicked1(mouseX, mouseY, mouseButton);
            }
        }
        for (ModulesPanels modulesPanels : this.modulesPanels) {
            modulesPanels.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.Move = false;
        }
        for (ModulesPanels modulesPanels : this.modulesPanels) {
            modulesPanels.mouseReleased(mouseX, mouseY, state);
        }
    }
}

