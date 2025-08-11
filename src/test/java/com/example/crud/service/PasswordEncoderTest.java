package com.example.crud.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new PasswordEncoder();

    @Test
    void shouldEncodePassword() {
        // Arrange
        String rawPassword = "mySecretPassword123!";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertNotNull(encodedPassword);
        assertTrue(encodedPassword.startsWith("$2a$"));
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    void shouldMatchValidPassword() {
        // Arrange
        String rawPassword = "anotherPassword456@";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Act & Assert
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void shouldNotMatchInvalidPassword() {
        // Arrange
        String encodedPassword = passwordEncoder.encode("correctPassword");

        // Act & Assert
        assertFalse(passwordEncoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void shouldHandleNullInputsSafely() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> passwordEncoder.encode(null));
        assertFalse(passwordEncoder.matches(null, "hash"));
        assertFalse(passwordEncoder.matches("password", null));
    }
}
