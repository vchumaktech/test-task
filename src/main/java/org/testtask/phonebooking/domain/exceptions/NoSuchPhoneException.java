package org.testtask.phonebooking.domain.exceptions;

public class NoSuchPhoneException extends RuntimeException {
    public NoSuchPhoneException(String message) {
        super(message);
    }
}
