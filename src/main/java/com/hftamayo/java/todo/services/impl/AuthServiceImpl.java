package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.LoginRequestDto;
import com.hftamayo.java.todo.dto.ActiveSessionResponseDto;
import com.hftamayo.java.todo.exceptions.UnauthorizedException;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.services.AuthService;
import com.hftamayo.java.todo.security.jwt.CustomTokenProvider;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final CustomTokenProvider customTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Override
    public ActiveSessionResponseDto login(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));
        User user = userService.getUserByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new UsernameNotFoundException("Invalid Credentials: Email or Password not found"));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().name()))
                .collect(Collectors.toList());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleEnum().toString())
                .collect(Collectors.toList());

        String username = user.getUsername();
        String email = user.getEmail();
        String token = customTokenProvider.getToken(userDetails);
        String tokenType = customTokenProvider.getTokenType();
        long expiresIn = customTokenProvider.getRemainingExpirationTime(token);

        return new ActiveSessionResponseDto(username, email, roles, token, tokenType, expiresIn);
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
