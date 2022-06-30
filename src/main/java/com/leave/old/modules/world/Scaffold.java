/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 */
package com.leave.old.modules.world;

import com.leave.old.Category;
import com.leave.old.Utils.A03A59A2;
import com.leave.old.Utils.BlockUtils;
import com.leave.old.Utils.Mappings;
import com.leave.old.Utils.MoveUtils;
import com.leave.old.Utils.PlayerUtils;
import com.leave.old.Utils.ReflectionUtil;
import com.leave.old.Utils.RenderUtils;
import com.leave.old.Utils.RobotUtils;
import com.leave.old.Utils.Rotation;
import com.leave.old.Utils.RotationUtil;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.Utils.Wrapper;
import com.leave.old.modules.Module;
import com.leave.old.modules.combat.Killaura;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Scaffold
extends Module {
    public static final List<Block> invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.furnace, Blocks.carpet, Blocks.crafting_table, Blocks.trapped_chest, Blocks.chest, Blocks.dispenser, Blocks.air, Blocks.water, Blocks.lava, Blocks.flowing_water, Blocks.flowing_lava, Blocks.sand, Blocks.snow_layer, Blocks.torch, Blocks.anvil, Blocks.jukebox, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.noteblock, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.wooden_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.yellow_flower, Blocks.red_flower, Blocks.anvil, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.cactus, Blocks.ladder, Blocks.web);
    private final List<Block> validBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
    private final BlockPos[] blockPositions = new BlockPos[]{new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1)};
    private final EnumFacing[] facings = new EnumFacing[]{EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH};
    private final TimerUtils towerStopwatch = new TimerUtils();
    private final Random rng = new Random();
    private float[] angles = new float[2];
    private boolean rotating;
    private int slot;
    private ItemStack currentblock;
    private int towerTick;
    public int godBridgeTimer;
    boolean sneaking;
    double y;
    public TimerUtils timer;
    public BlockData blockData;
    boolean isBridging = false;
    BlockPos blockDown = null;
    public static float[] facingCam = null;
    float startYaw = 0.0f;
    float startPitch = 0.0f;
    private final TimerUtils timerUtils;
    private List<Block> blacklist;
    ArrayList<String> sites = new ArrayList();
    private final ModeSetting Mode = new ModeSetting("Mode", "Eagle", Arrays.asList("Eagle", "Simple", "Hypixel", "Hanabi", "Mineland"), this);
    private EnableSetting tower = new EnableSetting("Tower", false);
    private EnableSetting towerboost = new EnableSetting("TowerBoost", false);
    private EnableSetting sprint = new EnableSetting("Sprint", false);
    private IntegerSetting legit = new IntegerSetting("Speed", 1.0, 0.1, 3.0, 1);
    private IntegerSetting expand = new IntegerSetting("Expand", 0.4, 0.0, 5.0, 1);
    private EnableSetting samey = new EnableSetting("Samey", false);
    private EnableSetting timervalue = new EnableSetting("Timer", false);
    private IntegerSetting timerspeed = new IntegerSetting("TimerSpeed", 1.4, 1.0, 2.0, 1);
    public static Timer timers = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
    TimerUtils towerTimer;
    boolean isLooking;
    private static final Rotation rotation = new Rotation(999.0f, 999.0f);
    private static final Random RANDOM = new Random();

    public Scaffold() {
        super("Scaffold", 0, Category.World, false);
        this.timerUtils = new TimerUtils();
        this.towerTimer = new TimerUtils();
        this.blacklist = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);
        this.getSetting().add(this.Mode);
        this.getSetting().add(this.tower);
        this.getSetting().add(this.towerboost);
        this.getSetting().add(this.sprint);
        this.getSetting().add(this.legit);
        this.getSetting().add(this.expand);
        this.getSetting().add(this.samey);
        this.getSetting().add(this.timervalue);
        this.getSetting().add(this.timerspeed);
    }

    @Override
    public void enable() {
        this.blockDown = null;
        facingCam = null;
        this.isBridging = false;
        this.startYaw = 0.0f;
        this.startPitch = 0.0f;
        this.y = Scaffold.mc.thePlayer.posY;
        this.blockData = null;
        this.slot = -1;
        rotation.setYaw(999.0f);
        rotation.setPitch(999.0f);
        this.towerTick = 0;
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC") && Scaffold.mc.gameSettings.keyBindBack.isKeyDown()) {
            KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindBack.getKeyCode(), false);
        }
        Scaffold.mc.thePlayer.setSprinting(false);
        this.sneaking = true;
        if (this.timervalue.getEnable()) {
            Scaffold.timers.timerSpeed = (float)this.timerspeed.getCurrent();
        }
        super.enable();
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (this.blockData != null) {
            this.blockDown = this.blockData.pos;
            RenderUtils.drawBlockESP(this.blockData.pos, 1.0f, 1.0f, 1.0f);
            if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
                BlockPos blockDown2 = new BlockPos(Scaffold.mc.thePlayer).down();
                BlockPos blockDown3 = new BlockPos(Scaffold.mc.thePlayer).down();
                if (Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.EAST) {
                    blockDown2 = new BlockPos(Scaffold.mc.thePlayer).down().west();
                    blockDown3 = new BlockPos(Scaffold.mc.thePlayer).down().west(2);
                } else if (Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) {
                    blockDown2 = new BlockPos(Scaffold.mc.thePlayer).down().south();
                    blockDown3 = new BlockPos(Scaffold.mc.thePlayer).down().south(2);
                } else if (Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) {
                    blockDown2 = new BlockPos(Scaffold.mc.thePlayer).down().north();
                    blockDown3 = new BlockPos(Scaffold.mc.thePlayer).down().north(2);
                } else if (Scaffold.mc.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
                    blockDown2 = new BlockPos(Scaffold.mc.thePlayer).down().east();
                    blockDown3 = new BlockPos(Scaffold.mc.thePlayer).down().east(2);
                }
                RenderUtils.drawBlockESP(blockDown2, 1.0f, 0.0f, 0.0f);
                RenderUtils.drawBlockESP(blockDown3, 1.0f, 0.0f, 0.0f);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickPost(TickEvent.PlayerTickEvent e) {
        if (this.Mode.getCurrent().equalsIgnoreCase("Hanabi") || this.Mode.getCurrent().equalsIgnoreCase("Mineland")) {
            int last = Scaffold.mc.thePlayer.inventory.currentItem;
            Scaffold.mc.thePlayer.inventory.currentItem = this.slot;
            if (Scaffold.mc.thePlayer.getCurrentEquippedItem() == null || this.blockData == null) {
                return;
            }
            if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), this.blockData.pos, this.blockData.face, Scaffold.getVec3(this.blockData.pos, this.blockData.face))) {
                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            Scaffold.mc.thePlayer.inventory.currentItem = last;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.Mode.getCurrent().equalsIgnoreCase("AAC")) {
            this.AAC();
            this.godBridgeTimer = 0;
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Simple")) {
            this.Simple();
            this.godBridgeTimer = 0;
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Hypixel")) {
            this.speed();
            this.godBridgeTimer = 0;
        } else if (this.Mode.getCurrent().equalsIgnoreCase("GodBridge")) {
            this.GodBridge();
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Mineland")) {
            this.godBridgeTimer = 0;
        }
        if (!this.Mode.getCurrent().equalsIgnoreCase("Eagle")) {
            return;
        }
        this.Eagle();
    }

    boolean check() {
        MovingObjectPosition object = Scaffold.mc.objectMouseOver;
        EntityPlayerSP player = Scaffold.mc.thePlayer;
        ItemStack stack = player.inventory.getCurrentItem();
        if (object == null || stack == null) {
            return false;
        }
        if (object.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return false;
        }
        if (player.rotationPitch <= 70.0f || !player.onGround || player.isOnLadder() || player.isInLava() || player.isInWater()) {
            return false;
        }
        return Scaffold.mc.gameSettings.keyBindBack.isKeyDown();
    }

    private boolean isPosSolid(BlockPos pos) {
        Block block = Scaffold.mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isVisuallyOpaque() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    private BlockData getBlockData1(BlockPos pos) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (this.isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (this.isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (this.isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (this.isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    private boolean isValidItem(Item item) {
        if (item instanceof ItemBlock) {
            ItemBlock iBlock = (ItemBlock)item;
            Block block = iBlock.getBlock();
            return !invalidBlocks.contains(block);
        }
        return false;
    }

    public int getBlockCount() {
        int n = 0;
        for (int i = 36; i < 45; ++i) {
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack stack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = stack.getItem();
            if (!(stack.getItem() instanceof ItemBlock) || !this.isValid(item)) continue;
            n += stack.stackSize;
        }
        return n;
    }

    void getBlock(int hotbarSlot) {
        for (int i = 9; i < 45; ++i) {
            ItemBlock block;
            Minecraft var10000 = mc;
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || Scaffold.mc.currentScreen != null && !(Scaffold.mc.currentScreen instanceof GuiInventory)) continue;
            var10000 = mc;
            ItemStack is = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (!(is.getItem() instanceof ItemBlock) || !this.isValidItem(block = (ItemBlock)is.getItem())) continue;
            if (36 + hotbarSlot == i) break;
            this.swap(i, hotbarSlot);
            break;
        }
    }

    int getBestSpoofSlot() {
        int spoofSlot = 5;
        for (int i = 36; i < 45; ++i) {
            if (Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            spoofSlot = i - 36;
            break;
        }
        return spoofSlot;
    }

    private Vec3 getVec3(BlockData data) {
        BlockPos pos = data.pos;
        EnumFacing face = data.face;
        double x = (double)pos.getX() + 0.5;
        double y = (double)pos.getY() + 0.5;
        double z = (double)pos.getZ() + 0.5;
        x += (double)face.getFrontOffsetX() / 2.0;
        z += (double)face.getFrontOffsetZ() / 2.0;
        y += (double)face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += this.randomNumber(0.3, -0.3);
            z += this.randomNumber(0.3, -0.3);
        } else {
            y += this.randomNumber(0.49, 0.5);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += this.randomNumber(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += this.randomNumber(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    void speed() {
        Scaffold.mc.thePlayer.stepHeight = 0.5f;
        Killaura.target = null;
        EntityPlayerSP player = Scaffold.mc.thePlayer;
        WorldClient world = Scaffold.mc.theWorld;
        if (this.getBlockCount() <= 0) {
            int spoofSlot = this.getBestSpoofSlot();
            this.getBlock(spoofSlot);
        }
        double yDif = 1.0;
        BlockData data = null;
        for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
            BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
            if (newData == null || !((yDif = player.posY - posY) <= 3.0)) continue;
            data = newData;
            break;
        }
        int slot = -1;
        int blockCount = 0;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemStack = player.inventory.getStackInSlot(i);
            if (itemStack == null) continue;
            int stackSize = itemStack.stackSize;
            if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount) continue;
            blockCount = stackSize;
            slot = i;
            this.currentblock = itemStack;
        }
        if (slot == -1) {
            // empty if block
        }
        if (data != null && slot != -1) {
            BlockPos pos = data.pos;
            Block block = world.getBlockState(pos.offset(data.face)).getBlock();
            Vec3 hitVec = this.getVec3(data);
            if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
                return;
            }
            if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                player.motionX = 0.0;
                player.motionY = 0.41982;
                player.motionZ = 0.0;
                if (this.towerStopwatch.hasReached(1500.0f)) {
                    player.motionY = -0.28;
                    this.towerStopwatch.reset();
                }
            } else {
                this.towerStopwatch.reset();
            }
        }
        if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
            this.setState(false);
        }
    }

    private double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    private BlockData getBlockData(BlockPos pos) {
        BlockPos[] blockPositions = this.blockPositions;
        EnumFacing[] facings = this.facings;
        List<Block> validBlocks = this.validBlocks;
        WorldClient world = Scaffold.mc.theWorld;
        BlockPos posBelow = new BlockPos(0, -1, 0);
        if (!validBlocks.contains(world.getBlockState(pos.add(posBelow)).getBlock())) {
            return new BlockData(pos.add(posBelow), EnumFacing.UP);
        }
        int blockPositionsLength = blockPositions.length;
        for (int i = 0; i < blockPositionsLength; ++i) {
            BlockPos blockPos = pos.add(blockPositions[i]);
            if (!validBlocks.contains(world.getBlockState(blockPos).getBlock())) {
                return new BlockData(blockPos, facings[i]);
            }
            for (int i1 = 0; i1 < blockPositionsLength; ++i1) {
                BlockPos blockPos1 = pos.add(blockPositions[i1]);
                BlockPos blockPos2 = blockPos.add(blockPositions[i1]);
                if (!validBlocks.contains(world.getBlockState(blockPos1).getBlock())) {
                    return new BlockData(blockPos1, facings[i1]);
                }
                if (validBlocks.contains(world.getBlockState(blockPos2).getBlock())) continue;
                return new BlockData(blockPos2, facings[i1]);
            }
        }
        return null;
    }

    public int getallBlockCount() {
        int n = 0;
        for (int i = 0; i < 36; ++i) {
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack stack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = stack.getItem();
            if (!(stack.getItem() instanceof ItemBlock) || !this.isValid(item)) continue;
            n += stack.stackSize;
        }
        return n;
    }

    private boolean isValid(Item item) {
        return item instanceof ItemBlock && !invalidBlocks.contains(((ItemBlock)item).getBlock());
    }

    private boolean isBlockUnder(double yOffset) {
        EntityPlayerSP player = Scaffold.mc.thePlayer;
        return !this.validBlocks.contains(Scaffold.mc.theWorld.getBlockState(new BlockPos(player.posX, player.posY - yOffset, player.posZ)).getBlock());
    }

    public void swap(int slot1, int hotbarSlot) {
        Scaffold.mc.playerController.windowClick(Scaffold.mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, Scaffold.mc.thePlayer);
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - Scaffold.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - Scaffold.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double y = (double)block.getY() + 0.5;
        if (this.Mode.getCurrent().equalsIgnoreCase("Legit")) {
            double dist = Scaffold.mc.thePlayer.getDistance((double)block.getX() + 0.5 + (double)face.getFrontOffsetX() / 2.0, block.getY(), (double)block.getZ() + 0.5 + (double)face.getFrontOffsetZ() / 2.0);
            y += 0.5;
        }
        double d1 = Scaffold.mc.thePlayer.posY + (double)Scaffold.mc.thePlayer.getEyeHeight() - y;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    private void a03c01(int value) {
        switch (value) {
            case 250: {
                try {
                    new A03A59A2();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static double getRandomDoubleInRange(double minDouble, double maxDouble) {
        return minDouble >= maxDouble ? minDouble : new Random().nextDouble() * (maxDouble - minDouble) + minDouble;
    }

    public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = (double)pos.getX() + 0.5;
        double y = (double)pos.getY() + 0.5;
        double z = (double)pos.getZ() + 0.5;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += Scaffold.getRandomDoubleInRange(0.3, -0.3);
            z += Scaffold.getRandomDoubleInRange(0.3, -0.3);
        } else {
            y += Scaffold.getRandomDoubleInRange(0.3, -0.3);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += Scaffold.getRandomDoubleInRange(0.3, -0.3);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += Scaffold.getRandomDoubleInRange(0.3, -0.3);
        }
        return new Vec3(x, y, z);
    }

    private BlockData getBlockDataHanabi(BlockPos pos) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (this.isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (this.isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (this.isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (this.isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (this.isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (this.isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (this.isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (this.isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (this.isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }

    public void setSpeed() {
        double motionx = Scaffold.mc.thePlayer.motionX;
        double motionz = Scaffold.mc.thePlayer.motionZ;
        if (this.Mode.getCurrent().equalsIgnoreCase("Legit") && Scaffold.mc.thePlayer.onGround && Scaffold.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01)) {
            double b;
            Scaffold.mc.thePlayer.setSprinting(false);
            if (this.timer.delay(50.0f)) {
                C0BPacketEntityAction p;
                if (this.timer.delay(250.0f)) {
                    if (!this.sneaking && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        p = new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING);
                        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(p);
                        this.sneaking = !this.sneaking;
                    } else if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                        p = new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING);
                        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(p);
                        this.sneaking = !this.sneaking;
                    }
                } else if (this.sneaking) {
                    p = new C0BPacketEntityAction(Scaffold.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING);
                    Scaffold.mc.thePlayer.sendQueue.addToSendQueue(p);
                    boolean bl = this.sneaking = !this.sneaking;
                }
            }
            if (Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                Scaffold.mc.thePlayer.jump();
            }
            Scaffold.mc.thePlayer.onGround = false;
            Scaffold.mc.thePlayer.jumpMovementFactor = 0.0f;
            double speed = this.sneaking ? 0.09 : 0.13;
            double x = Scaffold.mc.thePlayer.posX;
            double z = Scaffold.mc.thePlayer.posZ;
            double forward = Scaffold.mc.thePlayer.movementInput.moveForward;
            double strafe = Scaffold.mc.thePlayer.movementInput.moveStrafe;
            float YAW = Scaffold.mc.thePlayer.rotationYaw;
            double a = forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f));
            double c = Math.abs(a * (b = forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))));
            double slow = 1.0 - c * 5.0;
            if ((speed *= slow) < 0.05) {
                speed = 0.05;
            }
            speed += this.randomNumber(0.001, -0.001);
            double more = this.legit.getCurrent();
            MoveUtils.setMotion(speed *= more);
        }
    }

    private boolean hotbarContainBlock() {
        int i = 36;
        while (i < 45) {
            try {
                ItemStack stack = Scaffold.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock) || !this.isValid(stack.getItem())) {
                    ++i;
                    continue;
                }
                return true;
            }
            catch (Exception exception) {
            }
        }
        return false;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent e) {
        if (this.Mode.getCurrent().equalsIgnoreCase("Simple")) {
            EntityPlayerSP player = Scaffold.mc.thePlayer;
            WorldClient world = Scaffold.mc.theWorld;
            if (this.getBlockCount() <= 0) {
                int spoofSlot = this.getBestSpoofSlot();
                this.getBlock(spoofSlot);
            }
            double yDif = 1.0;
            BlockData data = null;
            for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
                BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
                if (newData == null || !((yDif = player.posY - posY) <= 3.0)) continue;
                data = newData;
                break;
            }
            int slot = -1;
            int blockCount = 0;
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = player.inventory.getStackInSlot(i);
                if (itemStack == null) continue;
                int stackSize = itemStack.stackSize;
                if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount) continue;
                blockCount = stackSize;
                slot = i;
                this.currentblock = itemStack;
            }
            if (slot == -1) {
                // empty if block
            }
            if (data != null && slot != -1) {
                BlockPos pos = data.pos;
                Block block = world.getBlockState(pos.offset(data.face)).getBlock();
                Vec3 hitVec = this.getVec3(data);
                if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
                    return;
                }
                int last = player.inventory.currentItem;
                player.inventory.currentItem = slot;
                if (Scaffold.mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos, data.face, hitVec)) {
                    Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                player.inventory.currentItem = last;
            }
            if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
                this.setState(false);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTickPre(TickEvent.PlayerTickEvent e) {
        if (this.Mode.getCurrent().contains("AAC")) {
            return;
        }
        if (this.Mode.getCurrent().contains("Hypixel")) {
            EntityPlayerSP player = Scaffold.mc.thePlayer;
            WorldClient world = Scaffold.mc.theWorld;
            if (this.getBlockCount() <= 0) {
                int spoofSlot = this.getBestSpoofSlot();
                this.getBlock(spoofSlot);
            }
            double yDif = 1.0;
            BlockData data = null;
            for (double posY = player.posY - 1.0; posY > 0.0; posY -= 1.0) {
                BlockData newData = this.getBlockData(new BlockPos(player.posX, posY, player.posZ));
                if (newData == null || !((yDif = player.posY - posY) <= 3.0)) continue;
                data = newData;
                break;
            }
            int slot = -1;
            int blockCount = 0;
            for (int i = 0; i < 9; ++i) {
                ItemStack itemStack = player.inventory.getStackInSlot(i);
                if (itemStack == null) continue;
                int stackSize = itemStack.stackSize;
                if (!this.isValidItem(itemStack.getItem()) || stackSize <= blockCount) continue;
                blockCount = stackSize;
                slot = i;
                this.currentblock = itemStack;
            }
            if (slot == -1) {
                // empty if block
            }
            if (data != null && slot != -1) {
                BlockPos pos = data.pos;
                Block block = world.getBlockState(pos.offset(data.face)).getBlock();
                Vec3 hitVec = this.getVec3(data);
                if (!this.validBlocks.contains(block) || this.isBlockUnder(yDif)) {
                    return;
                }
                int last = player.inventory.currentItem;
                player.inventory.currentItem = slot;
                if (data != null) {
                    float rot = 0.0f;
                    if (Scaffold.mc.thePlayer.movementInput.moveForward > 0.0f) {
                        rot = 180.0f;
                        if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                            rot = -120.0f;
                        } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                            rot = 120.0f;
                        }
                    } else if (Scaffold.mc.thePlayer.movementInput.moveForward == 0.0f) {
                        rot = 180.0f;
                        if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                            rot = -90.0f;
                        } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                            rot = 90.0f;
                        }
                    } else if (Scaffold.mc.thePlayer.movementInput.moveForward < 0.0f) {
                        if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                            rot = -45.0f;
                        } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                            rot = 45.0f;
                        }
                    }
                    if (PlayerUtils.isAirUnder(Scaffold.mc.thePlayer) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && Boolean.valueOf(this.tower.getEnable()).booleanValue()) {
                        rot = 180.0f;
                    }
                    float gcd = Scaffold.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
                    float a2 = gcd * gcd * gcd * 1.2f;
                    rot -= rot % a2;
                    float yaw = MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.rotationYaw) - rot;
                    float pitch = this.getRotationsHypixel(data.pos, data.face)[1];
                    if (data != null) {
                        rotation.setYaw((float)((double)yaw + Scaffold.getRandomDoubleInRange(-1.0, 1.0)));
                        rotation.setPitch(pitch);
                    }
                }
                if (rotation.getYaw() != 999.0f) {
                    Scaffold.mc.thePlayer.rotationYawHead = rotation.getYaw();
                    Scaffold.mc.thePlayer.renderYawOffset = rotation.getYaw();
                    Scaffold.mc.thePlayer.setRotationYawHead(rotation.getYaw());
                }
                if (rotation.getPitch() != 999.0f) {
                    // empty if block
                }
                if (Scaffold.mc.playerController.onPlayerRightClick(player, world, player.getCurrentEquippedItem(), pos, data.face, hitVec)) {
                    this.sendPacket(new C0APacketAnimation());
                }
                player.inventory.currentItem = last;
            }
            if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
                this.setState(false);
            }
            Scaffold.mc.thePlayer.setRotationYawHead(Scaffold.mc.thePlayer.rotationYaw);
            Scaffold.mc.thePlayer.renderYawOffset = Scaffold.mc.thePlayer.rotationYaw;
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Hanabi")) {
            if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
                this.setState(false);
            }
            if (this.getBlockCount() <= 0) {
                int spoofSlot = this.getBestSpoofSlot();
                this.getBlock(spoofSlot);
            }
            this.blockData = this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)) == null ? this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ).down(1)) : this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ));
            this.slot = this.getBlockSlot();
            this.currentblock = Scaffold.mc.thePlayer.inventoryContainer.getSlot(this.slot + 36).getStack();
            if (this.blockData == null || this.slot == -1 || this.getBlockCount() <= 0 || !MoveUtils.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 0.5, Scaffold.mc.thePlayer.posZ)).getBlock() == Blocks.air) {
                float rot = 0.0f;
                if (Scaffold.mc.thePlayer.movementInput.moveForward > 0.0f) {
                    rot = 180.0f;
                    if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                        rot = -120.0f;
                    } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                        rot = 120.0f;
                    }
                } else if (Scaffold.mc.thePlayer.movementInput.moveForward == 0.0f) {
                    rot = 180.0f;
                    if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                        rot = -90.0f;
                    } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                        rot = 90.0f;
                    }
                } else if (Scaffold.mc.thePlayer.movementInput.moveForward < 0.0f) {
                    if (Scaffold.mc.thePlayer.movementInput.moveStrafe > 0.0f) {
                        rot = -45.0f;
                    } else if (Scaffold.mc.thePlayer.movementInput.moveStrafe < 0.0f) {
                        rot = 45.0f;
                    }
                }
                if (PlayerUtils.isAirUnder(Scaffold.mc.thePlayer) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && Boolean.valueOf(this.tower.getEnable()).booleanValue()) {
                    rot = 180.0f;
                }
                float gcd = Scaffold.mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
                float a2 = gcd * gcd * gcd * 1.2f;
                rot -= rot % a2;
                float yaw = MathHelper.wrapAngleTo180_float(Scaffold.mc.thePlayer.rotationYaw) - rot;
                float pitch = this.getRotationsHypixel(this.blockData.pos, this.blockData.face)[1];
                rotation.setYaw(yaw);
                rotation.setPitch(pitch);
            }
            if (rotation.getYaw() != 999.0f) {
                Scaffold.mc.thePlayer.rotationYawHead = rotation.getYaw();
                Scaffold.mc.thePlayer.renderYawOffset = rotation.getYaw();
                Scaffold.mc.thePlayer.setRotationYawHead(rotation.getYaw());
            }
            if (rotation.getPitch() != 999.0f) {
                Rotation.setServerPitch(rotation.getPitch());
            }
            if (PlayerUtils.isAirUnder(Scaffold.mc.thePlayer) && MoveUtils.isOnGround(1.15) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && this.tower.getEnable()) {
                if (this.towerboost.getEnable()) {
                    Scaffold.timers.timerSpeed = 2.1078f;
                }
                Scaffold.mc.thePlayer.motionX = 0.0;
                Scaffold.mc.thePlayer.motionZ = 0.0;
                Scaffold.mc.thePlayer.movementInput.moveForward = 0.0f;
                Scaffold.mc.thePlayer.movementInput.moveStrafe = 0.0f;
                if (++this.towerTick < 10) {
                    Scaffold.mc.thePlayer.jump();
                } else {
                    this.towerTick = 0;
                }
            }
            if (!(MoveUtils.isOnGround(1.15) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && Boolean.valueOf(this.tower.getEnable()).booleanValue() || Scaffold.timers.timerSpeed != 2.1078f)) {
                Scaffold.timers.timerSpeed = 1.0f;
            }
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Legigt")) {
            if (this.getBlockCount() <= 0) {
                int spoofSlot = this.getBestSpoofSlot();
                this.getBlock(spoofSlot);
            }
            this.slot = this.getBlockSlot();
            if (!this.hotbarContainBlock()) {
                this.blockData = null;
                return;
            }
            double x = Scaffold.mc.thePlayer.posX;
            double z = Scaffold.mc.thePlayer.posZ;
            double forward = Scaffold.mc.thePlayer.movementInput.moveForward;
            double strafe = Scaffold.mc.thePlayer.movementInput.moveStrafe;
            float YAW = Scaffold.mc.thePlayer.rotationYaw;
            if (!Scaffold.mc.thePlayer.isCollidedHorizontally) {
                double[] coords = this.getExpandCoords(x, z, forward, strafe, YAW);
                x = coords[0];
                z = coords[1];
            }
            if (this.isAirBlock(Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)).getBlock())) {
                x = Scaffold.mc.thePlayer.posX;
                z = Scaffold.mc.thePlayer.posZ;
            }
            if (this.samey.getEnable()) {
                if ((double)Scaffold.mc.thePlayer.fallDistance > 1.2 + (double)(1 * MoveUtils.getJumpEffect()) || !PlayerUtils.isMoving2() && Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.y = Scaffold.mc.thePlayer.posY;
                }
            } else {
                this.y = Scaffold.mc.thePlayer.posY;
            }
            if (this.blockData != null && rotation.getPitch() != 999.0f) {
                rotation.setPitch(Scaffold.updateRotation(Scaffold.mc.thePlayer.rotationPitch, (float)(82.0 - this.randomNumber(0.0, 1.0)), 15.0f));
                Rotation.setServerPitch(Scaffold.updateRotation(Scaffold.mc.thePlayer.rotationPitch, (float)(82.0 - this.randomNumber(0.0, 1.0)), 15.0f));
            }
            this.setSpeed();
            if (this.getBlockCount() > 0 && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                Scaffold.timers.timerSpeed = 1.0f;
            }
            this.blockData = this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ)) == null ? this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ).down(1)) : this.getBlockDataHanabi(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ));
            float[] rot = this.getRotations(this.blockData.pos, this.blockData.face);
            int last = Scaffold.mc.thePlayer.inventory.currentItem;
            Scaffold.mc.thePlayer.inventory.currentItem = this.slot;
            if (Scaffold.mc.thePlayer.getCurrentEquippedItem() == null || this.blockData == null) {
                return;
            }
            if (Scaffold.mc.playerController.onPlayerRightClick(Scaffold.mc.thePlayer, Scaffold.mc.theWorld, Scaffold.mc.thePlayer.getCurrentEquippedItem(), this.blockData.pos, this.blockData.face, Scaffold.getVec3(this.blockData.pos, this.blockData.face))) {
                Scaffold.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            }
            Scaffold.mc.thePlayer.inventory.currentItem = last;
        } else if (this.Mode.getCurrent().equalsIgnoreCase("Mineland")) {
            BlockData data;
            if (this.getBlockCount() <= 0 && this.getallBlockCount() <= 0) {
                this.setState(false);
            }
            if (this.getBlockCount() <= 0) {
                int spoofSlot = this.getBestSpoofSlot();
                this.getBlock(spoofSlot);
            }
            BlockPos underPos = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
            Block underBlock = Wrapper.INSTANCE.world().getBlockState(underPos).getBlock();
            this.blockData = data = this.getBlockDataHanabi(underPos);
            this.slot = this.getBlockSlot();
            this.currentblock = Scaffold.mc.thePlayer.inventoryContainer.getSlot(this.slot + 36).getStack();
            if (this.blockData == null || this.slot == -1 || this.getBlockCount() <= 0 || !MoveUtils.isMoving() && !Scaffold.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (Scaffold.mc.theWorld.getBlockState(new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 0.5, Scaffold.mc.thePlayer.posZ)).getBlock() == Blocks.air && data != null) {
                float[] rot = this.getRotations(data.pos, data.face);
                if (BlockUtils.isBlockMaterial(underPos, Blocks.air) && data != null && rot != null) {
                    Rotation.setServerPitch(rot[1]);
                }
            }
            if (PlayerUtils.isAirUnder(Scaffold.mc.thePlayer) && MoveUtils.isOnGround(1.15) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && this.tower.getEnable()) {
                if (this.towerboost.getEnable()) {
                    Scaffold.timers.timerSpeed = 2.1078f;
                }
                Scaffold.mc.thePlayer.motionX = 0.0;
                Scaffold.mc.thePlayer.motionZ = 0.0;
                Scaffold.mc.thePlayer.movementInput.moveForward = 0.0f;
                Scaffold.mc.thePlayer.movementInput.moveStrafe = 0.0f;
                if (++this.towerTick < 10) {
                    Scaffold.mc.thePlayer.jump();
                } else {
                    this.towerTick = 0;
                }
            }
            if (!(MoveUtils.isOnGround(1.15) && Scaffold.mc.gameSettings.keyBindJump.isKeyDown() && !PlayerUtils.MovementInput() && Boolean.valueOf(this.tower.getEnable()).booleanValue() || Scaffold.timers.timerSpeed != 2.1078f)) {
                Scaffold.timers.timerSpeed = 1.0f;
            }
        }
    }

    public double[] getExpandCoords(double x, double z, double forward, double strafe, float YAW) {
        BlockPos underPos = new BlockPos(x, Scaffold.mc.thePlayer.posY - 1.0, z);
        Block underBlock = Scaffold.mc.theWorld.getBlockState(underPos).getBlock();
        double xCalc = -999.0;
        double zCalc = -999.0;
        double dist = 0.0;
        double expandDist = this.expand.getCurrent() * 2.0;
        while (!this.isAirBlock(underBlock)) {
            xCalc = x;
            zCalc = z;
            if ((dist += 1.0) > expandDist) {
                dist = expandDist;
            }
            xCalc += (forward * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f)) + strafe * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f))) * dist;
            zCalc += (forward * 0.45 * Math.sin(Math.toRadians(YAW + 90.0f)) - strafe * 0.45 * Math.cos(Math.toRadians(YAW + 90.0f))) * dist;
            if (dist == expandDist) break;
            underPos = new BlockPos(xCalc, Scaffold.mc.thePlayer.posY - 1.0, zCalc);
            underBlock = Scaffold.mc.theWorld.getBlockState(underPos).getBlock();
        }
        return new double[]{xCalc, zCalc};
    }

    public void sendPacket(Packet packet) {
        Scaffold.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public float[] getRotationsHypixel(BlockPos paramBlockPos, EnumFacing paramEnumFacing) {
        paramBlockPos = paramBlockPos.offset(paramEnumFacing.getOpposite());
        return RotationUtil.getRotationFromPosition((double)paramBlockPos.getX() + 0.5, (double)paramBlockPos.getZ() + 0.5, paramBlockPos.getY());
    }

    public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
            return !(block instanceof BlockSnow) || !(block.getBlockBoundsMaxY() > 0.125);
        }
        return false;
    }

    void AAC() {
        int newSlot;
        EntityPlayerSP player = Scaffold.mc.thePlayer;
        int oldSlot = -1;
        if (!this.check()) {
            if (this.isBridging) {
                KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), BlockUtils.isBlockMaterial(new BlockPos(player).down(), Blocks.air));
                this.isBridging = false;
                if (oldSlot != -1) {
                    player.inventory.currentItem = oldSlot;
                }
            }
            this.startYaw = 0.0f;
            this.startPitch = 0.0f;
            facingCam = null;
            this.blockDown = null;
            return;
        }
        this.startYaw = Scaffold.mc.thePlayer.rotationYaw;
        this.startPitch = Scaffold.mc.thePlayer.rotationPitch;
        KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        this.blockDown = new BlockPos(player).down();
        float r1 = new Random().nextFloat();
        if (r1 == 1.0f) {
            r1 -= 1.0f;
        }
        if ((newSlot = this.findSlotWithBlock()) == -1) {
            return;
        }
        oldSlot = player.inventory.currentItem;
        player.inventory.currentItem = newSlot;
        player.rotationPitch = Scaffold.updateRotation(player.rotationPitch, 82.0f - r1, 15.0f);
        int currentCPS = Scaffold.random(3, 4);
        if (this.timer.isDelay(1000 / currentCPS)) {
            RobotUtils.clickMouse(1);
            Scaffold.swingMainHand();
            this.timer.setLastMS();
        }
        this.isBridging = true;
        KeyBinding.setKeyBindState(Scaffold.mc.gameSettings.keyBindSneak.getKeyCode(), BlockUtils.isBlockMaterial(new BlockPos(player).down(), Blocks.air));
    }

    public static void swingMainHand() {
        Scaffold.mc.thePlayer.swingItem();
    }

    public static int random(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float var4 = MathHelper.wrapAngleTo180_float(p_70663_2_ - p_70663_1_);
        if (var4 > p_70663_3_) {
            var4 = p_70663_3_;
        }
        if (var4 < -p_70663_3_) {
            var4 = -p_70663_3_;
        }
        return p_70663_1_ + var4;
    }

    void Simple() {
        Vec3 below = Scaffold.mc.thePlayer.getPositionVector();
        this.blockDown = new BlockPos(below).add(0, -1, 0);
        if (!BlockUtils.getBlock(this.blockDown).getMaterial().isReplaceable()) {
            return;
        }
        int newSlot = this.findSlotWithBlock();
        if (newSlot == -1) {
            return;
        }
        int oldSlot = Scaffold.mc.thePlayer.inventory.currentItem;
        Scaffold.mc.thePlayer.inventory.currentItem = newSlot;
        Scaffold.mc.thePlayer.inventory.currentItem = oldSlot;
    }

    void GodBridge() {
        if (this.godBridgeTimer > 0) {
            --this.godBridgeTimer;
        }
        if (Scaffold.mc.theWorld == null || Scaffold.mc.thePlayer == null) {
            return;
        }
        WorldClient world = Scaffold.mc.theWorld;
        EntityPlayerSP player = Scaffold.mc.thePlayer;
        MovingObjectPosition movingObjectPosition = player.rayTrace(Scaffold.mc.playerController.getBlockReachDistance(), 1.0f);
        boolean isKeyUseDown = false;
        int keyCode = Scaffold.mc.gameSettings.keyBindUseItem.getKeyCode();
        isKeyUseDown = keyCode >= 0 ? Keyboard.isKeyDown((int)keyCode) : Mouse.isButtonDown((int)(keyCode + 100));
        if (movingObjectPosition != null && movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition.sideHit == EnumFacing.UP && isKeyUseDown) {
            ItemBlock itemblock;
            int i;
            ItemStack itemstack = player.inventory.getCurrentItem();
            int n = i = itemstack != null ? itemstack.stackSize : 0;
            if (itemstack != null && itemstack.getItem() instanceof ItemBlock && !(itemblock = (ItemBlock)itemstack.getItem()).canPlaceBlockOnSide(world, movingObjectPosition.getBlockPos(), movingObjectPosition.sideHit, player, itemstack)) {
                BlockPos blockPos = movingObjectPosition.getBlockPos();
                IBlockState blockState = world.getBlockState(blockPos);
                AxisAlignedBB axisalignedbb = blockState.getBlock().getSelectedBoundingBox(world, blockPos);
                if (axisalignedbb == null || world.isAirBlock(blockPos)) {
                    return;
                }
                Vec3 targetVec3 = null;
                Vec3 eyeVec3 = player.getPositionEyes(1.0f);
                double x1 = axisalignedbb.minX;
                double x2 = axisalignedbb.maxX;
                double y1 = axisalignedbb.minY;
                double y2 = axisalignedbb.maxY;
                double z1 = axisalignedbb.minZ;
                double z2 = axisalignedbb.maxZ;
                class Data
                implements Comparable<Data> {
                    public BlockPos blockPos;
                    public EnumFacing enumFacing;
                    public double cost;

                    public Data(BlockPos blockPos, EnumFacing enumFacing, double cost) {
                        this.blockPos = blockPos;
                        this.enumFacing = enumFacing;
                        this.cost = cost;
                    }

                    @Override
                    public int compareTo(Data data) {
                        return this.cost - data.cost > 0.0 ? -1 : (this.cost - data.cost < 0.0 ? 1 : 0);
                    }
                }
                ArrayList<Data> list = new ArrayList<Data>();
                if (!(x1 <= eyeVec3.xCoord && eyeVec3.xCoord <= x2 && y1 <= eyeVec3.yCoord && eyeVec3.yCoord <= y2 && z1 <= eyeVec3.zCoord && eyeVec3.zCoord <= z2)) {
                    double xCost = Math.abs(eyeVec3.xCoord - 0.5 * (axisalignedbb.minX + axisalignedbb.maxX));
                    double yCost = Math.abs(eyeVec3.yCoord - 0.5 * (axisalignedbb.minY + axisalignedbb.maxY));
                    double zCost = Math.abs(eyeVec3.zCoord - 0.5 * (axisalignedbb.minZ + axisalignedbb.maxZ));
                    double sumCost = xCost + yCost + zCost;
                    if (eyeVec3.xCoord < x1) {
                        list.add(new Data(blockPos.west(), EnumFacing.WEST, xCost));
                    } else if (eyeVec3.xCoord > x2) {
                        list.add(new Data(blockPos.east(), EnumFacing.EAST, xCost));
                    }
                    if (eyeVec3.zCoord < z1) {
                        list.add(new Data(blockPos.north(), EnumFacing.NORTH, zCost));
                    } else if (eyeVec3.zCoord > z2) {
                        list.add(new Data(blockPos.south(), EnumFacing.SOUTH, zCost));
                    }
                    Collections.sort(list);
                    double border = 0.05;
                    double x = MathHelper.clamp_double(eyeVec3.xCoord, x1 + border, x2 - border);
                    double y = MathHelper.clamp_double(eyeVec3.yCoord, y1 + border, y2 - border);
                    double z = MathHelper.clamp_double(eyeVec3.zCoord, z1 + border, z2 - border);
                    for (Data data : list) {
                        if (!world.isAirBlock(data.blockPos)) continue;
                        if (data.enumFacing == EnumFacing.WEST || data.enumFacing == EnumFacing.EAST) {
                            x = MathHelper.clamp_double(eyeVec3.xCoord, x1, x2);
                        } else if (data.enumFacing == EnumFacing.UP || data.enumFacing == EnumFacing.DOWN) {
                            y = MathHelper.clamp_double(eyeVec3.yCoord, y1, y2);
                        } else {
                            z = MathHelper.clamp_double(eyeVec3.zCoord, z1, z2);
                        }
                        targetVec3 = new Vec3(x, y, z);
                        break;
                    }
                    if (targetVec3 != null) {
                        double d0 = targetVec3.xCoord - eyeVec3.xCoord;
                        double d1 = targetVec3.yCoord - eyeVec3.yCoord;
                        double d2 = targetVec3.zCoord - eyeVec3.zCoord;
                        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
                        float f = (float)(MathHelper.atan2(d2, d0) * 180.0 / Math.PI) - 90.0f;
                        float f1 = (float)(-(MathHelper.atan2(d1, d3) * 180.0 / Math.PI));
                        float f2 = player.rotationYaw;
                        float f3 = player.rotationPitch;
                        player.rotationYaw = f;
                        player.rotationPitch = f1;
                        MovingObjectPosition movingObjectPosition1 = player.rayTrace(Scaffold.mc.playerController.getBlockReachDistance(), 1.0f);
                        if (movingObjectPosition1.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && movingObjectPosition1.getBlockPos().getX() == blockPos.getX() && movingObjectPosition1.getBlockPos().getY() == blockPos.getY() && movingObjectPosition1.getBlockPos().getZ() == blockPos.getZ()) {
                            if (Scaffold.mc.playerController.onPlayerRightClick(player, Scaffold.mc.theWorld, itemstack, blockPos, movingObjectPosition1.sideHit, movingObjectPosition1.hitVec)) {
                                player.swingItem();
                            }
                            if (itemstack != null) {
                                if (itemstack.stackSize == 0) {
                                    player.inventory.mainInventory[player.inventory.currentItem] = null;
                                } else if (itemstack.stackSize != i || Scaffold.mc.playerController.isInCreativeMode()) {
                                    Scaffold.mc.entityRenderer.itemRenderer.resetEquippedProgress();
                                }
                            }
                        }
                        player.rotationYaw = f2;
                        player.rotationPitch = f3;
                        double targetPitch = 75.5;
                        double pitchDelta = 2.5;
                        if (targetPitch - pitchDelta < (double)player.rotationPitch && (double)player.rotationPitch < targetPitch + pitchDelta) {
                            double delta;
                            double mod = (double)player.rotationYaw % 45.0;
                            if (mod < 0.0) {
                                mod += 45.0;
                            }
                            if (mod < (delta = 5.0)) {
                                player.rotationYaw = (float)((double)player.rotationYaw - mod);
                                player.rotationPitch = (float)targetPitch;
                            } else if (45.0 - mod < delta) {
                                player.rotationYaw = (float)((double)player.rotationYaw + (45.0 - mod));
                                player.rotationPitch = (float)targetPitch;
                            }
                        }
                        ReflectionHelper.setPrivateValue(Minecraft.class, mc, new Integer(1), "rightClickDelayTimer", "field_71467_ac");
                        this.godBridgeTimer = 10;
                    }
                }
            }
        }
    }

    public int findSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = Scaffold.mc.thePlayer.inventory.getStackInSlot(i);
            if (stack == null || !(stack.getItem() instanceof ItemBlock) || !(block = Block.getBlockFromItem(stack.getItem()).getDefaultState().getBlock()).isFullBlock() || block == Blocks.sand || block == Blocks.gravel) continue;
            return i;
        }
        return -1;
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; ++i) {
            if (!Scaffold.mc.thePlayer.inventoryContainer.getSlot(i + 36).getHasStack() || !Scaffold.isScaffoldBlock(Scaffold.mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack())) continue;
            return i;
        }
        return -1;
    }

    public static boolean isScaffoldBlock(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (itemStack.stackSize <= 0) {
            return false;
        }
        if (!(itemStack.getItem() instanceof ItemBlock)) {
            return false;
        }
        ItemBlock itemBlock = (ItemBlock)itemStack.getItem();
        if (itemBlock.getBlock() == Blocks.glass) {
            return true;
        }
        if (invalidBlocks.contains(Block.getBlockFromItem(itemStack.getItem()))) {
            return false;
        }
        return itemBlock.getBlock().isFullBlock();
    }

    void Eagle() {
        try {
            if (Scaffold.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
                if (!Scaffold.mc.gameSettings.keyBindJump.isPressed()) {
                    BlockPos bp = new BlockPos(Scaffold.mc.thePlayer.posX, Scaffold.mc.thePlayer.posY - 1.0, Scaffold.mc.thePlayer.posZ);
                    if (Scaffold.mc.theWorld.getBlockState(bp).getBlock() == Blocks.air && !Keyboard.isKeyDown((int)17)) {
                        ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, true);
                    } else {
                        ReflectionUtil.pressed.set(Minecraft.getMinecraft().gameSettings.keyBindSneak, false);
                    }
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static class BlockData {
        public final BlockPos pos;
        public final EnumFacing face;

        private BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }
}

