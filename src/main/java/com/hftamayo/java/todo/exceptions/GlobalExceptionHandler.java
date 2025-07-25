package com.hftamayo.java.todo.exceptions;

import com.hftamayo.java.todo.dto.ErrorResponseDto;
import com.hftamayo.java.todo.utilities.endpoints.ResponseUtil;
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
import com.hftamayo.java.todo.utilities.endpoints.EndpointResponseDto;

/**
 * Global exception handler for centralized error handling across the application.
 * Provides consistent error responses for all exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.UNAUTHORIZED, "Authentication failed", ex),
            HttpStatus.UNAUTHORIZED
        );
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleValidationException(ValidationException ex, WebRequest request) {
        logger.warn("Validation failed: {}", ex.getMessage());
        String errorMessage = ex.getValidationErrors() != null && ex.getValidationErrors().length > 0 
            ? ex.getValidationErrors()[0] 
            : ex.getMessage();
        ValidationException wrappedEx = new ValidationException(errorMessage);
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Validation failed", wrappedEx),
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.NOT_FOUND, "Resource not found", ex),
            HttpStatus.NOT_FOUND
        );
    }
    
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleDuplicateResourceException(DuplicateResourceException ex, WebRequest request) {
        logger.warn("Duplicate resource: {}", ex.getMessage());
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.CONFLICT, "Resource already exists", ex),
            HttpStatus.CONFLICT
        );
    }
    
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleBusinessLogicException(BusinessLogicException ex, WebRequest request) {
        logger.warn("Business logic violation: {}", ex.getMessage());
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Business rule violation", ex),
            HttpStatus.UNPROCESSABLE_ENTITY
        );
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Method argument validation failed: {}", ex.getMessage());
        List<String> errors = ex.getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        ValidationException wrappedEx = new ValidationException(errors.toArray(new String[0]));
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.BAD_REQUEST, "Validation failed", wrappedEx),
            HttpStatus.BAD_REQUEST
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<EndpointResponseDto<ErrorResponseDto>> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Unexpected error occurred: ", ex);
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        Exception wrappedEx = new Exception(errorMessage);
        return new ResponseEntity<>(
            ResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage, wrappedEx),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
} 