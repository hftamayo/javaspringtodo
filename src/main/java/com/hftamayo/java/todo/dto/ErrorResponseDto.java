package com.hftamayo.java.todo.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor

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

    // Constructor adicional para un solo mensaje de error
    public ErrorResponseDto(LocalDateTime timestamp, HttpStatus status, String message, String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.errors = List.of(error);
    }
} 