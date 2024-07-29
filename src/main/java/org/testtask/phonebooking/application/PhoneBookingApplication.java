package org.testtask.phonebooking.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "org.testtask.phonebooking.application",
        "org.testtask.phonebooking.infrastructure",
        "org.testtask.phonebooking.application.controllers",
        "org.testtask.phonebooking.application.services"})
public class PhoneBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneBookingApplication.class, args);
    }
}