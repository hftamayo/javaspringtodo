package com.hftamayo.java.todo.utilities.endpoints;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.error.ErrorResponseDto;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ResponseUtil {
    /**
     * Formats an error response for the frontend and logging pipeline.
     * @param status HTTP status to use
     * @param message High-level error message
     * @param e The exception caught
     * @return EndpointResponseDto containing an ErrorResponseDto
     */
    public static EndpointResponseDto<ErrorResponseDto> errorResponse(HttpStatus status, String message, Exception e) {
        ErrorResponseDto error = new ErrorResponseDto(
            LocalDateTime.now(ZoneOffset.UTC),
            status,
            message,
            e.getMessage()
        );
        return new EndpointResponseDto<>(status.value(), message, error);
    }

    /**
     * Formats a success response with default message and status 200.
     */
    public static <T> EndpointResponseDto<T> successResponse(T data) {
        return new EndpointResponseDto<>(200, "SUCCESS", data);
    }

    /**
     * Formats a success response with a custom message and status 200.
     */
    public static <T> EndpointResponseDto<T> successResponse(T data, String message) {
        return new EndpointResponseDto<>(200, message, data);
    }

    /**
     * Formats a success response with a custom status and message.
     */
    public static <T> EndpointResponseDto<T> successResponse(T data, int code, String message) {
        return new EndpointResponseDto<>(code, message, data);
    }

    /**
     * Formats a created (201) response with a custom message.
     */
    public static <T> EndpointResponseDto<T> createdResponse(T data, String message) {
        return new EndpointResponseDto<>(201, message, data);
    }

    // Future: Add methods for paginated or list responses here
} 