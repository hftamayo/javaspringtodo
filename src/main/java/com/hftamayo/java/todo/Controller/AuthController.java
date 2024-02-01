package com.hftamayo.java.todo.Controller;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.JwtAuthResponse;
import com.hftamayo.java.todo.Services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticate(@RequestBody LoginDto loginDto) {
        String token = authService.login(loginDto);

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}
