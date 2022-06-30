/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.modules.movement;

import com.leave.old.Category;
import com.leave.old.Utils.BlockUtils;
import com.leave.old.Utils.ChatUtils;
import com.leave.old.Utils.Connection;
import com.leave.old.Utils.Mappings;
import com.leave.old.Utils.MoveUtils;
import com.leave.old.Utils.TimerUtils;
import com.leave.old.Utils.Wrapper;
import com.leave.old.modules.Module;
import com.leave.old.settings.EnableSetting;
import com.leave.old.settings.IntegerSetting;
import com.leave.old.settings.ModeSetting;
import java.util.Arrays;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Speed
extends Module {
    public boolean shouldslow = false;
    double count = 0.0;
    int jumps;
    private float air;
    private float ground;
    private float aacSlow;
    public static TimerUtils timer = new TimerUtils();
    boolean collided = false;
    boolean lessSlow;
    int spoofSlot = 0;
    double less;
    double stair;
    private double speed;
    private double speedvalue;
    private double lastDist;
    public static int stage;
    public static int aacCount;
    public static Timer timers;
    double movementSpeed;
    TimerUtils aac = new TimerUtils();
    TimerUtils lastFall = new TimerUtils();
    TimerUtils lastCheck = new TimerUtils();
    Timer mctimer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
    private int level = 1;
    private double moveSpeed = 0.2873;
    private double lastDist_ = 0.0;
    private int timerDelay = 0;
    private ModeSetting mode = new ModeSetting("Mode", "HypixelNew", Arrays.asList("Basic", "OldHypixel", "BHOP", "OnGround", "AAC", "HypixelNew"), this);
    private IntegerSetting speedVaule = new IntegerSetting("SpeedVaule", 0.25, 0.01, 10.0, 2);
    private EnableSetting lagback = new EnableSetting("LagBack", true);
    private EnableSetting fastfall = new EnableSetting("FastFall", false);

    public Speed() {
        super("Speed", 0, Category.Movement, false);
        this.getSetting().add(this.mode);
        this.getSetting().add(this.speedVaule);
        this.getSetting().add(this.lagback);
        this.getSetting().add(this.fastfall);
    }

    @Override
    public void enable() {
        this.less = 0.0;
        this.jumps = 0;
        this.count = 0.0;
        this.lastDist = 0.0;
        stage = 2;
        this.air = 0.0f;
        Speed.timers.timerSpeed = 1.0f;
        if (this.mode.getCurrent().equals("NCP")) {
            this.level = Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically ? 1 : 4;
        }
        super.enable();
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (this.mode.getCurrent().equals("NCP")) {
            double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
            double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
            this.lastDist = MathHelper.sqrt_double(xDist * xDist + zDist * zDist);
        } else if (this.mode.getCurrent().equals("OnGround")) {
            Speed.timers.timerSpeed = 1.085f;
            double forward = Speed.mc.thePlayer.movementInput.moveForward;
            double strafe = Speed.mc.thePlayer.movementInput.moveStrafe;
            if (forward == 0.0 && strafe == 0.0 || Speed.mc.gameSettings.keyBindJump.isKeyDown() || Speed.mc.thePlayer.isInWater() || Speed.mc.thePlayer.isOnLadder() || Speed.mc.thePlayer.isCollidedHorizontally || !Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, 0.4, 0.0)).isEmpty()) {
                // empty if block
            }
            this.speed = Math.max(Speed.mc.thePlayer.ticksExisted % 2 == 0 ? 2.1 : 1.3, MoveUtils.defaultSpeed());
            float yaw = Speed.mc.thePlayer.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                Speed.mc.thePlayer.motionX = 0.0;
                Speed.mc.thePlayer.motionZ = 0.0;
            } else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += (float)(forward > 0.0 ? -45 : 45);
                    } else if (strafe < 0.0) {
                        yaw += (float)(forward > 0.0 ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 0.15;
                    } else if (forward < 0.0) {
                        forward = -0.15;
                    }
                }
                if (strafe > 0.0) {
                    strafe = 0.15;
                } else if (strafe < 0.0) {
                    strafe = -0.15;
                }
                Speed.mc.thePlayer.motionX = forward * this.speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * this.speed * Math.sin(Math.toRadians(yaw + 90.0f));
                Speed.mc.thePlayer.motionZ = forward * this.speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * this.speed * Math.cos(Math.toRadians(yaw + 90.0f));
            }
        } else if (this.mode.getCurrent().equals("HypixelNew")) {
            if (Speed.mc.thePlayer.onGround && MoveUtils.isMoving()) {
                Speed.mc.thePlayer.jump();
                stage = 0;
                this.speed = 1.1f;
            }
            this.speed -= 0.004;
            MoveUtils.setSpeed(MoveUtils.getBaseMoveSpeed() * this.speed);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (this.mode.getCurrent().equals("Basic")) {
            boolean boost;
            boolean bl = boost = Math.abs(Speed.mc.thePlayer.rotationYawHead - Speed.mc.thePlayer.rotationYaw) < 90.0f;
            if (Speed.mc.thePlayer.moveForward > 0.0f && Speed.mc.thePlayer.hurtTime < 5) {
                if (Speed.mc.thePlayer.onGround) {
                    Speed.mc.thePlayer.motionY = 0.405;
                    float f = Speed.getDirection();
                    Speed.mc.thePlayer.motionX -= (double)(MathHelper.sin(f) * 0.2f);
                    Speed.mc.thePlayer.motionZ += (double)(MathHelper.cos(f) * 0.2f);
                } else {
                    double currentSpeed = Math.sqrt(Speed.mc.thePlayer.motionX * Speed.mc.thePlayer.motionX + Speed.mc.thePlayer.motionZ * Speed.mc.thePlayer.motionZ);
                    double speed = boost ? 1.0064 : 1.001;
                    double direction = Speed.getDirection();
                    Speed.mc.thePlayer.motionX = -Math.sin(direction) * speed * currentSpeed;
                    Speed.mc.thePlayer.motionZ = Math.cos(direction) * speed * currentSpeed;
                }
            }
        } else if (this.mode.getCurrent().equals("BvdLove")) {
            if (Speed.mc.thePlayer.onGround && Speed.MovementInput() && !Speed.mc.thePlayer.isInWater()) {
                Timer timer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
                timer.timerSpeed = 1.0f;
                Speed.mc.thePlayer.jump();
            } else if (Speed.MovementInput() && !Speed.mc.thePlayer.isInWater()) {
                Timer timer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
                Speed.setSpeed(this.speedVaule.getCurrent());
            }
        } else if (this.mode.getCurrent().equals("OldHypixel")) {
            if (Speed.mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            this.less -= this.less > 1.0 ? 0.12 : 0.11;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && Speed.isMoving2()) {
                this.collided = Speed.mc.thePlayer.isCollidedHorizontally;
                if (stage >= 0 || this.collided) {
                    stage = 0;
                    double motY = 0.407 + (double)MoveUtils.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        Speed.mc.thePlayer.jump();
                        Speed.mc.thePlayer.motionY = motY;
                    }
                    this.less += 1.0;
                    this.lessSlow = this.less > 1.0 && !this.lessSlow;
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(stage) + 0.0331;
            this.speed *= 0.91;
            if (this.stair > 0.0) {
                this.speed *= 0.7 - (double)MoveUtils.getSpeedEffect() * 0.1;
            }
            if (stage < 0) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.95;
            }
            if (BlockUtils.isInLiquid()) {
                this.speed = 0.55;
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(this.speed);
                ++stage;
            }
        } else if (this.mode.getCurrent().equals("BHOP")) {
            if (Speed.mc.thePlayer.moveForward == 0.0f && Speed.mc.thePlayer.moveStrafing == 0.0f) {
                this.speed = MoveUtils.defaultSpeed();
            }
            if (stage == 1 && Speed.mc.thePlayer.isCollidedVertically && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                this.speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
            }
            if (!BlockUtils.isInLiquid() && stage == 2 && Speed.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                Speed.mc.thePlayer.motionY = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump) ? 0.41999998688698 + (double)(Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1 : 0.41999998688698;
                Speed.mc.thePlayer.jump();
                this.speed *= 1.533;
            } else if (stage == 3) {
                double difference = 0.66 * (this.lastDist - MoveUtils.defaultSpeed());
                this.speed = this.lastDist - difference;
            } else {
                List<AxisAlignedBB> collidingList = Wrapper.INSTANCE.world().getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.getEntityBoundingBox().offset(0.0, Speed.mc.thePlayer.motionY, 0.0));
                if ((collidingList.size() > 0 || Speed.mc.thePlayer.isCollidedVertically) && stage > 0) {
                    stage = Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f ? 1 : 0;
                }
                this.speed = this.lastDist - this.lastDist / 159.0;
            }
            this.speed = Math.max(this.speed, MoveUtils.defaultSpeed());
            if (stage > 0) {
                if (BlockUtils.isInLiquid()) {
                    this.speed = 0.1;
                }
                this.setMotion(this.speed);
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                ++stage;
            }
        } else if (this.mode.getCurrent().equals("AAC")) {
            if ((double)Speed.mc.thePlayer.fallDistance > 1.2) {
                this.lastFall.reset();
            }
            if (!BlockUtils.isInLiquid() && Speed.mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                stage = 0;
                Speed.mc.thePlayer.jump();
                Speed.mc.thePlayer.motionY = Speed.mc.thePlayer.motionY = 0.41999998688698 + (double)MoveUtils.getJumpEffect();
                if (aacCount < 4) {
                    ++aacCount;
                }
            }
            this.speed = this.getAACSpeed(stage, aacCount);
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                if (BlockUtils.isInLiquid()) {
                    this.speed = 0.075;
                }
                this.setMotion(this.speed);
            }
            if (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) {
                ++stage;
            }
        }
    }

    public static boolean isMoving2() {
        return Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f;
    }

    @Override
    public void disable() {
        Timer timer = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
        timer.timerSpeed = 1.0f;
        this.moveSpeed = this.baseMoveSpeed();
        this.level = 0;
        super.disable();
    }

    public static boolean MovementInput() {
        return Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isKeyDown() || Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isKeyDown() || Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isKeyDown() || Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isKeyDown();
    }

    public static boolean MineLandMovementInput() {
        return (Wrapper.INSTANCE.mc().gameSettings.keyBindLeft.isKeyDown() || Wrapper.INSTANCE.mc().gameSettings.keyBindRight.isKeyDown() || Wrapper.INSTANCE.mc().gameSettings.keyBindBack.isKeyDown()) && !Wrapper.INSTANCE.mc().gameSettings.keyBindForward.isKeyDown();
    }

    public static float getDirection() {
        float var1 = Wrapper.INSTANCE.mc().thePlayer.rotationYaw;
        if (Wrapper.INSTANCE.mc().thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Wrapper.INSTANCE.mc().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Wrapper.INSTANCE.mc().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Wrapper.INSTANCE.mc().thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Wrapper.INSTANCE.mc().thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= (float)Math.PI / 180;
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

    public static void setSpeed(double speed) {
        Wrapper.INSTANCE.mc().thePlayer.motionX = -Math.sin(Speed.getDirection()) * speed;
        Wrapper.INSTANCE.mc().thePlayer.motionZ = Math.cos(Speed.getDirection()) * speed;
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (this.lagback.getEnable() && side == Connection.Side.IN && packet instanceof S08PacketPlayerPosLook) {
            ChatUtils.warning("Lagback checks->Speed");
            Speed.mc.thePlayer.onGround = false;
            Speed.mc.thePlayer.motionX *= 0.0;
            Speed.mc.thePlayer.motionZ *= 0.0;
            Speed.mc.thePlayer.jumpMovementFactor = 0.0f;
            this.toggle();
            stage = -4;
        }
        return super.onPacket(packet, side);
    }

    public int getSpeedEffect() {
        if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    private double defaultSpeed() {
        double baseSpeed = 0.2873;
        return baseSpeed;
    }

    private double baseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            baseSpeed *= 1.0 + 0.2 * (double)(Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return baseSpeed;
    }

    private double getAACSpeed(int stage, int jumps) {
        double value = 0.29;
        double firstvalue = 0.3019;
        double thirdvalue = 0.0286 - (double)stage / 1000.0;
        if (stage == 0) {
            Block block;
            value = 0.497;
            if (jumps >= 2) {
                value += 0.1069;
            }
            if (jumps >= 3) {
                value += 0.046;
            }
            if ((block = MoveUtils.getBlockUnderPlayer(Speed.mc.thePlayer, 0.01)) instanceof BlockIce || block instanceof BlockPackedIce) {
                value = 0.59;
            }
        } else if (stage == 1) {
            value = 0.3031;
            if (jumps >= 2) {
                value += 0.0642;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 2) {
            value = 0.302;
            if (jumps >= 2) {
                value += 0.0629;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 3) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0607;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 4) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0584;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 5) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0561;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 6) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0539;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 7) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0517;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 8) {
            value = firstvalue;
            if (MoveUtils.isOnGround(0.05)) {
                value -= 0.002;
            }
            if (jumps >= 2) {
                value += 0.0496;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 9) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0475;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 10) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0455;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 11) {
            value = 0.3;
            if (jumps >= 2) {
                value += 0.045;
            }
            if (jumps >= 3) {
                value += 0.018;
            }
        } else if (stage == 12) {
            value = 0.301;
            if (jumps <= 2) {
                aacCount = 0;
            }
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        } else if (stage == 13) {
            value = 0.298;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        } else if (stage == 14) {
            value = 0.297;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        if (Speed.mc.thePlayer.moveForward <= 0.0f) {
            value -= 0.06;
        }
        if (Speed.mc.thePlayer.isCollidedHorizontally) {
            value -= 0.1;
            aacCount = 0;
        }
        return value;
    }

    private double getHypixelSpeed(int stage) {
        double value = MoveUtils.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect() + (double)MoveUtils.getSpeedEffect() / 15.0;
        double firstvalue = 0.4145 + (double)MoveUtils.getSpeedEffect() / 12.5;
        double decr = (double)stage / 500.0 * 2.0;
        if (stage == 0) {
            if (timer.delay(300.0f)) {
                timer.reset();
            }
            if (!this.lastCheck.delay(500.0f)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            } else if (this.shouldslow) {
                this.shouldslow = false;
            }
            value = 0.64 + ((double)MoveUtils.getSpeedEffect() + 0.028 * (double)MoveUtils.getSpeedEffect()) * 0.134;
        } else if (stage == 1) {
            value = firstvalue;
        } else if (stage >= 2) {
            value = firstvalue - decr;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0f) || this.collided) {
            value = 0.2;
            if (stage == 0) {
                value = 0.0;
            }
        }
        return Math.max(value, this.shouldslow ? value : MoveUtils.defaultSpeed() + 0.028 * (double)MoveUtils.getSpeedEffect());
    }

    private void setMotion(double speed) {
        double forward = Speed.mc.thePlayer.movementInput.moveForward;
        double strafe = Speed.mc.thePlayer.movementInput.moveStrafe;
        float yaw = Speed.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Speed.mc.thePlayer.motionX = 0.0;
            Speed.mc.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Speed.mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            Speed.mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    static {
        timers = (Timer)ReflectionHelper.getPrivateValue(Minecraft.class, mc, Mappings.timer);
    }
}

