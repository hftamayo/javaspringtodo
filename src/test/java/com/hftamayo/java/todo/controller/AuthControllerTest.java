package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
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
import java.util.Map;

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
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        ActiveSessionResponseDto sessionResponse = new ActiveSessionResponseDto();
        sessionResponse.setAccessToken("token");
        sessionResponse.setTokenType("Bearer");
        sessionResponse.setExpiresIn(28800000L);
        sessionResponse.setRoles(List.of("ROLE_USER"));

        when(authService.login(loginRequest)).thenReturn(sessionResponse);

        ResponseEntity<String> response = authController.authenticate(loginRequest);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(response.getBody().contains("LOGIN_SUCCESSFUL")),
                () -> assertTrue(response.getBody().contains(loginRequest.getEmail())),
                () -> verify(authService).login(loginRequest)
        );
    }

    @Test
    void authenticate_WhenInvalidCredentials_ShouldReturnUnauthorized() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrong");

        when(authService.login(loginRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<String> response = authController.authenticate(loginRequest);

        assertAll(
                () -> assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode()),
                () -> assertEquals("LOGIN_INVALID_CREDENTIALS", response.getBody()),
                () -> verify(authService).login(loginRequest)
        );
    }

    @Test
    void saveUser_WhenValidUser_ShouldReturnSuccessResponse() {
        User user = new User();
        CrudOperationResponseDto<UserResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(201);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new UserResponseDto());

        when(userService.saveUser(user)).thenReturn(expectedResponse);

        CrudOperationResponseDto<UserResponseDto> response = authController.saveUser(user);

        assertAll(
                () -> assertEquals(201, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(userService).saveUser(user)
        );
    }

    @Test
    void logout_WhenSuccessful_ShouldReturnOkResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doNothing().when(authService).logout(request);

        ResponseEntity<?> response = authController.logout(request);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertTrue(((Map<?,?>)response.getBody()).get("message").toString()
                        .contains("Session destroyed as expected")),
                () -> verify(authService).logout(request)
        );
    }

    @Test
    void logout_WhenError_ShouldReturnErrorResponse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        doThrow(new RuntimeException("Logout error")).when(authService).logout(request);

        ResponseEntity<?> response = authController.logout(request);

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertEquals("An error occurred during logout: Logout error", response.getBody()),
                () -> verify(authService).logout(request)
        );
    }

    @Test
    void loggedOut_ShouldReturnOkResponse() {
        ResponseEntity<String> response = authController.loggedOut();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("You have been logged out", response.getBody())
        );
    }
}