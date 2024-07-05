package com.hftamayo.java.todo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserResponseDto {
    private Long id;
    private String name;
    private String email;
    private int age;
    private boolean isAdmin;
    private boolean status;
}
