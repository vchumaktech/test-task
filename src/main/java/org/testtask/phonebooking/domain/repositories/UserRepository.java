package org.testtask.phonebooking.domain.repositories;

import org.testtask.phonebooking.domain.entities.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> getUser(UUID userId);
}
