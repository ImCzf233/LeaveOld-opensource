/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old;

import com.leave.old.Client;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="Client", name="Client", version="1.0.0", acceptedMinecraftVersions="[1.8.9]")
public class ForgeMod {
    @Mod.EventHandler
    public void Mod(FMLPreInitializationEvent event) {
        new Client();
    }
}

