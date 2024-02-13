package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    String login(LoginDto loginDto);
    String extractTokenFromRequest(HttpServletRequest request);
    void invalidateToken(String token);
}
