package com.hftamayo.java.todo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for CORS settings.
 * Maps to the 'frontend' section in application.yml
 */
@Data
@Component
@ConfigurationProperties(prefix = "frontend")
public class CorsProperties {
    
    /**
     * List of allowed frontend origins for CORS
     * Defaults to localhost:5173 if not specified
     */
    private List<String> origins = List.of("http://localhost:5173");
    
    /**
     * Get origins as a comma-separated string for logging
     */
    public String getOriginsAsString() {
        return String.join(", ", origins);
    }
}
