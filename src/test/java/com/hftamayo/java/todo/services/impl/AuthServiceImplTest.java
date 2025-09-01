package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.exceptions.AuthenticationException;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private CustomTokenProvider customTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_WhenValidCredentials_ShouldReturnActiveSessionResponse() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = createUser(1L, "test@example.com", "password123", true);
        Roles role = createRole(1L, ERole.ROLE_USER);
        user.setRole(role);

        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "password123")).thenReturn(true);
        when(customTokenProvider.getToken("test@example.com")).thenReturn("jwt-token");
        when(customTokenProvider.getTokenType()).thenReturn("Bearer");
        when(customTokenProvider.getRemainingExpirationTime("jwt-token")).thenReturn(3600000L);

        // Act
        ActiveSessionResponseDto result = authService.login(loginRequest);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals(List.of("ROLE_USER"), result.getRoles());
        assertEquals("jwt-token", result.getAccessToken());
        assertEquals("Bearer", result.getTokenType());
        assertEquals(3600000L, result.getExpiresIn());

        verify(userService).loginRequest("test@example.com");
        verify(passwordEncoder).matches("password123", "password123");
        verify(customTokenProvider).getToken("test@example.com");
        verify(customTokenProvider).getTokenType();
        verify(customTokenProvider).getRemainingExpirationTime("jwt-token");
    }

    @Test
    void login_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("password123");

        when(userService.loginRequest("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> authService.login(loginRequest));
        
        assertEquals("Invalid Credentials: Email or Password not found", exception.getMessage());
        verify(userService).loginRequest("nonexistent@example.com");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(customTokenProvider);
    }

    @Test
    void login_WhenUserIsInactive_ShouldThrowAuthenticationException() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = createUser(1L, "test@example.com", "password123", false);

        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authService.login(loginRequest));
        
        assertEquals("Invalid email or password", exception.getMessage());
        verify(userService).loginRequest("test@example.com");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(customTokenProvider);
    }

    @Test
    void login_WhenInvalidPassword_ShouldThrowAuthenticationException() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongpassword");

        User user = createUser(1L, "test@example.com", "password123", true);

        when(userService.loginRequest("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "password123")).thenReturn(false);

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authService.login(loginRequest));
        
        assertEquals("Invalid username or password", exception.getMessage());
        verify(userService).loginRequest("test@example.com");
        verify(passwordEncoder).matches("wrongpassword", "password123");
        verifyNoInteractions(customTokenProvider);
    }

    @Test
    void logout_WhenValidToken_ShouldLogoutSuccessfully() {
        // Arrange
        String token = "valid-jwt-token";
        String email = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(customTokenProvider.isTokenValid(token, email)).thenReturn(true);
        doNothing().when(customTokenProvider).invalidateToken();

        // Act
        assertDoesNotThrow(() -> authService.logout(request));

        // Assert
        verify(request).getHeader("Authorization");
        verify(customTokenProvider).getEmailFromToken(token);
        verify(customTokenProvider).isTokenValid(token, email);
        verify(customTokenProvider).invalidateToken();
    }

    @Test
    void logout_WhenInvalidToken_ShouldThrowAuthenticationException() {
        // Arrange
        String token = "invalid-jwt-token";
        String email = "test@example.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(customTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(customTokenProvider.isTokenValid(token, email)).thenReturn(false);

        // Act & Assert
        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authService.logout(request));
        
        assertEquals("Invalid token", exception.getMessage());
        verify(request).getHeader("Authorization");
        verify(customTokenProvider).getEmailFromToken(token);
        verify(customTokenProvider).isTokenValid(token, email);
        verify(customTokenProvider, never()).invalidateToken();
    }

    @Test
    void invalidateToken_ShouldCallCustomTokenProvider() {
        // Arrange
        doNothing().when(customTokenProvider).invalidateToken();

        // Act
        assertDoesNotThrow(() -> authService.invalidateToken());

        // Assert
        verify(customTokenProvider).invalidateToken();
    }

    // Helper methods
    private User createUser(Long id, String email, String password, boolean status) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setStatus(status);
        user.setName("Test User");
        user.setAge(25);
        user.setAdmin(false);
        return user;
    }

    private Roles createRole(Long id, ERole roleEnum) {
        Roles role = new Roles();
        role.setId(id);
        role.setRoleEnum(roleEnum);
        role.setDescription("Description for " + roleEnum.name());
        role.setStatus(true);
        return role;
    }
}