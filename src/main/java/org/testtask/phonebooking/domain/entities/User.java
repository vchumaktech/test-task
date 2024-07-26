package org.testtask.phonebooking.domain.entities;

import java.util.UUID;

/**
 * Entity that represents user
 * @param id - unique user id
 * @param name - user name
 */
public record User(UUID id, String name) {
}