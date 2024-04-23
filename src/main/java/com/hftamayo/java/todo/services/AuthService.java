package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.LoginRequestDto;
import com.hftamayo.java.todo.dto.TokenResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponseDto login(LoginRequestDto loginRequest);

    void logout(HttpServletRequest request);

    void invalidateToken();

//    UserResponseDto register(RegisterRequest registerRequest);

}
