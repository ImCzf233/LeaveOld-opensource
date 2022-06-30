/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.config.configs;

import com.leave.old.config.ConfigManager;
import com.leave.old.modules.Tools;
import com.leave.old.ui.ClickGUI.CategorysPanels;
import com.leave.old.ui.ClickGUI.ClickGUI;

public class ClickGuiConfig {
    private static ConfigManager configManager = new ConfigManager(Tools.getConfigPath(), "clickgui.txt");

    public static void loadClickGui() {
        for (String s : configManager.read()) {
            String panelName = s.split(":")[0];
            float panelCoordX = Float.parseFloat(s.split(":")[1]);
            float panelCoordY = Float.parseFloat(s.split(":")[2]);
            boolean extended = Boolean.parseBoolean(s.split(":")[3]);
            for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
                if (!categorysPanels.category.name().equalsIgnoreCase(panelName)) continue;
                categorysPanels.setX((int)panelCoordX);
                categorysPanels.setY((int)panelCoordY);
                categorysPanels.setOpen(extended);
            }
        }
    }

    public static void saveClickGui() {
        try {
            configManager.clear();
            for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
                configManager.write(categorysPanels.category.name() + ":" + categorysPanels.getX() + ":" + categorysPanels.getY() + ":" + categorysPanels.isOpen());
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

