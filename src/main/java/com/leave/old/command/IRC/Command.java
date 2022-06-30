/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.command.IRC;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.Connection;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import java.lang.reflect.Field;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Command
extends Module {
    public TimerUtils timerUtils = new TimerUtils();

    public Command() {
        super("Command", 0, Category.Other, true);
    }

    @Override
    public void enable() {
        super.enable();
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        boolean send = true;
        if (side == Connection.Side.OUT && packet instanceof C01PacketChatMessage) {
            Field field = ReflectionHelper.findField(C01PacketChatMessage.class, "message", "field_149440_a");
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (packet instanceof C01PacketChatMessage) {
                    C01PacketChatMessage p = (C01PacketChatMessage)packet;
                    if (p.getMessage().subSequence(0, 1).equals(".")) {
                        send = false;
                        Client.commandManager.execute(p.getMessage());
                        return send;
                    }
                    send = true;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return send;
    }
}

