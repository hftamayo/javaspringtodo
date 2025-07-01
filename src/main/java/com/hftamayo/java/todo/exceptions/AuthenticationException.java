package com.hftamayo.java.todo.exceptions;

/**
 * Exception thrown when authentication fails.
 * Maps to HTTP 401 Unauthorized status.
 */
public class AuthenticationException extends RuntimeException {
    
    public AuthenticationException(String message) {
        super(message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
} 