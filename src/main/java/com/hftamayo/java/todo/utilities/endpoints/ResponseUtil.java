package com.hftamayo.java.todo.utilities.endpoints;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.ErrorResponseDto;
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

    // Future: Add methods for success, paginated, or custom responses here
} 