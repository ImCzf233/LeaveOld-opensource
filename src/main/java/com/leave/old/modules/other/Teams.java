/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.other;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.settings.ModeSetting;
import java.util.Arrays;

public class Teams
extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "ArmorColor", Arrays.asList("Base", "ArmorColor", "NameColor", "TabList"), this);

    public Teams() {
        super("Teams", 0, Category.Other, false);
        this.getSetting().add(this.mode);
    }
}

