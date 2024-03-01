package com.hftamayo.java.todo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends SecurityConfigurerAdapter
        <DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder();
        }
        return passwordEncoder;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((authorize) -> {
                    authorize.requestMatchers("/api/auth/**").permitAll();
                    authorize.anyRequest().authenticated();
                })
                .addFilter(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider((AuthenticationProvider) authenticationManagerBean());
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }
}