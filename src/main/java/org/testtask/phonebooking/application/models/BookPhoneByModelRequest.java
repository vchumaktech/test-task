package org.testtask.phonebooking.application.models;

import java.util.UUID;

/**
 * Input request for bookPhone input
 */
public record BookPhoneByModelRequest(String phoneModel, UUID userId) {
}
