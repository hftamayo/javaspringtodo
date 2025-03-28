package com.hftamayo.java.todo.security.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    private static final Logger logger = LoggerFactory.getLogger(CustomAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();

        if (isPublicEndpoint(url)) {
            // Allow the request to proceed if it's for the register or login workflows
            return;
        }

        //logger.info("entering to the requiredRoles loop");

        Set<String> requiredRoles = getRequiredRoles(configAttributes);
        Set<String> userRoles = getUserRoles(authentication);

        // Check if userRoles contains any of the requiredRoles
        if (Collections.disjoint(userRoles, requiredRoles)) {
            throw new AccessDeniedException("Access Denied: Required roles: " + requiredRoles + ", but given roles: " + userRoles);
        }
    }

    private boolean isPublicEndpoint(String url) {
        return url.startsWith("/api/auth/login") || url.startsWith("/api/auth/register") || url.startsWith("/api/health");
    }

    private Set<String> getRequiredRoles(Collection<ConfigAttribute> configAttributes) {
        return configAttributes.stream()
                .map(ConfigAttribute::getAttribute)
                .collect(Collectors.toSet());
    }

    private Set<String> getUserRoles(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
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