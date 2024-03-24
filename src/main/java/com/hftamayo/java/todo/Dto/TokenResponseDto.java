package com.hftamayo.java.todo.Dto;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TokenResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
