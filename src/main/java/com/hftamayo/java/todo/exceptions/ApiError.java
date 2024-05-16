package com.hftamayo.java.todo.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(HttpStatus httpStatus, String message, List<String> error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus httpStatus, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return errors.toString();
    }
}
