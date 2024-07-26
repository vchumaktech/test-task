package org.testtask.phonebooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
        "org.testtask.phonebooking.application",
        "org.testtask.phonebooking.infrastructure"})
public class PhoneBookingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PhoneBookingApplication.class, args);
    }
}