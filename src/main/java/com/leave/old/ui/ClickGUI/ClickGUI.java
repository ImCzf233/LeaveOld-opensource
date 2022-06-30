/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.ui.ClickGUI;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.ReflectionUtil;
import com.leave.old.config.configs.ClickGuiConfig;
import com.leave.old.modules.Module;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.ui.ClickGUI.CategorysPanels;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class ClickGUI
extends GuiScreen {
    public static ArrayList<CategorysPanels> categorysPanels;
    private boolean Move;
    private boolean hovered;
    private int x;
    private int y;
    private int tempX;
    private int tempY;
    public static String categoryName;
    public static String categoryNames;
    private int rainbowTickc = 0;

    public ClickGUI() {
        categorysPanels = new ArrayList();
        int CategoryY = 10;
        double scale = ClickGui.Scale.getCurrent();
        for (Category category : Category.values()) {
            categorysPanels.add(new CategorysPanels(category, 5, CategoryY, 85, 20));
            CategoryY += 30;
        }
    }

    public int getMaxModule() {
        Minecraft mc = Minecraft.getMinecraft();
        ArrayList<Module> modules = Client.moduleManager.getModules();
        modules.sort((o1, o2) -> mc.fontRendererObj.getStringWidth(o2.getName()) - mc.fontRendererObj.getStringWidth(o1.getName()));
        return mc.fontRendererObj.getStringWidth(modules.get(0).getName());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            ClickGuiConfig.saveClickGui();
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    public void initGui() {
        if (ClickGui.blur.getEnable()) {
            try {
                Method m = EntityRenderer.class.getDeclaredMethod(Client.isObfuscate ? "func_175069_a" : "loadShader", ResourceLocation.class);
                m.setAccessible(true);
                m.invoke(this.mc.entityRenderer, new ResourceLocation("shaders/post/blur.json"));
                m.setAccessible(false);
            }
            catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGuiClosed() {
        if (this.mc.entityRenderer.getShaderGroup() != null) {
            this.mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            try {
                ReflectionUtil.theShaderGroup.set(Minecraft.getMinecraft().entityRenderer, null);
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        ClickGuiConfig.saveClickGui();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
            categorysPanels.drawScreen(mouseX, mouseY, partialTicks);
        }
        GL11.glScaled((double)1.0, (double)1.0, (double)1.0);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hovered && mouseButton == 0) {
            this.Move = true;
            this.tempX = this.x - mouseX;
            this.tempY = this.y - mouseY;
        }
        for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
            categorysPanels.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (state == 0) {
            this.Move = false;
        }
        for (CategorysPanels categorysPanels : ClickGUI.categorysPanels) {
            categorysPanels.mouseReleased(mouseX, mouseY, state);
        }
    }
}

