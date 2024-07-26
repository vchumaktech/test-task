package org.testtask.phonebooking.infrastructure.mocks.repositories;

import org.springframework.stereotype.Component;
import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.repositories.BookingRepository;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.*;
import java.util.stream.Stream;

/**
 * Mock implementation of
 * {@link org.testtask.phonebooking.domain.repositories.BookingRepository}
 * May be replaced with real
 *
 * {@inheritDoc}
 */
@Component
public class MockBookingRepository implements BookingRepository {
    private Map<UUID, Booking> allBookings = new HashMap<>();

    @Override
    public Booking saveOrUpdate(Booking booking) {
        allBookings.put(booking.id(), booking);
        return booking;
    }

    @Override
    public Collection<Booking> findByPhoneModel(PhoneModel phoneModel, boolean activeOnly) {
        Stream<Booking> filteredBookings = allBookings.values().stream()
                .filter(booking -> booking.phone().model().equals(phoneModel));

        if(activeOnly) {
            filteredBookings = filteredBookings.filter(Booking::active);
        }

        return filteredBookings.toList();
    }

    @Override
    public Optional<Booking> findByBookingId(UUID bookingId) {
        return Optional.ofNullable(allBookings.get(bookingId));
    }

    @Override
    public Collection<Booking> findByPhoneId(UUID phoneId, boolean activeOnly) {
        Stream<Booking> filteredBookings = allBookings.values().stream()
                .filter(booking -> booking.phone().id().equals(phoneId));

        if(activeOnly) {
            filteredBookings = filteredBookings.filter(Booking::active);
        }

        return filteredBookings.toList();
    }

    @Override
    public void flush() {
        allBookings.clear();
    }
}
