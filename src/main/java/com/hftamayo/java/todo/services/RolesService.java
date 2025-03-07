package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    CrudOperationResponseDto<UserResponseDto> getRoles();
    CrudOperationResponseDto<UserResponseDto> getRoleByName(String name);

    CrudOperationResponseDto<UserResponseDto> saveRole(Roles newRole);
    CrudOperationResponseDto<UserResponseDto> updateRole(long roleId, Roles updatedRole);
    CrudOperationResponseDto deleteRole(long roleId);
}
