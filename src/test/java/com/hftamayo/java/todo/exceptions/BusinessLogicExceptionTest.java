package com.hftamayo.java.todo.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Business Logic Exception Tests")
class BusinessLogicExceptionTest {

    @Test
    @DisplayName("Should create BusinessLogicException with message")
    void shouldCreateBusinessLogicExceptionWithMessage() {
        // Arrange
        String errorMessage = "Task cannot be completed because it is already in progress";
        
        // Act
        BusinessLogicException exception = new BusinessLogicException(errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    @DisplayName("Should create BusinessLogicException with error code and message")
    void shouldCreateBusinessLogicExceptionWithErrorCodeAndMessage() {
        // Arrange
        String errorCode = "TASK-001";
        String errorMessage = "Cannot delete task that has dependencies";
        
        // Act
        BusinessLogicException exception = new BusinessLogicException(errorCode, errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
    }

    @Test
    @DisplayName("Should create BusinessLogicException with error code, message and cause")
    void shouldCreateBusinessLogicExceptionWithErrorCodeMessageAndCause() {
        // Arrange
        String errorCode = "USER-002";
        String errorMessage = "User account is locked";
        Throwable cause = new RuntimeException("Too many failed login attempts");
        
        // Act
        BusinessLogicException exception = new BusinessLogicException(errorCode, errorMessage, cause);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Should create BusinessLogicException with operation and resource")
    void shouldCreateBusinessLogicExceptionWithOperationAndResource() {
        // Arrange
        String operation = "DELETE";
        String resource = "Task";
        String reason = "Task has active dependencies";
        
        // Act
        BusinessLogicException exception = new BusinessLogicException(operation, resource, reason);
        
        // Assert
        assertNotNull(exception);
        assertEquals("Cannot DELETE Task: Task has active dependencies", exception.getMessage());
        assertEquals(operation, exception.getOperation());
        assertEquals(resource, exception.getResource());
        assertEquals(reason, exception.getReason());
    }

    @Test
    @DisplayName("Should handle null error code gracefully")
    void shouldHandleNullErrorCodeGracefully() {
        // Arrange
        String errorCode = null;
        String errorMessage = "Business rule violation";
        
        // Act
        BusinessLogicException exception = new BusinessLogicException(errorCode, errorMessage);
        
        // Assert
        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getErrorCode());
    }
} 