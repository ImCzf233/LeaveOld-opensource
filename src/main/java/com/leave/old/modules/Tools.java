/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.java.games.input.Mouse
 */
package com.leave.old.modules;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import net.java.games.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

public class Tools {
    private static Field timerField = null;
    private static Field mouseButton = null;
    private static Field mouseButtonState = null;
    private static Field mouseButtons = null;
    private static Minecraft mc = Minecraft.getMinecraft();

    public static String getLocalPath() {
        return System.getProperty("user.dir");
    }

    public static void su() {
        try {
            timerField = Minecraft.class.getDeclaredField("field_71428_T");
        }
        catch (Exception var4) {
            try {
                timerField = Minecraft.class.getDeclaredField("timer");
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        if (timerField != null) {
            timerField.setAccessible(true);
        }
        try {
            mouseButton = MouseEvent.class.getDeclaredField("button");
            mouseButtonState = MouseEvent.class.getDeclaredField("buttonstate");
            mouseButtons = Mouse.class.getDeclaredField("buttons");
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static boolean nullCheck() {
        return Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null;
    }

    public static void setMouseButtonState(int mouseButton, boolean held) {
        if (Tools.mouseButton != null && mouseButtonState != null && mouseButtons != null) {
            MouseEvent m = new MouseEvent();
            try {
                Tools.mouseButton.setAccessible(true);
                Tools.mouseButton.set(m, mouseButton);
                mouseButtonState.setAccessible(true);
                mouseButtonState.set(m, held);
                MinecraftForge.EVENT_BUS.post(m);
                mouseButtons.setAccessible(true);
                ByteBuffer bf = (ByteBuffer)mouseButtons.get(null);
                mouseButtons.setAccessible(false);
                bf.put(mouseButton, (byte)(held ? 1 : 0));
            }
            catch (IllegalAccessException illegalAccessException) {
                // empty catch block
            }
        }
    }

    public static boolean isPlayerHoldingWeapon() {
        if (Tools.mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        }
        Item item = Tools.mc.thePlayer.getCurrentEquippedItem().getItem();
        return item instanceof ItemSword || item instanceof ItemAxe;
    }

    public static String getConfigPath() {
        return Tools.getLocalPath() + "\\Leave";
    }

    public static boolean isHyp() {
        if (!Tools.isPlayerInGame()) {
            return false;
        }
        try {
            return !mc.isSingleplayer() && Tools.mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel.net");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }

    public static boolean isSingleplayer() {
        return mc.isSingleplayer();
    }

    public static boolean isHycraft() {
        if (!Tools.isPlayerInGame()) {
            return false;
        }
        try {
            return !mc.isSingleplayer() && Tools.mc.getCurrentServerData().serverIP.toLowerCase().contains("hycraft.com");
        }
        catch (Exception welpBruh) {
            welpBruh.printStackTrace();
            return false;
        }
    }

    public static String getFontPath() {
        return Tools.getLocalPath() + "\\Fair\\Fonts";
    }

    public static String getLogoPath() {
        return Tools.getLocalPath() + "\\Fair\\Logos";
    }

    public static boolean isPlayerInGame() {
        return Tools.mc.thePlayer != null && Tools.mc.theWorld != null;
    }

    public static int getCurrentPlayerSlot() {
        return Tools.mc.thePlayer.inventory.currentItem;
    }

    public static void hotkeyToSlot(int slot) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        Tools.mc.thePlayer.inventory.currentItem = slot;
    }

    public static boolean currentScreenMinecraft() {
        return Tools.mc.currentScreen == null;
    }

    public static boolean isMoving() {
        if (!Tools.mc.thePlayer.isCollidedHorizontally && !Tools.mc.thePlayer.isSneaking()) {
            return Tools.mc.thePlayer.movementInput.moveForward != 0.0f || Tools.mc.thePlayer.movementInput.moveStrafe != 0.0f;
        }
        return false;
    }

    public static boolean isBlocking() {
        if (Tools.mc.thePlayer.isUsingItem()) {
            return true;
        }
        return Tools.mc.thePlayer.isBlocking();
    }

    public static boolean isOnGround(double height) {
        return !Tools.mc.theWorld.getCollidingBoundingBoxes(Tools.mc.thePlayer, Tools.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }
}

