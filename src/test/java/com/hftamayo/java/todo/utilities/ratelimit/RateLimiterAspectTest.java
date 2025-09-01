package com.hftamayo.java.todo.utilities.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hftamayo.java.todo.dto.EndpointResponseDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimiterAspectTest {

    private RateLimiterAspect rateLimiterAspect;
    
    @Mock
    private RateLimiterUtil mockRateLimiterUtil;
    
    @Mock
    private RateLimiterConfig mockRateLimiterConfig;
    
    @Mock
    private ObjectMapper mockObjectMapper;

    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        rateLimiterAspect = new RateLimiterAspect(mockRateLimiterUtil, mockRateLimiterConfig, mockObjectMapper);
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        
        // Set up request context
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest, mockResponse));
    }

    @Test
    void shouldAllowRequestWhenTokensAvailable() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        Object result = rateLimiterAspect.rateLimit(method, args, target);

        // Then
        assertNull(result); // Should proceed to actual method
        verify(mockRateLimiterUtil).tryConsume(any(), eq(1L));
        verify(mockResponse).setHeader("X-RateLimit-Remaining", anyString());
        verify(mockResponse).setHeader("X-RateLimit-Reset", anyString());
    }

    @Test
    void shouldBlockRequestWhenNoTokensAvailable() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(false);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(0L);
        when(mockObjectMapper.writeValueAsString(any()))
                .thenReturn("{\"error\":\"Rate limit exceeded\"}");

        // When
        Object result = rateLimiterAspect.rateLimit(method, args, target);

        // Then
        assertNotNull(result);
        verify(mockResponse).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        verify(mockResponse).setContentType("application/json");
        verify(mockResponse).getWriter();
    }

    @Test
    void shouldUseEndpointSpecificConfiguration() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        mockRequest.setRequestURI("/api/users");
        
        RateLimiterConfig endpointConfig = createTestConfig();
        endpointConfig.setCapacity(50L);
        
        when(mockRateLimiterConfig.getCombinedConfig("/api/users", anyString()))
                .thenReturn(endpointConfig);
        when(mockRateLimiterUtil.createBucket(endpointConfig))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig("/api/users", anyString());
        verify(mockRateLimiterUtil).createBucket(endpointConfig);
    }

    @Test
    void shouldUseUserRoleConfiguration() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        mockRequest.setRequestURI("/api/admin");
        mockRequest.addHeader("Authorization", "Bearer admin-token");
        
        RateLimiterConfig userConfig = createTestConfig();
        userConfig.setCapacity(200L);
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("ADMIN")))
                .thenReturn(userConfig);
        when(mockRateLimiterUtil.createBucket(userConfig))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("ADMIN"));
        verify(mockRateLimiterUtil).createBucket(userConfig);
    }

    @Test
    void shouldSetCorrectRateLimitHeaders() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockResponse).setHeader("X-RateLimit-Limit", "10");
        verify(mockResponse).setHeader("X-RateLimit-Remaining", "9");
        verify(mockResponse).setHeader("X-RateLimit-Reset", anyString());
    }

    @Test
    void shouldHandleMethodWithoutAnnotation() throws Throwable {
        // Given
        Method method = getTestMethodWithoutAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();

        // When
        Object result = rateLimiterAspect.rateLimit(method, args, target);

        // Then
        assertNull(result); // Should proceed to actual method
        verifyNoInteractions(mockRateLimiterUtil, mockRateLimiterConfig);
    }

    @Test
    void shouldHandleNullRequestContext() throws Throwable {
        // Given
        RequestContextHolder.resetRequestAttributes();
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();

        // When & Then
        assertThrows(RateLimiterException.class, () -> 
            rateLimiterAspect.rateLimit(method, args, target));
    }

    @Test
    void shouldHandleNullMethod() throws Throwable {
        // Given
        Object[] args = new Object[]{};
        Object target = new Object();

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            rateLimiterAspect.rateLimit(null, args, target));
    }

    @Test
    void shouldHandleNullTarget() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            rateLimiterAspect.rateLimit(method, args, null));
    }

    @Test
    void shouldHandleCustomTokenConsumption() throws Throwable {
        // Given
        Method method = getTestMethodWithCustomTokens();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(5L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterUtil).tryConsume(any(), eq(5L));
    }

    @Test
    void shouldHandleRateLimiterException() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenThrow(new RateLimiterException("Configuration error"));

        // When & Then
        assertThrows(RateLimiterException.class, () -> 
            rateLimiterAspect.rateLimit(method, args, target));
    }

    @Test
    void shouldHandleObjectMapperException() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(false);
        when(mockObjectMapper.writeValueAsString(any()))
                .thenThrow(new RuntimeException("Serialization error"));

        // When & Then
        assertThrows(RateLimiterException.class, () -> 
            rateLimiterAspect.rateLimit(method, args, target));
    }

    @Test
    void shouldExtractUserRoleFromAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        mockRequest.addHeader("Authorization", "Bearer user-token");
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("USER")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("USER"));
    }

    @Test
    void shouldUseDefaultUserRoleWhenNoAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("ANONYMOUS")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("ANONYMOUS"));
    }

    @Test
    void shouldHandleEmptyAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        Object[] args = new Object[]{};
        Object target = new Object();
        mockRequest.addHeader("Authorization", "");
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("ANONYMOUS")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);

        // When
        rateLimiterAspect.rateLimit(method, args, target);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("ANONYMOUS"));
    }

    // Helper methods
    private Method getTestMethodWithAnnotation() throws NoSuchMethodException {
        return TestController.class.getMethod("testMethod");
    }

    private Method getTestMethodWithoutAnnotation() throws NoSuchMethodException {
        return TestController.class.getMethod("testMethodWithoutAnnotation");
    }

    private Method getTestMethodWithCustomTokens() throws NoSuchMethodException {
        return TestController.class.getMethod("testMethodWithCustomTokens");
    }

    private RateLimiterConfig createTestConfig() {
        RateLimiterConfig config = new RateLimiterConfig();
        config.setCapacity(10L);
        config.setRefillRate(5L);
        config.setRefillDuration(Duration.ofMinutes(1));
        return config;
    }

    private io.github.bucket4j.Bucket createTestBucket() {
        return io.github.bucket4j.Bucket.builder()
                .addLimit(io.github.bucket4j.Bandwidth.classic(10L, 
                    io.github.bucket4j.Refill.intervally(5L, Duration.ofMinutes(1))))
                .build();
    }

    // Test controller class for method reflection
    private static class TestController {
        @RateLimit
        public void testMethod() {}

        public void testMethodWithoutAnnotation() {}

        @RateLimit(tokens = 5)
        public void testMethodWithCustomTokens() {}
    }
} 