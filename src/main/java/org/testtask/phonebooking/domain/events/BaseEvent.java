package org.testtask.phonebooking.domain.events;

/**
 * Base event
 */
public abstract class BaseEvent {
    protected EventType type;

    public BaseEvent(EventType type) {
        this.type = type;
    }

    public EventType getType() {
        return type;
    }
}
