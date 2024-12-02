package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.exceptions.CustomAccessDeniedHandler;
import com.hftamayo.java.todo.security.managers.CustomAccessDecisionManager;
import com.hftamayo.java.todo.security.jwt.AuthenticationFilter;
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
                .csrf(csrf -> {
                    csrf.disable();
                    logger.info("CSRF is disabled");
                })

               .authorizeRequests(authorizeRequests -> configureAuthorization(authorizeRequests))
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

    private void configureAuthorization(HttpSecurity.AuthorizeRequestsConfigurer authorizeRequests) {
        try {
            authorizeRequests
                    .antMatchers("/api/auth/**", "/api/auth/register/**", "/api/auth/login/**", "/error").permitAll()
                    .antMatchers("/api/health/**").permitAll()
                    .antMatchers("/api/users/manager/**").hasAnyRole("SUPERVISOR", "ADMIN")
                    .antMatchers("/api/supervisor/**").hasAnyRole("SUPERVISOR", "ADMIN")
                    .antMatchers("/api/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .logout()
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessUrl("/api/auth/logged-out")
                    .permitAll();
            logger.info("Authorization requests configured");
        } catch (Exception e) {
            logger.error("Error in authorization: " + e.getMessage(), e);
        }
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