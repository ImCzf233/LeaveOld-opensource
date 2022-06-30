/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.settings;

import com.leave.old.Setting;
import com.leave.old.modules.Module;
import java.util.List;

public class ModeSetting
extends Setting {
    private Module parent;
    private String current;
    private final List<String> modes;

    public ModeSetting(String name, String current, List<String> modes, Module parent) {
        super(name);
        this.current = current;
        this.modes = modes;
        this.parent = parent;
    }

    public Module getParent() {
        return this.parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public List<String> getModes() {
        return this.modes;
    }

    public int getCurrentIndex() {
        int index = 0;
        for (String mode : this.modes) {
            if (mode.equalsIgnoreCase(this.current)) {
                return index;
            }
            ++index;
        }
        return 0;
    }
}

