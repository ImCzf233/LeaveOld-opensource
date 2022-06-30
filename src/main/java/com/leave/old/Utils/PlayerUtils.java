/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Multimap
 *  org.lwjgl.util.vector.Vector3f
 */
package com.leave.old.Utils;

import com.google.common.collect.Multimap;
import com.leave.old.event.EventMove;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.util.vector.Vector3f;

public class PlayerUtils {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final Minecraft MC = Minecraft.getMinecraft();

    public static boolean isAirUnder(Entity ent) {
        return PlayerUtils.mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1.0, ent.posZ)).getBlock() == Blocks.air;
    }

    public static boolean isHoldingSword() {
        return PlayerUtils.mc.thePlayer.getCurrentEquippedItem() != null && PlayerUtils.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        float yaw = PlayerUtils.mc.thePlayer.rotationYaw;
        if (PlayerUtils.mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtils.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (PlayerUtils.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtils.mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerUtils.mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static boolean isInWater() {
        return PlayerUtils.mc.theWorld.getBlockState(new BlockPos(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY, PlayerUtils.mc.thePlayer.posZ)).getBlock().getMaterial() == Material.water;
    }

    public static boolean isInLiquid() {
        if (PlayerUtils.mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        int y = (int)PlayerUtils.mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(PlayerUtils.mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = PlayerUtils.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block.getMaterial() == Material.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }

    public static void toFwd(double speed) {
        float yaw = PlayerUtils.mc.thePlayer.rotationYaw * ((float)Math.PI / 180);
        EntityPlayerSP thePlayer = PlayerUtils.mc.thePlayer;
        thePlayer.motionX -= (double)MathHelper.sin(yaw) * speed;
        EntityPlayerSP thePlayer2 = PlayerUtils.mc.thePlayer;
        thePlayer2.motionZ += (double)MathHelper.cos(yaw) * speed;
    }

    public static void setSpeed(double speed) {
        PlayerUtils.mc.thePlayer.motionX = -(Math.sin(PlayerUtils.getDirection()) * speed);
        PlayerUtils.mc.thePlayer.motionZ = Math.cos(PlayerUtils.getDirection()) * speed;
    }

    public static double getSpeed() {
        return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return PlayerUtils.getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
        return PlayerUtils.getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(double tpX, double tpY, double tpZ, double speed) {
        double d;
        ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        Minecraft mc = Minecraft.getMinecraft();
        double posX = tpX - mc.thePlayer.posX;
        double posY = tpY - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight() + 1.1);
        double posZ = tpZ - mc.thePlayer.posZ;
        float yaw = (float)(Math.atan2(posZ, posX) * 180.0 / Math.PI - 90.0);
        float pitch = (float)(-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0 / Math.PI);
        double tmpX = mc.thePlayer.posX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ = mc.thePlayer.posZ;
        double steps = 1.0;
        for (d = speed; d < PlayerUtils.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            steps += 1.0;
        }
        for (d = speed; d < PlayerUtils.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY, tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - Math.sin(PlayerUtils.getDirection(yaw)) * d;
            tmpZ = mc.thePlayer.posZ + Math.cos(PlayerUtils.getDirection(yaw)) * d;
            positions.add(new Vector3f((float)tmpX, (float)(tmpY -= (mc.thePlayer.posY - tpY) / steps), (float)tmpZ));
        }
        positions.add(new Vector3f((float)tpX, (float)tpY, (float)tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        return yaw *= (float)Math.PI / 180;
    }

    public static double getDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = x1 - x2;
        double d2 = y1 - y2;
        double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        return PlayerUtils.mc.gameSettings.keyBindForward.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindLeft.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindRight.isKeyDown() || PlayerUtils.mc.gameSettings.keyBindBack.isKeyDown();
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
        if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value && stack.getItem() instanceof ItemSword && (double)mc.thePlayer.swingProgress > 0.2) {
            mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        Iterator iterator;
        Multimap<String, AttributeModifier> multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
            double damage;
            Map.Entry entry = (Map.Entry)iterator.next();
            AttributeModifier attributeModifier = (AttributeModifier)entry.getValue();
            double d = damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2 ? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0;
            if (attributeModifier.getAmount() > 1.0) {
                return 1.0f + (float)damage;
            }
            return 1.0f;
        }
        return 1.0f;
    }

    public static List<EntityLivingBase> getLivingEntities() {
        return Arrays.asList(Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> entity != Minecraft.getMinecraft().thePlayer).map(entity -> (EntityLivingBase)entity).toArray(EntityLivingBase[]::new));
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = PlayerUtils.mc.thePlayer.inventoryContainer.getSlot(i1).getStack();
            if (itemstack == null || itemstack.getItem() != i) continue;
            PlayerUtils.mc.playerController.windowClick(0, i1, 0, 1, PlayerUtils.mc.thePlayer);
            break;
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = PlayerUtils.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemstack != null) continue;
            return false;
        }
        return true;
    }

    public static boolean isMoving() {
        if (!PlayerUtils.mc.thePlayer.isCollidedHorizontally && !PlayerUtils.mc.thePlayer.isSneaking()) {
            return PlayerUtils.mc.thePlayer.movementInput.moveForward != 0.0f || PlayerUtils.mc.thePlayer.movementInput.moveStrafe != 0.0f;
        }
        return false;
    }

    public static boolean isMoving2() {
        return PlayerUtils.mc.thePlayer.moveForward != 0.0f || PlayerUtils.mc.thePlayer.moveStrafing != 0.0f;
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = (double)endPos.getX() + 0.5;
        double endY = (double)endPos.getY() + 1.0;
        double endZ = (double)endPos.getZ() + 0.5;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack) {
            double offset;
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) break;
            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double d = offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
            if (diffX < 0.0) {
                curX = Math.abs(diffX) > offset ? (curX += offset) : (curX += Math.abs(diffX));
            }
            if (diffX > 0.0) {
                curX = Math.abs(diffX) > offset ? (curX -= offset) : (curX -= Math.abs(diffX));
            }
            if (diffY < 0.0) {
                curY = Math.abs(diffY) > 0.25 ? (curY += 0.25) : (curY += Math.abs(diffY));
            }
            if (diffY > 0.0) {
                curY = Math.abs(diffY) > 0.25 ? (curY -= 0.25) : (curY -= Math.abs(diffY));
            }
            if (diffZ < 0.0) {
                curZ = Math.abs(diffZ) > offset ? (curZ += offset) : (curZ += Math.abs(diffZ));
            }
            if (diffZ > 0.0) {
                curZ = Math.abs(diffZ) > offset ? (curZ -= offset) : (curZ -= Math.abs(diffZ));
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }

    public static void damage(int damage) {
        for (int index = 0; index <= 67 + 23 * (damage - 1); ++index) {
            PlayerUtils.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY + 2.535E-9, PlayerUtils.mc.thePlayer.posZ, false));
            PlayerUtils.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY + 1.05E-10, PlayerUtils.mc.thePlayer.posZ, false));
            PlayerUtils.mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(PlayerUtils.mc.thePlayer.posX, PlayerUtils.mc.thePlayer.posY + 0.0448865, PlayerUtils.mc.thePlayer.posZ, false));
        }
    }

    public static List<AxisAlignedBB> getCollidingBoundingList(EntityPlayerSP thePlayer, float f) {
        return PlayerUtils.mc.theWorld.getCollidingBoundingBoxes(thePlayer, thePlayer.getEntityBoundingBox().offset(0.0, -f, 0.0));
    }

    public static final Block getBlockBelowEntity(Entity entity, double offset) {
        Vec3 below = entity.getPositionVector();
        return PlayerUtils.MC.theWorld.getBlockState(new BlockPos(below).add(0.0, -offset, 0.0)).getBlock();
    }

    public static final Block getBlockBelowEntity(Entity entity) {
        return PlayerUtils.getBlockBelowEntity(entity, 1.0);
    }

    public static final Block getBlockBelowPlayer() {
        return PlayerUtils.getBlockBelowEntity(PlayerUtils.MC.thePlayer);
    }

    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians(yaw)) * (double)multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double)multiplyer;
        double moveY = up;
        PlayerUtils.mc.thePlayer.setPosition(moveX + PlayerUtils.mc.thePlayer.posX, moveY + PlayerUtils.mc.thePlayer.posY, moveZ + PlayerUtils.mc.thePlayer.posZ);
    }

    public final Block getBlockBelowPlayer(double offset) {
        return PlayerUtils.getBlockBelowEntity(PlayerUtils.MC.thePlayer, offset);
    }

    public final boolean isTeamMate(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        if (PlayerUtils.MC.thePlayer.getTeam() != null && entity.getTeam() != null && PlayerUtils.MC.thePlayer.isOnSameTeam(entity)) {
            return true;
        }
        if (PlayerUtils.MC.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            String playerName = PlayerUtils.MC.thePlayer.getDisplayName().getFormattedText().replace("\u6924\ue04e\u62f7", "");
            String entityName = entity.getDisplayName().getFormattedText().replace("\u6924\ue04e\u62f7", "");
            if (playerName.isEmpty() || entityName.isEmpty()) {
                return false;
            }
            return playerName.charAt(1) == entityName.charAt(1);
        }
        return false;
    }

    public static void setSpeed(double speed, float yaw, double strafe, double forward) {
        if (forward == 0.0 && strafe == 0.0) {
            PlayerUtils.mc.thePlayer.motionX = 0.0;
            PlayerUtils.mc.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                forward = forward > 0.0 ? 1.0 : -1.0;
            }
            PlayerUtils.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            PlayerUtils.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void setSpeed(EventMove event, double speed) {
        float yaw = PlayerUtils.mc.thePlayer.rotationYaw;
        double forward = PlayerUtils.mc.thePlayer.movementInput.moveForward;
        double strafe = PlayerUtils.mc.thePlayer.movementInput.moveStrafe;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                forward = forward > 0.0 ? 1.0 : -1.0;
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    public static double getLastDist() {
        double xDist = PlayerUtils.mc.thePlayer.posX - PlayerUtils.mc.thePlayer.prevPosX;
        double zDist = PlayerUtils.mc.thePlayer.posZ - PlayerUtils.mc.thePlayer.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static ArrayList<BlockPos> getBlockPosesEntityIsStandingOn(Entity en) {
        BlockPos pos1 = new BlockPos(en.getCollisionBoundingBox().minX, en.getCollisionBoundingBox().minY - 0.01, en.getCollisionBoundingBox().minZ);
        BlockPos pos2 = new BlockPos(en.getCollisionBoundingBox().maxX, en.getCollisionBoundingBox().minY - 0.01, en.getCollisionBoundingBox().maxZ);
        Iterable<BlockPos> collisionBlocks = BlockPos.getAllInBox(pos1, pos2);
        ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
        for (BlockPos pos : collisionBlocks) {
            returnList.add(pos);
        }
        return returnList;
    }

    public static boolean isEntityOnGround(Entity en) {
        ArrayList<BlockPos> poses = PlayerUtils.getBlockPosesEntityIsStandingOn(en);
        for (BlockPos pos : poses) {
            IBlockState blockState = PlayerUtils.getBlockState(pos);
            Block block = blockState.getBlock();
            if (blockState.getBlock().getMaterial() instanceof MaterialTransparent || blockState.getBlock().getMaterial() == Material.air || block instanceof BlockLiquid || !blockState.getBlock().isFullCube()) continue;
            return true;
        }
        return false;
    }

    public static IBlockState getBlockState(BlockPos blockPos) {
        return PlayerUtils.mc.theWorld.getBlockState(blockPos);
    }
}

