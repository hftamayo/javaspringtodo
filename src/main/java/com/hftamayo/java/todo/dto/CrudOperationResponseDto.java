package com.hftamayo.java.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class CrudOperationResponseDto {
    private int code;
    private String resultMessage;
    private Object data;

    public CrudOperationResponseDto(int code, String resultMessage) {
        this.code = code;
        this.resultMessage = resultMessage;
    }
}
