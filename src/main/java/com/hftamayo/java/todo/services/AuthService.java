package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.LoginDto;
import com.hftamayo.java.todo.dto.TokenResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponseDto login(LoginDto loginRequest);

    void logout(HttpServletRequest request);

    void invalidateToken();

//    UserResponseDto register(RegisterRequest registerRequest);

}
