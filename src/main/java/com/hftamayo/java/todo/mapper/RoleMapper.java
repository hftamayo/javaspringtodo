package com.hftamayo.java.todo.mapper;

import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RoleMapper {
    public RolesResponseDto toRolesResponseDto(Roles roles) {
        return new RolesResponseDto(
                roles.getId(),
                roles.getRoleEnum().name(),
                roles.getDescription(),
                roles.isStatus(),
                roles.getDateAdded().toString()
        );
    }

public Roles toEntity(RolesResponseDto rolesResponseDto) {
        Roles roles = new Roles();
        roles.setId(rolesResponseDto.getId());
        roles.setRoleEnum(ERole.valueOf(rolesResponseDto.getRoleName()));
        roles.setDescription(rolesResponseDto.getRoleDescription());
        roles.setStatus(rolesResponseDto.isStatus());
        roles.setDateAdded(LocalDateTime.parse(rolesResponseDto.getDateAdded()));
        return roles;
    }
}
