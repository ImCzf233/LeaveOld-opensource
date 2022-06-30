/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Setting;
import com.leave.old.command.IRC.Command;
import com.leave.old.event.EventPacket;
import com.leave.old.modules.Module;
import com.leave.old.modules.combat.AimAssist;
import com.leave.old.modules.combat.AutoClicker;
import com.leave.old.modules.combat.ChestStealer;
import com.leave.old.modules.combat.HitBox;
import com.leave.old.modules.combat.Killaura;
import com.leave.old.modules.combat.Reach;
import com.leave.old.modules.combat.Regen;
import com.leave.old.modules.combat.SuperKnockBack;
import com.leave.old.modules.combat.Velocity;
import com.leave.old.modules.configs.ReloadConfig;
import com.leave.old.modules.configs.SaveConfig;
import com.leave.old.modules.movement.AutoJump;
import com.leave.old.modules.movement.Fly;
import com.leave.old.modules.movement.HighJump;
import com.leave.old.modules.movement.InvMove;
import com.leave.old.modules.movement.KeepSprint;
import com.leave.old.modules.movement.NoSlowDown;
import com.leave.old.modules.movement.Speed;
import com.leave.old.modules.movement.Sprint;
import com.leave.old.modules.movement.WTAP;
import com.leave.old.modules.other.AntiAFK;
import com.leave.old.modules.other.Kick;
import com.leave.old.modules.other.SafeWalk;
import com.leave.old.modules.other.SelfDestruct;
import com.leave.old.modules.render.Chams;
import com.leave.old.modules.render.ChestESP;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.modules.render.ESP;
import com.leave.old.modules.render.FullBright;
import com.leave.old.modules.render.Hud;
import com.leave.old.modules.render.ItemESP;
import com.leave.old.modules.render.Nametags;
import com.leave.old.modules.render.NoBob;
import com.leave.old.modules.render.Projectiles;
import com.leave.old.modules.render.Tracers;
import com.leave.old.modules.render.Xray;
import com.leave.old.modules.world.AntiRain;
import com.leave.old.modules.world.AutoArmor;
import com.leave.old.modules.world.AutoSoup;
import com.leave.old.modules.world.AutoTool;
import com.leave.old.modules.world.FastPlace;
import com.leave.old.modules.world.FuckBed;
import com.leave.old.modules.world.InvCleaner;
import com.leave.old.modules.world.NoFall;
import com.leave.old.modules.world.Scaffold;
import com.leave.old.modules.world.Timer;
import com.leave.old.settings.ModeSetting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ModuleManager {
    static ArrayList<Module> list = new ArrayList();

    public ArrayList<Module> getModules() {
        return list;
    }

    public static void onPacketEvent(EventPacket event) {
        for (Module module : Client.moduleManager.getModules()) {
            module.onPacketEvent(event);
        }
    }

    public final List<String> getSettingByModule(String name) {
        ArrayList setting = new ArrayList();
        for (Module it : Client.moduleManager.getModules()) {
            Iterator<Setting> iterator;
            if (!it.getName().equalsIgnoreCase(name) || !(iterator = it.getSetting().iterator()).hasNext()) continue;
            Setting s = iterator.next();
            return ((ModeSetting)s).getModes();
        }
        return null;
    }

    public void regMods(Module ... mods) {
        Collections.addAll(this.getModules(), mods);
    }

    public final List<Module> getModulesForCategory(Category category) {
        ArrayList<Module> localModules = new ArrayList<Module>();
        ArrayList<Module> modules = list;
        int modulesSize = modules.size();
        for (int i = 0; i < modulesSize; ++i) {
            Module module = modules.get(i);
            if (module.getCategory() != category) continue;
            localModules.add(module);
        }
        return localModules;
    }

    public Module getModule(String name) {
        for (Module m : list) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    static {
        list.add(new Command());
        list.add(new AimAssist());
        list.add(new AutoClicker());
        list.add(new Killaura());
        list.add(new HitBox());
        list.add(new Sprint());
        list.add(new Speed());
        list.add(new Hud());
        list.add(new NoFall());
        list.add(new Reach());
        list.add(new ChestStealer());
        list.add(new SuperKnockBack());
        list.add(new Velocity());
        list.add(new Chams());
        list.add(new ChestESP());
        list.add(new FullBright());
        list.add(new ItemESP());
        list.add(new Nametags());
        list.add(new ClickGui());
        list.add(new Fly());
        list.add(new AntiRain());
        list.add(new AntiAFK());
        list.add(new AutoJump());
        list.add(new InvMove());
        list.add(new NoSlowDown());
        list.add(new KeepSprint());
        list.add(new SafeWalk());
        list.add(new AutoArmor());
        list.add(new HighJump());
        list.add(new AutoTool());
        list.add(new FastPlace());
        list.add(new FuckBed());
        list.add(new InvCleaner());
        list.add(new Scaffold());
        list.add(new Timer());
        list.add(new ESP());
        list.add(new NoBob());
        list.add(new Projectiles());
        list.add(new Tracers());
        list.add(new Xray());
        list.add(new SaveConfig());
        list.add(new ReloadConfig());
        list.add(new Regen());
        list.add(new WTAP());
        list.add(new AutoSoup());
        list.add(new Kick());
        list.add(new SelfDestruct());
    }
}

