package org.testtask.phonebooking.domain.entities;

import org.testtask.phonebooking.domain.valueobjects.PhoneModel;

import java.util.UUID;

/**
 * Entity that represent phone
 *
 * @param id - uniques phone id
 * @param model - phone models (@see PhoneModel)
 */
public record Phone(UUID id, PhoneModel model) {
}