package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.exceptions.AuthenticationException;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private CustomTokenProvider customTokenProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_WhenValidCredentials_ShouldReturnActiveSession() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = createUser();
        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(customTokenProvider.getToken(user.getUsername())).thenReturn("token123");
        when(customTokenProvider.getTokenType()).thenReturn("Bearer");
        when(customTokenProvider.getRemainingExpirationTime("token123")).thenReturn(3600L);

        // Act
        ActiveSessionResponseDto result = authService.login(loginRequest);

        // Assert
        assertNotNull(result);
        //assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("token123", result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(3600L, result.getExpiresIn());
        assertTrue(result.getRoles().contains("ROLE_USER"));
    }

    @Test
    void login_WhenUserInactive_ShouldThrowUnauthorizedException() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");

        User inactiveUser = createUser();
        inactiveUser.setStatus(false);

        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authService.login(loginRequest));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_WhenInvalidPassword_ShouldThrowUnauthorizedException() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpass");

        User user = createUser();
        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authService.login(loginRequest));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void logout_WhenValidToken_ShouldInvalidateToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(customTokenProvider.getEmailFromToken("validToken")).thenReturn("test@example.com");
        when(customTokenProvider.isTokenValid("validToken", "test@example.com")).thenReturn(true);

        // Act
        authService.logout(request);

        // Assert
        verify(customTokenProvider).invalidateToken();
    }

    @Test
    void logout_WhenInvalidToken_ShouldThrowUnauthorizedException() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(customTokenProvider.getEmailFromToken("invalidToken")).thenReturn("test@example.com");
        when(customTokenProvider.isTokenValid("invalidToken", "test@example.com")).thenReturn(false);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authService.logout(request));
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword");
        user.setStatus(true);

        Roles role = new Roles();
        role.setRoleEnum(ERole.ROLE_USER);
        user.setRole(role);

        return user;
    }
}