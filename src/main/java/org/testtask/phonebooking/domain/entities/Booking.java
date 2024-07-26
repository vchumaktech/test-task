package org.testtask.phonebooking.domain.entities;

import java.util.Date;
import java.util.UUID;

/**
 * Entity that represents phone booking
 *
 * @param id - unique booking id
 * @param who - person who booked a phone
 * @param phone - phone that was booked
 * @param when - date when booking activity happens (when booking was activated / inactivated)
 * @param active - activity flag
 */
public record Booking(UUID id,
                      User who,
                      Phone phone,
                      Date when,
                      boolean active) {

    /**
     * inactivate booking
     * @return - inactivated copy of initial booking
     */
    public Booking inactivate() {
        return new Booking(this.id, this.who, this.phone, new Date(), false);
    }
}