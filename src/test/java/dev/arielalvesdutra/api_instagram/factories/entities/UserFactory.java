package dev.arielalvesdutra.api_instagram.factories.entities;

import dev.arielalvesdutra.api_instagram.entities.User;

/**
 * Factory to help building tests.
 */
public class UserFactory {

    public static User toPersist() {
        return new User()
                .setName("Geralt of Rivia")
                .setEmail("geralt@teste.com")
                .setUsername("geralt");
    }

    public static User toPersistSecondOption() {
        return new User()
                .setName("Triss Merigold")
                .setEmail("triss@teste.com")
                .setUsername("triss");
    }
}
