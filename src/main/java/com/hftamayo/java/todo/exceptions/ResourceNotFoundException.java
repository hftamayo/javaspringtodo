package com.hftamayo.java.todo.exceptions;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found status.
 */
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceType;
    private Long resourceId;
    private String resourceIdentifier;
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceType, Long resourceId) {
        super(String.format("%s with id %d not found", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public ResourceNotFoundException(String resourceType, String resourceIdentifier) {
        super(String.format("%s with identifier %s not found", resourceType, resourceIdentifier));
        this.resourceType = resourceType;
        this.resourceIdentifier = resourceIdentifier;
    }
    
    public String getResourceType() {
        return resourceType;
    }
    
    public Long getResourceId() {
        return resourceId;
    }
    
    public String getResourceIdentifier() {
        return resourceIdentifier;
    }
} 