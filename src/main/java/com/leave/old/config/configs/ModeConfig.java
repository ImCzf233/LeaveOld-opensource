/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.config.configs;

import com.leave.old.Client;
import com.leave.old.Setting;
import com.leave.old.config.ConfigManager;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.ModeSetting;

public class ModeConfig {
    private static ConfigManager configManager = new ConfigManager(Tools.getConfigPath(), "Mode.txt");

    public static void saveState() {
        try {
            configManager.clear();
            for (Module module : Client.moduleManager.getModules()) {
                if (module.getSettings().isEmpty()) continue;
                for (Setting setting : module.getSettings()) {
                    if (!(setting instanceof ModeSetting)) continue;
                    String line = setting.getName() + ":" + module.getName() + ":" + ((ModeSetting)setting).getCurrent();
                    configManager.write(line);
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void loadState() {
        try {
            for (String s : configManager.read()) {
                for (Module module : Client.moduleManager.getModules()) {
                    if (module.getSettings().isEmpty()) continue;
                    for (Setting setting : module.getSettings()) {
                        String name = s.split(":")[0];
                        String mod = s.split(":")[1];
                        String value = s.split(":")[2];
                        if (!setting.getName().equalsIgnoreCase(name) || !((ModeSetting)setting).getParent().getName().equalsIgnoreCase(mod)) continue;
                        ((ModeSetting)setting).setCurrent(value);
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

