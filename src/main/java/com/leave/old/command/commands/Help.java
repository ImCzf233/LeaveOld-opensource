/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.command.commands;

import com.leave.old.Client;
import com.leave.old.command.Command;
import net.minecraft.util.EnumChatFormatting;

public class Help
extends Command {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDesc() {
        return "Gives you the syntax of all commands and what they do.";
    }

    @Override
    public String getSyntax() {
        return ".help";
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            for (Command c : Client.commandManager.getCommands()) {
                Help.msg(c.getSyntax() + " " + (Object)((Object)EnumChatFormatting.AQUA) + "- " + c.getDesc());
            }
        }
    }
}

