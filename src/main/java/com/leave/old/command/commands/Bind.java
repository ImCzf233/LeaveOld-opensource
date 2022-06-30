/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package com.leave.old.command.commands;

import com.leave.old.Client;
import com.leave.old.Utils.ChatUtils;
import com.leave.old.command.Command;
import com.leave.old.config.configs.KeyBindConfig;
import com.leave.old.modules.Module;
import org.lwjgl.input.Keyboard;

public class Bind
extends Command {
    @Override
    public void execute(String[] args) {
        try {
            if (args.length == 0) {
                Bind.msg(this.getAll());
            } else if (args.length == 2) {
                for (Module module : Client.moduleManager.getModules()) {
                    if (!module.getName().equalsIgnoreCase(args[0])) continue;
                    module.setKey(Keyboard.getKeyIndex((String)args[1].toUpperCase()));
                    KeyBindConfig.saveKey();
                    ChatUtils.message(module.getName() + " key changed to \u00a79" + Keyboard.getKeyName((int)module.getKey()));
                }
            }
        }
        catch (Exception e) {
            ChatUtils.error("Usage: " + this.getSyntax());
        }
    }

    @Override
    public String getName() {
        return "bind";
    }

    @Override
    public String getSyntax() {
        return ".bind <module> <key>";
    }

    @Override
    public String getDesc() {
        return "Sets binds for modules.";
    }

    public String getAll() {
        return this.getSyntax() + " - " + this.getDesc();
    }
}

