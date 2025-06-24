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
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomFilterInvocationSecurityMetadataSource cfisMetadataSource;

    @InjectMocks
    private FilterConfig filterConfig;

    @BeforeEach
    void setUp() throws Exception {
        // No HttpSecurity mocking needed for pure unit test
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
}