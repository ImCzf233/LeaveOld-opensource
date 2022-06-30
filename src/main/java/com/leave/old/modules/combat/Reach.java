/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.combat;

import com.leave.old.Category;
import com.leave.old.modules.Module;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Reach
extends Module {
    public static IntegerSetting maxRange = new IntegerSetting("Max Range", 3.3, 3.0, 6.0, 1);
    public static IntegerSetting minRange = new IntegerSetting("Min Range", 3.1, 3.0, 6.0, 1);
    private static EnableSetting weapon_only = new EnableSetting("weapon_only", false);
    private static EnableSetting moving_only = new EnableSetting("moving_only", false);
    private static EnableSetting sprint_only = new EnableSetting("sprint_only", false);
    private static EnableSetting hit_through_blocks = new EnableSetting("ThroughBlocks", false);
    private static Random rand;

    public Reach() {
        super("Reach", 0, Category.Combat, false);
        this.getSetting().add(maxRange);
        this.getSetting().add(minRange);
        this.getSetting().add(weapon_only);
        this.getSetting().add(moving_only);
        this.getSetting().add(sprint_only);
        rand = new Random();
    }

    @SubscribeEvent
    public void onMove(MouseEvent ev) {
        BlockPos blocksReach;
        if (weapon_only.getEnable()) {
            if (Reach.mc.thePlayer.getCurrentEquippedItem() == null) {
                return;
            }
            if (!(Reach.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) && !(Reach.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe)) {
                return;
            }
        }
        if (moving_only.getEnable() && (double)Reach.mc.thePlayer.moveForward == 0.0 && (double)Reach.mc.thePlayer.moveStrafing == 0.0) {
            return;
        }
        if (sprint_only.getEnable() && !Reach.mc.thePlayer.isSprinting()) {
            return;
        }
        if (!hit_through_blocks.getEnable() && Reach.mc.objectMouseOver != null && (blocksReach = Reach.mc.objectMouseOver.getBlockPos()) != null && Reach.mc.theWorld.getBlockState(blocksReach).getBlock() != Blocks.air) {
            return;
        }
        double Reach2 = minRange.getCurrent();
        Object[] reachs = Reach.doReach(Reach2, 0.0, 0.0f);
        if (reachs == null) {
            return;
        }
        Reach.mc.objectMouseOver = new MovingObjectPosition((Entity)reachs[0], (Vec3)reachs[1]);
        Reach.mc.pointedEntity = (Entity)reachs[0];
    }

    public static Object[] doReach(double reachValue, double AABB, float cwc) {
        Entity target = mc.getRenderViewEntity();
        Entity entity = null;
        if (target == null || Reach.mc.theWorld == null) {
            return null;
        }
        Reach.mc.mcProfiler.startSection("pick");
        Vec3 targetEyes = target.getPositionEyes(0.0f);
        Vec3 targetLook = target.getLook(0.0f);
        Vec3 targetVector = targetEyes.addVector(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue);
        Vec3 targetVec = null;
        List<Entity> targetHitbox = Reach.mc.theWorld.getEntitiesWithinAABBExcludingEntity(target, target.getEntityBoundingBox().addCoord(targetLook.xCoord * reachValue, targetLook.yCoord * reachValue, targetLook.zCoord * reachValue).expand(1.0, 1.0, 1.0));
        double reaching = reachValue;
        for (int i = 0; i < targetHitbox.size(); ++i) {
            double targetHitVec;
            Entity targetEntity = targetHitbox.get(i);
            if (!targetEntity.canBeCollidedWith()) continue;
            float targetCollisionBorderSize = targetEntity.getCollisionBorderSize();
            AxisAlignedBB targetAABB = targetEntity.getEntityBoundingBox().expand(targetCollisionBorderSize, targetCollisionBorderSize, targetCollisionBorderSize);
            targetAABB = targetAABB.expand(AABB, AABB, AABB);
            MovingObjectPosition tagetPosition = targetAABB.calculateIntercept(targetEyes, targetVector);
            if (targetAABB.isVecInside(targetEyes)) {
                if (!(0.0 < reaching) && reaching != 0.0) continue;
                entity = targetEntity;
                targetVec = tagetPosition == null ? targetEyes : tagetPosition.hitVec;
                reaching = 0.0;
                continue;
            }
            if (tagetPosition == null || !((targetHitVec = targetEyes.distanceTo(tagetPosition.hitVec)) < reaching) && reaching != 0.0) continue;
            boolean canRiderInteract = false;
            if (targetEntity == target.ridingEntity) {
                if (reaching != 0.0) continue;
                entity = targetEntity;
                targetVec = tagetPosition.hitVec;
                continue;
            }
            entity = targetEntity;
            targetVec = tagetPosition.hitVec;
            reaching = targetHitVec;
        }
        if (reaching < reachValue && !(entity instanceof EntityLivingBase) && !(entity instanceof EntityItemFrame)) {
            entity = null;
        }
        Reach.mc.mcProfiler.endSection();
        if (entity == null || targetVec == null) {
            return null;
        }
        return new Object[]{entity, targetVec};
    }
}

