package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceConfigTest {

    @Mock
    private UserInfoProviderManager userInfoProviderManager;

    @InjectMocks
    private UserDetailsServiceConfig userDetailsServiceConfig;

    @Test
    void userDetailsService_ShouldReturnUserDetailsService() {
        // Arrange
        String email = "test@example.com";
        UserDetails expectedUserDetails = mock(UserDetails.class);
        when(userInfoProviderManager.getUserDetails(email)).thenReturn(expectedUserDetails);

        // Act
        UserDetailsService userDetailsService = userDetailsServiceConfig.userDetailsService();
        UserDetails actualUserDetails = userDetailsService.loadUserByUsername(email);

        // Assert
        assertAll(
                () -> assertNotNull(userDetailsService),
                () -> assertEquals(expectedUserDetails, actualUserDetails),
                () -> verify(userInfoProviderManager).getUserDetails(email)
        );
    }

    @Test
    void userDetailsService_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userInfoProviderManager.getUserDetails(email))
                .thenThrow(new UsernameNotFoundException("Invalid Credentials: Email or Password not found"));

        // Act & Assert
        UserDetailsService userDetailsService = userDetailsServiceConfig.userDetailsService();
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email));
        
        assertEquals("Invalid Credentials: Email or Password not found", exception.getMessage());
    }
}