package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.services.impl.UserDetailsServiceImpl;
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
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticationManagerConfig(UserDetailsServiceImpl userDetailsServiceImpl,
                                       AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}