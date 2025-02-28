package com.hftamayo.java.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class CrudOperationResponseDto<T> {
    private int code;
    private String resultMessage;
    private T data;
    private Map<String, T> dataMap;

    public CrudOperationResponseDto(int code, String resultMessage) {
        this.code = code;
        this.resultMessage = resultMessage;
    }

    public CrudOperationResponseDto(int code, String resultMessage, T data) {
        this.code = code;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    public CrudOperationResponseDto(int code, String resultMessage, Map<String, T> dataMap) {
        this.code = code;
        this.resultMessage = resultMessage;
        this.dataMap = dataMap;
    }
}