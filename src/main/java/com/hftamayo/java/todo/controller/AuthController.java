package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;
    private final UserService userService;
    
    @Value("${app.environment}")
    private String environment;

    @PostMapping("/login")
    public ResponseEntity<EndpointResponseDto<?>> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        logger.info("Login attempt: {}", loginRequestDto.getEmail());
        try {
            ActiveSessionResponseDto activeSessionResponseDto = authService.login(loginRequestDto);

            logger.info("Login successful, welcome: {}!, assigned roles: {}",
                    loginRequestDto.getEmail(), activeSessionResponseDto.getRoles());

            // Only log JWT details in development or when explicitly enabled
            if ("development".equals(environment)) {
                logger.info("token: {} type: {} expires in: {} hours",
                        activeSessionResponseDto.getAccessToken(), activeSessionResponseDto.getTokenType(),
                        activeSessionResponseDto.getExpiresIn());
            }

            EndpointResponseDto<ActiveSessionResponseDto> response = ResponseUtil
                    .successResponse(activeSessionResponseDto, "LOGIN_SUCCESSFUL");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("LOGIN_INVALID_ATTEMPT " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseUtil.errorResponse(HttpStatus.UNAUTHORIZED, "LOGIN_INVALID_CREDENTIALS", e));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<EndpointResponseDto<?>> saveUser(@RequestBody User user) {
        try {
            EndpointResponseDto<UserResponseDto> response = ResponseUtil
                    .createdResponse(userService.saveUser(user).getData(), "USER_REGISTERED");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "User registration failed", e),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<EndpointResponseDto<?>> logout(HttpServletRequest request) {
        try {
            authService.logout(request);
            EndpointResponseDto<String> response = ResponseUtil
                    .successResponse("Session destroyed as expected, logout successful, have a nice day",
                            "Logout successful");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.
                        INTERNAL_SERVER_ERROR, "An error occurred during logout", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/logged-out")
    public ResponseEntity<EndpointResponseDto<?>> loggedOut() {
        try {
            EndpointResponseDto<String> response = ResponseUtil
                    .successResponse("You have been logged out", "LOGGED_OUT");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus
                        .INTERNAL_SERVER_ERROR, "An error occurred while processing logged-out endpoint", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
