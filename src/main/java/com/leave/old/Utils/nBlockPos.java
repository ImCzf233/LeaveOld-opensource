/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import net.minecraft.util.BlockPos;

public class nBlockPos
extends BlockPos {
    private int x;
    private int y;
    private int z;

    public nBlockPos() {
        super(0, 0, 0);
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getZ() {
        return this.z;
    }
}

