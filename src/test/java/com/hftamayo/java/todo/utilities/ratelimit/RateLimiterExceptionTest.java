package com.hftamayo.java.todo.utilities.ratelimit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RateLimiterExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Rate limit configuration error";

        // When
        RateLimiterException exception = new RateLimiterException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithMessageAndCause() {
        // Given
        String message = "Rate limit exceeded";
        Throwable cause = new RuntimeException("Underlying error");

        // When
        RateLimiterException exception = new RateLimiterException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCauseOnly() {
        // Given
        Throwable cause = new IllegalArgumentException("Invalid configuration");

        // When
        RateLimiterException exception = new RateLimiterException(cause);

        // Then
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getMessage().contains(cause.getMessage()));
    }

    @Test
    void shouldHandleNullMessage() {
        // When
        RateLimiterException exception = new RateLimiterException((String) null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleEmptyMessage() {
        // Given
        String emptyMessage = "";

        // When
        RateLimiterException exception = new RateLimiterException(emptyMessage);

        // Then
        assertNotNull(exception);
        assertEquals(emptyMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleNullCause() {
        // Given
        String message = "Test message";

        // When
        RateLimiterException exception = new RateLimiterException(message, null);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleNullMessageAndNullCause() {
        // When
        RateLimiterException exception = new RateLimiterException(null, null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleNullCauseOnly() {
        // When
        RateLimiterException exception = new RateLimiterException((Throwable) null);

        // Then
        assertNotNull(exception);
        assertNull(exception.getCause());
        assertNotNull(exception.getMessage()); // Should have default message
    }

    @Test
    void shouldCreateExceptionWithLongMessage() {
        // Given
        String longMessage = "This is a very long error message that contains detailed information " +
                "about what went wrong with the rate limiting configuration. It should be able to " +
                "handle messages of considerable length without any issues.";

        // When
        RateLimiterException exception = new RateLimiterException(longMessage);

        // Then
        assertNotNull(exception);
        assertEquals(longMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithSpecialCharacters() {
        // Given
        String specialMessage = "Rate limit error with special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        RateLimiterException exception = new RateLimiterException(specialMessage);

        // Then
        assertNotNull(exception);
        assertEquals(specialMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithUnicodeCharacters() {
        // Given
        String unicodeMessage = "Rate limit error with unicode: ñáéíóú 中文 русский";

        // When
        RateLimiterException exception = new RateLimiterException(unicodeMessage);

        // Then
        assertNotNull(exception);
        assertEquals(unicodeMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldPropagateCauseStackTrace() {
        // Given
        RuntimeException cause = new RuntimeException("Original error");
        cause.fillInStackTrace();

        // When
        RateLimiterException exception = new RateLimiterException("Wrapper error", cause);

        // Then
        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getStackTrace().length > 0);
        assertTrue(cause.getStackTrace().length > 0);
    }

    @Test
    void shouldHandleChainedExceptions() {
        // Given
        RuntimeException originalCause = new RuntimeException("Original cause");
        IllegalArgumentException intermediateCause = new IllegalArgumentException("Intermediate cause", originalCause);
        String message = "Final rate limit error";

        // When
        RateLimiterException exception = new RateLimiterException(message, intermediateCause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(intermediateCause, exception.getCause());
        assertEquals(originalCause, exception.getCause().getCause());
    }

    @Test
    void shouldBeInstanceOfRuntimeException() {
        // Given
        String message = "Test exception";

        // When
        RateLimiterException exception = new RateLimiterException(message);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
        assertTrue(exception instanceof Throwable);
    }

    @Test
    void shouldHaveCorrectExceptionName() {
        // Given
        String message = "Test exception";

        // When
        RateLimiterException exception = new RateLimiterException(message);

        // Then
        assertEquals("RateLimiterException", exception.getClass().getSimpleName());
        assertEquals("com.hftamayo.java.todo.utilities.ratelimit.RateLimiterException", 
                exception.getClass().getName());
    }

    @Test
    void shouldHandleExceptionWithZeroLengthMessage() {
        // Given
        String zeroLengthMessage = "";

        // When
        RateLimiterException exception = new RateLimiterException(zeroLengthMessage);

        // Then
        assertNotNull(exception);
        assertEquals(zeroLengthMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleExceptionWithWhitespaceMessage() {
        // Given
        String whitespaceMessage = "   ";

        // When
        RateLimiterException exception = new RateLimiterException(whitespaceMessage);

        // Then
        assertNotNull(exception);
        assertEquals(whitespaceMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleExceptionWithNewlineCharacters() {
        // Given
        String newlineMessage = "Rate limit error\nwith newlines\nand multiple lines";

        // When
        RateLimiterException exception = new RateLimiterException(newlineMessage);

        // Then
        assertNotNull(exception);
        assertEquals(newlineMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldHandleExceptionWithTabCharacters() {
        // Given
        String tabMessage = "Rate limit error\twith tabs\tand spacing";

        // When
        RateLimiterException exception = new RateLimiterException(tabMessage);

        // Then
        assertNotNull(exception);
        assertEquals(tabMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateMultipleExceptionsIndependently() {
        // Given
        String message1 = "First exception";
        String message2 = "Second exception";

        // When
        RateLimiterException exception1 = new RateLimiterException(message1);
        RateLimiterException exception2 = new RateLimiterException(message2);

        // Then
        assertNotNull(exception1);
        assertNotNull(exception2);
        assertEquals(message1, exception1.getMessage());
        assertEquals(message2, exception2.getMessage());
        assertNotSame(exception1, exception2);
    }

    @Test
    void shouldHandleExceptionWithVeryLongCauseMessage() {
        // Given
        String longCauseMessage = "This is a very long cause message that contains detailed information " +
                "about what went wrong with the underlying system. It should be able to handle " +
                "messages of considerable length without any issues. The message continues with " +
                "more details about the error condition and potential solutions.";
        RuntimeException cause = new RuntimeException(longCauseMessage);

        // When
        RateLimiterException exception = new RateLimiterException("Wrapper message", cause);

        // Then
        assertNotNull(exception);
        assertEquals("Wrapper message", exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(longCauseMessage, exception.getCause().getMessage());
    }

    @Test
    void shouldHandleExceptionWithNullMessageAndValidCause() {
        // Given
        RuntimeException cause = new RuntimeException("Valid cause");

        // When
        RateLimiterException exception = new RateLimiterException(null, cause);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldHandleExceptionWithEmptyMessageAndValidCause() {
        // Given
        String emptyMessage = "";
        RuntimeException cause = new RuntimeException("Valid cause");

        // When
        RateLimiterException exception = new RateLimiterException(emptyMessage, cause);

        // Then
        assertNotNull(exception);
        assertEquals(emptyMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
} 