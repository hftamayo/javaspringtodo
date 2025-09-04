package com.hftamayo.java.todo.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EndpointResponseDto<T> {
    private int code;
    private String resultMessage;
    private T data;
    private List<T> dataList;

    public EndpointResponseDto(int code, String resultMessage) {
        this.code = code;
        this.resultMessage = resultMessage;
    }

    public EndpointResponseDto(int code, String resultMessage, T data) {
        this.code = code;
        this.resultMessage = resultMessage;
        this.data = data;
    }

    public EndpointResponseDto(int code, String resultMessage, List<T> dataList) {
        this.code = code;
        this.resultMessage = resultMessage;
        this.dataList = dataList;
    }
}