package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.LoginRequestDto;
import com.hftamayo.java.todo.dto.ActiveSessionResponseDto;
import com.hftamayo.java.todo.dto.RegisterUserResponseDto;
import com.hftamayo.java.todo.model.User;
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
        logger.info("Login attempt: " + loginRequestDto.getEmail() + " " + loginRequestDto.getPassword());
        try {
            ActiveSessionResponseDto activeSessionResponseDto = authService.login(loginRequestDto);

            logger.info("Login successful, welcome: " + loginRequestDto.getEmail() + "!");

            System.out.println("token: " + activeSessionResponseDto.getAccessToken() + " type: " +
                    activeSessionResponseDto.getTokenType() + " expires in: " + activeSessionResponseDto.getExpiresIn() + "hours");

            return ResponseEntity.ok("Login successful, welcome: " + loginRequestDto.getEmail() + "!"+
                    ", your role is: "+ activeSessionResponseDto.getRole());
        } catch (Exception e) {
            logger.error("Error in login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserResponseDto saveUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        RegisterUserResponseDto registerUserResponseDto = new RegisterUserResponseDto();
        registerUserResponseDto.setId(savedUser.getId());
        registerUserResponseDto.setName(savedUser.getName());
        registerUserResponseDto.setEmail(savedUser.getEmail());
        registerUserResponseDto.setAge(savedUser.getAge());
        registerUserResponseDto.setAdmin(savedUser.isAdmin());
        registerUserResponseDto.setStatus(savedUser.isStatus());

        return registerUserResponseDto;
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
}
