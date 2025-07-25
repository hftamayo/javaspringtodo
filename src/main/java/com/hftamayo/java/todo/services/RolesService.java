package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.entity.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    List<RolesResponseDto> getRoles();
    RolesResponseDto getRoleByName(String name);
    PaginatedDataDto<RolesResponseDto> getPaginatedRoles(PageRequestDto pageRequestDto);

    RolesResponseDto saveRole(Roles newRole);
    RolesResponseDto updateRole(long roleId, Roles updatedRole);
    void deleteRole(long roleId);
}
