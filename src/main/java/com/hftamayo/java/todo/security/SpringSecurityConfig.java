package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.jwt.JwtConfig;
import com.hftamayo.java.todo.security.managers.UserInfoProviderManager;
import com.hftamayo.java.todo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SpringSecurityConfig.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoProviderManager userInfoProviderManager;

    @Autowired
    public SpringSecurityConfig(UserService userService, PasswordEncoder passwordEncoder,
                                UserInfoProviderManager userInfoProviderManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userInfoProviderManager = userInfoProviderManager;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userInfoProviderManager::getUserDetails);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }
}