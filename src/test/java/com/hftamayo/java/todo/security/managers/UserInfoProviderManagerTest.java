package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoProviderManagerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserInfoProviderManager userInfoProviderManager;

    private User testUser;
    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        // Create a test user with all necessary fields
        testUser = new User();
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(TEST_PASSWORD);
        testUser.setEnabled(true);
        testUser.setAccountNonExpired(true);
        testUser.setCredentialsNonExpired(true);
        testUser.setAccountNonLocked(true);
        
        // Set up a role for the user
        Roles role = new Roles();
        role.setRoleEnum(ERole.ROLE_USER);
        testUser.setRole(role);
    }

    @Test
    void getUserDetails_WhenUserExists_ShouldReturnUserDetails() {
        // Arrange
        when(userService.loginRequest(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userInfoProviderManager.getUserDetails(TEST_EMAIL);

        // Assert
        assertAll(
            () -> assertNotNull(userDetails),
            () -> assertEquals(TEST_EMAIL, userDetails.getUsername()),
            () -> assertEquals(TEST_PASSWORD, userDetails.getPassword()),
            () -> assertTrue(userDetails.isEnabled()),
            () -> assertTrue(userDetails.isAccountNonExpired()),
            () -> assertTrue(userDetails.isCredentialsNonExpired()),
            () -> assertTrue(userDetails.isAccountNonLocked()),
            () -> assertEquals(1, userDetails.getAuthorities().size()),
            () -> assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority())
        );
        
        verify(userService).loginRequest(TEST_EMAIL);
    }

    @Test
    void getUserDetails_WhenUserDoesNotExist_ShouldThrowUsernameNotFoundException() {
        // Arrange
        when(userService.loginRequest(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> 
            userInfoProviderManager.getUserDetails(TEST_EMAIL)
        );
        
        verify(userService).loginRequest(TEST_EMAIL);
    }

    @Test
    void getUserDetails_WhenUserIsDisabled_ShouldReturnDisabledUserDetails() {
        // Arrange
        testUser.setEnabled(false);
        when(userService.loginRequest(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userInfoProviderManager.getUserDetails(TEST_EMAIL);

        // Assert
        assertAll(
            () -> assertNotNull(userDetails),
            () -> assertFalse(userDetails.isEnabled()),
            () -> assertEquals(TEST_EMAIL, userDetails.getUsername())
        );
        
        verify(userService).loginRequest(TEST_EMAIL);
    }

    @Test
    void getUserDetails_WhenUserIsLocked_ShouldReturnLockedUserDetails() {
        // Arrange
        testUser.setAccountNonLocked(false);
        when(userService.loginRequest(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userInfoProviderManager.getUserDetails(TEST_EMAIL);

        // Assert
        assertAll(
            () -> assertNotNull(userDetails),
            () -> assertFalse(userDetails.isAccountNonLocked()),
            () -> assertEquals(TEST_EMAIL, userDetails.getUsername())
        );
        
        verify(userService).loginRequest(TEST_EMAIL);
    }
}