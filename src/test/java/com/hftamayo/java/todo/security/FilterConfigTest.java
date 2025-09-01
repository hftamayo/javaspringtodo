package com.hftamayo.java.todo.security;

import com.hftamayo.java.todo.config.CorsProperties;
import com.hftamayo.java.todo.security.handlers.CustomAccessDeniedHandler;
import com.hftamayo.java.todo.security.jwt.AuthenticationFilter;
import com.hftamayo.java.todo.security.managers.CustomAccessDecisionManager;
import com.hftamayo.java.todo.security.managers.CustomFilterInvocationSecurityMetadataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterConfigTest {

    @Mock
    private AuthenticationFilter authenticationFilter;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Mock
    private CustomAccessDecisionManager customAccessDecisionManager;

    @Mock
    private CorsProperties corsProperties;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomFilterInvocationSecurityMetadataSource cfisMetadataSource;

    @InjectMocks
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() throws Exception {
        // Setup CorsProperties mock
        when(corsProperties.getOrigins()).thenReturn(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        when(corsProperties.getOriginsAsString()).thenReturn("http://localhost:5173, http://localhost:3000");
    }

    @Test
    void filterSecurityInterceptor_ShouldConfigureInterceptorCorrectly() {
        // Act
        FilterSecurityInterceptor interceptor = filterConfig.filterSecurityInterceptor(
            authenticationManager, cfisMetadataSource);

        // Assert
        assertAll(
            () -> assertNotNull(interceptor),
            () -> assertEquals(authenticationManager, interceptor.getAuthenticationManager()),
            () -> assertEquals(customAccessDecisionManager, interceptor.getAccessDecisionManager()),
            () -> assertEquals(cfisMetadataSource, interceptor.getSecurityMetadataSource())
        );
    }

    @Test
    void corsConfigurationSource_ShouldConfigureCorsCorrectly() {
        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();

        // Assert
        assertNotNull(corsConfig);
        
        // Verify that CorsProperties methods were called
        verify(corsProperties).getOrigins();
        verify(corsProperties).getOriginsAsString();
    }

    @Test
    void corsConfigurationSource_ShouldHaveCorrectConfiguration() {
        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();
        CorsConfiguration configuration = corsConfig.getCorsConfiguration(null);

        // Assert
        assertNotNull(configuration);
        assertTrue(configuration.getAllowedOriginPatterns().contains("http://localhost:5173"));
        assertTrue(configuration.getAllowedOriginPatterns().contains("http://localhost:3000"));
        assertTrue(configuration.getAllowedMethods().containsAll(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")));
        assertTrue(configuration.getAllowedHeaders().contains("*"));
        assertTrue(configuration.getAllowCredentials());
        assertEquals(3600L, configuration.getMaxAge());
        assertTrue(configuration.getExposedHeaders().containsAll(Arrays.asList(
            "Authorization",
            "X-RateLimit-Limit",
            "X-RateLimit-Remaining",
            "X-RateLimit-Reset",
            "X-API-Version",
            "X-Build-Version"
        )));
    }

    @Test
    void corsConfigurationSource_ShouldHandleEmptyOrigins() {
        // Arrange
        when(corsProperties.getOrigins()).thenReturn(Arrays.asList());
        when(corsProperties.getOriginsAsString()).thenReturn("");

        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();
        CorsConfiguration configuration = corsConfig.getCorsConfiguration(null);

        // Assert
        assertNotNull(configuration);
        assertTrue(configuration.getAllowedOriginPatterns().isEmpty());
    }

    @Test
    void corsConfigurationSource_ShouldHandleNullOrigins() {
        // Arrange
        when(corsProperties.getOrigins()).thenReturn(null);
        when(corsProperties.getOriginsAsString()).thenReturn("null");

        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();
        CorsConfiguration configuration = corsConfig.getCorsConfiguration(null);

        // Assert
        assertNotNull(configuration);
        assertNull(configuration.getAllowedOriginPatterns());
    }
}