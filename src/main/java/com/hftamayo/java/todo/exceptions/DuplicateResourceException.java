package com.hftamayo.java.todo.exceptions;

/**
 * Exception thrown when attempting to create a resource that already exists.
 * Maps to HTTP 409 Conflict status.
 */
public class DuplicateResourceException extends RuntimeException {
    
    private String resourceType;
    private Long resourceId;
    private String resourceIdentifier;
    private String field;
    private String value;
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String resourceType, Long resourceId) {
        super(String.format("%s with id %d already exists", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }
    
    public DuplicateResourceException(String resourceType, String resourceIdentifier) {
        super(String.format("%s with identifier %s already exists", resourceType, resourceIdentifier));
        this.resourceType = resourceType;
        this.resourceIdentifier = resourceIdentifier;
    }
    
    public DuplicateResourceException(String field, String value) {
        super(String.format("Resource with %s '%s' already exists", field, value));
        this.field = field;
        this.value = value;
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
    
    public String getField() {
        return field;
    }
    
    public String getValue() {
        return value;
    }
} 