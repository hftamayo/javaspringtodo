package com.hftamayo.java.todo.dto.roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolesResponseDto {
    private Long id;
    private String roleName;
    private String roleDescription;
    private boolean status;
    private String dateAdded;
}
