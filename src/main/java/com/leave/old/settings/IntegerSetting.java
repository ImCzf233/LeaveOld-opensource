/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.settings;

import com.leave.old.Setting;
import com.leave.old.modules.Module;

public class IntegerSetting
extends Setting {
    private final double min;
    private final double max;
    private double current;
    private Module parent;
    private int dou;

    public IntegerSetting(String name, double current, double min, double max, int dou) {
        super(name);
        this.current = current;
        this.min = min;
        this.max = max;
        this.dou = dou;
    }

    public int getDou() {
        return this.dou;
    }

    public void setDou(int dou) {
        this.dou = dou;
    }

    public double getCurrent() {
        return this.current;
    }

    @Override
    public Module getParentMod() {
        return this.parent;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }
}

