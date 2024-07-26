package org.testtask.phonebooking.infrastructure.mocks.repositories;

import org.springframework.stereotype.Component;
import org.testtask.phonebooking.domain.entities.Phone;
import org.testtask.phonebooking.domain.repositories.PhoneRepository;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Mock implementation of
 * {@link PhoneRepository}
 * May be replaced with real
 *
 * {@inheritDoc}
 */
@Component
public class MockPhoneRepository implements PhoneRepository {
    private final List<Phone> phones;

    public MockPhoneRepository(Phone... phones) {
        this.phones = List.of(phones);
    }

    @Override
    public Optional<Phone> getPhoneById(UUID phoneId) {
        return phones.stream()
                .filter(phone -> phone.id().equals(phoneId))
                .findFirst();
    }

    @Override
    public Collection<Phone> getPhoneByModel(PhoneModel model) {
        return phones.stream()
                .filter(phone -> phone.model().equals(model))
                .toList();
    }
}
