package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.model.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    List<RolesResponseDto> getRoles();
    Optional<RolesResponseDto> getRoleByEnum(String roleEnum);

    RolesResponseDto saveRole(Roles newRole);
    Roles updateRole(long roleId, Roles updatedRole);
    void deleteRole(long roleId);

    RolesResponseDto roleToDto(Roles role);
}
