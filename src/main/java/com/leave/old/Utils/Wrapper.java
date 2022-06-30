/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.Utils.ReflectUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Timer;

public class Wrapper {
    public static Wrapper INSTANCE = new Wrapper();

    public static Timer getTimer(Minecraft mc) {
        return (Timer)ReflectUtil.getField("timer", "field_71428_T", mc);
    }

    public static double getRenderPosX() {
        return (Double)ReflectUtil.getField("renderPosX", "field_78725_b", Minecraft.getMinecraft().getRenderManager());
    }

    public static double getRenderPosY() {
        return (Double)ReflectUtil.getField("renderPosY", "field_78726_c", Minecraft.getMinecraft().getRenderManager());
    }

    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static double getRenderPosZ() {
        return (Double)ReflectUtil.getField("renderPosZ", "field_78723_d", Minecraft.getMinecraft().getRenderManager());
    }

    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    public WorldClient world() {
        return Wrapper.INSTANCE.mc().theWorld;
    }

    public static void orientCamera(float pass) {
        ReflectUtil.invoke(Minecraft.getMinecraft().entityRenderer, "orientCamera", "func_78467_g", new Class[]{Float.TYPE}, new Object[]{Float.valueOf(pass)});
    }

    public static void clickMouse() {
        ReflectUtil.invoke(Minecraft.getMinecraft(), "clickMouse", "func_147116_af", new Class[0], new Object[0]);
    }
}

