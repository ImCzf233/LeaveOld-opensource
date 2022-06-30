/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Utils.ColorUtils;
import com.leave.old.Utils.HUDUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.ModeSetting;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ESP
extends Module {
    private static final HashMap<EntityPlayer, Long> newEnt = new HashMap();
    private final ModeSetting Mode = new ModeSetting("ESPMode", "2D", Arrays.asList("2D", "Arrow", "Box", "Health", "Ring", "Shaded"), this);
    private EnableSetting Players = new EnableSetting("Players", true);
    private EnableSetting Mobs = new EnableSetting("Mobs", false);
    private EnableSetting Animals = new EnableSetting("Animals", false);
    private EnableSetting Inv = new EnableSetting("Invisible", false);

    public ESP() {
        super("EntityESP", 0, Category.Render, false);
        this.getSetting().add(this.Mode);
        this.getSetting().add(this.Players);
        this.getSetting().add(this.Mobs);
        this.getSetting().add(this.Animals);
        this.getSetting().add(this.Inv);
    }

    public static boolean isHyp() {
        if (!Tools.isPlayerInGame()) {
            return false;
        }
        try {
            return !mc.isSingleplayer() && ESP.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }

    public static boolean bot(Entity en) {
        if (!Tools.isPlayerInGame() || ESP.mc.currentScreen != null) {
            return false;
        }
        if (!ESP.isHyp()) {
            return false;
        }
        if (!newEnt.isEmpty() && newEnt.containsKey(en)) {
            return true;
        }
        if (en.getName().startsWith("\ufffd\ufffdc")) {
            return true;
        }
        String n = en.getDisplayName().getUnformattedText();
        if (n.contains("\ufffd\ufffd")) {
            return n.contains("[NPC] ");
        }
        if (n.isEmpty() && en.getName().isEmpty()) {
            return true;
        }
        if (n.length() == 10) {
            char[] var4;
            int num = 0;
            int let = 0;
            for (char c : var4 = n.toCharArray()) {
                if (Character.isLetter(c)) {
                    if (Character.isUpperCase(c)) {
                        return false;
                    }
                    ++let;
                    continue;
                }
                if (!Character.isDigit(c)) {
                    return false;
                }
                ++num;
            }
            return num >= 2 && let >= 2;
        }
        return false;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        int rgb = ColorUtils.rainbow().getRGB();
        Iterator var3 = ESP.mc.theWorld.loadedEntityList.iterator();
        while (var3.hasNext()) {
            Entity en = (Entity)var3.next();
            if (en == ESP.mc.thePlayer || en.isDead || !this.Inv.getEnable() && en.isInvisible() || ESP.bot(en)) continue;
            if (en instanceof EntityPlayer && this.Players.getEnable()) {
                if (this.Mode.getCurrent() == "2D") {
                    HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Arrow") {
                    HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Box") {
                    HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
                }
                if (this.Mode.getCurrent() == "Health") {
                    HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Ring") {
                    HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Shaded") {
                    HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
                }
            }
            if (en instanceof EntityAnimal && this.Animals.getEnable()) {
                if (this.Mode.getCurrent() == "2D") {
                    HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Arrow") {
                    HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Box") {
                    HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
                }
                if (this.Mode.getCurrent() == "Health") {
                    HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Ring") {
                    HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
                }
                if (this.Mode.getCurrent() == "Shaded") {
                    HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
                }
            }
            if (!(en instanceof EntityMob) || !this.Mobs.getEnable()) continue;
            if (this.Mode.getCurrent() == "2D") {
                HUDUtils.ee(en, 3, 0.0, 1.0, rgb, true);
            }
            if (this.Mode.getCurrent() == "Arrow") {
                HUDUtils.ee(en, 5, 0.0, 1.0, rgb, true);
            }
            if (this.Mode.getCurrent() == "Box") {
                HUDUtils.ee(en, 1, 0.0, 1.0, rgb, false);
            }
            if (this.Mode.getCurrent() == "Health") {
                HUDUtils.ee(en, 4, 0.0, 1.0, rgb, true);
            }
            if (this.Mode.getCurrent() == "Ring") {
                HUDUtils.ee(en, 6, 0.0, 1.0, rgb, true);
            }
            if (this.Mode.getCurrent() != "Shaded") continue;
            HUDUtils.ee(en, 2, 0.0, 1.0, rgb, true);
        }
        return;
    }
}

