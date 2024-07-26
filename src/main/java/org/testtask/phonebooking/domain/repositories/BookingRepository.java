package org.testtask.phonebooking.domain.repositories;

import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.entities.Phone;
import org.testtask.phonebooking.domain.entities.User;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Base interface of booking repository
 */
public interface BookingRepository {
    /**
     * creates a new booking if not exists or updates existing
     * @param booking - booking entity
     * @return - created or updated booking entity
     */
    Booking saveOrUpdate(Booking booking);

    /**
     * get booking info for phones that corresponds one of supported model
     *
     * @param phoneModel - supported model. see {@link PhoneModel}
     * @param activeOnly - return active only bookings (or all bookings of activeOnly=false)
     * @return - booking entity according to parameters
     */
    Collection<Booking> findByPhoneModel(PhoneModel phoneModel, boolean activeOnly);

    /**
     * get booking info by id
     *
     * @param bookingId - unique booking identifier
     * @return - booking entity according to parameters
     */
    Optional<Booking> findByBookingId(UUID bookingId);

    /**
     * get booking info by id
     *
     * @param phoneId - unique booking identifier
     * @param activeOnly - return active only bookings (or all bookings of activeOnly=false)
     * @return - booking entity according to parameters
     */
    Collection<Booking> findByPhoneId(UUID phoneId, boolean activeOnly);

    /**
     * removes all existing bookings
     */
    default void flush() {
        throw new UnsupportedOperationException("method is not implemented");
    }
}
