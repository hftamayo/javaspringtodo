package com.hftamayo.java.todo.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Validation Exception Tests")
class ValidationExceptionTest {

    @Test
    @DisplayName("Should create ValidationException with message")
    void shouldCreateValidationExceptionWithMessage() {
        // Arrange
        String errorMessage = "Email is required";
        
        // Act
        ValidationException exception = new ValidationException(errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create ValidationException with field and message")
    void shouldCreateValidationExceptionWithFieldAndMessage() {
        // Arrange
        String field = "email";
        String errorMessage = "Email format is invalid";
        
        // Act
        ValidationException exception = new ValidationException(field, errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(field, exception.getField());
    }

    @Test
    @DisplayName("Should create ValidationException with multiple validation errors")
    void shouldCreateValidationExceptionWithMultipleErrors() {
        // Arrange
        String[] errors = {"Email is required", "Password must be at least 8 characters"};
        
        // Act
        ValidationException exception = new ValidationException(errors);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Validation failed", exception.getMessage());
        assertArrayEquals(errors, exception.getValidationErrors());
    }

    @Test
    @DisplayName("Should handle null validation errors")
    void shouldHandleNullValidationErrors() {
        // Act
        ValidationException exception = new ValidationException((String[]) null);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Validation failed", exception.getMessage());
        assertNull(exception.getValidationErrors());
    }
} 