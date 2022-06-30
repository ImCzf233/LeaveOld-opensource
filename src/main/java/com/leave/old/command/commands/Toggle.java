/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.command.commands;

import com.leave.old.Client;
import com.leave.old.command.Command;
import com.leave.old.config.configs.ModuleConfig;
import com.leave.old.modules.Module;
import net.minecraft.util.EnumChatFormatting;

public class Toggle
extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            Toggle.msg(this.getAll());
        } else {
            String module = args[0];
            Module mod = Client.moduleManager.getModule(module);
            if (mod == null) {
                Toggle.msg((Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "The requested module was not found!");
            } else {
                Client.moduleManager.getModule(module).toggle();
                Toggle.msg(String.format((Object)((Object)EnumChatFormatting.DARK_AQUA) + "%s " + (Object)((Object)EnumChatFormatting.AQUA) + "has been %s", Client.moduleManager.getModule(module).getName(), Client.moduleManager.getModule(module).getState() ? (Object)((Object)EnumChatFormatting.GREEN) + "enabled" : (Object)((Object)EnumChatFormatting.LIGHT_PURPLE) + "disabled."));
                ModuleConfig.saveModules();
            }
        }
    }

    @Override
    public String getName() {
        return "t";
    }

    @Override
    public String getDesc() {
        return "Toggles modules.";
    }

    @Override
    public String getSyntax() {
        return ".t";
    }

    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
}

