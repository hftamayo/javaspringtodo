package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginRequestDto loginRequestDto) {
        logger.info("Login attempt: " + loginRequestDto.getEmail());
        try {
            ActiveSessionResponseDto activeSessionResponseDto = authService.login(loginRequestDto);

            logger.info("Login successful, welcome: " + loginRequestDto.getEmail() +
                    "!, assigned roles: " + activeSessionResponseDto.getRoles());

            logger.info("token: " + activeSessionResponseDto.getAccessToken() + " type: " +
                    activeSessionResponseDto.getTokenType() + " expires in: " +
                    activeSessionResponseDto.getExpiresIn() + " hours");

            return ResponseEntity.ok("Login successful, welcome: " + loginRequestDto.getEmail() + "!" +
                    ", your role is: " + activeSessionResponseDto.getRoles());
        } catch (Exception e) {
            logger.error("Error in login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            authService.logout(request);
            logger.info("Session destroyed, logout successful");
            return ResponseEntity.ok(Collections.singletonMap("message", "Session destroyed as expected, logout successful, have a nice day"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout: " + e.getMessage());
        }
    }

    @GetMapping("/logged-out")
    public ResponseEntity<String> loggedOut() {
        return ResponseEntity.ok("You have been logged out");
    }
}
