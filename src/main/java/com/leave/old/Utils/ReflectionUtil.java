/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.Client;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ReflectionUtil {
    public static final Field delayTimer = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");
    public static final Field running = ReflectionHelper.findField(Minecraft.class, "field_71425_J", "running");
    public static final Field pressed = ReflectionHelper.findField(KeyBinding.class, "field_74513_e", "pressed");
    public static final Field theShaderGroup = ReflectionHelper.findField(EntityRenderer.class, "field_147707_d", "theShaderGroup");
    public static final Field listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");

    public static void rightClickMouse() {
        try {
            String s = !Client.isObfuscate ? "rightClickMouse" : "func_147121_ag";
            Minecraft mc = Minecraft.getMinecraft();
            Class<?> c = mc.getClass();
            Method m = c.getDeclaredMethod(s, new Class[0]);
            m.setAccessible(true);
            m.invoke(mc, new Object[0]);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    static {
        delayTimer.setAccessible(true);
        running.setAccessible(true);
        pressed.setAccessible(true);
        theShaderGroup.setAccessible(true);
        listShaders.setAccessible(true);
    }
}

