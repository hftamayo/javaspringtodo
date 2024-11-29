package com.hftamayo.java.todo.security.managers;

import com.hftamayo.java.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserInfoProviderManagerConfig {
    private final UserService userService;

    @Bean
    public UserInfoProviderManager userInfoProviderManager() {
        return new UserInfoProviderManager(userService);
    }
}