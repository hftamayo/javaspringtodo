package com.hftamayo.java.todo.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    private String email;
    private String password;
}
