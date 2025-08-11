package com.hftamayo.java.todo.dto.version;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for application version information.
 * Provides comprehensive version details for API consumers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VersionInfoDto {
    
    @JsonProperty("application")
    private ApplicationInfo application;
    
    @JsonProperty("api")
    private ApiInfo api;
    
    @JsonProperty("build")
    private BuildInfo build;
    
    @JsonProperty("compatibility")
    private CompatibilityInfo compatibility;
    
    @JsonProperty("timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    
    /**
     * Application information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationInfo {
        @JsonProperty("name")
        private String name;
        
        @JsonProperty("version")
        private String version;
        
        @JsonProperty("build")
        private String build;
        
        @JsonProperty("description")
        private String description;
        
        @JsonProperty("fullVersion")
        private String fullVersion;
    }
    
    /**
     * API version information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiInfo {
        @JsonProperty("currentVersion")
        private String currentVersion;
        
        @JsonProperty("supportedVersions")
        private List<String> supportedVersions;
        
        @JsonProperty("basePath")
        private String basePath;
        
        @JsonProperty("legacySupport")
        private boolean legacySupport;
    }
    
    /**
     * Build information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuildInfo {
        @JsonProperty("timestamp")
        private String timestamp;
        
        @JsonProperty("javaVersion")
        private String javaVersion;
        
        @JsonProperty("operatingSystem")
        private String operatingSystem;
        
        @JsonProperty("springBootVersion")
        private String springBootVersion;
    }
    
    /**
     * Compatibility information.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompatibilityInfo {
        @JsonProperty("minimumJavaVersion")
        private String minimumJavaVersion;
        
        @JsonProperty("recommendedJavaVersion")
        private String recommendedJavaVersion;
        
        @JsonProperty("deprecatedFeatures")
        private List<String> deprecatedFeatures;
        
        @JsonProperty("breakingChanges")
        private List<String> breakingChanges;
    }
}


