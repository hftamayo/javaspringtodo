package com.hftamayo.java.todo.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Duplicate Resource Exception Tests")
class DuplicateResourceExceptionTest {

    @Test
    @DisplayName("Should create DuplicateResourceException with resource type and identifier")
    void shouldCreateDuplicateResourceExceptionWithResourceTypeAndIdentifier() {
        // Arrange
        String resourceType = "User";
        String identifier = "test@example.com";
        
        // Act
        DuplicateResourceException exception = DuplicateResourceException.withIdentifier(resourceType, identifier);
        
        // Assert
        assertNotNull(exception);
        assertEquals("User with identifier test@example.com already exists", exception.getMessage());
        assertEquals(resourceType, exception.getResourceType());
        assertEquals(identifier, exception.getResourceIdentifier());
    }

    @Test
    @DisplayName("Should create DuplicateResourceException with resource type and id")
    void shouldCreateDuplicateResourceExceptionWithResourceTypeAndId() {
        // Arrange
        String resourceType = "Task";
        Long resourceId = 456L;
        
        // Act
        DuplicateResourceException exception = new DuplicateResourceException(resourceType, resourceId);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Task with id 456 already exists", exception.getMessage());
        assertEquals(resourceType, exception.getResourceType());
        assertEquals(resourceId, exception.getResourceId());
    }

    @Test
    @DisplayName("Should create DuplicateResourceException with custom message")
    void shouldCreateDuplicateResourceExceptionWithCustomMessage() {
        // Arrange
        String customMessage = "A user with this email address is already registered";
        
        // Act
        DuplicateResourceException exception = new DuplicateResourceException(customMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
        assertNull(exception.getResourceType());
        assertNull(exception.getResourceIdentifier());
    }

    @Test
    @DisplayName("Should create DuplicateResourceException with field and value")
    void shouldCreateDuplicateResourceExceptionWithFieldAndValue() {
        // Arrange
        String field = "email";
        String value = "admin@company.com";
        
        // Act
        DuplicateResourceException exception = DuplicateResourceException.withFieldValue(field, value);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Resource with email 'admin@company.com' already exists", exception.getMessage());
        assertEquals(field, exception.getField());
        assertEquals(value, exception.getValue());
    }

    @Test
    @DisplayName("Should handle null resource type gracefully")
    void shouldHandleNullResourceTypeGracefully() {
        // Arrange
        String resourceType = null;
        String identifier = "test@example.com";
        
        // Act
        DuplicateResourceException exception = DuplicateResourceException.withIdentifier(resourceType, identifier);
        
        // Assert
        assertNotNull(exception);
        assertEquals("null with identifier test@example.com already exists", exception.getMessage());
        assertNull(exception.getResourceType());
        assertEquals(identifier, exception.getResourceIdentifier());
    }
} 