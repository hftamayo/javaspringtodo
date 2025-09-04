package com.hftamayo.java.todo.utilities.ratelimit;

/**
 * Custom exception for rate limiting errors.
 * Extends RuntimeException to be unchecked and provides multiple constructors
 * for different exception scenarios.
 */
public class RateLimiterException extends RuntimeException {

    /**
     * Constructs a new RateLimiterException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     */
    public RateLimiterException(String message) {
        super(message);
    }

    /**
     * Constructs a new RateLimiterException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method)
     * @param cause The cause (which is saved for later retrieval by the getCause() method)
     */
    public RateLimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new RateLimiterException with the specified cause and a detail message
     * of (cause==null ? null : cause.toString()) (which typically contains the class and
     * detail message of cause).
     *
     * @param cause The cause (which is saved for later retrieval by the getCause() method)
     */
    public RateLimiterException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new RateLimiterException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message The detail message
     * @param cause The cause
     * @param enableSuppression Whether or not suppression is enabled or disabled
     * @param writableStackTrace Whether or not the stack trace should be writable
     */
    public RateLimiterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
} 