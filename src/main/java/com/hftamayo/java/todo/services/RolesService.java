package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    CrudOperationResponseDto<RolesResponseDto> getRoles();
    CrudOperationResponseDto<RolesResponseDto> getRoleByName(String name);
    PaginatedDataDto<RolesResponseDto> getPaginatedRoles(PageRequestDto pageRequestDto);

    CrudOperationResponseDto<RolesResponseDto> saveRole(Roles newRole);
    CrudOperationResponseDto<RolesResponseDto> updateRole(long roleId, Roles updatedRole);
    CrudOperationResponseDto deleteRole(long roleId);
}
