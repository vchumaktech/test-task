package org.testtask.phonebooking.domain.services;

import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.*;

/**
 * Service that represents phone booking activity
 */
public interface PhoneBookingService {

    /**
     * book phone by specific id
     *
     * @param phoneId - unique phone id
     * @param userId - unique used id who booked a phone
     * @return - booking entity
     */
    Booking bookPhone(UUID phoneId, UUID userId);

    /**
     * book phone of any supported model
     *
     * @param model - supported model. see {@link PhoneModel}
     * @param userId - user unique identifier who makes a booking
     * @return - booking entity
     */

    Booking bookPhone(String model, UUID userId);

    /**
     * return phone back
     *
     * @param phoneId - unique phone identifier
     */
     Booking returnPhone(UUID phoneId);

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
}
