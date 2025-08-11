package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.version.VersionInfoDto;

/**
 * Service interface for version management.
 * Provides methods to retrieve application version information.
 */
public interface VersionService {
    
    /**
     * Gets comprehensive version information for the application.
     * @return VersionInfoDto containing all version details
     */
    VersionInfoDto getVersionInfo();
    
    /**
     * Gets version information for a specific API version.
     * @param apiVersion The API version to get information for
     * @return VersionInfoDto for the specified API version
     */
    VersionInfoDto getVersionInfoForApiVersion(String apiVersion);
    
    /**
     * Checks if a specific API version is supported.
     * @param apiVersion The API version to check
     * @return true if supported, false otherwise
     */
    boolean isApiVersionSupported(String apiVersion);
    
    /**
     * Gets the current API version.
     * @return Current API version string
     */
    String getCurrentApiVersion();
    
    /**
     * Gets the list of supported API versions.
     * @return List of supported API version strings
     */
    java.util.List<String> getSupportedApiVersions();
}


