/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 */
package com.leave.old.eventapi;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer
implements IClassTransformer,
ClassFileTransformer {
    public static Set<String> classNameSet = new HashSet<String>();

    public byte[] transform(String name, String transformedName, byte[] classByte) {
        return ClassTransformer.transform(transformedName, classByte);
    }

    public static boolean needTransform(String name) {
        return classNameSet.contains(name);
    }

    public static byte[] transform(String name, byte[] classByte) {
        return classByte;
    }

    @Override
    public byte[] transform(ClassLoader arg0, String name, Class<?> clazz, ProtectionDomain arg3, byte[] classByte) throws IllegalClassFormatException {
        return ClassTransformer.transform(clazz.getName(), classByte);
    }

    static {
        String[] nameArray = new String[]{"net.minecraft.client.multiplayer.PlayerControllerMP", "net.minecraft.entity.EntityLivingBase", "net.minecraft.client.Minecraft"};
        for (int i = 0; i < nameArray.length; ++i) {
            classNameSet.add(nameArray[i]);
        }
    }
}

