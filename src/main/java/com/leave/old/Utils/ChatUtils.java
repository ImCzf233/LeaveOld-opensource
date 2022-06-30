/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class ChatUtils {
    public static void component(ChatComponentText component) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().ingameGUI.getChatGUI() == null) {
            return;
        }
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("").appendSibling(component));
    }

    public static void message(Object message) {
        ChatUtils.component(new ChatComponentText((Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "[LeaveOld]\u00a77" + message));
    }

    public static void report(String message) {
        ChatUtils.message((Object)((Object)EnumChatFormatting.GREEN) + message);
    }

    public static void warning(Object message) {
        ChatUtils.message("\u00a78[\u00a7eWARNING\u00a78]\u00a7e " + message);
    }

    public static void error(Object message) {
        ChatUtils.message("\u00a78[\u00a74ERROR\u00a78]\u00a7c " + message);
    }
}

