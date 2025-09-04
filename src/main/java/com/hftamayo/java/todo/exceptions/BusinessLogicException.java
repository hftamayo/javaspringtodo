package com.hftamayo.java.todo.exceptions;

/**
 * Exception thrown when business logic rules are violated.
 * Maps to HTTP 422 Unprocessable Entity status.
 */
public class BusinessLogicException extends RuntimeException {
    
    private String errorCode;
    private String operation;
    private String resource;
    private String reason;
    
    public BusinessLogicException(String message) {
        super(message);
    }
    
    public BusinessLogicException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public BusinessLogicException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public BusinessLogicException(String operation, String resource, String reason) {
        super(String.format("Cannot %s %s: %s", operation, resource, reason));
        this.operation = operation;
        this.resource = resource;
        this.reason = reason;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getResource() {
        return resource;
    }
    
    public String getReason() {
        return reason;
    }
} 