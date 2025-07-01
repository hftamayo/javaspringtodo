package com.hftamayo.java.todo.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Resource Not Found Exception Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should create ResourceNotFoundException with resource type and id")
    void shouldCreateResourceNotFoundExceptionWithResourceTypeAndId() {
        // Arrange
        String resourceType = "User";
        Long resourceId = 123L;
        
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, resourceId);
        
        // Assert
        assertNotNull(exception);
        assertEquals("User with id 123 not found", exception.getMessage());
        assertEquals(resourceType, exception.getResourceType());
        assertEquals(resourceId, exception.getResourceId());
    }

    @Test
    @DisplayName("Should create ResourceNotFoundException with resource type and identifier")
    void shouldCreateResourceNotFoundExceptionWithResourceTypeAndIdentifier() {
        // Arrange
        String resourceType = "Task";
        String identifier = "TASK-001";
        
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, identifier);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Task with identifier TASK-001 not found", exception.getMessage());
        assertEquals(resourceType, exception.getResourceType());
        assertEquals(identifier, exception.getResourceIdentifier());
    }

    @Test
    @DisplayName("Should create ResourceNotFoundException with custom message")
    void shouldCreateResourceNotFoundExceptionWithCustomMessage() {
        // Arrange
        String customMessage = "The requested user profile could not be located";
        
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(customMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(customMessage, exception.getMessage());
        assertNull(exception.getResourceType());
        assertNull(exception.getResourceId());
    }

    @Test
    @DisplayName("Should handle null resource type gracefully")
    void shouldHandleNullResourceTypeGracefully() {
        // Arrange
        String resourceType = null;
        Long resourceId = 123L;
        
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceType, resourceId);
        
        // Assert
        assertNotNull(exception);
        assertEquals("null with id 123 not found", exception.getMessage());
        assertNull(exception.getResourceType());
        assertEquals(resourceId, exception.getResourceId());
    }
} 