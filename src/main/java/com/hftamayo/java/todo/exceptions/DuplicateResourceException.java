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

    private DuplicateResourceException(String message, String resourceType, String resourceIdentifier, String field, String value) {
        super(message);
        this.resourceType = resourceType;
        this.resourceIdentifier = resourceIdentifier;
        this.field = field;
        this.value = value;
    }

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, Long resourceId) {
        super(String.format("%s with id %d already exists", resourceType, resourceId));
        this.resourceType = resourceType;
        this.resourceId = resourceId;
    }

    public static DuplicateResourceException withIdentifier(String resourceType, String resourceIdentifier) {
        return new DuplicateResourceException(
                String.format("%s with identifier %s already exists", resourceType, resourceIdentifier),
                resourceType,
                resourceIdentifier,
                null,
                null
        );
    }

    public static DuplicateResourceException withFieldValue(String field, String value) {
        return new DuplicateResourceException(
                String.format("Resource with %s '%s' already exists", field, value),
                null,
                null,
                field,
                value
        );
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