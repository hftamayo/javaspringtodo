package com.hftamayo.java.todo.dto;


import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ActiveSessionResponseDto {
    private String accessToken;
    private String tokenType;
    private boolean isTokenValid;
    private Date expiresAt;
    private long expiresIn;

    public ActiveSessionResponseDto(String accessToken, String tokenType, long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
