package org.testtask.phonebooking.domain.services;

import org.testtask.phonebooking.domain.events.BaseEvent;

/**
 * Sends notification events
 */
public interface NotificationService {
    void sendEvent(BaseEvent event);
}
