package org.testtask.phonebooking.domain.repositories;

import org.testtask.phonebooking.domain.entities.Phone;
import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Phone repository
 * Provides method for phone state management
 */
public interface PhoneRepository {
    /**
     * Retrieves phone by unique id
     * @param phoneId - phone unique identifier
     * @return - phone by specific id or empty optional in case if phone not found
     */
    Optional<Phone> getPhoneById(UUID phoneId);

    /**
     * Retrieves all phones of specific model
     * @param model - supported phone model. see {@link PhoneModel}
     * @return - collection of phone with current model
     */
    Collection<Phone> getPhoneByModel(PhoneModel model);
}
