/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.render;

import com.leave.old.Category;
import com.leave.old.Utils.HUDUtils;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.Utils.nBlockPos;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Xray
extends Module {
    public static EnableSetting spawner = new EnableSetting("Spawner", true);
    public static EnableSetting coal = new EnableSetting("Coal", true);
    public static EnableSetting iron = new EnableSetting("Iron", true);
    public static EnableSetting lapis = new EnableSetting("Lapis", true);
    public static EnableSetting emerald = new EnableSetting("Emerald", true);
    public static EnableSetting redstone = new EnableSetting("Redstone", true);
    public static EnableSetting gold = new EnableSetting("Gold", true);
    public static EnableSetting diammond = new EnableSetting("Diammond", true);
    private Timer t;
    private List<BlockPos> ren;
    private final long per = 200L;
    public static IntegerSetting r = new IntegerSetting("Range", 20.0, 5.0, 100.0, 1);
    private final TimerUtils refresh = new TimerUtils();
    public nBlockPos pos = new nBlockPos();
    public static List<BlockPos> toRender = new ArrayList<BlockPos>();

    public Xray() {
        super("Xray", 0, Category.Render, false);
        this.getSetting().add(spawner);
        this.getSetting().add(coal);
        this.getSetting().add(iron);
        this.getSetting().add(lapis);
        this.getSetting().add(emerald);
        this.getSetting().add(redstone);
        this.getSetting().add(gold);
        this.getSetting().add(diammond);
        this.getSetting().add(r);
    }

    @Override
    public void enable() {
        super.enable();
        this.ren = new ArrayList<BlockPos>();
        this.t = new Timer();
        this.t.scheduleAtFixedRate(this.t(), 0L, 200L);
    }

    @Override
    public void disable() {
        super.disable();
        if (this.t != null) {
            this.t.cancel();
            this.t.purge();
            this.t = null;
        }
    }

    private TimerTask t() {
        return new TimerTask(){

            @Override
            public void run() {
                int ra;
                Xray.this.ren.clear();
                for (int y = ra = (int)r.getCurrent(); y >= -ra; --y) {
                    for (int x = -ra; x <= ra; ++x) {
                        for (int z = -ra; z <= ra; ++z) {
                            if (!Tools.isPlayerInGame()) continue;
                            BlockPos p = new BlockPos(Module.mc.thePlayer.posX + (double)x, Module.mc.thePlayer.posY + (double)y, Module.mc.thePlayer.posZ + (double)z);
                            Block bl = Module.mc.theWorld.getBlockState(p).getBlock();
                            if (!(iron.getEnable() && bl.equals(Blocks.iron_ore) || gold.getEnable() && bl.equals(Blocks.gold_ore) || diammond.getEnable() && bl.equals(Blocks.diamond_ore) || emerald.getEnable() && bl.equals(Blocks.emerald_ore) || lapis.getEnable() && bl.equals(Blocks.lapis_ore) || redstone.getEnable() && bl.equals(Blocks.redstone_ore) || coal.getEnable() && bl.equals(Blocks.coal_ore)) && (!spawner.getEnable() || !bl.equals(Blocks.mob_spawner))) continue;
                            Xray.this.ren.add(p);
                        }
                    }
                }
            }
        };
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Tools.isPlayerInGame() && !this.ren.isEmpty()) {
            ArrayList<BlockPos> tRen = new ArrayList<BlockPos>(this.ren);
            for (BlockPos p : tRen) {
                this.dr(p);
            }
        }
    }

    private void dr(BlockPos p) {
        int[] rgb = this.c(Xray.mc.theWorld.getBlockState(p).getBlock());
        if (rgb[0] + rgb[1] + rgb[2] != 0) {
            HUDUtils.re(p, new Color(rgb[0], rgb[1], rgb[2]).getRGB(), true);
        }
    }

    private int[] c(Block b) {
        int red = 0;
        int green = 0;
        int blue = 0;
        if (b.equals(Blocks.iron_ore)) {
            red = 255;
            green = 255;
            blue = 255;
        } else if (b.equals(Blocks.gold_ore)) {
            red = 255;
            green = 255;
        } else if (b.equals(Blocks.diamond_ore)) {
            green = 220;
            blue = 255;
        } else if (b.equals(Blocks.emerald_ore)) {
            red = 35;
            green = 255;
        } else if (b.equals(Blocks.lapis_ore)) {
            green = 50;
            blue = 255;
        } else if (b.equals(Blocks.redstone_ore)) {
            red = 255;
        } else if (b.equals(Blocks.mob_spawner)) {
            red = 30;
            blue = 135;
        }
        return new int[]{red, green, blue};
    }
}

