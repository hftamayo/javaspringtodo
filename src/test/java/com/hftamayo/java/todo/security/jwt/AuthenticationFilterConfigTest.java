package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationFilterConfigTest {

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @Mock
    private CustomTokenProvider customTokenProvider;

    @InjectMocks
    private AuthenticationFilterConfig authenticationFilterConfig;

    @Test
    void authenticationFilter_ShouldReturnValidFilter() {
        // Act
        AuthenticationFilter filter = authenticationFilterConfig.authenticationFilter();

        // Assert
        assertAll(
                () -> assertNotNull(filter),
                () -> assertInstanceOf(AuthenticationFilter.class, filter)
        );
    }
}