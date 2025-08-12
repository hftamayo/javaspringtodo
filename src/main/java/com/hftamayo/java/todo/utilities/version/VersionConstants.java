package com.hftamayo.java.todo.utilities.version;

/**
 * Centralized version constants for the application.
 * This class provides a single source of truth for all version-related information.
 */
public final class VersionConstants {
    
    // Private constructor to prevent instantiation
    private VersionConstants() {}
    
    // Application Version Information
    public static final String APP_VERSION = "0.1.5";
    public static final String APP_BUILD = "main";
    public static final String APP_NAME = "Todo Application";
    public static final String APP_DESCRIPTION = "Spring Boot Todo Application with JWT Authentication";
    
    // API Version Information
    public static final String API_VERSION_V1 = "v1";
    public static final String API_VERSION_V2 = "v2";
    public static final String CURRENT_API_VERSION = API_VERSION_V1;
    
    // API Base Paths
    public static final String API_BASE_PATH_V1 = "/api/" + API_VERSION_V1;
    public static final String API_BASE_PATH_V2 = "/api/" + API_VERSION_V2;
    public static final String CURRENT_API_BASE_PATH = API_BASE_PATH_V1;
    
    // Legacy Support (for backward compatibility)
    public static final String LEGACY_API_BASE_PATH = "/api";
    
    // Version Headers
    public static final String VERSION_HEADER = "X-API-Version";
    public static final String BUILD_HEADER = "X-Build-Version";
    
    // Version Response Fields
    public static final String VERSION_FIELD = "version";
    public static final String BUILD_FIELD = "build";
    public static final String API_VERSION_FIELD = "apiVersion";
    public static final String RELEASE_DATE_FIELD = "releaseDate";
    public static final String COMPATIBILITY_FIELD = "compatibility";
    
    // Build Information
    public static final String BUILD_TIMESTAMP = System.getProperty("build.timestamp", "unknown");
    public static final String BUILD_JAVA_VERSION = System.getProperty("java.version", "unknown");
    public static final String BUILD_OS = System.getProperty("os.name", "unknown");
    
    /**
     * Gets the full application version string.
     * @return Full version string (e.g., "0.1.5-main")
     */
    public static String getFullVersion() {
        return APP_VERSION + "-" + APP_BUILD;
    }
    
    /**
     * Gets the current API version.
     * @return Current API version string
     */
    public static String getCurrentApiVersion() {
        return CURRENT_API_VERSION;
    }
    
    /**
     * Gets the current API base path.
     * @return Current API base path
     */
    public static String getCurrentApiBasePath() {
        return CURRENT_API_BASE_PATH;
    }
    
    /**
     * Checks if a given API version is supported.
     * @param version API version to check
     * @return true if supported, false otherwise
     */
    public static boolean isApiVersionSupported(String version) {
        return API_VERSION_V1.equals(version) || API_VERSION_V2.equals(version);
    }
    
    /**
     * Gets the API base path for a specific version.
     * @param version API version
     * @return API base path for the specified version
     */
    public static String getApiBasePathForVersion(String version) {
        switch (version) {
            case API_VERSION_V1:
                return API_BASE_PATH_V1;
            case API_VERSION_V2:
                return API_BASE_PATH_V2;
            default:
                return CURRENT_API_BASE_PATH;
        }
    }
}




