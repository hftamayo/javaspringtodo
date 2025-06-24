package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesRepository;
    private final RoleMapper roleMapper;

    //helper methods
    private Optional<Roles> getRoleById(long roleId) {
        return rolesRepository.findRolesById(roleId);
    }

    private Optional<Roles> getRoleByEnum(ERole roleEnum) {
        return rolesRepository.findByRoleEnum(roleEnum);
    }

    public static @NotNull Roles getExistingRole(Roles updatedRole, Optional<Roles> requestedRoleOptional) {
        Roles existingRole = requestedRoleOptional.get();

        if (updatedRole.getRoleEnum() != null) {
            existingRole.setRoleEnum(updatedRole.getRoleEnum());
        }
        if (updatedRole.getDescription() != null) {
            existingRole.setDescription(updatedRole.getDescription());
        }
        // Status is a primitive boolean, so it will always be updated
        existingRole.setStatus(updatedRole.isStatus());

        return existingRole;
    }

    @Override
    public CrudOperationResponseDto<RolesResponseDto> getRoles() {
        try {
            List<Roles> rolesList = rolesRepository.findAll();
            if (!rolesList.isEmpty()) {
                List<RolesResponseDto> rolesResponseDtoList = rolesList.stream().map(roleMapper::toRolesResponseDto).toList();
                return new CrudOperationResponseDto<>(200, "OPERATION SUCCESSFUL", rolesResponseDtoList);
            } else {
                return new CrudOperationResponseDto<>(404, "NO ROLES FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto<>(500, "INTERNAL SERVER ERROR");
        }
    }

    @Override
    public CrudOperationResponseDto<RolesResponseDto> getRoleByName(String name) {
        try {
            ERole eRole = ERole.valueOf(name);
            Optional<Roles> roleOptional = getRoleByEnum(eRole);
            if (roleOptional.isPresent()) {
                Roles role = roleOptional.get();
                RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(role);
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", roleToDto);
            } else {
                return new CrudOperationResponseDto(404, "ROLE NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }

    }

    @Transactional
    @Override
    public CrudOperationResponseDto<RolesResponseDto> saveRole(Roles newRole) {
        try {
            Optional<Roles> requestedRole = getRoleByEnum(newRole.getRoleEnum());
            if (!requestedRole.isPresent()) {
                Roles savedRole = rolesRepository.save(newRole);
                RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(savedRole);
                return new CrudOperationResponseDto(201, "OPERATION SUCCESSFUL", roleToDto);
            } else {
                return new CrudOperationResponseDto(400, "ROLE ALREADY EXISTS");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<RolesResponseDto> updateRole(long roleId, Roles updatedRole) {
        try {
            Optional<Roles> requestedRoleOptional = getRoleById(roleId);
            if (requestedRoleOptional.isPresent()) {
                Roles existingRole = getExistingRole(updatedRole, requestedRoleOptional);

                Roles savedRole = rolesRepository.save(existingRole);
                RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(savedRole);
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", roleToDto);
            } else {
                throw new EntityNotFoundException("Role not found");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto deleteRole(long roleId) {
        try {
            Optional<Roles> requestedRoleOptional = getRoleById(roleId);
            if (requestedRoleOptional.isPresent()) {
                rolesRepository.deleteRolesById(requestedRoleOptional.get().getId());
                return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL");
            } else {
                return new CrudOperationResponseDto(404, "ROLE NOT FOUND");
            }
        } catch (Exception e) {
            return new CrudOperationResponseDto(500, "INTERNAL SERVER ERROR");
        }
    }
}
