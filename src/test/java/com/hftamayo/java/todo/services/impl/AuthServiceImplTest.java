package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.exceptions.UnauthorizedException;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import com.hftamayo.java.todo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private CustomTokenProvider customTokenProvider;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private UserInfoProviderManager userInfoProviderManager;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        customTokenProvider = Mockito.mock(CustomTokenProvider.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        userInfoProviderManager = Mockito.mock(UserInfoProviderManager.class);
        userService = Mockito.mock(UserService.class);

        authService = new AuthServiceImpl(customTokenProvider, passwordEncoder,
                authenticationManager, userInfoProviderManager, userService);
    }

    @Test
    public void testLogin_Success() {
        LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "password");
        User user = new User();
        user.setName("testuser");
        user.setEmail("test@example.com");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(customTokenProvider.getToken(anyString())).thenReturn("token");
        when(customTokenProvider.getTokenType()).thenReturn("Bearer");
        when(customTokenProvider.getRemainingExpirationTime(anyString())).thenReturn(3600L);

        ActiveSessionResponseDto response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(Collections.singletonList("USER"), response.getRoles());
        assertEquals("token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(3600L, response.getExpiresIn());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        LoginRequestDto loginRequest = new LoginRequestDto("test@example.com", "password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new UsernameNotFoundException("Invalid Credentials"));

        assertThrows(UsernameNotFoundException.class, () -> authService.login(loginRequest));
    }

    @Test
    public void testLogout_Success() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(customTokenProvider.getUsernameFromToken(anyString())).thenReturn("testuser");
        when(customTokenProvider.isTokenValid(anyString(), anyString())).thenReturn(true);

        authService.logout(request);

        verify(customTokenProvider, times(1)).invalidateToken();
    }

    @Test
    public void testLogout_InvalidToken() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(customTokenProvider.getUsernameFromToken(anyString())).thenReturn("testuser");
        when(customTokenProvider.isTokenValid(anyString(), anyString())).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.logout(request));
    }

    @Test
    public void testInvalidateToken() {
        authService.invalidateToken();
        verify(customTokenProvider, times(1)).invalidateToken();
    }
}