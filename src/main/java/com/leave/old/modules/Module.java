/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Notifications.Notification;
import com.leave.old.Setting;
import com.leave.old.Utils.Connection;
import com.leave.old.event.EventPacket;
import com.leave.old.eventapi.EventManager;
import com.leave.old.modules.render.Hud;
import com.leave.old.settings.ModeSetting;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Module {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public boolean state;
    public int key;
    public String name;
    public Category category;
    public ArrayList<Setting> setting;

    public Module(String name, int key, Category category, boolean toggle) {
        this.name = name;
        this.key = key;
        this.category = category;
        this.setting = new ArrayList();
        if (toggle) {
            this.setState(true);
        }
    }

    public boolean onPacket(Object packet, Connection.Side side) {
        return true;
    }

    public boolean isToggledMode(String modeName) {
        for (Setting value : this.setting) {
            if (!(value instanceof ModeSetting)) continue;
            ModeSetting modeValue = (ModeSetting)value;
            for (String name : modeValue.getModes()) {
                if (!name.equalsIgnoreCase(modeName) || modeValue.getCurrent() != name) continue;
                return true;
            }
        }
        return false;
    }

    public void onCheck(Setting setting) {
    }

    public void setMode(Setting setting) {
    }

    public void onPacketEvent(EventPacket event) {
    }

    public ArrayList<Setting> getSettings() {
        return this.setting;
    }

    public void isSilder(Setting setting) {
    }

    public List<Setting> getSetting() {
        return this.setting;
    }

    public void toggle() {
        this.setState(!this.state);
    }

    public void setState(boolean state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (state) {
            EventManager.register(this);
            MinecraftForge.EVENT_BUS.register(this);
            FMLCommonHandler.instance().bus().register(this);
            this.enable();
            if (!Hud.notification.getEnable()) {
                return;
            }
            if (this.getName().equalsIgnoreCase("ClickGUI")) {
                Client.inited = true;
                return;
            }
            if (this.getName().equalsIgnoreCase("SaveConfig")) {
                Client.notificationManager.add(new Notification("Config Save!!!", Notification.Type.NONE, 0));
                return;
            }
            if (this.getName().equalsIgnoreCase("ReloadConfig")) {
                Client.notificationManager.add(new Notification("Config Loaded!!!", Notification.Type.NONE, 0));
                return;
            }
            if (Client.inited) {
                Client.notificationManager.add(new Notification(this.getName(), Notification.Type.Enable, 0));
            }
        } else {
            EventManager.unregister(this);
            MinecraftForge.EVENT_BUS.unregister(this);
            FMLCommonHandler.instance().bus().unregister(this);
            this.disable();
            if (!Hud.notification.getEnable()) {
                return;
            }
            if (this.getName().equalsIgnoreCase("ClickGUI") || this.getName().equalsIgnoreCase("SaveConfig") || this.getName().equalsIgnoreCase("ReloadConfig")) {
                return;
            }
            if (Client.inited) {
                Client.notificationManager.add(new Notification(this.getName(), Notification.Type.Disable, 0));
            }
        }
    }

    public void enable() {
    }

    public void disable() {
    }

    public String getName() {
        return this.name;
    }

    public int getKey() {
        return this.key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getState() {
        return this.state;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

