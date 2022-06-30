/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class WTAP
extends Module {
    public static double comboLasts;
    private static final HashMap<EntityPlayer, Long> newEnt;
    public static boolean comboing;
    public static boolean hitCoolDown;
    public static boolean alreadyHit;
    public static int hitTimeout;
    public static int hitsWaited;
    private EnableSetting onlyPlayers = new EnableSetting("Players Only", true);
    private IntegerSetting minActionTicks = new IntegerSetting("Min Delay: ", 5.0, 1.0, 100.0, 1);
    private IntegerSetting maxActionTicks = new IntegerSetting("Max Delay: ", 12.0, 1.0, 100.0, 1);
    private IntegerSetting minOnceEvery = new IntegerSetting("Min Hits:", 1.0, 1.0, 10.0, 1);
    private IntegerSetting maxOnceEvery = new IntegerSetting("Max Hits:", 1.0, 1.0, 10.0, 1);
    private IntegerSetting range = new IntegerSetting("Range", 3.0, 3.0, 6.0, 1);

    public WTAP() {
        super("WTAP", 0, Category.Movement, false);
        this.getSetting().add(this.onlyPlayers);
        this.getSetting().add(this.minActionTicks);
        this.getSetting().add(this.maxActionTicks);
        this.getSetting().add(this.minOnceEvery);
        this.getSetting().add(this.maxOnceEvery);
        this.getSetting().add(this.range);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (event.entity instanceof EntityPlayer && event.entity != WTAP.mc.thePlayer) {
            newEnt.put((EntityPlayer)event.entity, System.currentTimeMillis());
        }
    }

    public static boolean bot(Entity en) {
        if (!Tools.isPlayerInGame() || WTAP.mc.currentScreen != null) {
            return false;
        }
        if (!Tools.isHyp()) {
            return false;
        }
        if (!newEnt.isEmpty() && newEnt.containsKey(en)) {
            return true;
        }
        if (en.getName().startsWith("\u00a7c")) {
            return true;
        }
        String n = en.getDisplayName().getUnformattedText();
        if (n.contains("\u00a7")) {
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
    public void onTick(TickEvent.RenderTickEvent e) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (comboing) {
            if ((double)System.currentTimeMillis() >= comboLasts) {
                comboing = false;
                WTAP.finishCombo();
                return;
            }
            return;
        }
        if (WTAP.mc.objectMouseOver != null && WTAP.mc.objectMouseOver.entityHit instanceof Entity && Mouse.isButtonDown((int)0)) {
            Entity target = WTAP.mc.objectMouseOver.entityHit;
            if (target.isDead) {
                return;
            }
            if ((double)WTAP.mc.thePlayer.getDistanceToEntity(target) <= this.range.getCurrent()) {
                if (target.hurtResistantTime >= 10) {
                    if (this.onlyPlayers.getEnable() && !(target instanceof EntityPlayer)) {
                        return;
                    }
                    if (WTAP.bot(target)) {
                        return;
                    }
                    if (hitCoolDown && !alreadyHit) {
                        if (++hitsWaited >= hitTimeout) {
                            hitCoolDown = false;
                            hitsWaited = 0;
                        } else {
                            alreadyHit = true;
                            return;
                        }
                    }
                    if (!alreadyHit) {
                        hitTimeout = this.minOnceEvery.getCurrent() == this.maxOnceEvery.getCurrent() ? (int)this.minOnceEvery.getCurrent() : ThreadLocalRandom.current().nextInt((int)this.minOnceEvery.getCurrent(), (int)this.maxOnceEvery.getCurrent());
                        hitCoolDown = true;
                        hitsWaited = 0;
                        comboLasts = ThreadLocalRandom.current().nextDouble(this.minActionTicks.getCurrent(), this.maxActionTicks.getCurrent() + 0.01) + (double)System.currentTimeMillis();
                        comboing = true;
                        WTAP.startCombo();
                        alreadyHit = true;
                    }
                } else {
                    if (alreadyHit) {
                        // empty if block
                    }
                    alreadyHit = false;
                }
            }
        }
    }

    private static void finishCombo() {
        if (Keyboard.isKeyDown((int)WTAP.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTAP.mc.gameSettings.keyBindForward.getKeyCode(), true);
        }
    }

    private static void startCombo() {
        if (Keyboard.isKeyDown((int)WTAP.mc.gameSettings.keyBindForward.getKeyCode())) {
            KeyBinding.setKeyBindState(WTAP.mc.gameSettings.keyBindForward.getKeyCode(), false);
            KeyBinding.onTick(WTAP.mc.gameSettings.keyBindForward.getKeyCode());
        }
    }

    static {
        newEnt = new HashMap();
    }
}

