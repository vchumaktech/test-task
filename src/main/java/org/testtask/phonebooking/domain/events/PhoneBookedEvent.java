package org.testtask.phonebooking.domain.events;

import java.util.UUID;

/**
 * Represents phone booked activity
 */
public class PhoneBookedEvent extends BaseEvent {
    private final UUID phoneId;

    public PhoneBookedEvent(UUID phoneId) {
        super(EventType.PHONE_BOOKED);

        this.phoneId = phoneId;
    }

    public UUID getPhoneId() {
        return phoneId;
    }
}
