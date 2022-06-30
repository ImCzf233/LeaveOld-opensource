/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.LaunchClassLoader
 */
package com.leave.old;

import com.leave.old.eventapi.ClassTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URL;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class Agent {
    public static Instrumentation instrumentation;

    public static void agentmain(String args, Instrumentation instrumentation) throws Exception {
        Agent.instrumentation = instrumentation;
        Agent.loadThisJar();
        Agent.forceloadclass();
        Agent.retransform();
    }

    public static void loadThisJar() {
        LaunchClassLoader cl = Agent.getLaunchClassLoader();
        if (cl != null) {
            URL url = Agent.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println("add jar: " + url.toString());
            cl.addURL(url);
        }
    }

    public static LaunchClassLoader getLaunchClassLoader() {
        for (Class c : instrumentation.getAllLoadedClasses()) {
            if (c.getClassLoader() == null || !c.getClassLoader().getClass().getName().equals("net.minecraft.launchwrapper.LaunchClassLoader")) continue;
            ClassLoader cl = c.getClassLoader();
            return (LaunchClassLoader)cl;
        }
        return null;
    }

    public static void retransform() {
        ClassTransformer ct = new ClassTransformer();
        instrumentation.addTransformer(ct, true);
        for (Class clazz : instrumentation.getAllLoadedClasses()) {
            if (!ClassTransformer.needTransform(clazz.getName())) continue;
            try {
                instrumentation.retransformClasses(clazz);
            }
            catch (UnmodifiableClassException unmodifiableClassException) {
                // empty catch block
            }
        }
        instrumentation.removeTransformer(ct);
    }

    public static void forceloadclass() {
        LaunchClassLoader cl = Agent.getLaunchClassLoader();
        if (cl != null) {
            for (String name : ClassTransformer.classNameSet) {
                try {
                    cl.loadClass(name);
                }
                catch (ClassNotFoundException e) {
                    System.out.println("can not load class " + name);
                }
            }
        }
    }
}

