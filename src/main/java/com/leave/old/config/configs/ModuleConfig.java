/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.config.configs;

import com.leave.old.Client;
import com.leave.old.config.ConfigManager;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;

public class ModuleConfig {
    private static ConfigManager configManager = new ConfigManager(Tools.getConfigPath(), "modules.txt");

    public static void loadModules() {
        try {
            for (String s : configManager.read()) {
                for (Module module : Client.moduleManager.getModules()) {
                    String name = s.split(":")[0];
                    boolean toggled = Boolean.parseBoolean(s.split(":")[1]);
                    if (!module.getName().equalsIgnoreCase(name) || module.getName() == "ClickGui") continue;
                    module.setState(toggled);
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void saveModules() {
        try {
            configManager.clear();
            for (Module module : Client.moduleManager.getModules()) {
                if (module.getName() == "ClickGUI") continue;
                String line = module.getName() + ":" + String.valueOf(module.getState());
                configManager.write(line);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

