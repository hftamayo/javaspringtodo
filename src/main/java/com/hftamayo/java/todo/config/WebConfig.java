package com.hftamayo.java.todo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration for interceptors and web-related settings.
 * Registers the version interceptor to add version headers to all responses.
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    
    private final VersionInterceptor versionInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add version interceptor to all API endpoints
        registry.addInterceptor(versionInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/health/**"); // Exclude health checks
    }
}


