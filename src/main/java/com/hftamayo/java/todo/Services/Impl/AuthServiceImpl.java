package com.hftamayo.java.todo.Services.Impl;

import com.hftamayo.java.todo.Dto.LoginDto;
import com.hftamayo.java.todo.Repository.UserRepository;
import com.hftamayo.java.todo.Security.JwtTokenProvider;
import com.hftamayo.java.todo.Security.SpringSecurityConfig;
import com.hftamayo.java.todo.Services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@DependsOn("springSecurityConfig")
public class AuthServiceImpl implements AuthService {
    private SpringSecurityConfig springSecurityConfig;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            SpringSecurityConfig springSecurityConfig) {
        this.springSecurityConfig = springSecurityConfig;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication = springSecurityConfig.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
    }

    @Override
    public String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public void invalidateToken(String token) {

        jwtTokenProvider.invalidateToken(token);
    }
}
