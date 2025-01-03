package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.controller.AuthController;
import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.exceptions.UnauthorizedException;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final CustomTokenProvider customTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserInfoProviderManager userInfoProviderManager;
    private final UserService userService;

    @Override
    public ActiveSessionResponseDto login(LoginRequestDto loginRequest) {
        logger.info("Authenticating user: {}", loginRequest.getEmail());
        authenticateUser(loginRequest);
        User user = fetchUser(loginRequest.getEmail());
        return generateActiveSessionResponse(user);
    }

    private void authenticateUser(LoginRequestDto loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), loginRequest.getPassword()));
            logger.info("Authentication successful for user: {}", loginRequest.getEmail());
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new UnauthorizedException("Invalid Credentials: Email or Password not found");
        }
    }

    private User fetchUser(String email) {
        return userService.getUserByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", email);
                    return new UsernameNotFoundException("Invalid Credentials: Email or Password not found");
                });
    }

    private ActiveSessionResponseDto generateActiveSessionResponse(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleEnum().name()));

        String roleName = user.getRole().getRoleEnum().name();
        List<String> roles = Collections.singletonList(roleName);
        String username = user.getUsername();
        String email = user.getEmail();
        String token = customTokenProvider.getToken(username);
        String tokenType = customTokenProvider.getTokenType();
        long expiresIn = customTokenProvider.getRemainingExpirationTime(token);

        logger.info("User authenticated: {}, roles: {}", username, roles);
        logger.info("Generated token: {}, type: {}, expires in: {} ms", token, tokenType, expiresIn);

        return new ActiveSessionResponseDto(username, email, roles, token, tokenType, expiresIn);
    }
    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer " prefix
        String email = customTokenProvider.getEmailFromToken(token);

        if (!customTokenProvider.isTokenValid(token, email)) {
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
