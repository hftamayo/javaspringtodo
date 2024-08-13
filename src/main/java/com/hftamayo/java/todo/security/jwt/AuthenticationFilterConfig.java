package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class AuthenticationFilterConfig {
    private final UserInfoProviderManager userInfoProviderManager;
    private final CustomTokenProvider customTokenProvider;

    public AuthenticationFilterConfig(UserInfoProviderManager userInfoProviderManager, CustomTokenProvider customTokenProvider) {
        this.userInfoProviderManager = userInfoProviderManager;
        this.customTokenProvider = customTokenProvider;
    }

    @Bean
    @Lazy
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(userInfoProviderManager, customTokenProvider);
    }
}
