package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration

public class UserDetailsServiceConfig {
    private final UserInfoProviderManager userInfoProviderManager;

    @Autowired
    public UserDetailsServiceConfig(UserInfoProviderManager userInfoProviderManager) {
        this.userInfoProviderManager = userInfoProviderManager;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userEmail -> (UserDetails) userService.getUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials: Username or Password not found"));
    }
}