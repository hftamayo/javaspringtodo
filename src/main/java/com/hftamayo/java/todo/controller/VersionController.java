package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.version.VersionInfoDto;
import com.hftamayo.java.todo.services.VersionService;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;
import com.hftamayo.java.todo.utilities.ratelimit.RateLimit;
import com.hftamayo.java.todo.utilities.version.VersionConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for version information endpoints.
 * Provides comprehensive version details and API versioning information.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class VersionController {
    
    private final VersionService versionService;
    
    /**
     * Get version information for the current API version.
     * @return Current version information
     */
    @RateLimit(tokens = 1)
    @GetMapping("/version")
    public ResponseEntity<EndpointResponseDto<?>> getVersionInfo() {
        try {
            VersionInfoDto versionInfo = versionService.getVersionInfo();
            EndpointResponseDto<VersionInfoDto> response = ResponseUtil
                .successResponse(versionInfo, "VERSION_INFO_RETRIEVED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to retrieve version info", e));
        }
    }
    
    /**
     * Get version information for a specific API version.
     * @param version The API version to get information for
     * @return Version information for the specified API version
     */
    @RateLimit(tokens = 1)
    @GetMapping("/version/{version}")
    public ResponseEntity<EndpointResponseDto<?>> getVersionInfoForVersion(
            @PathVariable String version) {
        try {
            if (!versionService.isApiVersionSupported(version)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, 
                        "Unsupported API version: " + version, 
                        new IllegalArgumentException("Unsupported API version")));
            }
            
            VersionInfoDto versionInfo = versionService.getVersionInfoForApiVersion(version);
            EndpointResponseDto<VersionInfoDto> response = ResponseUtil
                .successResponse(versionInfo, "VERSION_INFO_RETRIEVED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to retrieve version info", e));
        }
    }
    
    /**
     * Get list of supported API versions.
     * @return List of supported API versions
     */
    @RateLimit(tokens = 1)
    @GetMapping("/versions")
    public ResponseEntity<EndpointResponseDto<?>> getSupportedVersions() {
        try {
            List<String> supportedVersions = versionService.getSupportedApiVersions();
            EndpointResponseDto<List<String>> response = ResponseUtil
                .successResponse(supportedVersions, "SUPPORTED_VERSIONS_RETRIEVED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to retrieve supported versions", e));
        }
    }
    
    /**
     * Get current API version.
     * @return Current API version
     */
    @RateLimit(tokens = 1)
    @GetMapping("/version/current")
    public ResponseEntity<EndpointResponseDto<?>> getCurrentVersion() {
        try {
            String currentVersion = versionService.getCurrentApiVersion();
            EndpointResponseDto<String> response = ResponseUtil
                .successResponse(currentVersion, "CURRENT_VERSION_RETRIEVED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to retrieve current version", e));
        }
    }
    
    /**
     * Check if a specific API version is supported.
     * @param version The API version to check
     * @return Boolean indicating if the version is supported
     */
    @RateLimit(tokens = 1)
    @GetMapping("/version/{version}/supported")
    public ResponseEntity<EndpointResponseDto<?>> isVersionSupported(
            @PathVariable String version) {
        try {
            boolean isSupported = versionService.isApiVersionSupported(version);
            EndpointResponseDto<Boolean> response = ResponseUtil
                .successResponse(isSupported, "VERSION_SUPPORT_CHECKED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to check version support", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    /**
     * Legacy endpoint for backward compatibility.
     * @return Current version information
     */
    @RateLimit(tokens = 1)
    @GetMapping("/info")
    public ResponseEntity<EndpointResponseDto<?>> getLegacyInfo() {
        try {
            VersionInfoDto versionInfo = versionService.getVersionInfo();
            EndpointResponseDto<VersionInfoDto> response = ResponseUtil
                .successResponse(versionInfo, "LEGACY_INFO_RETRIEVED");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Failed to retrieve legacy info", e),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}



