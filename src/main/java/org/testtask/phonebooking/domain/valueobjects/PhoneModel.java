package org.testtask.phonebooking.domain.valueobjects;

import java.util.Arrays;
import java.util.Optional;

/**
 * Supported models
 *
 * - Samsung Galaxy S9
 * - 2x Samsung Galaxy S8
 * - Motorola Nexus 6
 * - Oneplus 9
 * - Apple iPhone 13
 * - Apple iPhone 12
 * - Apple iPhone 11
 * - iPhone X
 * - Nokia 3310
 */
public enum PhoneModel {
    SAMSUNG_GALAXY_S9("Samsung Galaxy S9"),
    SAMSUNG_GALAXY_S8("Samsung Galaxy S8"),
    MOTOROLA_NEXUS_6("Motorola Nexus 6"),
    ONEPLUS_9("Oneplus 9"),
    APPLE_IPHONE_13("Apple iPhone 13"),
    APPLE_IPHONE_12("Apple iPhone 12"),
    APPLE_IPHONE_11("Apple iPhone 11"),
    APPLE_IPHONE_X("iPhone X"),
    NOKIA_3310("Nokia 3310");

    public final String label;

    PhoneModel(String label) {
        this.label = label;
    }

    public static Optional<PhoneModel> getByLabel(String label) {
        return Arrays.stream(values())
                .filter(model -> model.label.equals(label))
                .findFirst();
    }
}
