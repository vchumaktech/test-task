package org.testtask.phonebooking.infrastructure.mocks.services;

import org.springframework.stereotype.Component;
import org.testtask.phonebooking.domain.events.BaseEvent;
import org.testtask.phonebooking.domain.services.NotificationService;

/**
 * Mock implementation of notification service
 * May be replaced with real
 *
 * {@inheritDoc}
 */
@Component
public class MockNotificationService implements NotificationService {
    private BaseEvent lastEvent;

    @Override
    public void sendEvent(BaseEvent event) {
        this.lastEvent = event;
    }

    public BaseEvent getLastEvent() {
        return lastEvent;
    }
}
