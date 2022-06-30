/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.command;

import com.leave.old.command.Command;
import com.leave.old.command.commands.Bind;
import com.leave.old.command.commands.Help;
import com.leave.old.command.commands.Toggle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.util.EnumChatFormatting;

public class CommandManager {
    public NetHandlerPlayClient sendQueue;
    private static CommandManager me = new CommandManager();
    private List<Command> commands = new ArrayList<Command>();
    private String prefix = ".";

    public CommandManager() {
        this.add(new Bind());
        this.add(new Help());
        this.add(new Toggle());
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    public static CommandManager get() {
        return me;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public List<Command> getCommands() {
        return this.commands;
    }

    public boolean execute(String text) {
        if (!text.startsWith(this.prefix)) {
            return false;
        }
        text = text.substring(1);
        String[] arguments = text.split(" ");
        String ranCmd = arguments[0];
        for (Command cmd : this.commands) {
            if (!cmd.getName().equalsIgnoreCase(arguments[0])) continue;
            String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
            String[] args1 = text.split(" ");
            cmd.execute(args);
            return true;
        }
        Command.msg("The command" + (Object)((Object)EnumChatFormatting.AQUA) + ranCmd + (Object)((Object)EnumChatFormatting.GREEN) + " has not been found!");
        return false;
    }
}

