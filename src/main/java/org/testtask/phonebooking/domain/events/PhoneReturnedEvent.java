package org.testtask.phonebooking.domain.events;

import java.util.UUID;

/**
 * Represents phone return activity
 */
public class PhoneReturnedEvent extends BaseEvent {
    private final UUID phoneId;

    public PhoneReturnedEvent(UUID phoneId) {
        super(EventType.PHONE_RETURNED);

        this.phoneId = phoneId;
    }

    public UUID getPhoneId() {
        return phoneId;
    }
}
