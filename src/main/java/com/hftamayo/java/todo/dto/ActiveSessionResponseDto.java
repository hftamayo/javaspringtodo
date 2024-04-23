package com.hftamayo.java.todo.dto;


import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ActiveSessionResponseDto {
    private String username;
    private String email;
    private List<String> roles;
    private String accessToken;
    private String tokenType;
    private boolean isTokenValid;
    private Date expiresAt;
    private long expiresIn;

    public ActiveSessionResponseDto(String username, String email, List<String> roles,
                                    String accessToken, String tokenType, long expiresIn) {
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
}
