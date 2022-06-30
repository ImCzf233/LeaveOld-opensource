/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.Client;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.modules.Module;
import com.leave.old.modules.Tools;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class Killaura
extends Module {
    public static EntityLivingBase target;
    private List<Entity> targets = new ArrayList<Entity>(0);
    private int index;
    private IntegerSetting cps = new IntegerSetting("CPS", 10.0, 1.0, 30.0, 1);
    private IntegerSetting range = new IntegerSetting("range", 4.0, 1.0, 6.0, 1);
    private IntegerSetting BlockRange = new IntegerSetting("AutoBlock range", 4.0, 1.0, 6.0, 1);
    private IntegerSetting SwitchDelay = new IntegerSetting("SwitchDelay", 800.0, 1.0, 5000.0, 0);
    private IntegerSetting turnSpeed = new IntegerSetting("TurnSpeed(percent)", 40.0, 1.0, 100.0, 1);
    private IntegerSetting switchEntitys = new IntegerSetting("switchEntity", 2.0, 1.0, 5.0, 0);
    private EnableSetting autoblock = new EnableSetting("AutoBlock", true);
    private EnableSetting players = new EnableSetting("players", true);
    private EnableSetting animals = new EnableSetting("animals", false);
    private EnableSetting mobs = new EnableSetting("mobs", false);
    private EnableSetting invisibles = new EnableSetting("invisibles", false);
    private EnableSetting villager = new EnableSetting("Villager", false);
    private EnableSetting esp = new EnableSetting("ESP", true);
    private EnableSetting random = new EnableSetting("Random", true);
    private EnableSetting targetHud = new EnableSetting("targetHud", true);
    private EnableSetting hover = new EnableSetting("Hover", true);
    private EnableSetting pitch = new EnableSetting("Pitch", false);
    private EnableSetting yaw = new EnableSetting("Yaw", false);
    private final ModeSetting Mode = new ModeSetting("Mode", "HurtTime", Arrays.asList("Delay", "HurtTime"), this);
    private final ModeSetting AttackMode = new ModeSetting("AttackMode", "Packet", Arrays.asList("Packet", "Attack"), this);
    private final ModeSetting switchMode = new ModeSetting("SwitchMode", "Switch", Arrays.asList("Switch", "Single"), this);
    public static float[] lastRotations;
    public static float lastYaw;
    public static float lastPitch;
    public static boolean shouldSetRot;
    public static int kgw;
    public static Entity lastTarget;
    private int rotyaw;
    private int rotpitch;
    private TimerUtils attackTimer = new TimerUtils();
    private TimerUtils switchTimer = new TimerUtils();
    private TimerUtils timerUtils = new TimerUtils();
    private TimerUtils timerUtils1 = new TimerUtils();
    private int Index;
    private float delay;
    private float virtualYaw;
    private float virtualPitch;
    private float pitchExpand;
    private boolean expandDirectionPitch;
    private float yawExpand;
    private boolean expandDirectionYaw;
    private int aps;
    private TimerUtils apsTimer = new TimerUtils();
    private int apsDelay;
    private boolean hitting;
    private int rangeExtend;

    public Killaura() {
        super("Killaura", 0, Category.Combat, false);
        this.getSetting().add(this.cps);
        this.getSetting().add(this.range);
        this.getSetting().add(this.BlockRange);
        this.getSetting().add(this.SwitchDelay);
        this.getSetting().add(this.turnSpeed);
        this.getSetting().add(this.switchEntitys);
        this.getSetting().add(this.autoblock);
        this.getSetting().add(this.players);
        this.getSetting().add(this.animals);
        this.getSetting().add(this.mobs);
        this.getSetting().add(this.invisibles);
        this.getSetting().add(this.esp);
        this.getSetting().add(this.random);
        this.getSetting().add(this.targetHud);
        this.getSetting().add(this.hover);
        this.getSetting().add(this.Mode);
        this.getSetting().add(this.yaw);
        this.getSetting().add(this.pitch);
        this.getSetting().add(this.AttackMode);
        this.getSetting().add(this.switchMode);
    }

    @Override
    public void enable() {
        super.enable();
        this.virtualPitch = Killaura.mc.thePlayer.rotationPitch;
        this.virtualYaw = Killaura.mc.thePlayer.rotationYaw;
        this.pitchExpand = 0.0f;
        this.yawExpand = 0.0f;
        this.expandDirectionYaw = false;
        this.expandDirectionPitch = false;
        this.setDelay();
        this.setApsDelay();
    }

    @Override
    public void disable() {
        super.disable();
        target = null;
        this.targets.clear();
        if (this.autoblock.getEnable() && this.hasSword() && Killaura.mc.thePlayer.isBlocking()) {
            this.unBlock();
        }
    }

    private void block() {
        if (Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            KeyBinding.setKeyBindState(Killaura.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            if (Killaura.mc.playerController.sendUseItem(Killaura.mc.thePlayer, Killaura.mc.theWorld, Killaura.mc.thePlayer.inventory.getCurrentItem())) {
                mc.getItemRenderer().resetEquippedProgress2();
            }
        }
    }

    private void unBlock() {
        if (Killaura.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            KeyBinding.setKeyBindState(Killaura.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            Killaura.mc.playerController.onStoppedUsingItem(Killaura.mc.thePlayer);
        }
    }

    private boolean hasSword() {
        if (Killaura.mc.thePlayer.inventory.getCurrentItem() != null) {
            return Killaura.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
        }
        return false;
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        if (target != null) {
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            Killaura.mc.fontRendererObj.drawStringWithShadow(target.getName(), (float)scaledResolution.getScaledWidth() / 2.0f - (float)Killaura.mc.fontRendererObj.getStringWidth(target.getName()) / 2.0f, (float)scaledResolution.getScaledHeight() / 2.0f - 33.0f, 0xFFFFFF);
        }
    }

    private void setDelay() {
        this.delay = (int)(1000.0 / (double)this.aps) - new Random().nextInt(20);
    }

    private void setApsDelay() {
        this.apsDelay = 300 + new Random().nextInt(100);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void attack(EntityLivingBase entity) {
        if (Killaura.mc.thePlayer.isBlocking()) {
            Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (entity != null) {
            if (entity.getDistanceToEntity(Killaura.mc.thePlayer) <= 4.0f) {
                Killaura.mc.thePlayer.swingItem();
                Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((Entity)entity, C02PacketUseEntity.Action.ATTACK));
                Killaura.mc.thePlayer.onEnchantmentCritical(entity);
                Killaura.mc.thePlayer.onEnchantmentCritical(entity);
                this.hitting = true;
                return;
            }
        }
        Killaura.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        this.hitting = false;
    }

    @SubscribeEvent
    public void onRenderTick1(TickEvent.RenderTickEvent ev) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (target == null && this.autoblock.getEnable() && Killaura.mc.thePlayer.isBlocking() && !Mouse.isButtonDown((int)1) && this.hasSword()) {
            this.unBlock();
        }
        if (this.hasSword()) {
            if (target != null && this.autoblock.getEnable() && (double)target.getDistanceToEntity(Killaura.mc.thePlayer) < this.BlockRange.getCurrent()) {
                this.block();
            }
        }
        this.targets = this.getTargets(this.range.getCurrent() + 1.0);
        if (this.targets.size() > 1 && this.targets.size() < (int)this.switchEntitys.getCurrent()) {
            if (this.switchTimer.delay((long)this.SwitchDelay.getCurrent())) {
                ++this.index;
                this.switchTimer.reset();
            } else if (target != null && Killaura.target.hurtTime != 0 && Killaura.mc.thePlayer.ticksExisted % 60 == 0) {
                ++this.index;
            }
        }
        if (Killaura.mc.thePlayer.ticksExisted % (int)this.SwitchDelay.getCurrent() == 0 && this.targets.size() > 1 && this.switchMode.getCurrent() == "Single") {
            if ((double)target.getDistanceToEntity(Killaura.mc.thePlayer) > this.range.getCurrent()) {
                ++this.index;
            } else if (Killaura.target.isDead) {
                ++this.index;
            }
        }
        if (target != null) {
            target = null;
        }
        if (!this.targets.isEmpty()) {
            if (this.index >= this.targets.size()) {
                this.index = 0;
            }
            lastTarget = target;
            target = (EntityLivingBase)this.targets.get(this.index);
            if (this.yaw.getEnable()) {
                Killaura.mc.thePlayer.rotationYaw %= 360.0f;
            }
            lastYaw = this.pq(target)[0];
            lastPitch = this.pq(target)[1];
            if (Killaura.mc.thePlayer.motionX == 0.0 & Killaura.mc.thePlayer.motionZ == 0.0) {
                this.rotyaw = (int)((double)this.rotyaw + ((float)this.rotyaw < lastYaw ? (double)Math.abs(lastYaw - (float)this.rotyaw) * (this.turnSpeed.getCurrent() / 100.0) : (double)(-Math.abs(lastYaw - (float)this.rotyaw)) * (this.turnSpeed.getCurrent() / 100.0) * (double)(Killaura.mc.thePlayer.ticksExisted % 2)));
                this.rotpitch = (int)((double)this.rotpitch + ((float)this.rotpitch < lastPitch ? (double)Math.abs(lastPitch - (float)this.rotpitch) * (this.turnSpeed.getCurrent() / 100.0) : (double)(-Math.abs(lastPitch - (float)this.rotpitch)) * (this.turnSpeed.getCurrent() / 100.0)));
                Random random = new Random();
                if (this.pitch.getEnable()) {
                    Killaura.mc.thePlayer.rotationYaw = (float)this.rotyaw + random.nextFloat() / 2.0f;
                    Killaura.mc.thePlayer.rotationPitch = this.rotpitch;
                }
            }
        }
    }

    public float[] pq(EntityLivingBase EntityLivingBase2) {
        if (EntityLivingBase2 != null) {
            double d = Killaura.mc.thePlayer.posX;
            double d2 = Killaura.mc.thePlayer.posY + (double)Killaura.mc.thePlayer.getEyeHeight();
            double d3 = Killaura.mc.thePlayer.posZ;
            double d4 = EntityLivingBase2.posX;
            double d5 = EntityLivingBase2.posY + (double)(EntityLivingBase2.height / 2.0f);
            double d6 = EntityLivingBase2.posZ;
            double d7 = d - d4;
            double d8 = d2 - d5;
            double d9 = d3 - d6;
            double d10 = Math.sqrt(Math.pow(d7, 2.0) + Math.pow(d9, 2.0));
            float f = (float)(Math.toDegrees(Math.atan2(d9, d7)) + 90.0);
            float f2 = (float)Math.toDegrees(Math.atan2(d10, d8));
            return new float[]{(float)((double)f + (new Random().nextBoolean() ? Math.random() : -Math.random())), (float)((double)(90.0f - f2) + (new Random().nextBoolean() ? Math.random() : -Math.random()))};
        }
        return null;
    }

    public static float[] d(float[] arrf, float[] arrf2, float f) {
        double d = Killaura.getAngleDifference(arrf2[0], arrf[0]);
        double d2 = Killaura.getAngleDifference(arrf2[1], arrf[1]);
        arrf[0] = (float)((double)arrf[0] + (d > (double)f ? (double)f : (d < (double)(-f) ? (double)(-f) : d)));
        arrf[1] = (float)((double)arrf[1] + (d2 > (double)f ? (double)f : (d2 < (double)(-f) ? (double)(-f) : d2)));
        return arrf;
    }

    public static double getAngleDifference(double d, double d2) {
        return ((d - d2) % 360.0 + 540.0) % 360.0 - 180.0;
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent ev) {
        if (!Tools.isPlayerInGame()) {
            return;
        }
        if (target != null) {
            if (this.shouldAttack()) {
                if (this.hasSword() && Killaura.mc.thePlayer.isBlocking()) {
                    if (this.canAttack(target)) {
                        this.unBlock();
                    }
                }
                Killaura.mc.thePlayer.swingItem();
                if (this.AttackMode.getCurrent() == "Packet") {
                    mc.getNetHandler().addToSendQueue(new C02PacketUseEntity((Entity)target, C02PacketUseEntity.Action.ATTACK));
                } else if (this.AttackMode.getCurrent() == "Attach") {
                    Killaura.mc.playerController.attackEntity(Killaura.mc.thePlayer, target);
                }
                this.attackTimer.reset();
            }
            if (!Killaura.mc.thePlayer.isBlocking() && this.hasSword() && this.autoblock.getEnable() && (double)target.getDistanceToEntity(Killaura.mc.thePlayer) < this.BlockRange.getCurrent()) {
                this.block();
            }
        }
    }

    public List<Entity> getTargets(Double value) {
        ArrayList<Entity> ents = new ArrayList<Entity>();
        for (Entity ent : Killaura.mc.theWorld.loadedEntityList) {
            if (!((double)Killaura.mc.thePlayer.getDistanceToEntity(ent) <= value) || !this.canAttack(ent)) continue;
            if (ents.size() >= (int)this.switchEntitys.getCurrent()) {
                ents.remove(0);
            }
            ents.add(ent);
        }
        return ents;
    }

    public boolean canAttack(Entity e) {
        if (e == Killaura.mc.thePlayer) {
            return false;
        }
        if (!e.isEntityAlive()) {
            return false;
        }
        if (e instanceof EntityPlayer && this.players.getEnable()) {
            return true;
        }
        if (e instanceof EntityMob || e instanceof EntityBat || e instanceof EntityWaterMob && this.mobs.getEnable()) {
            return true;
        }
        if (e instanceof EntityAnimal && this.animals.getEnable()) {
            return true;
        }
        if (e.isInvisible() && this.invisibles.getEnable() && e instanceof EntityPlayer) {
            return true;
        }
        return e instanceof EntityVillager && this.villager.getEnable();
    }

    private boolean shouldAttack() {
        return this.attackTimer.isDelayComplete(1000.0 / this.cps.getCurrent());
    }

    public static boolean isTeam(EntityLivingBase entity) {
        Module teams = Client.moduleManager.getModule("Teams");
        if (teams.getState() && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (teams.isToggledMode("Base") && player.getTeam() != null && Killaura.mc.thePlayer.getTeam() != null && player.getTeam().isSameTeam(Killaura.mc.thePlayer.getTeam())) {
                return false;
            }
            if (teams.isToggledMode("ArmorColor") && !Killaura.checkEnemyColor(player)) {
                return false;
            }
            if (teams.isToggledMode("NameColor") && !Killaura.checkEnemyNameColor(player)) {
                return false;
            }
            if (teams.isToggledMode("TabList")) {
                Collection<NetworkPlayerInfo> list = Killaura.mc.thePlayer.sendQueue.getPlayerInfoMap();
                for (NetworkPlayerInfo networkplayerinfo : list) {
                    if (entity.getName() != Killaura.mc.ingameGUI.getTabList().getPlayerName(networkplayerinfo)) continue;
                }
            }
        }
        return true;
    }

    public static boolean checkEnemyColor(EntityPlayer enemy) {
        int colorEnemy0 = Killaura.getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(0));
        int colorEnemy1 = Killaura.getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(1));
        int colorEnemy2 = Killaura.getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(2));
        int colorEnemy3 = Killaura.getPlayerArmorColor(enemy, enemy.inventory.armorItemInSlot(3));
        int colorPlayer0 = Killaura.getPlayerArmorColor(Killaura.mc.thePlayer, Killaura.mc.thePlayer.inventory.armorItemInSlot(0));
        int colorPlayer1 = Killaura.getPlayerArmorColor(Killaura.mc.thePlayer, Killaura.mc.thePlayer.inventory.armorItemInSlot(1));
        int colorPlayer2 = Killaura.getPlayerArmorColor(Killaura.mc.thePlayer, Killaura.mc.thePlayer.inventory.armorItemInSlot(2));
        int colorPlayer3 = Killaura.getPlayerArmorColor(Killaura.mc.thePlayer, Killaura.mc.thePlayer.inventory.armorItemInSlot(3));
        return !(colorEnemy0 == colorPlayer0 && colorPlayer0 != -1 && colorEnemy0 != 1 || colorEnemy1 == colorPlayer1 && colorPlayer1 != -1 && colorEnemy1 != 1 || colorEnemy2 == colorPlayer2 && colorPlayer2 != -1 && colorEnemy2 != 1) && (colorEnemy3 != colorPlayer3 || colorPlayer3 == -1 || colorEnemy3 == 1);
    }

    public static int getPlayerArmorColor(EntityPlayer player, ItemStack stack) {
        if (player == null || stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemArmor)) {
            return -1;
        }
        ItemArmor itemArmor = (ItemArmor)stack.getItem();
        if (itemArmor == null || itemArmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER) {
            return -1;
        }
        return itemArmor.getColor(stack);
    }

    public static boolean checkEnemyNameColor(EntityLivingBase entity) {
        String name = entity.getDisplayName().getFormattedText();
        return !Killaura.getEntityNameColor(Killaura.mc.thePlayer).equals(Killaura.getEntityNameColor(entity));
    }

    public static String getEntityNameColor(EntityLivingBase entity) {
        String name = entity.getDisplayName().getFormattedText();
        if (name.contains("\u00a7")) {
            if (name.contains("\u00a71")) {
                return "\u00a71";
            }
            if (name.contains("\u00a72")) {
                return "\u00a72";
            }
            if (name.contains("\u00a73")) {
                return "\u00a73";
            }
            if (name.contains("\u00a74")) {
                return "\u00a74";
            }
            if (name.contains("\u00a75")) {
                return "\u00a75";
            }
            if (name.contains("\u00a76")) {
                return "\u00a76";
            }
            if (name.contains("\u00a77")) {
                return "\u00a77";
            }
            if (name.contains("\u00a78")) {
                return "\u00a78";
            }
            if (name.contains("\u00a79")) {
                return "\u00a79";
            }
            if (name.contains("\u00a70")) {
                return "\u00a70";
            }
            if (name.contains("\u00a7e")) {
                return "\u00a7e";
            }
            if (name.contains("\u00a7d")) {
                return "\u00a7d";
            }
            if (name.contains("\u00a7a")) {
                return "\u00a7a";
            }
            if (name.contains("\u00a7b")) {
                return "\u00a7b";
            }
            if (name.contains("\u00a7c")) {
                return "\u00a7c";
            }
            if (name.contains("\u00a7f")) {
                return "\u00a7f";
            }
        }
        return "null";
    }

    static {
        lastTarget = null;
    }
}

