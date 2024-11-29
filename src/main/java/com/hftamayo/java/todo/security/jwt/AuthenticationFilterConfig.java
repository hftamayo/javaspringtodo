package com.hftamayo.java.todo.security.jwt;

import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilterConfig {
    private final UserInfoProviderManager userInfoProviderManager;
    private final CustomTokenProvider customTokenProvider;

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(userInfoProviderManager, customTokenProvider);
    }
}
