package org.testtask.phonebooking.infrastructure.mocks.repositories;

import org.springframework.stereotype.Component;
import org.testtask.phonebooking.domain.entities.User;
import org.testtask.phonebooking.domain.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Mock implementation of
 * {@link org.testtask.phonebooking.domain.repositories.UserRepository}
 * May be replaced with real
 *
 * {@inheritDoc}
 */
@Component
public class MockUserRepository implements UserRepository {
    private final List<User> users;

    public MockUserRepository(User... users) {
        this.users = List.of(users);
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return users.stream()
                .filter(user -> user.id().equals(userId))
                .findFirst();
    }
}
