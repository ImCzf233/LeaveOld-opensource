/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.other;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.ChatUtils;
import com.leave.old.modules.Module;
import net.minecraftforge.common.MinecraftForge;

public class SelfDestruct
extends Module {
    public SelfDestruct() {
        super("SelfDestruct", 0, Category.Other, false);
    }

    @Override
    public void enable() {
        ChatUtils.message("Destructed");
        mc.displayGuiScreen(null);
        for (Module m : Client.moduleManager.getModules()) {
            m.setState(false);
        }
        MinecraftForge.EVENT_BUS.unregister(Client.instance);
        Client.state = false;
        super.enable();
    }
}

