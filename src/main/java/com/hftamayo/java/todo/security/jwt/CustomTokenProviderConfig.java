package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomTokenProviderConfig {
    private final JwtConfig jwtConfig;
    private final UserInfoProviderManager userInfoProviderManager;

    @Bean
    public CustomTokenProvider customTokenProvider() {
        return new CustomTokenProvider(jwtConfig, userInfoProviderManager);
    }
}