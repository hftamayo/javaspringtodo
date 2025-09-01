package com.hftamayo.java.todo.dto;

import com.hftamayo.java.todo.dto.error.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

@DisplayName("Error Response DTO Tests")
class ErrorResponseDtoTest {

    @Test
    @DisplayName("Should create ErrorResponseDto with all fields")
    void shouldCreateErrorResponseDtoWithAllFields() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = "Validation failed";
        List<String> errors = List.of("Email is required", "Password must be at least 8 characters");
        
        // Act
        ErrorResponseDto errorResponse = new ErrorResponseDto(timestamp, status, message, errors);
        
        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(errors, errorResponse.getErrors());
        assertEquals(status.value(), errorResponse.getStatus().value());
    }

    @Test
    @DisplayName("Should create ErrorResponseDto with single error")
    void shouldCreateErrorResponseDtoWithSingleError() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = "Resource not found";
        String singleError = "User with id 123 not found";
        
        // Act
        ErrorResponseDto errorResponse = new ErrorResponseDto(timestamp, status, message, singleError);
        
        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(List.of(singleError), errorResponse.getErrors());
        assertEquals(status.value(), errorResponse.getStatus().value());
    }

    @Test
    @DisplayName("Should create ErrorResponseDto with null errors")
    void shouldCreateErrorResponseDtoWithNullErrors() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "An unexpected error occurred";
        
        // Act
        ErrorResponseDto errorResponse = new ErrorResponseDto(timestamp, status, message, (List<String>) null);
        
        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertNull(errorResponse.getErrors());
        assertEquals(status.value(), errorResponse.getStatus().value());
    }

    @Test
    @DisplayName("Should create ErrorResponseDto with empty errors list")
    void shouldCreateErrorResponseDtoWithEmptyErrorsList() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "Authentication failed";
        List<String> emptyErrors = List.of();
        
        // Act
        ErrorResponseDto errorResponse = new ErrorResponseDto(timestamp, status, message, emptyErrors);
        
        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertTrue(errorResponse.getErrors().isEmpty());
        assertEquals(status.value(), errorResponse.getStatus().value());
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void shouldHandleNullMessageGracefully() {
        // Arrange
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = null;
        String error = "Some error occurred";
        
        // Act
        ErrorResponseDto errorResponse = new ErrorResponseDto(timestamp, status, message, error);
        
        // Assert
        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertNull(errorResponse.getMessage());
        assertEquals(List.of(error), errorResponse.getErrors());
        assertEquals(status.value(), errorResponse.getStatus().value());
    }
} 