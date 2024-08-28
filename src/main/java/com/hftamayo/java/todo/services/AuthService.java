package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    ActiveSessionResponseDto login(LoginRequestDto loginRequest);

    void logout(HttpServletRequest request);

    void invalidateToken();

//    UserResponseDto register(RegisterRequest registerRequest);

}
