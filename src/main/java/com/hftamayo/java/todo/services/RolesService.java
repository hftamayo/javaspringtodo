package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    List<RolesResponseDto> getRoles();
    Optional<RolesResponseDto> getRoleByEnum(String roleEnum);

    RolesResponseDto saveRole(Roles newRole);
    RolesResponseDto updateRole(long roleId, Roles updatedRole);
    CrudOperationResponseDto deleteRole(long roleId);

    RolesResponseDto roleToDto(Roles role);
}
