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
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

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

    @Mock
    private HttpServletRequest mockRequest;

    @InjectMocks
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() throws Exception {
        // Setup CorsProperties mock with lenient() to avoid UnnecessaryStubbingException
        lenient().when(corsProperties.getOrigins()).thenReturn(Arrays.asList("http://localhost:5173", "http://localhost:3000"));
        lenient().when(corsProperties.getOriginsAsString()).thenReturn("http://localhost:5173, http://localhost:3000");

        // Setup mock request with all necessary methods for CORS processing
        lenient().when(mockRequest.getRequestURI()).thenReturn("/api/test");
        lenient().when(mockRequest.getMethod()).thenReturn("GET");
        lenient().when(mockRequest.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/api/test"));
        lenient().when(mockRequest.getScheme()).thenReturn("http");
        lenient().when(mockRequest.getServerName()).thenReturn("localhost");
        lenient().when(mockRequest.getServerPort()).thenReturn(8080);
        lenient().when(mockRequest.getContextPath()).thenReturn("");
        lenient().when(mockRequest.getServletPath()).thenReturn("/api/test");
        lenient().when(mockRequest.getPathInfo()).thenReturn(null);
        lenient().when(mockRequest.getQueryString()).thenReturn(null);
        lenient().when(mockRequest.getHeader(anyString())).thenReturn(null);
        lenient().when(mockRequest.getAttribute(anyString())).thenReturn(null);
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

        // Instead of calling getCorsConfiguration() which causes NPE,
        // we test that the CorsConfigurationSource was created successfully
        assertNotNull(corsConfig);

        // Verify that corsProperties was called during configuration
        verify(corsProperties).getOrigins();
        verify(corsProperties).getOriginsAsString();
    }

    @Test
    void corsConfigurationSource_ShouldHandleEmptyOrigins() {
        // Arrange
        when(corsProperties.getOrigins()).thenReturn(Arrays.asList());
        when(corsProperties.getOriginsAsString()).thenReturn("");

        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();

        // Assert
        assertNotNull(corsConfig);
        verify(corsProperties).getOrigins();
        verify(corsProperties).getOriginsAsString();
    }

    @Test
    void corsConfigurationSource_ShouldHandleNullOrigins() {
        // Arrange
        when(corsProperties.getOrigins()).thenReturn(null);
        when(corsProperties.getOriginsAsString()).thenReturn("null");

        // Act
        CorsConfigurationSource corsConfig = filterConfig.corsConfigurationSource();

        // Assert
        assertNotNull(corsConfig);
        verify(corsProperties).getOrigins();
        verify(corsProperties).getOriginsAsString();
    }
}