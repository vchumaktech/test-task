package org.testtask.phonebooking.application;

import org.springframework.stereotype.Component;
import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.entities.Phone;
import org.testtask.phonebooking.domain.entities.User;
import org.testtask.phonebooking.domain.events.PhoneReturnedEvent;
import org.testtask.phonebooking.domain.exceptions.AlreadyBookedException;
import org.testtask.phonebooking.domain.exceptions.NoSuchBookingException;
import org.testtask.phonebooking.domain.exceptions.NoSuchPhoneException;
import org.testtask.phonebooking.domain.exceptions.NoSuchUserException;
import org.testtask.phonebooking.domain.repositories.BookingRepository;
import org.testtask.phonebooking.domain.repositories.PhoneRepository;
import org.testtask.phonebooking.domain.repositories.UserRepository;
import org.testtask.phonebooking.domain.services.NotificationService;
import org.testtask.phonebooking.domain.services.PhoneBookingService;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.*;

/**
 *{@inheritDoc}
 */
@Component
public class PhoneBookingServiceImpl implements PhoneBookingService {
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final NotificationService notificationService;


    public PhoneBookingServiceImpl(PhoneRepository phoneRepository,
                                   UserRepository userRepository,
                                   BookingRepository bookingRepository,
                                   NotificationService notificationService) {
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.notificationService = notificationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Booking bookPhone(UUID phoneId, UUID userId) {
        // verify phone existence
        final Phone phone = phoneRepository
                .getPhoneById(phoneId)
                .orElseThrow(() -> new NoSuchPhoneException("can't find a phone with id: " + phoneId));

        // verify user existence
        final User user = userRepository
                .getUser(userId)
                .orElseThrow(() -> new NoSuchUserException("can't find a user with id: " + userId));

        // check phone is booked
        final Collection<Booking> existingActiveBookings = findByPhoneId(phoneId, true);
        if(!existingActiveBookings.isEmpty()) {
            throw new AlreadyBookedException("phone with id: " + phoneId + " is already booked");
        }

        final Booking booking = new Booking(UUID.randomUUID(),
                user, phone,
                new Date(), true);

        return bookingRepository.saveOrUpdate(booking);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Booking bookPhone(String model, UUID userId) {
        final PhoneModel phoneModel = PhoneModel.getByLabel(model)
                .orElseThrow(() -> new NoSuchPhoneException(model + " is not supported"));

        final Collection<Booking> existingActiveBookings = findByPhoneModel(phoneModel, true);
        final Collection<Phone> phonesWithModel = phoneRepository.getPhoneByModel(phoneModel);

        if(existingActiveBookings.size() == phonesWithModel.size()) {
            throw new AlreadyBookedException("all phones of model: " + model + " are already booked");
        }

        final List<Phone> bookedPhones = existingActiveBookings
                .stream().map(Booking::phone).toList();
        final Optional<Phone> firstAvailablePhone = phonesWithModel
                .stream().filter(phone -> !bookedPhones.contains(phone))
                .findFirst();

        return bookPhone(firstAvailablePhone
                .map(Phone::id).get(), userId);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public synchronized Booking returnPhone(UUID phoneId) {
        // check phone is booked
        final Collection<Booking> existingActiveBookings = findByPhoneId(phoneId, true);
        if(existingActiveBookings.isEmpty()) {
            throw new NoSuchBookingException("phone with id: " + phoneId + " hasn't been booked");
        }

        // inactivate the booking and save state
        final Booking activeBooking = existingActiveBookings.iterator().next();
        final Booking inactiveBooking = activeBooking.inactivate();

        bookingRepository.saveOrUpdate(inactiveBooking);

        // notify phone returned
        notificationService.sendEvent(new PhoneReturnedEvent(inactiveBooking.phone().id()));

        return inactiveBooking;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Collection<Booking> findByPhoneModel(PhoneModel phoneModel, boolean activeOnly) {
        return bookingRepository.findByPhoneModel(phoneModel, activeOnly);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Optional<Booking> findByBookingId(UUID bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public Collection<Booking> findByPhoneId(UUID phoneId, boolean activeOnly) {
        return bookingRepository.findByPhoneId(phoneId, activeOnly);
    }
}
