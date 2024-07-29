package org.testtask.phonebooking.application;

import org.junit.jupiter.api.*;
import org.testtask.phonebooking.domain.entities.Booking;
import org.testtask.phonebooking.domain.entities.Phone;
import org.testtask.phonebooking.domain.entities.User;
import org.testtask.phonebooking.domain.events.BaseEvent;
import org.testtask.phonebooking.domain.events.EventType;
import org.testtask.phonebooking.domain.exceptions.AlreadyBookedException;
import org.testtask.phonebooking.domain.exceptions.NoSuchBookingException;
import org.testtask.phonebooking.domain.exceptions.NoSuchPhoneException;
import org.testtask.phonebooking.domain.exceptions.NoSuchUserException;
import org.testtask.phonebooking.domain.repositories.BookingRepository;
import org.testtask.phonebooking.domain.repositories.PhoneRepository;
import org.testtask.phonebooking.domain.repositories.UserRepository;
import org.testtask.phonebooking.domain.services.PhoneBookingService;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;
import org.testtask.phonebooking.infrastructure.mocks.repositories.MockBookingRepository;
import org.testtask.phonebooking.infrastructure.mocks.repositories.MockPhoneRepository;
import org.testtask.phonebooking.infrastructure.mocks.repositories.MockUserRepository;
import org.testtask.phonebooking.infrastructure.mocks.services.MockNotificationService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PhoneBookingServiceImplTest {
    private PhoneBookingService systemUnderTest;
    private MockNotificationService notificationService;

    private final Phone samsung_g_s9 = new Phone(UUID.randomUUID(), PhoneModel.SAMSUNG_GALAXY_S9);
    private final Phone samsung_g_s8_first = new Phone(UUID.randomUUID(), PhoneModel.SAMSUNG_GALAXY_S8);
    private final Phone samsung_g_s8_second = new Phone(UUID.randomUUID(), PhoneModel.SAMSUNG_GALAXY_S8);
    private final Phone motorola_nexus_6 = new Phone(UUID.randomUUID(), PhoneModel.MOTOROLA_NEXUS_6);
    private final Phone oneplus_9 = new Phone(UUID.randomUUID(), PhoneModel.ONEPLUS_9);
    private final Phone iphone_13 = new Phone(UUID.randomUUID(), PhoneModel.APPLE_IPHONE_13);
    private final Phone iphone_12 = new Phone(UUID.randomUUID(), PhoneModel.APPLE_IPHONE_12);
    private final Phone iphone_11 = new Phone(UUID.randomUUID(), PhoneModel.APPLE_IPHONE_11);
    private final Phone iphone_x = new Phone(UUID.randomUUID(), PhoneModel.APPLE_IPHONE_X);
    private final Phone nokia_3310 = new Phone(UUID.randomUUID(), PhoneModel.NOKIA_3310);

    private final User user1 = new User(UUID.randomUUID(), "Ivan Ivanov");
    private final User user2 = new User(UUID.randomUUID(), "Pert Petrov");

    @BeforeEach
    void setUp() {
        final PhoneRepository phoneRepository = new MockPhoneRepository(
                samsung_g_s9, samsung_g_s8_first, samsung_g_s8_second,
                motorola_nexus_6, oneplus_9, iphone_13,
                iphone_12, iphone_11, iphone_x, nokia_3310);
        final BookingRepository bookingRepository = new MockBookingRepository();
        final UserRepository userRepository = new MockUserRepository(user1, user2);
        notificationService = new MockNotificationService();
        systemUnderTest = new PhoneBookingServiceImpl(phoneRepository, userRepository, bookingRepository, notificationService);
    }

    @Nested
    class BookPhoneScenario {
        @Test
        @DisplayName("verify phone booking by id")
        public void shouldBookAPhoneById() {
            final Booking booking = systemUnderTest.bookPhone(iphone_13.id(), user1.id());

            assertEquals(iphone_13.id(), booking.phone().id(), "booking should have correct phone id");
            assertEquals(user1.id(), booking.who().id(), "booking should have correct user id");
            assertNotNull(booking.when(), "activity date should be populated");
            assertNotNull(booking.id(), "booking id should be populated");
            assertTrue(booking.active(), "booking should be active");
        }

        @Test
        @DisplayName("verify phone booking by model")
        public void shouldBookAPhoneByModel() {
            final Booking booking = systemUnderTest.bookPhone(PhoneModel.NOKIA_3310.label, user1.id());

            assertEquals(nokia_3310.id(), booking.phone().id(), "booking should have correct phone id");
            assertEquals(user1.id(), booking.who().id(), "booking should have correct user id");
            assertNotNull(booking.when(), "activity date should be populated");
            assertNotNull(booking.id(), "booking id should be populated");
            assertTrue(booking.active(), "booking should be active");
        }

        @Test
        @DisplayName("verify phone may be booked by model in case if 2 phones with same model exists (relevant for Samsung Galaxy S8)")
        public void shouldBookAPhoneWithSameModel() {
            final Booking booking1 = systemUnderTest.bookPhone(PhoneModel.SAMSUNG_GALAXY_S8.label, user1.id());
            final Booking booking2 = systemUnderTest.bookPhone(PhoneModel.SAMSUNG_GALAXY_S8.label, user2.id());

            assertTrue(booking1.active(), "booking 1 for Samsung Galaxy S8 should be active");
            assertTrue(booking2.active(), "booking 2 for Samsung Galaxy S8 should be active");

        }

        @Test
        @DisplayName("verify already booked phone can't be booked again")
        public void shouldntBeBookedTwice() {
            systemUnderTest.bookPhone(nokia_3310.id(), user1.id());
            assertThrows(AlreadyBookedException.class, () -> systemUnderTest.bookPhone(nokia_3310.id(), user2.id()));
        }

        @Test
        @DisplayName("verify exception when user is unknown")
        public void shouldntBeBookedByUnknownUser() {
            assertThrows(NoSuchUserException.class, () -> systemUnderTest.bookPhone(nokia_3310.id(), UUID.randomUUID()));
        }

        @Test
        @DisplayName("verify exception when phone has unknown id")
        public void shouldntBeBookedWhenIdUnknown() {
            assertThrows(NoSuchPhoneException.class, () -> systemUnderTest.bookPhone(UUID.randomUUID(), user2.id()));
        }

        @Test
        @DisplayName("verify exception when phone has unknown model")
        public void shouldntBeBookedWhenModelUnknown() {
            assertThrows(NoSuchPhoneException.class, () -> systemUnderTest.bookPhone("fake iphone", user2.id()));
        }

        @Test
        @DisplayName("verify notification event sending when phone booked")
        public void shouldNotifyWhenPhoneBooked() {
            systemUnderTest.bookPhone(nokia_3310.id(), user1.id());

            final BaseEvent lastEvent = notificationService.getLastEvent();
            assertNotNull(lastEvent, "notification service should have an event");
            Assertions.assertEquals(lastEvent.getType(), EventType.PHONE_BOOKED, "wrong notification event type");
        }
    }

    @Nested
    class ReturnPhoneScenario {
        @Test
        @DisplayName("verify exception when phone has unknown id")
        public void shouldFailWhenIdUnknown() {
            assertThrows(NoSuchBookingException.class, () -> systemUnderTest.returnPhone(UUID.randomUUID()));
        }

        @Test
        @DisplayName("verify exception when phone hasn't been booked")
        public void shouldFailWhenPhoneHasntBeenBooked() {
            assertThrows(NoSuchBookingException.class, () -> systemUnderTest.returnPhone(iphone_x.id()));
        }

        @Test
        @DisplayName("verify notification event sending when phone returned")
        public void shouldNotifyWhenPhoneReturned() {
            systemUnderTest.bookPhone(nokia_3310.id(), user1.id());
            systemUnderTest.returnPhone(nokia_3310.id());

            final BaseEvent lastEvent = notificationService.getLastEvent();
            assertNotNull(lastEvent, "notification service should have an event");
            Assertions.assertEquals(lastEvent.getType(), EventType.PHONE_RETURNED, "wrong notification event type");
        }

        @Test
        @DisplayName("verify booking status changed when phone has been returned")
        public void shouldInactivateBookingWhenPhoneReturned() {
            final Booking initialBooking = systemUnderTest.bookPhone(nokia_3310.id(), user1.id());
            final Booking finalBooking = systemUnderTest.returnPhone(nokia_3310.id());

            assertTrue(initialBooking.active(), "initial booking should be active");
            assertFalse(finalBooking.active(), "final booking shouldn't be active");
            assertEquals(initialBooking.who(), finalBooking.who(), "booking users should be equal");
            assertEquals(initialBooking.phone(), finalBooking.phone(), "booked phones should be equal");
        }
    }

    @Nested
    class RetrieveBookingInfoScenario {

        @Test
        @DisplayName("verify empty when booking id is unknown")
        public void shouldReturnEmptyWhenBookingUnknown() {
            final Optional<Booking> booking = systemUnderTest.findByBookingId(UUID.randomUUID());
            assertTrue(booking.isEmpty(), "booking should be empty");
        }

        @Test
        @DisplayName("verify correct booking returned by booking id")
        public void shouldReturnCorrectBookingById() {
            final Booking booking = systemUnderTest.bookPhone(nokia_3310.model().label, user2.id());
            final Optional<Booking> result = systemUnderTest.findByBookingId(booking.id());

            assertFalse(result.isEmpty(), "booking shouldn't be empty");

            final Phone expectedPhone = result.map(Booking::phone).get();
            assertEquals(expectedPhone, nokia_3310, "wrong phone booked");
        }

        @Test
        @DisplayName("verify correct booking returned by model")
        public void shouldReturnCorrectBookingByModel() {
            systemUnderTest.bookPhone(iphone_11.model().label, user2.id());

            final Collection<Booking> result = systemUnderTest.findByPhoneId(iphone_11.id(), false);
            assertFalse(result.isEmpty(), "booking history shouldn't be empty");
            assertEquals(result.size(), 1, "booking history should contain only 1 element");

            final Phone expectedPhone = result.stream().findFirst().map(Booking::phone).get();
            assertEquals(expectedPhone, iphone_11, "wrong phone booked");
        }

        @Test
        @DisplayName("verify correct booking history returned")
        public void shouldRetrieveBookingHistory() {
            systemUnderTest.bookPhone(iphone_x.id(), user2.id());
            systemUnderTest.returnPhone(iphone_x.id());
            systemUnderTest.bookPhone(iphone_x.id(), user1.id());
            systemUnderTest.returnPhone(iphone_x.id());
            systemUnderTest.bookPhone(iphone_x.id(), user1.id());


            final Collection<Booking> result = systemUnderTest.findByPhoneId(iphone_x.id(), false);
            assertFalse(result.isEmpty(), "booking history shouldn't be empty");
            assertEquals(result.size(), 3, "booking history should contain 3 elements");

            final List<Booking> activeBookings = result.stream().filter(Booking::active).toList();
            assertEquals(activeBookings.size(), 1, "only 1 active booking should be presented");
        }
    }
}
