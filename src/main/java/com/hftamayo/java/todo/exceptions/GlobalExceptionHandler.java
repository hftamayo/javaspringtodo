package com.hftamayo.java.todo.exceptions;

import com.hftamayo.java.todo.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for centralized error handling across the application.
 * Provides consistent error responses for all exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED,
            "Authentication failed",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(ValidationException ex, WebRequest request) {
        logger.warn("Validation failed: {}", ex.getMessage());
        String errorMessage = ex.getValidationErrors() != null && ex.getValidationErrors().length > 0 
            ? ex.getValidationErrors()[0] 
            : ex.getMessage();
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            errorMessage
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND,
            "Resource not found",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.CONFLICT,
            "Resource already exists",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
        logger.warn("Business logic violation: {}", ex.getMessage());
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.UNPROCESSABLE_ENTITY,
            "Business rule violation",
            ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Method argument validation failed: {}", ex.getMessage());
        List<String> errors = ex.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST,
            "Validation failed",
            errors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: ", ex);
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        ErrorResponseDto errorResponse = new ErrorResponseDto(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            errorMessage,
            errorMessage
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 