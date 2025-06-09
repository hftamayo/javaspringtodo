package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.jwt.JwtConfig;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpringSecurityConfigTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SpringSecurityConfig springSecurityConfig;

    @Test
    void authenticationProvider_ShouldConfigureDaoAuthenticationProvider() {
        // Arrange
        String testUsername = "test@example.com";
        String testPassword = "password123";
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(userInfoProviderManager.getUserDetails(testUsername)).thenReturn(mockUserDetails);
        when(mockUserDetails.getPassword()).thenReturn(testPassword);
        when(passwordEncoder.matches(testPassword, testPassword)).thenReturn(true);

        // Act
        AuthenticationProvider provider = springSecurityConfig.authenticationProvider();

        // Assert
        assertAll(
            () -> assertNotNull(provider),
            () -> assertInstanceOf(DaoAuthenticationProvider.class, provider)
        );

        // Test the behavior by attempting authentication
        UsernamePasswordAuthenticationToken authentication = 
            new UsernamePasswordAuthenticationToken(testUsername, testPassword);
        var result = provider.authenticate(authentication);
        
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(testUsername, result.getName()),
            () -> verify(userInfoProviderManager).getUserDetails(testUsername),
            () -> verify(passwordEncoder).matches(testPassword, testPassword)
        );
    }

    @Test
    void authenticationManager_ShouldReturnAuthenticationManager() throws Exception {
        // Arrange
        AuthenticationManager expectedManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager())
            .thenReturn(expectedManager);

        // Act
        AuthenticationManager manager = springSecurityConfig.authenticationManager(authenticationConfiguration);

        // Assert
        assertAll(
            () -> assertNotNull(manager),
            () -> assertEquals(expectedManager, manager)
        );
        verify(authenticationConfiguration).getAuthenticationManager();
    }

    @Test
    void jwtConfiguration_ShouldReturnJwtConfig() {
        // Act
        JwtConfig jwtConfig = springSecurityConfig.jwtConfiguration();

        // Assert
        assertAll(
            () -> assertNotNull(jwtConfig),
            () -> assertInstanceOf(JwtConfig.class, jwtConfig)
        );
    }
}