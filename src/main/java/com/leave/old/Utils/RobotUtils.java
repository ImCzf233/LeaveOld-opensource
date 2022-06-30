/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import java.awt.AWTException;
import java.awt.Robot;

public class RobotUtils {
    public static void clickMouse(int button) {
        block4: {
            try {
                Robot bot = new Robot();
                if (button == 0) {
                    bot.mousePress(16);
                    bot.mouseRelease(16);
                    break block4;
                }
                if (button == 1) {
                    bot.mousePress(4096);
                    bot.mouseRelease(4096);
                    break block4;
                }
                return;
            }
            catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setMouse(int button, boolean state) {
        block8: {
            try {
                Robot bot = new Robot();
                if (button == 0) {
                    if (state) {
                        bot.mousePress(16);
                    } else {
                        bot.mouseRelease(16);
                    }
                    break block8;
                }
                if (button == 1) {
                    if (state) {
                        bot.mousePress(4096);
                    } else {
                        bot.mouseRelease(4096);
                    }
                    break block8;
                }
                return;
            }
            catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
}

