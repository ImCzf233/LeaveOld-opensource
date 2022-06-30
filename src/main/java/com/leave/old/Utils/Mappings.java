/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Mappings {
    public static String timer = Mappings.isMCP() ? "timer" : "field_71428_T";
    public static String anti = Mappings.isMCP() ? "MovementInput" : "field_71158_b";
    public static String isInWeb = Mappings.isMCP() ? "isInWeb" : "field_70134_J";
    public static String registerReloadListener = Mappings.isMCP() ? "registerReloadListener" : "func_110542_a";
    public static String session = Mappings.isNotObfuscated() ? "session" : "field_71449_j";
    public static String yaw = Mappings.isNotObfuscated() ? "yaw" : "field_149476_e";
    public static String pitch = Mappings.isNotObfuscated() ? "pitch" : "field_149473_f";
    public static String rightClickDelayTimer = Mappings.isNotObfuscated() ? "rightClickDelayTimer" : "field_71467_ac";
    public static String getPlayerInfo = Mappings.isNotObfuscated() ? "getPlayerInfo" : "func_175155_b";
    public static String playerTextures = Mappings.isNotObfuscated() ? "playerTextures" : "field_187107_a";
    public static String currentGameType = Mappings.isNotObfuscated() ? "currentGameType" : "field_78779_k";
    public static String connection = Mappings.isNotObfuscated() ? "connection" : "field_78774_b";
    public static String blockHitDelay = Mappings.isNotObfuscated() ? "blockHitDelay" : "field_78781_i";
    public static String curBlockDamageMP = Mappings.isNotObfuscated() ? "curBlockDamageMP" : "field_78770_f";
    public static String isHittingBlock = Mappings.isNotObfuscated() ? "isHittingBlock" : "field_78778_j";
    public static String onUpdateWalkingPlayer = Mappings.isNotObfuscated() ? "onUpdateWalkingPlayer" : "func_175161_p";
    public static final Field delayTimer = ReflectionHelper.findField(Minecraft.class, "field_71467_ac", "rightClickDelayTimer");
    public static final Field running = ReflectionHelper.findField(Minecraft.class, "field_71425_J", "running");
    public static final Field pressed = ReflectionHelper.findField(KeyBinding.class, "field_74513_e", "pressed");
    public static final Field theShaderGroup = ReflectionHelper.findField(EntityRenderer.class, "field_147707_d", "theShaderGroup");
    public static final Field listShaders = ReflectionHelper.findField(ShaderGroup.class, "field_148031_d", "listShaders");

    public static boolean isNotObfuscated() {
        try {
            return Minecraft.class.getDeclaredField("instance") != null;
        }
        catch (Exception ex) {
            return false;
        }
    }

    private static boolean isMCP() {
        try {
            return ReflectionHelper.findField(Minecraft.class, "theMinecraft") != null;
        }
        catch (Exception var1) {
            return false;
        }
    }
}

