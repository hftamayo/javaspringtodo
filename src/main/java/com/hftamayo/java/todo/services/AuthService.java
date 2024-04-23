package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.LoginRequestDto;
import com.hftamayo.java.todo.dto.ActiveSessionResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    ActiveSessionResponseDto login(LoginRequestDto loginRequest);

    void logout(HttpServletRequest request);

    void invalidateToken();

//    UserResponseDto register(RegisterRequest registerRequest);

}
