package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    private AuthController authController;
    private AuthService authService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        authService = Mockito.mock(AuthService.class);
        userService = Mockito.mock(UserService.class);
        authController = new AuthController(authService, userService);
    }

    @Test
    public void testAuthenticate_Success() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");
        ActiveSessionResponseDto activeSessionResponseDto = new ActiveSessionResponseDto();
        activeSessionResponseDto.setRoles(Collections.singletonList("ROLE_USER"));
        activeSessionResponseDto.setAccessToken("token");
        activeSessionResponseDto.setTokenType("Bearer");
        activeSessionResponseDto.setExpiresIn(1);

        when(authService.login(loginRequestDto)).thenReturn(activeSessionResponseDto);

        ResponseEntity<String> response = authController.authenticate(loginRequestDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody()
                .contains("Login successful, welcome: test@example.com!"));
    }

    @Test
    public void testAuthenticate_Failure() {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("test@example.com");

        when(authService.login(loginRequestDto)).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<String> response = authController.authenticate(loginRequestDto);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        UserResponseDto userResponseDto = new UserResponseDto();
        when(userService.saveUser(user)).thenReturn(userResponseDto);

        UserResponseDto response = authController.saveUser(user);
        assertEquals(userResponseDto, response);
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testLogout_Success() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        doNothing().when(authService).logout(request);

        ResponseEntity<?> response = authController.logout(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Session destroyed as expected, logout successful, have a nice day",
                ((Map<String, String>) response.getBody()).get("message"));
    }

    @Test
    public void testLogout_Failure() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        doThrow(new RuntimeException("Logout error")).when(authService).logout(request);

        ResponseEntity<?> response = authController.logout(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An error occurred during logout: Logout error", response.getBody());
    }

    @Test
    public void testLoggedOut() {
        ResponseEntity<String> response = authController.loggedOut();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("You have been logged out", response.getBody());
    }
}