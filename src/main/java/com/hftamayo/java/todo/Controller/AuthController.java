package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.TokenResponseDto;
import com.hftamayo.java.todo.Dto.UserResponseDto;
import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Services.AuthService;
import com.hftamayo.java.todo.Services.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private AuthService authService;
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginDto loginDto) {
        logger.info("Login attempt: " + loginDto.getEmail() + " " + loginDto.getPassword());
        try {
            TokenResponseDto tokenResponseDto = authService.login(loginDto);

            logger.info("Login successful, welcome: " + loginDto.getEmail() + "!");

            System.out.println("token: " + tokenResponseDto.getAccessToken() + " type: " +
                    tokenResponseDto.getTokenType() + " expires in: " + tokenResponseDto.getExpiresIn());

            return ResponseEntity.ok("Login successful, welcome: " + loginDto.getEmail() + "!");
        } catch (Exception e) {
            logger.error("Error in login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@RequestBody User user){
        User savedUser = userService.saveUser(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(savedUser.getId());
        userResponseDto.setName(savedUser.getName());
        userResponseDto.setEmail(savedUser.getEmail());
        userResponseDto.setAge(savedUser.getAge());
        userResponseDto.setAdmin(savedUser.isAdmin());
        userResponseDto.setStatus(savedUser.isStatus());

        return userResponseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.invalidateToken();
        return ResponseEntity.ok("Logout successful");
    }
}
