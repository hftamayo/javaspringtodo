package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.exceptions.CustomAccessDeniedHandler;
import com.hftamayo.java.todo.security.managers.CustomAccessDecisionManager;
import com.hftamayo.java.todo.security.jwt.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class FilterConfig {
    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAccessDecisionManager customAccessDecisionManager;
    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        logger.info("Configuring Security Filter Chain");
        return httpSecurity
                .csrf(csrf -> {
                    csrf.disable();
                    logger.info("CSRF is disabled");
                })

                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers("/api/auth/**", "/api/auth/register/**", "/api/auth/login/**").permitAll()
                            .requestMatchers("/api/health/**").permitAll()
                            .requestMatchers("/api/user/**").hasAnyRole("USER", "SUPERVISOR", "ADMIN")
                            .requestMatchers("/api/supervisor/**").hasAnyRole("SUPERVISOR", "ADMIN")
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                            .accessDecisionManager(customAccessDecisionManager);
                    logger.info("Authorization requests configured");
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling
                            .accessDeniedHandler(accessDeniedHandler);
                    logger.info("Exception handling configured");
                })
                .sessionManagement(sessionManager -> {
                    sessionManager
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    logger.info("Session management configured");
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
