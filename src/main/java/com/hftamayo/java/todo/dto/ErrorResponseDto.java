package com.hftamayo.java.todo.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response DTO for consistent API error responses.
 */
public class ErrorResponseDto {
    
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;
    
    public ErrorResponseDto(LocalDateTime timestamp, HttpStatus status, String message, List<String> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = errors;
    }
    
    public ErrorResponseDto(LocalDateTime timestamp, HttpStatus status, String message, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = List.of(error);
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public HttpStatus getStatus() {
        return status;
    }
    
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public int getStatusCode() {
        return status != null ? status.value() : 500;
    }
} 