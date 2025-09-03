package com.hftamayo.java.todo.utilities.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
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

    @Mock
    private ProceedingJoinPoint mockJoinPoint;

    @Mock
    private MethodSignature mockMethodSignature;

    private MockHttpServletRequest mockRequest;
    private MockHttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        rateLimiterAspect = new RateLimiterAspect(mockRateLimiterUtil, mockRateLimiterConfig, mockObjectMapper);
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        
        // Set up request context
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest, mockResponse));

        // Set up common mock behavior with lenient to avoid unnecessary stubbing warnings
        lenient().when(mockJoinPoint.getSignature()).thenReturn(mockMethodSignature);
    }

    @Test
    void shouldAllowRequestWhenTokensAvailable() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");

        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        Object result = rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        assertEquals("success", result);
        verify(mockJoinPoint).proceed();
        verify(mockRateLimiterUtil).tryConsume(any(), eq(1L));
        assertEquals("10", mockResponse.getHeader("X-RateLimit-Limit"));
        assertEquals("9", mockResponse.getHeader("X-RateLimit-Remaining"));
        assertNotNull(mockResponse.getHeader("X-RateLimit-Reset"));
    }

    @Test
    void shouldBlockRequestWhenNoTokensAvailable() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);

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
        Object result = rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        assertNull(result); // Returns null when rate limited
        verify(mockJoinPoint, never()).proceed();
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), mockResponse.getStatus());
        assertEquals("application/json", mockResponse.getContentType());
    }

    @Test
    void shouldUseEndpointSpecificConfiguration() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");
        mockRequest.setRequestURI("/api/users");
        
        RateLimiterConfig endpointConfig = createTestConfig();
        endpointConfig.setCapacity(50L);
        
        when(mockRateLimiterConfig.getCombinedConfig(eq("/api/users"), anyString()))
                .thenReturn(endpointConfig);
        when(mockRateLimiterUtil.createBucket(endpointConfig))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(49L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(eq("/api/users"), anyString());
        verify(mockRateLimiterUtil).createBucket(endpointConfig);
    }

    @Test
    void shouldUseUserRoleConfiguration() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");
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
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(199L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("ADMIN"));
        verify(mockRateLimiterUtil).createBucket(userConfig);
    }

    @Test
    void shouldSetCorrectRateLimitHeaders() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");

        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        assertEquals("10", mockResponse.getHeader("X-RateLimit-Limit"));
        assertEquals("9", mockResponse.getHeader("X-RateLimit-Remaining"));
        assertNotNull(mockResponse.getHeader("X-RateLimit-Reset"));
    }

    @Test
    void shouldHandleMethodWithoutAnnotation() throws Throwable {
        // Given
        Method method = getTestMethodWithoutAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");

        // When
        Object result = rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        assertEquals("success", result);
        verify(mockJoinPoint).proceed();
        verifyNoInteractions(mockRateLimiterUtil, mockRateLimiterConfig);
    }

    @Test
    void shouldHandleNullRequestContext() throws Throwable {
        // Given
        RequestContextHolder.resetRequestAttributes();

        // When & Then
        assertThrows(RateLimiterException.class, () -> 
            rateLimiterAspect.rateLimit(mockJoinPoint));
    }

    @Test
    void shouldHandleCustomTokenConsumption() throws Throwable {
        // Given
        Method method = getTestMethodWithCustomTokens();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");

        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(5L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(5L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        verify(mockRateLimiterUtil).tryConsume(any(), eq(5L));
    }

    @Test
    void shouldHandleRateLimiterException() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);

        when(mockRateLimiterConfig.getCombinedConfig(anyString(), anyString()))
                .thenThrow(new RateLimiterException("Configuration error"));

        // When & Then
        assertThrows(RateLimiterException.class, () -> 
            rateLimiterAspect.rateLimit(mockJoinPoint));
    }

    @Test
    void shouldHandleObjectMapperException() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);

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
            rateLimiterAspect.rateLimit(mockJoinPoint));
    }

    @Test
    void shouldExtractUserRoleFromAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");
        mockRequest.addHeader("Authorization", "Bearer user-token");
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("USER")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("USER"));
    }

    @Test
    void shouldUseDefaultUserRoleWhenNoAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");

        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("ANONYMOUS")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

        // Then
        verify(mockRateLimiterConfig).getCombinedConfig(anyString(), eq("ANONYMOUS"));
    }

    @Test
    void shouldHandleEmptyAuthorizationHeader() throws Throwable {
        // Given
        Method method = getTestMethodWithAnnotation();
        when(mockMethodSignature.getMethod()).thenReturn(method);
        when(mockJoinPoint.proceed()).thenReturn("success");
        mockRequest.addHeader("Authorization", "");
        
        when(mockRateLimiterConfig.getCombinedConfig(anyString(), eq("ANONYMOUS")))
                .thenReturn(createTestConfig());
        when(mockRateLimiterUtil.createBucket(any(RateLimiterConfig.class)))
                .thenReturn(createTestBucket());
        when(mockRateLimiterUtil.tryConsume(any(), eq(1L)))
                .thenReturn(true);
        when(mockRateLimiterUtil.getAvailableTokens(any()))
                .thenReturn(9L);

        // When
        rateLimiterAspect.rateLimit(mockJoinPoint);

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
                .addLimit(io.github.bucket4j.Bandwidth.simple(10L, Duration.ofMinutes(1)))
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
