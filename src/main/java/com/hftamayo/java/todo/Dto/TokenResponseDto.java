package com.hftamayo.java.todo.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class TokenResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresIn;
}
