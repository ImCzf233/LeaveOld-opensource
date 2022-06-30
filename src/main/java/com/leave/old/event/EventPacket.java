/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.event;

import com.leave.old.eventapi.events.Event;
import com.leave.old.eventapi.types.EventType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;

public class EventPacket
implements Event {
    private EventType type;
    public static boolean sendcancel;
    public EntityLivingBase target;
    private boolean cancelled;
    public static boolean recievecancel;
    public Packet packet;

    public EventPacket(EventType type, Object packet) {
        this.type = type;
        this.packet = (Packet)packet;
    }

    public EventType getType() {
        return this.type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public static boolean isSendcancel() {
        return sendcancel;
    }

    public static void setSendcancel(boolean sendcancel) {
        EventPacket.sendcancel = sendcancel;
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }

    public void setTarget(EntityLivingBase target) {
        this.target = target;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static boolean isRecievecancel() {
        return recievecancel;
    }

    public static void setRecievecancel(boolean recievecancel) {
        EventPacket.recievecancel = recievecancel;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

