package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTokenProviderConfig {
    private final JwtConfig jwtConfig;
    private final UserInfoProviderManager userInfoProviderManager;

    @Autowired
    public CustomTokenProviderConfig(JwtConfig jwtConfig,
                                     UserInfoProviderManager userInfoProviderManager) {
        this.jwtConfig = jwtConfig;
        this.userInfoProviderManager = userInfoProviderManager;
    }

    @Bean
    public CustomTokenProvider customTokenProvider() {
        return new CustomTokenProvider(jwtConfig, userInfoProviderManager);
    }
}