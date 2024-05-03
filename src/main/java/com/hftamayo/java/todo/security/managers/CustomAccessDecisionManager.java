package com.hftamayo.java.todo.security.managers;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();

        if (url.startsWith("/api/auth/login") || url.startsWith("/api/auth/register")) {
            // Allow the request to proceed if it's for the register or login workflows
            return;
        }

        Set<String> requiredRoles = configAttributes.stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toSet());

        Set<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Check if userRoles contains any of the requiredRoles
        if (Collections.disjoint(userRoles, requiredRoles)) {
            throw new AccessDeniedException("Access Denied: Required roles: " + requiredRoles + ", but given roles: " + userRoles);
        }
    }


    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}