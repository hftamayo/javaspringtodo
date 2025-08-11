package com.hftamayo.java.todo.config;

import com.hftamayo.java.todo.utilities.version.VersionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor to automatically add version headers to API responses.
 * Ensures all API responses include version information for client awareness.
 */
@Component
@RequiredArgsConstructor
public class VersionInterceptor implements HandlerInterceptor {
    
    private final VersionProperties versionProperties;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (versionProperties.getApi().getHeaders().isEnabled()) {
            // Add API version header
            response.setHeader(
                versionProperties.getApi().getHeaders().getVersionHeader(),
                VersionConstants.getCurrentApiVersion()
            );
            
            // Add build version header
            response.setHeader(
                versionProperties.getApi().getHeaders().getBuildHeader(),
                VersionConstants.getFullVersion()
            );
        }
        
        return true;
    }
}


