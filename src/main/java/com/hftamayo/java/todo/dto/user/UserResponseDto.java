package com.hftamayo.java.todo.dto.user;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private int age;
    private boolean isAdmin;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean status;
    private String dateAdded;
    private String role;
}
