package com.hftamayo.java.todo.Security;

import com.hftamayo.java.todo.Services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerConfig {


    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticationManagerConfig(CustomUserDetailsService customUserDetailsService,
                                       AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}