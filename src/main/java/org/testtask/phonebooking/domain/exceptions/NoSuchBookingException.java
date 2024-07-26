package org.testtask.phonebooking.domain.exceptions;

public class NoSuchBookingException extends RuntimeException {
    public NoSuchBookingException(String message) {
        super(message);
    }
}
