/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.configs;

import com.leave.old.Category;
import com.leave.old.config.configs.ClickGuiConfig;
import com.leave.old.config.configs.EnableConfig;
import com.leave.old.config.configs.IntegerConfig;
import com.leave.old.config.configs.KeyBindConfig;
import com.leave.old.config.configs.ModeConfig;
import com.leave.old.config.configs.ModuleConfig;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import java.io.File;

public class SaveConfig
extends Module {
    private boolean kp;

    public SaveConfig() {
        super("SaveConfig", 0, Category.Configs, false);
    }

    @Override
    public void enable() {
        super.enable();
        this.kp = true;
        this.toggle();
        try {
            File ConfigDir = new File(Tools.getConfigPath());
            if (!ConfigDir.exists()) {
                ConfigDir.mkdir();
            }
            EnableConfig.saveState();
            IntegerConfig.saveState();
            KeyBindConfig.saveKey();
            ModeConfig.saveState();
            ModuleConfig.saveModules();
            ClickGuiConfig.saveClickGui();
        }
        catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
}

