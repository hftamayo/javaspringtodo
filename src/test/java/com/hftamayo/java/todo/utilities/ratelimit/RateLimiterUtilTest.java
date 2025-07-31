package com.hftamayo.java.todo.utilities.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateLimiterUtilTest {

    private RateLimiterUtil rateLimiterUtil;
    
    @Mock
    private RateLimiterConfig mockConfig;

    @BeforeEach
    void setUp() {
        rateLimiterUtil = new RateLimiterUtil();
    }

    @Test
    void shouldCreateBucketWithValidConfiguration() {
        // Given
        when(mockConfig.getCapacity()).thenReturn(10L);
        when(mockConfig.getRefillRate()).thenReturn(5L);
        when(mockConfig.getRefillDuration()).thenReturn(Duration.ofMinutes(1));

        // When
        Bucket bucket = rateLimiterUtil.createBucket(mockConfig);

        // Then
        assertNotNull(bucket);
        assertTrue(bucket.getAvailableTokens() >= 0);
    }

    @Test
    void shouldCreateBucketWithDefaultConfiguration() {
        // When
        Bucket bucket = rateLimiterUtil.createDefaultBucket();

        // Then
        assertNotNull(bucket);
        assertTrue(bucket.getAvailableTokens() >= 0);
    }

    @Test
    void shouldConsumeTokenSuccessfully() {
        // Given
        Bucket bucket = rateLimiterUtil.createDefaultBucket();
        long initialTokens = bucket.getAvailableTokens();

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, 1);

        // Then
        assertTrue(consumed);
        assertEquals(initialTokens - 1, bucket.getAvailableTokens());
    }

    @Test
    void shouldFailToConsumeWhenInsufficientTokens() {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(1L, 1L, Duration.ofHours(1));
        rateLimiterUtil.tryConsume(bucket, 1); // Consume the only token

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, 1);

        // Then
        assertFalse(consumed);
        assertEquals(0, bucket.getAvailableTokens());
    }

    @Test
    void shouldConsumeMultipleTokensSuccessfully() {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(10L, 5L, Duration.ofMinutes(1));
        long initialTokens = bucket.getAvailableTokens();

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, 5);

        // Then
        assertTrue(consumed);
        assertEquals(initialTokens - 5, bucket.getAvailableTokens());
    }

    @Test
    void shouldFailToConsumeMoreTokensThanAvailable() {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(5L, 2L, Duration.ofMinutes(1));
        long initialTokens = bucket.getAvailableTokens();

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, initialTokens + 1);

        // Then
        assertFalse(consumed);
        assertEquals(initialTokens, bucket.getAvailableTokens()); // Tokens should remain unchanged
    }

    @Test
    void shouldRefillTokensOverTime() throws InterruptedException {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(10L, 5L, Duration.ofSeconds(1));
        rateLimiterUtil.tryConsume(bucket, 10); // Consume all tokens
        assertEquals(0, bucket.getAvailableTokens());

        // When
        Thread.sleep(1100); // Wait for refill

        // Then
        assertTrue(bucket.getAvailableTokens() > 0);
    }

    @Test
    void shouldHandleConcurrentTokenConsumption() throws InterruptedException {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(100L, 10L, Duration.ofMinutes(1));
        ExecutorService executor = Executors.newFixedThreadPool(10);
        int numberOfThreads = 10;
        int tokensPerThread = 5;

        // When
        CompletableFuture<Boolean>[] futures = new CompletableFuture[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            futures[i] = CompletableFuture.supplyAsync(() -> 
                rateLimiterUtil.tryConsume(bucket, tokensPerThread), executor);
        }

        // Wait for all threads to complete
        CompletableFuture.allOf(futures).get(5, TimeUnit.SECONDS);

        // Then
        for (CompletableFuture<Boolean> future : futures) {
            assertTrue(future.get());
        }
        assertEquals(50, 100 - bucket.getAvailableTokens()); // Should have consumed 50 tokens total

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test
    void shouldHandleZeroTokenConsumption() {
        // Given
        Bucket bucket = rateLimiterUtil.createDefaultBucket();
        long initialTokens = bucket.getAvailableTokens();

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, 0);

        // Then
        assertTrue(consumed);
        assertEquals(initialTokens, bucket.getAvailableTokens()); // Should remain unchanged
    }

    @Test
    void shouldHandleNegativeTokenConsumption() {
        // Given
        Bucket bucket = rateLimiterUtil.createDefaultBucket();
        long initialTokens = bucket.getAvailableTokens();

        // When
        boolean consumed = rateLimiterUtil.tryConsume(bucket, -1);

        // Then
        assertFalse(consumed);
        assertEquals(initialTokens, bucket.getAvailableTokens()); // Should remain unchanged
    }

    @Test
    void shouldCreateBucketWithCustomBandwidth() {
        // Given
        Bandwidth bandwidth = Bandwidth.classic(20L, Refill.intervally(10L, Duration.ofMinutes(2)));

        // When
        Bucket bucket = rateLimiterUtil.createBucketWithBandwidth(bandwidth);

        // Then
        assertNotNull(bucket);
        assertEquals(20L, bucket.getAvailableTokens());
    }

    @Test
    void shouldGetAvailableTokens() {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(15L, 5L, Duration.ofMinutes(1));

        // When
        long availableTokens = rateLimiterUtil.getAvailableTokens(bucket);

        // Then
        assertEquals(15L, availableTokens);
    }

    @Test
    void shouldGetAvailableTokensAfterConsumption() {
        // Given
        Bucket bucket = rateLimiterUtil.createBucket(15L, 5L, Duration.ofMinutes(1));
        rateLimiterUtil.tryConsume(bucket, 7);

        // When
        long availableTokens = rateLimiterUtil.getAvailableTokens(bucket);

        // Then
        assertEquals(8L, availableTokens);
    }

    @Test
    void shouldHandleNullBucketGracefully() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> 
            rateLimiterUtil.tryConsume(null, 1));
        
        assertThrows(IllegalArgumentException.class, () -> 
            rateLimiterUtil.getAvailableTokens(null));
    }

    @Test
    void shouldCreateBucketWithHighCapacity() {
        // Given
        long highCapacity = 10000L;

        // When
        Bucket bucket = rateLimiterUtil.createBucket(highCapacity, 1000L, Duration.ofMinutes(1));

        // Then
        assertNotNull(bucket);
        assertEquals(highCapacity, bucket.getAvailableTokens());
    }

    @Test
    void shouldCreateBucketWithVeryShortRefillDuration() {
        // Given
        Duration shortDuration = Duration.ofMillis(100);

        // When
        Bucket bucket = rateLimiterUtil.createBucket(10L, 5L, shortDuration);

        // Then
        assertNotNull(bucket);
        assertTrue(bucket.getAvailableTokens() >= 0);
    }
} 