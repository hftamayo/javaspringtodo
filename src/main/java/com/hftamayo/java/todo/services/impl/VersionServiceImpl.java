package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.version.VersionInfoDto;
import com.hftamayo.java.todo.services.VersionService;
import com.hftamayo.java.todo.utilities.version.VersionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of VersionService.
 * Provides comprehensive version information for the application.
 */
@Service
@RequiredArgsConstructor
public class VersionServiceImpl implements VersionService {
    
    private final Environment environment;
    private final BuildProperties buildProperties;
    
    @Override
    public VersionInfoDto getVersionInfo() {
        return buildVersionInfo(VersionConstants.getCurrentApiVersion());
    }
    
    @Override
    public VersionInfoDto getVersionInfoForApiVersion(String apiVersion) {
        if (!isApiVersionSupported(apiVersion)) {
            throw new IllegalArgumentException("Unsupported API version: " + apiVersion);
        }
        return buildVersionInfo(apiVersion);
    }
    
    @Override
    public boolean isApiVersionSupported(String apiVersion) {
        return VersionConstants.isApiVersionSupported(apiVersion);
    }
    
    @Override
    public String getCurrentApiVersion() {
        return VersionConstants.getCurrentApiVersion();
    }
    
    @Override
    public List<String> getSupportedApiVersions() {
        return Arrays.asList(
            VersionConstants.API_VERSION_V1,
            VersionConstants.API_VERSION_V2
        );
    }
    
    /**
     * Builds comprehensive version information.
     * @param apiVersion The API version to build information for
     * @return Complete VersionInfoDto
     */
    private VersionInfoDto buildVersionInfo(String apiVersion) {
        return VersionInfoDto.builder()
            .application(buildApplicationInfo())
            .api(buildApiInfo(apiVersion))
            .build(buildBuildInfo())
            .compatibility(buildCompatibilityInfo())
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    /**
     * Builds application information section.
     * @return ApplicationInfo object
     */
    private VersionInfoDto.ApplicationInfo buildApplicationInfo() {
        return VersionInfoDto.ApplicationInfo.builder()
            .name(VersionConstants.APP_NAME)
            .version(VersionConstants.APP_VERSION)
            .build(VersionConstants.APP_BUILD)
            .description(VersionConstants.APP_DESCRIPTION)
            .fullVersion(VersionConstants.getFullVersion())
            .build();
    }
    
    /**
     * Builds API information section.
     * @param apiVersion The API version to build info for
     * @return ApiInfo object
     */
    private VersionInfoDto.ApiInfo buildApiInfo(String apiVersion) {
        return VersionInfoDto.ApiInfo.builder()
            .currentVersion(apiVersion)
            .supportedVersions(getSupportedApiVersions())
            .basePath(VersionConstants.getApiBasePathForVersion(apiVersion))
            .legacySupport(apiVersion.equals(VersionConstants.API_VERSION_V1))
            .build();
    }
    
    /**
     * Builds build information section.
     * @return BuildInfo object
     */
    private VersionInfoDto.BuildInfo buildBuildInfo() {
        return VersionInfoDto.BuildInfo.builder()
            .timestamp(VersionConstants.BUILD_TIMESTAMP)
            .javaVersion(VersionConstants.BUILD_JAVA_VERSION)
            .operatingSystem(VersionConstants.BUILD_OS)
            .springBootVersion(getSpringBootVersion())
            .build();
    }
    
    /**
     * Builds compatibility information section.
     * @return CompatibilityInfo object
     */
    private VersionInfoDto.CompatibilityInfo buildCompatibilityInfo() {
        return VersionInfoDto.CompatibilityInfo.builder()
            .minimumJavaVersion("17")
            .recommendedJavaVersion("17")
            .deprecatedFeatures(List.of("No deprecated features in current version"))
            .breakingChanges(List.of("No breaking changes in current version"))
            .build();
    }
    
    /**
     * Gets Spring Boot version from environment or build properties.
     * @return Spring Boot version string
     */
    private String getSpringBootVersion() {
        try {
            if (buildProperties != null) {
                return buildProperties.getVersion();
            }
            return environment.getProperty("spring-boot.version", "3.0.6");
        } catch (Exception e) {
            return "3.0.6"; // Fallback to known version
        }
    }
}


