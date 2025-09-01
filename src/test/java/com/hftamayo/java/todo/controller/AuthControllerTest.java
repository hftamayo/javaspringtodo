package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.exceptions.AuthenticationException;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void authenticate_WhenValidCredentials_ShouldReturnSuccessResponse() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        ActiveSessionResponseDto sessionResponse = new ActiveSessionResponseDto();
        sessionResponse.setAccessToken("token");
        sessionResponse.setTokenType("Bearer");
        sessionResponse.setExpiresIn(28800000L);
        sessionResponse.setRoles(List.of("ROLE_USER"));

        when(authService.login(loginRequest)).thenReturn(sessionResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.authenticate(loginRequest);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("LOGIN_SUCCESSFUL", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(authService).login(loginRequest)
        );
    }

    @Test
    void authenticate_WhenInvalidCredentials_ShouldReturnUnauthorizedResponse() {
        // Arrange
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong");

        when(authService.login(loginRequest)).thenThrow(new AuthenticationException("Invalid credentials"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.authenticate(loginRequest);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("LOGIN_INVALID_CREDENTIALS", response.getBody().getResultMessage()),
                () -> assertEquals(401, response.getBody().getCode()),
                () -> verify(authService).login(loginRequest)
        );
    }

    @Test
    void saveUser_WhenValidUser_ShouldReturnCreatedResponse() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setEmail("test@example.com");

        when(userService.saveUser(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.saveUser(user);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("USER_REGISTERED", response.getBody().getResultMessage()),
                () -> assertEquals(201, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(userService).saveUser(user)
        );
    }

    @Test
    void saveUser_WhenInvalidUser_ShouldReturnBadRequestResponse() {
        // Arrange
        User user = new User();
        user.setEmail("invalid-email");

        when(userService.saveUser(user)).thenThrow(new RuntimeException("Invalid user data"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.saveUser(user);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("User registration failed", response.getBody().getResultMessage()),
                () -> assertEquals(400, response.getBody().getCode()),
                () -> verify(userService).saveUser(user)
        );
    }

    @Test
    void logout_WhenSuccessful_ShouldReturnOkResponse() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        doNothing().when(authService).logout(request);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.logout(request);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Logout successful", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(authService).logout(request)
        );
    }

    @Test
    void logout_WhenError_ShouldReturnInternalServerErrorResponse() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        doThrow(new RuntimeException("Logout error")).when(authService).logout(request);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.logout(request);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("An error occurred during logout", response.getBody().getResultMessage()),
                () -> assertEquals(500, response.getBody().getCode()),
                () -> verify(authService).logout(request)
        );
    }

    @Test
    void loggedOut_ShouldReturnOkResponse() {
        // Act
        ResponseEntity<EndpointResponseDto<?>> response = authController.loggedOut();

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("LOGGED_OUT", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData())
        );
    }
}