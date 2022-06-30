/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.eventapi.events.callables;

import com.leave.old.eventapi.events.Event;
import com.leave.old.eventapi.events.Typed;

public abstract class EventTyped
implements Event,
Typed {
    private final byte type;

    protected EventTyped(byte eventType) {
        this.type = eventType;
    }

    @Override
    public byte getType() {
        return this.type;
    }
}

