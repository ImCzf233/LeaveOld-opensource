/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import com.leave.old.Utils.TimerUtils;

public class AnimationUtils {
    private static float defaultSpeed = 0.125f;
    private TimerUtils timerUtil = new TimerUtils();

    public static float calculateCompensation(float target, float current, long delta, double speed) {
        float diff = current - target;
        if (delta < 1L) {
            delta = 1L;
        }
        if (delta > 1000L) {
            delta = 16L;
        }
        if ((double)diff > speed) {
            double xD = speed * (double)delta / 16.0 < 0.5 ? 0.5 : speed * (double)delta / 16.0;
            current = (float)((double)current - xD);
            if (0 < target) {
                current = target;
            }
        } else if ((double)diff < -speed) {
            double xD = speed * (double)delta / 16.0 < 0.5 ? 0.5 : speed * (double)delta / 16.0;
            current = (float)((double)current + xD);
            if (0 > target) {
                current = target;
            }
        } else {
            current = target;
        }
        return current;
    }

    public float mvoeUD(float current, float end, float minSpeed) {
        return this.moveUD(current, end, defaultSpeed, minSpeed);
    }

    public double animate(double target, double current, double speed) {
        if (this.timerUtil.isDelay(4L)) {
            boolean larger = target > current;
            boolean bl = larger;
            if (speed < 0.0) {
                speed = 0.0;
            } else if (speed > 1.0) {
                speed = 1.0;
            }
            double dif = Math.max(target, current) - Math.min(target, current);
            double factor = dif * speed;
            if (factor < 0.1) {
                factor = 0.1;
            }
            current = larger ? (current = current + factor) : (current = current - factor);
            this.timerUtil.reset();
        }
        return current;
    }

    public float animate(float target, float current, float speed) {
        if (this.timerUtil.isDelay(4L)) {
            boolean larger = target > current;
            boolean bl = larger;
            if (speed < 0.0f) {
                speed = 0.0f;
            } else if ((double)speed > 1.0) {
                speed = 1.0f;
            }
            float dif = Math.max(target, current) - Math.min(target, current);
            float factor = dif * speed;
            if (factor < 0.1f) {
                factor = 0.1f;
            }
            current = larger ? (current = current + factor) : (current = current - factor);
            this.timerUtil.reset();
        }
        if ((double)Math.abs(current - target) < 0.2) {
            return target;
        }
        return current;
    }

    public float moveUD(float current, float end, float smoothSpeed, float minSpeed) {
        float movement = 0.0f;
        if (this.timerUtil.isDelay(20L)) {
            movement = (end - current) * smoothSpeed;
            if (movement > 0.0f) {
                movement = Math.max(minSpeed, movement);
                movement = Math.min(end - current, movement);
            } else if (movement < 0.0f) {
                movement = Math.min(-minSpeed, movement);
                movement = Math.max(end - current, movement);
            }
            this.timerUtil.reset();
        }
        return current + movement;
    }
}

