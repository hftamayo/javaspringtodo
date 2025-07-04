package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;
    private UserService userService;
    
    @Value("${app.environment}")
    private String environment;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
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

            return ResponseEntity.ok("LOGIN_SUCCESSFUL " + loginRequestDto.getEmail() + "!" +
                    ", your role is: " + activeSessionResponseDto.getRoles());
        } catch (Exception e) {
            logger.error("LOGIN_INVALID_ATTEMPT " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LOGIN_INVALID_CREDENTIALS");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CrudOperationResponseDto<UserResponseDto> saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            authService.logout(request);
            logger.info("Session destroyed, logout successful");
            return ResponseEntity.ok(Collections
                    .singletonMap("message", "Session destroyed as expected, logout successful, have a nice day"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during logout: " + e.getMessage());
        }
    }

    @GetMapping("/logged-out")
    public ResponseEntity<String> loggedOut() {
        return ResponseEntity.ok("You have been logged out");
    }
}
