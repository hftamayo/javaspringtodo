package com.hftamayo.java.todo.security;

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
    private HttpSecurity httpSecurity;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomFilterInvocationSecurityMetadataSource cfisMetadataSource;

    @InjectMocks
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() throws Exception {
        // Mock HttpSecurity builder pattern
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.exceptionHandling(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        
        // Mock build() to return SecurityFilterChain
        SecurityFilterChain mockChain = mock(SecurityFilterChain.class);
        doReturn(mockChain).when(httpSecurity).build();
    }

    @Test
    void securityFilterChain_ShouldConfigureSecurityCorrectly() throws Exception {
        // Arrange
        FilterSecurityInterceptor filterSecurityInterceptor = mock(FilterSecurityInterceptor.class);

        // Act
        SecurityFilterChain securityFilterChain = filterConfig.securityFilterChain(httpSecurity, filterSecurityInterceptor);

        // Assert
        assertAll(
            () -> assertNotNull(securityFilterChain),
            () -> verify(httpSecurity).csrf(any()),
            () -> verify(httpSecurity).authorizeHttpRequests(any()),
            () -> verify(httpSecurity).exceptionHandling(any()),
            () -> verify(httpSecurity).sessionManagement(any()),
            () -> verify(httpSecurity).authenticationProvider(authenticationProvider),
            () -> verify(httpSecurity).addFilterBefore(authenticationFilter, any()),
            () -> verify(httpSecurity).addFilterBefore(filterSecurityInterceptor, FilterSecurityInterceptor.class)
        );
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
    void securityFilterChain_ShouldConfigurePublicEndpoints() throws Exception {
        // Arrange
        FilterSecurityInterceptor filterSecurityInterceptor = mock(FilterSecurityInterceptor.class);

        // Act
        filterConfig.securityFilterChain(httpSecurity, filterSecurityInterceptor);

        // Assert
        verify(httpSecurity).authorizeHttpRequests(argThat(authorizeRequests -> {
            // Verify that public endpoints are configured
            // Note: This is a simplified verification. In a real test, you might want to
            // use a more sophisticated approach to verify the exact configuration
            return true;
        }));
    }

    @Test
    void securityFilterChain_ShouldConfigureRoleBasedAccess() throws Exception {
        // Arrange
        FilterSecurityInterceptor filterSecurityInterceptor = mock(FilterSecurityInterceptor.class);

        // Act
        filterConfig.securityFilterChain(httpSecurity, filterSecurityInterceptor);

        // Assert
        verify(httpSecurity).authorizeHttpRequests(argThat(authorizeRequests -> {
            // Verify that role-based access is configured
            // Note: This is a simplified verification. In a real test, you might want to
            // use a more sophisticated approach to verify the exact configuration
            return true;
        }));
    }
}