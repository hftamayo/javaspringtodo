package com.hftamayo.java.todo.security.managers;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation filterInvocation = (FilterInvocation) object;
        String url = filterInvocation.getRequestUrl();

        // Here you would typically load the roles from a database or other source
        // based on the URL. For simplicity, we'll just hard-code some roles.
        if (url.startsWith("/api/auth/login") || url.startsWith("/api/auth/register")) {
            return List.of(new SecurityConfig("ROLE_ANONYMOUS"));
        } else {
            return List.of(new SecurityConfig("ROLE_USER"),
                    new SecurityConfig("ROLE_SUPERVISOR"), new SecurityConfig("ROLE_ADMIN"));
        }
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}