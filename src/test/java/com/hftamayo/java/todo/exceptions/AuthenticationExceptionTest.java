package com.hftamayo.java.todo.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentication Exception Tests")
class AuthenticationExceptionTest {

    @Test
    @DisplayName("Should create AuthenticationException with message")
    void shouldCreateAuthenticationExceptionWithMessage() {
        // Arrange
        String errorMessage = "Invalid credentials";
        
        // Act
        AuthenticationException exception = new AuthenticationException(errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create AuthenticationException with message and cause")
    void shouldCreateAuthenticationExceptionWithMessageAndCause() {
        // Arrange
        String errorMessage = "Invalid credentials";
        Throwable cause = new RuntimeException("Database connection failed");
        
        // Act
        AuthenticationException exception = new AuthenticationException(errorMessage, cause);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void shouldHandleNullMessageGracefully() {
        // Act
        AuthenticationException exception = new AuthenticationException(null);
        
        // Assert
        assertNotNull(exception);
        assertNull(exception.getMessage());
    }
} 