/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.codec.binary.Hex
 */
package com.leave.old;

import com.leave.old.Notifications.NotificationManager;
import com.leave.old.Utils.CFontRenderer;
import com.leave.old.Utils.FontLoaders;
import com.leave.old.Utils.Nan0EventRegister;
import com.leave.old.command.CommandManager;
import com.leave.old.eventEngine;
import com.leave.old.modules.Module;
import com.leave.old.modules.ModuleManager;
import com.leave.old.modules.Tools;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.apache.commons.codec.binary.Hex;

public class Client {
    public static Client instance;
    public static boolean state;
    public static boolean isObfuscate;
    public static ModuleManager moduleManager;
    public static CFontRenderer cFontRenderer;
    public static eventEngine eventEngine;
    public static CommandManager commandManager;
    public static NotificationManager notificationManager;
    public static int Verify;
    public static String LicenceCodeX;
    public static String UID;
    public static String UN;
    public static String UP;
    public static boolean inited;

    public Client() {
        if (state) {
            return;
        }
        state = true;
        // Ez crack lol
        // this.NativeLoaderX();
        // this.Send("127.0.0.1", 9801, "I am LeaveOld");
                                this.setOBF();
                                moduleManager = new ModuleManager();
                                cFontRenderer = new CFontRenderer(FontLoaders.getFont(16), true, true);
                                notificationManager = new NotificationManager();
                                commandManager = new CommandManager();
                                eventEngine = new eventEngine();
                                instance = this;
                                Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventEngine);
                                Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventEngine);
                                this.CreateDir();
    }

    public void CreateDir() {
        File ConfigDir = new File(Tools.getConfigPath());
        if (ConfigDir.exists()) {
            ConfigDir.mkdir();
        }
    }

    public void setOBF() {
        instance = this;
        try {
            Field F = S18PacketEntityTeleport.class.getDeclaredField("field_149456_b");
            isObfuscate = true;
        }
        catch (NoSuchFieldException ex) {
            try {
                Field F = S18PacketEntityTeleport.class.getDeclaredField("posX");
                isObfuscate = false;
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getHWID(boolean isMD5) throws IOException, NoSuchAlgorithmException {
        String property = System.getProperty("os.name").toLowerCase();
        String cpuSerialNumber = Client.getCPUSerialNumber();
        String hardDiskSerialNumber = InetAddress.getLocalHost().getHostName().toString();
        String serial = cpuSerialNumber + hardDiskSerialNumber;
        if (isMD5) {
            MessageDigest messageDigest = null;
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] ciphertext = messageDigest.digest(serial.getBytes());
            return Hex.encodeHexString((byte[])ciphertext);
        }
        return serial;
    }

    public static String getCPUSerialNumber() {
        String serial;
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"wmic", "cpu", "get", "ProcessorId"});
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            serial = sc.next();
            serial = sc.next();
        }
        catch (IOException e) {
            throw new RuntimeException("");
        }
        return serial;
    }

    public static String BuildLoginString(String UID, String Username, String Password) {
        try {
            String UIDW = "[USERID][" + UID + "][UserName][";
            String UsernameW = Username + "][Password][";
            String PasswordW = Password + "][USERHWID][";
            String HWIDW = Client.getHWID(true) + "]";
            String BuildString = "[Target][LOGIN]" + UIDW + UsernameW + PasswordW + HWIDW;
            return BuildString;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void NativeLoaderX() {
        state = true;
        instance = this;
        String pathname = "C:\\user.LEAVE";
        String LicenceCode = null;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader bfr = new BufferedReader(reader);){
            while ((LicenceCode = bfr.readLine()) != null) {
                Socket socket = new Socket("175.178.66.160", 19730);
                OutputStream ops = socket.getOutputStream();
                OutputStreamWriter opsw = new OutputStreamWriter(ops);
                BufferedWriter bw = new BufferedWriter(opsw);
                LicenceCodeX = LicenceCode;
                UID = Client.getSubString(LicenceCode, "[USERID][", "][UserName][");
                UN = Client.getSubString(LicenceCode, "[UserName][", "][Password][");
                UP = Client.getSubString(LicenceCode, "][Password][", "]");
                String BuildS = Client.BuildLoginString(UID, UN, UP);
                bw.write(BuildS);
                bw.flush();
                InputStream ips = socket.getInputStream();
                InputStreamReader ipsr = new InputStreamReader(ips, "GBK");
                BufferedReader br = new BufferedReader(ipsr);
                String s = null;
                block26: while ((s = br.readLine()) != null) {
                    String result = Client.getSubString(s, "[result]", "[");
                    Verify = result.hashCode();
                    switch (result.hashCode()) {
                        case 883338842: {
                            this.setOBF();
                            String hwid = Client.getSubString(s, "[hwid]", "[");
                            if (hwid.contains("rnmEXSV406" + Client.getHWID(true))) {
                                this.setOBF();
                                moduleManager = new ModuleManager();
                                cFontRenderer = new CFontRenderer(FontLoaders.getFont(16), true, true);
                                notificationManager = new NotificationManager();
                                commandManager = new CommandManager();
                                eventEngine = new eventEngine();
                                instance = this;
                                Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventEngine);
                                Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventEngine);
                                this.CreateDir();
                                continue block26;
                            }
                            String reasonX = Client.getSubString(s, "[reason]", "");
                            System.out.println("Minecraft 1.8.9 \u00d7" + reasonX);
                            continue block26;
                        }
                    }
                    String reason = Client.getSubString(s, "[reason]", "");
                    System.out.println("Minecraft 1.8.9 \u00d7" + reason);
                }
                socket.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSubString(String text, String left, String right) {
        int zLen;
        String result = "";
        zLen = left == null || left.isEmpty() ? 0 : ((zLen = text.indexOf(left)) > -1 ? (zLen += left.length()) : 0);
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right == null || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }

    public String Send(String IP, int Port, String Message) {
        try {
            Socket socket = new Socket(IP, Port);
            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops, "GBK");
            BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            InputStream ips = socket.getInputStream();
            InputStreamReader ipsr = new InputStreamReader(ips, "GBK");
            BufferedReader br = new BufferedReader(ipsr);
            String s = null;
            while ((s = br.readLine()) != null) {
                if (s.contains("Close")) continue;
                JOptionPane.showMessageDialog(null, "Failed Inject", "LeaveOld", 0);
                Minecraft.getMinecraft().displayGuiScreen(null);
                for (Module m : moduleManager.getModules()) {
                    m.setState(false);
                }
                MinecraftForge.EVENT_BUS.unregister(instance);
                state = false;
            }
            socket.close();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed Connect to The Server", "LeaveOld", 0);
            e.printStackTrace();
            Minecraft.getMinecraft().displayGuiScreen(null);
            for (Module m : moduleManager.getModules()) {
                m.setState(false);
            }
            MinecraftForge.EVENT_BUS.unregister(instance);
            state = false;
        }
        return null;
    }

    public static String Connect(String IP, int Port, String Message) {
        try {
            Socket socket = new Socket(IP, Port);
            OutputStream ops = socket.getOutputStream();
            OutputStreamWriter opsw = new OutputStreamWriter(ops, "GBK");
            BufferedWriter bw = new BufferedWriter(opsw);
            bw.write(Message);
            bw.flush();
            socket.close();
        }
        catch (Exception exception) {
            // empty catch block
        }
        return null;
    }

    static {
        state = false;
        isObfuscate = false;
        inited = false;
    }
}

