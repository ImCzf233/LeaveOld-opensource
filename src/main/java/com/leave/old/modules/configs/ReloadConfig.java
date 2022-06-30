/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.configs;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.config.configs.ClickGuiConfig;
import com.leave.old.config.configs.EnableConfig;
import com.leave.old.config.configs.IntegerConfig;
import com.leave.old.config.configs.KeyBindConfig;
import com.leave.old.config.configs.ModeConfig;
import com.leave.old.config.configs.ModuleConfig;
import com.leave.old.modules.Module;

public class ReloadConfig
extends Module {
    public ReloadConfig() {
        super("ReloadConfig", 0, Category.Configs, false);
    }

    @Override
    public void enable() {
        super.enable();
        this.toggle();
        try {
            IntegerConfig.loadState();
            EnableConfig.loadState();
            ModeConfig.loadState();
            KeyBindConfig.loadKey();
            ModuleConfig.loadModules();
            ClickGuiConfig.loadClickGui();
            for (Module m : Client.moduleManager.getModules()) {
                m.isSilder(null);
                m.setMode(null);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

