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
import com.leave.old.modules.Module;
import com.leave.old.modules.combat.Reach;
import com.leave.old.modules.render.ClickGui;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class HitBox
extends Module {
    private ModeSetting mode = new ModeSetting("Mode", "Basic", Arrays.asList("Basic", "Box", "Raven"), this);
    private IntegerSetting width = new IntegerSetting("Width", 1.0, 0.6, 5.0, 1);
    private IntegerSetting height = new IntegerSetting("Height", 2.2, 1.8, 5.0, 1);
    private IntegerSetting expand = new IntegerSetting("Expand", 0.1, 1.0, 2.0, 1);
    private EnableSetting extra = new EnableSetting("Extra", false);
    private EnableSetting noFire = new EnableSetting("NoFire", false);
    private IntegerSetting extraV = new IntegerSetting("ExtraExpand", 0.0, 0.0, 15.0, 1);
    private IntegerSetting Multiplier = new IntegerSetting("Multiplier", 1.2, 1.0, 5.0, 1);
    private EnableSetting showBox = new EnableSetting("ShowBox", true);
    private EnableSetting walls = new EnableSetting("ThroughWalls", false);
    int getInput = 2;
    private static MovingObjectPosition mv;

    public HitBox() {
        super("HitBox", 0, Category.Combat, false);
        this.getSetting().add(this.mode);
        this.getSetting().add(this.width);
        this.getSetting().add(this.height);
        this.getSetting().add(this.expand);
        this.getSetting().add(this.extra);
        this.getSetting().add(this.noFire);
        this.getSetting().add(this.extraV);
        this.getSetting().add(this.Multiplier);
        this.getSetting().add(this.showBox);
        this.getSetting().add(this.walls);
    }

    public static void setEntityBoundingBoxSize(Entity entity, float width, float height) {
        if (entity.width == width && entity.height == height) {
            return;
        }
        entity.width = width;
        entity.height = height;
        double d0 = (double)width / 2.0;
        entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0, entity.posY + (double)entity.height, entity.posZ + d0));
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (event.button == 0 && event.buttonstate && mv != null) {
            HitBox.mc.objectMouseOver = mv;
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (this.mode.getCurrent().equalsIgnoreCase("Raven")) {
            Module click = Client.moduleManager.getModule("AutoClicker");
            if (click != null && !click.getState()) {
                return;
            }
            if (click != null && click.getState() && Mouse.isButtonDown((int)0) && mv != null) {
                HitBox.mc.objectMouseOver = mv;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (this.mode.getCurrent().equalsIgnoreCase("Raven") && this.showBox.getEnable()) {
            for (Entity en : HitBox.mc.theWorld.loadedEntityList) {
                if (en == HitBox.mc.thePlayer || !(en instanceof EntityLivingBase) || ((EntityLivingBase)en).deathTime != 0 || en instanceof EntityArmorStand || en.isInvisible()) continue;
                this.rh(en, new Color((int)ClickGui.red.getCurrent(), (int)ClickGui.green.getCurrent(), (int)ClickGui.blue.getCurrent()), event.partialTicks);
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mode.getCurrent().equalsIgnoreCase("Basic")) {
            for (Entity object : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
                EntityLivingBase entity;
                if (!(object instanceof EntityLivingBase) || !this.check(entity = (EntityLivingBase)object)) continue;
                if (this.noFire.getEnable()) {
                    entity.setFire(0);
                }
                HitBox.setEntityBoundingBoxSize(entity, (float)this.width.getCurrent(), (float)this.height.getCurrent());
            }
        } else if (this.mode.getCurrent().equalsIgnoreCase("Box")) {
            List loadedEntityList = Reach.mc.theWorld.loadedEntityList;
            for (int i = 0; i < loadedEntityList.size(); ++i) {
                Entity e = (Entity)loadedEntityList.get(i);
                if (this.noFire.getEnable()) {
                    e.setFire(0);
                }
                e.width = (float)(this.extra.getEnable() ? 0.6 + this.expand.getCurrent() + this.extraV.getCurrent() : 0.6 + this.expand.getCurrent());
            }
        } else if (this.mode.getCurrent().equalsIgnoreCase("Raven")) {
            this.gmo(1.0f);
        }
    }

    public void gmo(float partialTicks) {
        if (mc.getRenderViewEntity() != null && HitBox.mc.theWorld != null) {
            HitBox.mc.pointedEntity = null;
            Entity pE = null;
            double d0 = 3.0;
            mv = mc.getRenderViewEntity().rayTrace(d0, partialTicks);
            double d2 = d0;
            Vec3 vec3 = mc.getRenderViewEntity().getPositionEyes(partialTicks);
            if (mv != null) {
                d2 = HitBox.mv.hitVec.distanceTo(vec3);
            }
            Vec3 vec4 = mc.getRenderViewEntity().getLook(partialTicks);
            Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
            Vec3 vec6 = null;
            float f1 = 1.0f;
            List<Entity> list = HitBox.mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.getRenderViewEntity(), mc.getRenderViewEntity().getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(f1, f1, f1));
            double d3 = d2;
            for (Entity o : list) {
                double d4;
                Entity entity = o;
                if (!entity.canBeCollidedWith()) continue;
                float ex = (float)((double)entity.getCollisionBorderSize() * this.exp(entity));
                AxisAlignedBB ax = entity.getEntityBoundingBox().expand(ex, ex, ex);
                MovingObjectPosition mop = ax.calculateIntercept(vec3, vec5);
                if (ax.isVecInside(vec3)) {
                    if (!(0.0 < d3) && d3 != 0.0) continue;
                    pE = entity;
                    vec6 = mop == null ? vec3 : mop.hitVec;
                    d3 = 0.0;
                    continue;
                }
                if (mop == null || !((d4 = vec3.distanceTo(mop.hitVec)) < d3) && d3 != 0.0) continue;
                if (entity == HitBox.mc.getRenderViewEntity().ridingEntity && !entity.canRiderInteract()) {
                    if (d3 != 0.0) continue;
                    pE = entity;
                    vec6 = mop.hitVec;
                    continue;
                }
                pE = entity;
                vec6 = mop.hitVec;
                d3 = d4;
            }
            if (pE != null && (d3 < d2 || mv == null)) {
                mv = new MovingObjectPosition(pE, vec6);
                if (pE instanceof EntityLivingBase || pE instanceof EntityItemFrame) {
                    HitBox.mc.pointedEntity = pE;
                }
            }
        }
    }

    private void rh(Entity e, Color c, float partialTicks) {
        if (e instanceof EntityLivingBase) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)partialTicks - HitBox.mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)partialTicks - HitBox.mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)partialTicks - HitBox.mc.getRenderManager().viewerPosZ;
            float ex = (float)((double)e.getCollisionBorderSize() * (double)this.getInput);
            AxisAlignedBB bbox = e.getEntityBoundingBox().expand(ex, ex, ex);
            AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)3553);
            GL11.glDisable((int)2929);
            GL11.glDepthMask((boolean)false);
            GL11.glLineWidth((float)2.0f);
            GL11.glColor3d((double)c.getRed(), (double)c.getGreen(), (double)c.getBlue());
            RenderGlobal.drawSelectionBoundingBox(axis);
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDepthMask((boolean)true);
            GL11.glDisable((int)3042);
        }
    }

    public boolean check(EntityLivingBase entity) {
        if (entity instanceof EntityArmorStand) {
            return false;
        }
        if (entity == HitBox.mc.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (this.walls.getEnable() && HitBox.mc.thePlayer.canEntityBeSeen(entity)) {
            return false;
        }
        return entity.canBeCollidedWith();
    }

    public double exp(Entity en) {
        Module hitBox = Client.moduleManager.getModule("HitBox");
        return hitBox != null && this.getState() ? (double)this.getInput : 1.0;
    }

    class EntitySize {
        public float width;
        public float height;

        public EntitySize(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}

