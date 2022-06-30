/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.settings;

import com.leave.old.Setting;

public class EnableSetting
extends Setting {
    private boolean enable;

    public EnableSetting(String name, boolean enable) {
        super(name);
        this.enable = enable;
    }

    public boolean getEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

