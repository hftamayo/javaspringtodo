package com.hftamayo.java.todo.exceptions;

import com.hftamayo.java.todo.dto.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Global Exception Handler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
    }

    @Test
    @DisplayName("Should handle AuthenticationException")
    void shouldHandleAuthenticationException() {
        // Arrange
        AuthenticationException exception = new AuthenticationException("Invalid credentials");
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleAuthenticationException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Authentication failed", response.getBody().getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, response.getBody().getStatus());
        assertEquals(401, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("Invalid credentials"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle ValidationException")
    void shouldHandleValidationException() {
        // Arrange
        ValidationException exception = new ValidationException("email", "Email format is invalid");
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleValidationException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
        assertEquals(400, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("Email format is invalid"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("User", 123L);
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
        assertEquals(404, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("User with id 123 not found"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException")
    void shouldHandleDuplicateResourceException() {
        // Arrange
        DuplicateResourceException exception = new DuplicateResourceException("User", "test@example.com");
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleDuplicateResourceException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource already exists", response.getBody().getMessage());
        assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
        assertEquals(409, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("User with identifier test@example.com already exists"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle BusinessLogicException")
    void shouldHandleBusinessLogicException() {
        // Arrange
        BusinessLogicException exception = new BusinessLogicException("TASK-001", "Cannot delete task with dependencies");
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleBusinessLogicException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business rule violation", response.getBody().getMessage());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getBody().getStatus());
        assertEquals(422, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("Cannot delete task with dependencies"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        // Arrange
        FieldError fieldError1 = new FieldError("user", "email", "Email is required");
        FieldError fieldError2 = new FieldError("user", "password", "Password must be at least 8 characters");
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleMethodArgumentNotValid(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
        assertEquals(400, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(2, response.getBody().getErrors().size());
        assertTrue(response.getBody().getErrors().contains("Email is required"));
        assertTrue(response.getBody().getErrors().contains("Password must be at least 8 characters"));
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        // Arrange
        Exception exception = new Exception("An unexpected error occurred");
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleGenericException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals(500, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("An unexpected error occurred"), response.getBody().getErrors());
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void shouldHandleExceptionWithNullMessage() {
        // Arrange
        Exception exception = new Exception();
        
        // Act
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleGenericException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());
        assertEquals(500, response.getBody().getStatusCode());
        assertNotNull(response.getBody().getTimestamp());
        assertEquals(List.of("An unexpected error occurred"), response.getBody().getErrors());
    }
} 