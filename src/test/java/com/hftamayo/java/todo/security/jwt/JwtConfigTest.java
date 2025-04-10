package com.hftamayo.java.todo.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtConfigTest {

    @InjectMocks
    private JwtConfig jwtConfig;

    @Test
    void generateKey_ShouldReturnValidSecretKey() {
        // Act
        SecretKey secretKey = jwtConfig.generateKey();

        // Assert
        assertAll(
                () -> assertNotNull(secretKey),
                () -> assertEquals("HmacSHA512", secretKey.getAlgorithm()),
                () -> assertEquals(64, secretKey.getEncoded().length)  // 512 bits = 64 bytes
        );
    }

    @Test
    void generateKey_ShouldGenerateUniqueKeys() {
        // Act
        SecretKey key1 = jwtConfig.generateKey();
        SecretKey key2 = jwtConfig.generateKey();

        // Assert
        assertAll(
                () -> assertNotNull(key1),
                () -> assertNotNull(key2),
                () -> assertNotEquals(
                        Base64.getEncoder().encodeToString(key1.getEncoded()),
                        Base64.getEncoder().encodeToString(key2.getEncoded())
                )
        );
    }
}