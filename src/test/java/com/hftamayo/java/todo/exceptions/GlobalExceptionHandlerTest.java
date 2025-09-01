package com.hftamayo.java.todo.exceptions;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.error.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

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
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleAuthenticationException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Authentication failed", response.getBody().getResultMessage());
        assertEquals(401, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle ValidationException")
    void shouldHandleValidationException() {
        // Arrange
        ValidationException exception = new ValidationException("email", "Email format is invalid");
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleValidationException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getResultMessage());
        assertEquals(400, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("User", 123L);
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleResourceNotFoundException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().getResultMessage());
        assertEquals(404, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle DuplicateResourceException")
    void shouldHandleDuplicateResourceException() {
        // Arrange
        DuplicateResourceException exception = DuplicateResourceException.withIdentifier("User", "test@example.com");
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleDuplicateResourceException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource already exists", response.getBody().getResultMessage());
        assertEquals(409, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle BusinessLogicException")
    void shouldHandleBusinessLogicException() {
        // Arrange
        BusinessLogicException exception = new BusinessLogicException("TASK-001", "Cannot delete task with dependencies");
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleBusinessLogicException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business rule violation", response.getBody().getResultMessage());
        assertEquals(422, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
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
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleMethodArgumentNotValid(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getResultMessage());
        assertEquals(400, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        // Arrange
        Exception exception = new Exception("An unexpected error occurred");
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleGenericException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().getResultMessage());
        assertEquals(500, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }

    @Test
    @DisplayName("Should handle exception with null message")
    void shouldHandleExceptionWithNullMessage() {
        // Arrange
        Exception exception = new Exception();
        
        // Act
        ResponseEntity<EndpointResponseDto<ErrorResponseDto>> response = exceptionHandler.handleGenericException(exception, webRequest);
        
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred", response.getBody().getResultMessage());
        assertEquals(500, response.getBody().getCode());
        assertNotNull(response.getBody().getData());
        assertTrue(response.getBody().getData() instanceof ErrorResponseDto);
    }
}