package com.hftamayo.java.todo.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponseDto {
    
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;

} 