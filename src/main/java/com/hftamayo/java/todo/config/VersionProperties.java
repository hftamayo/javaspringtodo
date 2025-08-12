package com.hftamayo.java.todo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Configuration properties for version management.
 * Maps configuration from application.yml to Java objects.
 */
@Data
@Component
@ConfigurationProperties(prefix = "version")
public class VersionProperties {
    
    private Api api = new Api();
    
    @Data
    public static class Api {
        private String current = "v1";
        private List<String> supported = List.of("v1", "v2");
        private boolean legacySupport = true;
        private Headers headers = new Headers();
    }
    
    @Data
    public static class Headers {
        private boolean enabled = true;
        private String versionHeader = "X-API-Version";
        private String buildHeader = "X-Build-Version";
    }
}




