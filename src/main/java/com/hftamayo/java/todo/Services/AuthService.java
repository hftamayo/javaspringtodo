package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.TokenResponseDto;
import com.hftamayo.java.todo.Dto.UserResponseDto;

public interface AuthService {

    TokenResponseDto login(LoginDto loginRequest);

//    UserResponseDto register(RegisterRequest registerRequest);

}
