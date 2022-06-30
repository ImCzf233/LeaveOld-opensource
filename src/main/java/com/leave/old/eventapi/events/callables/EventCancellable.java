/*
 * Decompiled with CFR 0.152.
 */
package com.leave.old.eventapi.events.callables;

import com.leave.old.eventapi.events.Cancellable;
import com.leave.old.eventapi.events.Event;

public abstract class EventCancellable
implements Event,
Cancellable {
    private boolean cancelled;

    protected EventCancellable() {
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancelled = state;
    }
}

