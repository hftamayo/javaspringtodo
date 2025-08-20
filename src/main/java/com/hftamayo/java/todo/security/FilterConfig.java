package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.security.handlers.CustomAccessDeniedHandler;
import com.hftamayo.java.todo.security.jwt.AuthenticationFilter;
import com.hftamayo.java.todo.security.managers.CustomAccessDecisionManager;
import com.hftamayo.java.todo.security.managers.CustomFilterInvocationSecurityMetadataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class FilterConfig {
    private static final Logger logger = LoggerFactory.getLogger(FilterConfig.class);

    private final AuthenticationFilter authenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAccessDecisionManager customAccessDecisionManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   FilterSecurityInterceptor filterSecurityInterceptor)
            throws Exception {
        logger.info("Configuring Security Filter Chain");

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> {
                    csrf.disable();
                    logger.info("CSRF is disabled");
                })
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers("/api/auth/**", "/api/auth/register/**", "/api/auth/login/**", "/error").permitAll()
                            .requestMatchers("/api/health/**").permitAll()
                            .requestMatchers("/api/users/manager/**").hasAnyRole("SUPERVISOR", "ADMIN")
                            .requestMatchers("/api/supervisor/**").hasAnyRole("SUPERVISOR", "ADMIN")
                            .requestMatchers("/api/admin/**").hasRole("ADMIN")
                            .requestMatchers("/api/tasks/**").authenticated()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(accessDeniedHandler);
                    logger.info("Exception handling configured");
                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    logger.info("Session management configured");
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow specific origins (your frontend URLs)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",      // React default
            "http://localhost:5173",      // Vite default
            "http://localhost:4200",      // Angular default
            "http://localhost:8080",      // Vue default
            "http://127.0.0.1:3000",
            "http://127.0.0.1:5173",
            "http://127.0.0.1:4200",
            "http://127.0.0.1:8080"
        ));
        
        // Allow all HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Allow all headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // Set max age for preflight requests
        configuration.setMaxAge(3600L);
        
        // Configure exposed headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-RateLimit-Limit",
            "X-RateLimit-Remaining", 
            "X-RateLimit-Reset",
            "X-API-Version",
            "X-Build-Version"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        logger.info("CORS configuration loaded with allowed origins: {}", configuration.getAllowedOriginPatterns());
        return source;
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor(AuthenticationManager authenticationManager,
                                                               CustomFilterInvocationSecurityMetadataSource cfisMetadataSource) {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        filterSecurityInterceptor.setAccessDecisionManager(customAccessDecisionManager);
        filterSecurityInterceptor.setSecurityMetadataSource(cfisMetadataSource);
        return filterSecurityInterceptor;
    }
}