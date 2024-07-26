package org.testtask.phonebooking.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.testtask.phonebooking.application.models.BookPhoneByModelRequest;
import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.exceptions.NoSuchPhoneException;
import org.testtask.phonebooking.domain.services.PhoneBookingService;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class PhoneBookingController {
    private final PhoneBookingService phoneBookingService;

    @Autowired
    public PhoneBookingController(PhoneBookingService phoneBookingService) {
        this.phoneBookingService = phoneBookingService;
    }

    @GetMapping(value = "/{bookingId}", produces = "application/json")
    public Optional<Booking> getBookingById(@PathVariable UUID bookingId) {
        return this.phoneBookingService.findByBookingId(bookingId);
    }

    @GetMapping(value = "/phones/models", produces = "application/json")
    public Collection<Booking> getBookingByPhoneModel(@RequestParam String model, @RequestParam boolean activeOnly) {
        final PhoneModel phoneModel = PhoneModel.getByLabel(model)
                .orElseThrow(() -> new NoSuchPhoneException(model + " is not supported"));

        return this.phoneBookingService.findByPhoneModel(phoneModel, activeOnly);
    }

    @GetMapping(value = "/phones/{phoneId}", produces = "application/json")
    public Collection<Booking> getBookingByPhoneId(@PathVariable UUID phoneId, @RequestParam boolean activeOnly) {
        return this.phoneBookingService.findByPhoneId(phoneId, activeOnly);
    }

    @PostMapping(value = "/phones/models", produces = "application/json")
    public Booking bookPhoneByPhoneModel(@RequestBody BookPhoneByModelRequest bookPhoneRequest) {
        return this.phoneBookingService.bookPhone(bookPhoneRequest.phoneModel(), bookPhoneRequest.userId());
    }

    @PostMapping(value = "/phones/{phoneId}", produces = "application/json")
    public Booking bookPhoneByPhoneId(@PathVariable UUID phoneId, @RequestParam UUID userId) {
        return this.phoneBookingService.bookPhone(phoneId, userId);
    }

    @PostMapping(value = "/phones/{phoneId}/returns", produces = "application/json")
    public Booking returnPhone(@PathVariable UUID phoneId) {
        return this.phoneBookingService.returnPhone(phoneId);
    }


}
