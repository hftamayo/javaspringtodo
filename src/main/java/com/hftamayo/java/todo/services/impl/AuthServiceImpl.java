package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.auth.LoginRequestDto;
import com.hftamayo.java.todo.dto.auth.ActiveSessionResponseDto;
import com.hftamayo.java.todo.exceptions.UnauthorizedException;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
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


    private final CustomTokenProvider customTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserInfoProviderManager userInfoProviderManager;
    private final UserService userService;

    @Override
    public ActiveSessionResponseDto login(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userService.getUserByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid Credentials: Email or Password not found"));

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleEnum().name()));

        String roleName = user.getRole().getRoleEnum().name();
        List<String> roles = Collections.singletonList(roleName);
        String username = user.getUsername();
        String email = user.getEmail();
        String token = customTokenProvider.getToken(username);
        String tokenType = customTokenProvider.getTokenType();
        long expiresIn = customTokenProvider.getRemainingExpirationTime(token);

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
