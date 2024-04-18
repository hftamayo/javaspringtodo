package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.TokenResponseDto;
import com.hftamayo.java.todo.Dto.UserResponseDto;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponseDto login(LoginDto loginRequest);

    void logout(HttpServletRequest request);

    void invalidateToken();

//    UserResponseDto register(RegisterRequest registerRequest);

}
