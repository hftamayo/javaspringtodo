package com.hftamayo.java.todo.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PasswordEncoderConfigTest {

    @InjectMocks
    private PasswordEncoderConfig passwordEncoderConfig;

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // Act
        PasswordEncoder encoder = passwordEncoderConfig.passwordEncoder();

        // Assert
        assertAll(
                () -> assertNotNull(encoder),
                () -> assertTrue(encoder instanceof BCryptPasswordEncoder)
        );
    }

}
