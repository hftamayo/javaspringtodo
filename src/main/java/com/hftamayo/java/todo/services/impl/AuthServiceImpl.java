package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.LoginRequestDto;
import com.hftamayo.java.todo.dto.ActiveSessionResponseDto;
import com.hftamayo.java.todo.exceptions.UnauthorizedException;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomTokenProvider customTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Override
    public ActiveSessionResponseDto login(LoginRequestDto loginRequest) {
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
        String tokenType = customTokenProvider.getTokenType();
        long expiresIn = customTokenProvider.getRemainingExpirationTime(token);

        return new ActiveSessionResponseDto(token, tokenType, expiresIn);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer " prefix
        String username = customTokenProvider.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!customTokenProvider.isTokenValid(token, userDetails)) {
            throw new UnauthorizedException("Invalid token: the session is not valid");
        }
        customTokenProvider.invalidateToken();
    }

    @Override
    public void invalidateToken() {
        customTokenProvider.invalidateToken();
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
