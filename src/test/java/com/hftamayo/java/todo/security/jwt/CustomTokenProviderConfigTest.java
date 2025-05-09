package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomTokenProviderConfigTest {

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @InjectMocks
    private CustomTokenProviderConfig customTokenProviderConfig;

    @Test
    void createCustomTokenProvider_ShouldReturnValidProvider() {
        // Act
        CustomTokenProvider provider = customTokenProviderConfig.createCustomTokenProvider();

        // Assert
        assertAll(
                () -> assertNotNull(provider),
                () -> assertInstanceOf(CustomTokenProvider.class, provider)
        );
    }
}