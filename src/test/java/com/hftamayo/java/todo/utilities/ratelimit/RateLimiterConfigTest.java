package com.hftamayo.java.todo.utilities.ratelimit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RateLimiterConfigTest {

    private RateLimiterConfig rateLimiterConfig;

    @BeforeEach
    void setUp() {
        rateLimiterConfig = new RateLimiterConfig();
    }

    @Test
    void shouldLoadDefaultConfiguration() {
        // When
        rateLimiterConfig.loadDefaultConfiguration();

        // Then
        assertNotNull(rateLimiterConfig.getCapacity());
        assertNotNull(rateLimiterConfig.getRefillRate());
        assertNotNull(rateLimiterConfig.getRefillDuration());
        assertTrue(rateLimiterConfig.getCapacity() > 0);
        assertTrue(rateLimiterConfig.getRefillRate() > 0);
        assertTrue(rateLimiterConfig.getRefillDuration().toMillis() > 0);
    }

    @Test
    void shouldLoadConfigurationFromProperties() {
        // Given
        Map<String, Object> properties = new HashMap<>();
        properties.put("rate.limiter.default.capacity", 100L);
        properties.put("rate.limiter.default.refill-rate", 10L);
        properties.put("rate.limiter.default.refill-duration", "PT1M");

        // When
        rateLimiterConfig.loadConfigurationFromProperties(properties);

        // Then
        assertEquals(100L, rateLimiterConfig.getCapacity());
        assertEquals(10L, rateLimiterConfig.getRefillRate());
        assertEquals(Duration.ofMinutes(1), rateLimiterConfig.getRefillDuration());
    }

    @Test
    void shouldLoadEndpointSpecificConfiguration() {
        // Given
        String endpoint = "/api/users";
        Map<String, Object> properties = new HashMap<>();
        properties.put("rate.limiter.endpoints." + endpoint + ".capacity", 50L);
        properties.put("rate.limiter.endpoints." + endpoint + ".refill-rate", 5L);
        properties.put("rate.limiter.endpoints." + endpoint + ".refill-duration", "PT30S");

        // When
        rateLimiterConfig.loadEndpointConfiguration(endpoint, properties);

        // Then
        RateLimiterConfig endpointConfig = rateLimiterConfig.getEndpointConfig(endpoint);
        assertNotNull(endpointConfig);
        assertEquals(50L, endpointConfig.getCapacity());
        assertEquals(5L, endpointConfig.getRefillRate());
        assertEquals(Duration.ofSeconds(30), endpointConfig.getRefillDuration());
    }

    @Test
    void shouldLoadUserSpecificConfiguration() {
        // Given
        String userRole = "ADMIN";
        Map<String, Object> properties = new HashMap<>();
        properties.put("rate.limiter.users." + userRole + ".capacity", 200L);
        properties.put("rate.limiter.users." + userRole + ".refill-rate", 20L);
        properties.put("rate.limiter.users." + userRole + ".refill-duration", "PT2M");

        // When
        rateLimiterConfig.loadUserConfiguration(userRole, properties);

        // Then
        RateLimiterConfig userConfig = rateLimiterConfig.getUserConfig(userRole);
        assertNotNull(userConfig);
        assertEquals(200L, userConfig.getCapacity());
        assertEquals(20L, userConfig.getRefillRate());
        assertEquals(Duration.ofMinutes(2), userConfig.getRefillDuration());
    }

    @Test
    void shouldReturnDefaultConfigWhenEndpointConfigNotFound() {
        // Given
        String nonExistentEndpoint = "/api/nonexistent";
        rateLimiterConfig.loadDefaultConfiguration();

        // When
        RateLimiterConfig config = rateLimiterConfig.getEndpointConfig(nonExistentEndpoint);

        // Then
        assertNotNull(config);
        assertEquals(rateLimiterConfig.getCapacity(), config.getCapacity());
        assertEquals(rateLimiterConfig.getRefillRate(), config.getRefillRate());
        assertEquals(rateLimiterConfig.getRefillDuration(), config.getRefillDuration());
    }

    @Test
    void shouldReturnDefaultConfigWhenUserConfigNotFound() {
        // Given
        String nonExistentUser = "NONEXISTENT_USER";
        rateLimiterConfig.loadDefaultConfiguration();

        // When
        RateLimiterConfig config = rateLimiterConfig.getUserConfig(nonExistentUser);

        // Then
        assertNotNull(config);
        assertEquals(rateLimiterConfig.getCapacity(), config.getCapacity());
        assertEquals(rateLimiterConfig.getRefillRate(), config.getRefillRate());
        assertEquals(rateLimiterConfig.getRefillDuration(), config.getRefillDuration());
    }

    @Test
    void shouldValidateConfigurationWithValidValues() {
        // Given
        rateLimiterConfig.setCapacity(100L);
        rateLimiterConfig.setRefillRate(10L);
        rateLimiterConfig.setRefillDuration(Duration.ofMinutes(1));

        // When & Then
        assertDoesNotThrow(() -> rateLimiterConfig.validateConfiguration());
    }

    @Test
    void shouldThrowExceptionForInvalidCapacity() {
        // Given
        rateLimiterConfig.setCapacity(-1L);
        rateLimiterConfig.setRefillRate(10L);
        rateLimiterConfig.setRefillDuration(Duration.ofMinutes(1));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rateLimiterConfig.validateConfiguration());
        assertTrue(exception.getMessage().contains("Capacity"));
    }

    @Test
    void shouldThrowExceptionForInvalidRefillRate() {
        // Given
        rateLimiterConfig.setCapacity(100L);
        rateLimiterConfig.setRefillRate(0L);
        rateLimiterConfig.setRefillDuration(Duration.ofMinutes(1));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rateLimiterConfig.validateConfiguration());
        assertTrue(exception.getMessage().contains("Refill rate"));
    }

    @Test
    void shouldThrowExceptionForInvalidRefillDuration() {
        // Given
        rateLimiterConfig.setCapacity(100L);
        rateLimiterConfig.setRefillRate(10L);
        rateLimiterConfig.setRefillDuration(Duration.ZERO);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rateLimiterConfig.validateConfiguration());
        assertTrue(exception.getMessage().contains("Refill duration"));
    }

    @Test
    void shouldParseDurationStringCorrectly() {
        // Given
        String durationString = "PT1M30S";

        // When
        Duration duration = rateLimiterConfig.parseDuration(durationString);

        // Then
        assertEquals(Duration.ofMinutes(1).plus(Duration.ofSeconds(30)), duration);
    }

    @Test
    void shouldHandleInvalidDurationString() {
        // Given
        String invalidDuration = "INVALID_DURATION";

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rateLimiterConfig.parseDuration(invalidDuration));
        assertTrue(exception.getMessage().contains("Invalid duration format"));
    }

    @Test
    void shouldHandleNullDurationString() {
        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rateLimiterConfig.parseDuration(null));
        assertTrue(exception.getMessage().contains("Duration string cannot be null"));
    }

    @Test
    void shouldMergeConfigurationsCorrectly() {
        // Given
        RateLimiterConfig baseConfig = new RateLimiterConfig();
        baseConfig.setCapacity(100L);
        baseConfig.setRefillRate(10L);
        baseConfig.setRefillDuration(Duration.ofMinutes(1));

        RateLimiterConfig overrideConfig = new RateLimiterConfig();
        overrideConfig.setCapacity(50L);
        overrideConfig.setRefillRate(5L);
        // refillDuration not set, should use base config

        // When
        RateLimiterConfig mergedConfig = rateLimiterConfig.mergeConfigurations(baseConfig, overrideConfig);

        // Then
        assertEquals(50L, mergedConfig.getCapacity());
        assertEquals(5L, mergedConfig.getRefillRate());
        assertEquals(Duration.ofMinutes(1), mergedConfig.getRefillDuration());
    }

    @Test
    void shouldHandleNullOverrideConfiguration() {
        // Given
        RateLimiterConfig baseConfig = new RateLimiterConfig();
        baseConfig.setCapacity(100L);
        baseConfig.setRefillRate(10L);
        baseConfig.setRefillDuration(Duration.ofMinutes(1));

        // When
        RateLimiterConfig mergedConfig = rateLimiterConfig.mergeConfigurations(baseConfig, null);

        // Then
        assertEquals(baseConfig.getCapacity(), mergedConfig.getCapacity());
        assertEquals(baseConfig.getRefillRate(), mergedConfig.getRefillRate());
        assertEquals(baseConfig.getRefillDuration(), mergedConfig.getRefillDuration());
    }

    @Test
    void shouldGetCombinedConfigForEndpointAndUser() {
        // Given
        String endpoint = "/api/users";
        String userRole = "ADMIN";

        // Set up endpoint config
        Map<String, Object> endpointProperties = new HashMap<>();
        endpointProperties.put("rate.limiter.endpoints." + endpoint + ".capacity", 50L);
        endpointProperties.put("rate.limiter.endpoints." + endpoint + ".refill-rate", 5L);
        rateLimiterConfig.loadEndpointConfiguration(endpoint, endpointProperties);

        // Set up user config
        Map<String, Object> userProperties = new HashMap<>();
        userProperties.put("rate.limiter.users." + userRole + ".capacity", 200L);
        userProperties.put("rate.limiter.users." + userRole + ".refill-rate", 20L);
        rateLimiterConfig.loadUserConfiguration(userRole, userProperties);

        // When
        RateLimiterConfig combinedConfig = rateLimiterConfig.getCombinedConfig(endpoint, userRole);

        // Then
        assertNotNull(combinedConfig);
        // Should use the more restrictive configuration (endpoint config)
        assertEquals(50L, combinedConfig.getCapacity());
        assertEquals(5L, combinedConfig.getRefillRate());
    }

    @Test
    void shouldHandleEmptyPropertiesMap() {
        // Given
        Map<String, Object> emptyProperties = new HashMap<>();

        // When & Then
        assertDoesNotThrow(() -> rateLimiterConfig.loadConfigurationFromProperties(emptyProperties));
        assertDoesNotThrow(() -> rateLimiterConfig.loadEndpointConfiguration("/test", emptyProperties));
        assertDoesNotThrow(() -> rateLimiterConfig.loadUserConfiguration("test", emptyProperties));
    }

    @Test
    void shouldHandleNullPropertiesMap() {
        // When & Then
        assertDoesNotThrow(() -> rateLimiterConfig.loadConfigurationFromProperties(null));
        assertDoesNotThrow(() -> rateLimiterConfig.loadEndpointConfiguration("/test", null));
        assertDoesNotThrow(() -> rateLimiterConfig.loadUserConfiguration("test", null));
    }

    @Test
    void shouldSetAndGetConfigurationValues() {
        // Given
        Long capacity = 150L;
        Long refillRate = 15L;
        Duration refillDuration = Duration.ofMinutes(2);

        // When
        rateLimiterConfig.setCapacity(capacity);
        rateLimiterConfig.setRefillRate(refillRate);
        rateLimiterConfig.setRefillDuration(refillDuration);

        // Then
        assertEquals(capacity, rateLimiterConfig.getCapacity());
        assertEquals(refillRate, rateLimiterConfig.getRefillRate());
        assertEquals(refillDuration, rateLimiterConfig.getRefillDuration());
    }

    @Test
    void shouldHandleVeryLargeCapacity() {
        // Given
        Long largeCapacity = Long.MAX_VALUE;

        // When
        rateLimiterConfig.setCapacity(largeCapacity);

        // Then
        assertEquals(largeCapacity, rateLimiterConfig.getCapacity());
        assertDoesNotThrow(() -> rateLimiterConfig.validateConfiguration());
    }

    @Test
    void shouldHandleVeryShortDuration() {
        // Given
        Duration shortDuration = Duration.ofNanos(1);

        // When
        rateLimiterConfig.setRefillDuration(shortDuration);

        // Then
        assertEquals(shortDuration, rateLimiterConfig.getRefillDuration());
        assertDoesNotThrow(() -> rateLimiterConfig.validateConfiguration());
    }
}
