package com.hftamayo.java.todo.security.managers;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
        if (configAttributes == null || configAttributes.isEmpty()) {
            throw new AccessDeniedException("Access Denied: No required roles found");
        }

        String requiredRole = configAttributes.stream()
                .map(ConfigAttribute::getAttribute)
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("No required roles found"));

        String userRole = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("No roles found for user"));

        if (!requiredRole.equals(userRole)) {
            throw new AccessDeniedException("Access Denied: Required role: " + requiredRole + ", but given role: " + userRole);
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