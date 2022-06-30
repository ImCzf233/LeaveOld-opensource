/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.command;

import com.leave.old.Utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public abstract class Command {
    public abstract void execute(String[] var1);

    public abstract String getName();

    public abstract String getSyntax();

    public abstract String getDesc();

    public static void msg(String msg) {
        ChatUtils.report(msg);
    }

    public void normal(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }

    public String getCmd() {
        return this.getName();
    }

    public String getName1() {
        return this.getName();
    }

    public String getHelp() {
        return null;
    }
}

