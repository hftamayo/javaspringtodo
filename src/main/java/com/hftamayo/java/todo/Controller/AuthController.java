package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.JwtAuthResponse;
import com.hftamayo.java.todo.Dto.UserResponseDto;
import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Services.AuthService;
import com.hftamayo.java.todo.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticate(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
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
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = authService.extractTokenFromRequest(request);
        authService.invalidateToken(token);
        return ResponseEntity.ok("Logout successful");
    }
}
