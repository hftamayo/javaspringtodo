# 🚀 API Versioning Guide

## 📋 Overview

This document describes the comprehensive versioning system implemented in the Java Spring Boot Todo Application. The system provides API versioning, build information, and backward compatibility features.

## 🎯 Features

### **1. API Versioning**
- **URL Path-based**: `/api/v1/`, `/api/v2/`
- **Legacy Support**: `/api/` (backward compatibility)
- **Version Headers**: Automatic version headers in responses

### **2. Version Information Endpoints**
- **Current Version**: `/api/version`
- **Specific Version**: `/api/version/{version}`
- **Supported Versions**: `/api/versions`
- **Version Check**: `/api/version/{version}/supported`
- **Legacy Info**: `/api/info`

### **3. Build Information**
- **Build Timestamp**: When the application was built
- **Java Version**: Runtime Java version
- **Operating System**: Build environment OS
- **Spring Boot Version**: Framework version

## 🔧 Configuration

### **Application Properties**
```yaml
version:
  api:
    current: v1
    supported:
      - v1
      - v2
    legacy-support: true
    headers:
      enabled: true
      version-header: X-API-Version
      build-header: X-Build-Version
```

### **Environment Variables**
```bash
# Build timestamp (auto-generated)
BUILD_TIMESTAMP=2024-01-15 10:30:00

# API version
API_VERSION=v1
```

## 📡 API Endpoints

### **Version Information**

#### **Get Current Version Info**
```http
GET /api/version
```

**Response:**
```json
{
  "code": 200,
  "resultMessage": "VERSION_INFO_RETRIEVED",
  "data": {
    "application": {
      "name": "Todo Application",
      "version": "0.1.5",
      "build": "main",
      "description": "Spring Boot Todo Application with JWT Authentication",
      "fullVersion": "0.1.5-main"
    },
    "api": {
      "currentVersion": "v1",
      "supportedVersions": ["v1", "v2"],
      "basePath": "/api/v1",
      "legacySupport": true
    },
    "build": {
      "timestamp": "2024-01-15 10:30:00",
      "javaVersion": "17",
      "operatingSystem": "Linux",
      "springBootVersion": "3.0.6"
    },
    "compatibility": {
      "minimumJavaVersion": "17",
      "recommendedJavaVersion": "17",
      "deprecatedFeatures": ["No deprecated features in current version"],
      "breakingChanges": ["No breaking changes in current version"]
    },
    "timestamp": "2024-01-15T10:30:00Z"
  }
}
```

#### **Get Version Info for Specific API Version**
```http
GET /api/version/v2
```

#### **Get Supported Versions**
```http
GET /api/versions
```

#### **Check Version Support**
```http
GET /api/version/v2/supported
```

#### **Legacy Endpoint**
```http
GET /api/info
```

## 🔒 Rate Limiting

All version endpoints are protected with rate limiting:
- **Token Cost**: 1 token per request
- **Purpose**: Prevent abuse of version information endpoints

## 📊 Response Headers

### **Automatic Headers**
Every API response includes:
```http
X-API-Version: v1
X-Build-Version: 0.1.5-main
```

### **Custom Headers**
You can also set custom headers in your controllers:
```java
response.setHeader("X-Custom-Version", "1.0.0");
```

## 🏗️ Architecture

### **Package Structure**
```
src/main/java/com/hftamayo/java/todo/
├── config/
│   ├── VersionProperties.java      # Configuration properties
│   ├── VersionInterceptor.java     # Response header interceptor
│   └── WebConfig.java             # Web configuration
├── controller/
│   └── VersionController.java      # Version endpoints
├── dto/
│   └── version/
│       └── VersionInfoDto.java     # Version data transfer objects
├── services/
│   ├── VersionService.java         # Version service interface
│   └── impl/
│       └── VersionServiceImpl.java # Version service implementation
└── utilities/
    └── version/
        └── VersionConstants.java    # Version constants
```

### **Components**

#### **1. VersionConstants**
- Centralized version information
- API version constants
- Build information constants
- Utility methods for version management

#### **2. VersionInfoDto**
- Comprehensive version data structure
- Nested classes for different information types
- JSON serialization support

#### **3. VersionService**
- Business logic for version management
- API version validation
- Build information retrieval

#### **4. VersionController**
- REST endpoints for version information
- Error handling and validation
- Rate limiting integration

#### **5. VersionInterceptor**
- Automatic header injection
- Configurable header management
- Response enhancement

## 🔄 Version Migration

### **Adding New API Version**

#### **1. Update VersionConstants**
```java
public static final String API_VERSION_V3 = "v3";
public static final String API_BASE_PATH_V3 = "/api/" + API_VERSION_V3;
```

#### **2. Update VersionService**
```java
@Override
public List<String> getSupportedApiVersions() {
    return Arrays.asList(
        VersionConstants.API_VERSION_V1,
        VersionConstants.API_VERSION_V2,
        VersionConstants.API_VERSION_V3  // Add new version
    );
}
```

#### **3. Create Version-specific Controllers**
```java
@RestController
@RequestMapping("/api/v3")
public class V3Controller {
    // New version implementation
}
```

#### **4. Update Configuration**
```yaml
version:
  api:
    supported:
      - v1
      - v2
      - v3  # Add new version
```

### **Deprecating Old Versions**

#### **1. Mark as Deprecated**
```java
@Deprecated(since = "0.2.0", forRemoval = true)
public class V1Controller {
    // Old version implementation
}
```

#### **2. Update Compatibility Info**
```java
private VersionInfoDto.CompatibilityInfo buildCompatibilityInfo() {
    return VersionInfoDto.CompatibilityInfo.builder()
        .deprecatedFeatures(List.of(
            "API v1 endpoints will be removed in version 0.3.0"
        ))
        .breakingChanges(List.of(
            "API v1 will be removed in version 0.3.0"
        ))
        .build();
}
```

## 🧪 Testing

### **Unit Tests**
```java
@ExtendWith(MockitoExtension.class)
class VersionServiceTest {
    
    @Test
    void shouldReturnCurrentVersionInfo() {
        // Test implementation
    }
    
    @Test
    void shouldValidateApiVersion() {
        // Test validation
    }
}
```

### **Integration Tests**
```java
@SpringBootTest
@AutoConfigureTestDatabase
class VersionControllerIntegrationTest {
    
    @Test
    void shouldReturnVersionInfo() {
        // Test endpoint
    }
}
```

## 🚨 Best Practices

### **1. Version Naming**
- Use semantic versioning: `v1`, `v2`, `v3`
- Avoid descriptive names: `v1-stable`, `v2-beta`

### **2. Backward Compatibility**
- Always maintain backward compatibility when possible
- Use deprecation warnings for planned removals
- Provide migration guides for breaking changes

### **3. Version Headers**
- Include version information in all responses
- Use consistent header naming conventions
- Document header meanings and values

### **4. Error Handling**
- Provide clear error messages for unsupported versions
- Include version information in error responses
- Suggest supported versions when possible

## 📚 References

- [Spring Boot Versioning](https://spring.io/guides/gs/rest-service/)
- [API Versioning Best Practices](https://restfulapi.net/versioning/)
- [Semantic Versioning](https://semver.org/)

## 🤝 Contributing

When contributing to the versioning system:

1. **Follow the existing patterns**
2. **Update all related components**
3. **Add comprehensive tests**
4. **Update this documentation**
5. **Consider backward compatibility**

---

**Last Updated**: January 2024  
**Version**: 1.0.0  
**Author**: Development Team




