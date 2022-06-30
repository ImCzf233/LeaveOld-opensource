/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.config;

import com.leave.old.modules.Tools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigManager {
    public String path;
    public String filename;

    public ConfigManager(String path, String filename) {
        this.path = path;
        this.filename = filename;
        this.createFile();
    }

    public void createFile() {
        try {
            File FontDir;
            File n = new File(this.path + "\\" + this.filename);
            File ConfigDir = new File(Tools.getConfigPath());
            if (!ConfigDir.exists()) {
                ConfigDir.mkdir();
            }
            if (!(FontDir = new File(Tools.getFontPath())).exists()) {
                FontDir.mkdir();
            }
            if (!n.exists()) {
                n.createNewFile();
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public void clear() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.filename)));
            bw.write("");
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void write(String text) {
        this.write(new String[]{text});
    }

    public final ArrayList<String> read() {
        ArrayList<String> list = new ArrayList<String>();
        try {
            String text;
            BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(new File(this.path, this.filename).getAbsolutePath()))));
            while ((text = br.readLine()) != null) {
                list.add(text.trim());
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void write(String[] text) {
        if (text == null || text.length == 0 || text[0].trim() == "") {
            return;
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.path, this.filename), true));
            for (String line : text) {
                bw.write(line);
                bw.write("\r\n");
            }
            bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

