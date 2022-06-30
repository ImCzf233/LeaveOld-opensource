/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelDuplexHandler
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelHandlerContext
 *  io.netty.channel.ChannelPipeline
 *  io.netty.channel.ChannelPromise
 */
package com.leave.old.Utils;

import com.leave.old.Utils.ChatUtils;
import com.leave.old.eventEngine;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;

public class Connection
extends ChannelDuplexHandler {
    private eventEngine eventEngine;

    public Connection(eventEngine eventEngine2) {
        this.eventEngine = eventEngine2;
        try {
            ChannelPipeline pipeline = Minecraft.getMinecraft().getNetHandler().getNetworkManager().channel().pipeline();
            pipeline.addBefore("packet_handler", "PacketHandler", (ChannelHandler)this);
        }
        catch (Exception exception) {
            ChatUtils.error("Connection: Error on attaching");
            exception.printStackTrace();
        }
    }

    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        if (!this.eventEngine.onPacket(packet, Side.IN)) {
            return;
        }
        super.channelRead(ctx, packet);
    }

    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if (!this.eventEngine.onPacket(packet, Side.OUT)) {
            return;
        }
        super.write(ctx, packet, promise);
    }

    public static enum Side {
        IN,
        OUT;

    }
}

