package com.hftamayo.java.todo.exceptions;

/**
 * Exception thrown when validation fails.
 * Maps to HTTP 400 Bad Request status.
 */
public class ValidationException extends RuntimeException {
    
    private String field;
    private String[] validationErrors;
    
    public ValidationException(String message) {
        super(message);
    }
    
    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }
    
    public ValidationException(String[] validationErrors) {
        super("Validation failed");
        this.validationErrors = validationErrors;
    }
    
    public String getField() {
        return field;
    }
    
    public String[] getValidationErrors() {
        return validationErrors;
    }
} 