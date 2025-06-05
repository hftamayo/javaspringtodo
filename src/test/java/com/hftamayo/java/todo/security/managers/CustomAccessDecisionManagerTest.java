package com.hftamayo.java.todo.security.managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterInvocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDecisionManagerTest {

    @InjectMocks
    private CustomAccessDecisionManager accessDecisionManager;

    @Mock
    private Authentication authentication;

    @Mock
    private FilterInvocation filterInvocation;

    @Test
    void decide_PublicEndpoint_ShouldAllowAccess() {
        when(filterInvocation.getRequestUrl()).thenReturn("/api/auth/login");
        Collection<ConfigAttribute> attributes = Collections.emptyList();

        assertDoesNotThrow(() ->
                accessDecisionManager.decide(authentication, filterInvocation, attributes)
        );
    }

    @Test
    void decide_MatchingRoles_ShouldAllowAccess() {
        when(filterInvocation.getRequestUrl()).thenReturn("/api/secured/endpoint");
        Collection<ConfigAttribute> attributes = List.of(
                new SecurityConfig("ROLE_USER")
        );

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        assertDoesNotThrow(() ->
                accessDecisionManager.decide(authentication, filterInvocation, attributes)
        );
    }

    @Test
    void decide_NonMatchingRoles_ShouldDenyAccess() {
        when(filterInvocation.getRequestUrl()).thenReturn("/api/secured/endpoint");
        Collection<ConfigAttribute> attributes = List.of(
                new SecurityConfig("ROLE_ADMIN")
        );

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        assertThrows(AccessDeniedException.class, () ->
                accessDecisionManager.decide(authentication, filterInvocation, attributes)
        );
    }

    @Test
    void decide_MultipleRoles_ShouldAllowAccessWithOneMatch() {
        when(filterInvocation.getRequestUrl()).thenReturn("/api/secured/endpoint");
        Collection<ConfigAttribute> attributes = List.of(
                new SecurityConfig("ROLE_ADMIN"),
                new SecurityConfig("ROLE_USER")
        );

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        );
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

        assertDoesNotThrow(() ->
                accessDecisionManager.decide(authentication, filterInvocation, attributes)
        );
    }

    @Test
    void supports_ConfigAttribute_ShouldReturnTrue() {
        assertTrue(accessDecisionManager.supports(new SecurityConfig("ROLE_USER")));
    }

    @Test
    void supports_Class_ShouldReturnTrue() {
        assertTrue(accessDecisionManager.supports(Object.class));
    }
}