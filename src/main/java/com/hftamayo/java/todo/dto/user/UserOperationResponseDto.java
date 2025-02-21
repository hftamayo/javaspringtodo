package com.hftamayo.java.todo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserOperationResponseDto {
    private int code;
    private String resultMessage;
    private UserResponseDto user;

    public UserOperationResponseDto(int code, String resultMessage) {
        this.code = code;
        this.resultMessage = resultMessage;
    }
}
