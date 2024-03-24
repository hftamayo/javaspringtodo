package com.hftamayo.java.todo.Services.Impl;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Dto.TokenResponseDto;
import com.hftamayo.java.todo.Model.User;
import com.hftamayo.java.todo.Repository.UserRepository;
import com.hftamayo.java.todo.Services.AuthService;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomTokenProvider customTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponseDto login(LoginDto loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid Credentials: Email or Password not found"));
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(new ArrayList<>())
                .build();


        String token = customTokenProvider.getToken(userDetails);
        return new TokenResponseDto(token, customTokenProvider.getTokenType(),
                customTokenProvider.getRemainingExpirationTime(token));
    }

//    @Override
//    public AuthResponse register(RegisterRequest registerRequest) {
//        User user = User.builder()
//                .email(registerRequest.getEmail())
//                .password(passwordEncoder.encode(registerRequest.getPassword()))
//                .build();
//
//        userRepository.save(user);
//
//        return AuthResponse.builder()
//                .token(customTokenProvider.getToken((UserDetails) user))
//                .build();
//    }
}
