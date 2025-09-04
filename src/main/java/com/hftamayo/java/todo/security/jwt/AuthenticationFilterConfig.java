package com.hftamayo.java.todo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilterConfig {
    private final UserInfoProviderManager userInfoProviderManager;
    private final CustomTokenProvider customTokenProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(userInfoProviderManager, customTokenProvider, objectMapper);
    }
}
