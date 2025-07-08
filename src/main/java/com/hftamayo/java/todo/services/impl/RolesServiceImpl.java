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
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        List<Roles> rolesList = rolesRepository.findAll();
        if (!rolesList.isEmpty()) {
            List<RolesResponseDto> rolesResponseDtoList = rolesList.stream().map(roleMapper::toRolesResponseDto).toList();
            return new CrudOperationResponseDto<>(200, "OPERATION SUCCESSFUL", rolesResponseDtoList);
        } else {
            throw new ResourceNotFoundException("Role", "all");
        }
    }

    @Override
    public CrudOperationResponseDto<RolesResponseDto> getRoleByName(String name) {
        ERole eRole = ERole.valueOf(name);
        Optional<Roles> roleOptional = getRoleByEnum(eRole);
        if (roleOptional.isPresent()) {
            Roles role = roleOptional.get();
            RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(role);
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", roleToDto);
        } else {
            throw new ResourceNotFoundException("Role", name);
        }
    }

    @Override
    public PageResponseDto<RolesResponseDto> getPaginatedRoles(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(
            pageRequestDto.getPage(),
            pageRequestDto.getSize()
        );
        Page<Roles> rolesPage = rolesRepository.findAll(pageable);
        List<RolesResponseDto> content = rolesPage.getContent().stream()
            .map(roleMapper::toRolesResponseDto)
            .toList();
        return new PageResponseDto<>(
            content,
            rolesPage.getNumber(),
            rolesPage.getSize(),
            rolesPage.getTotalElements(),
            rolesPage.getTotalPages(),
            rolesPage.isLast()
        );
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<RolesResponseDto> saveRole(Roles newRole) {
        Optional<Roles> requestedRole = getRoleByEnum(newRole.getRoleEnum());
        if (!requestedRole.isPresent()) {
            Roles savedRole = rolesRepository.save(newRole);
            RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(savedRole);
            return new CrudOperationResponseDto(201, "OPERATION SUCCESSFUL", roleToDto);
        } else {
            throw DuplicateResourceException.withIdentifier("Role", newRole.getRoleEnum().name());
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto<RolesResponseDto> updateRole(long roleId, Roles updatedRole) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            Roles existingRole = getExistingRole(updatedRole, requestedRoleOptional);
            Roles savedRole = rolesRepository.save(existingRole);
            RolesResponseDto roleToDto = roleMapper.toRolesResponseDto(savedRole);
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL", roleToDto);
        } else {
            throw new ResourceNotFoundException("Role", roleId);
        }
    }

    @Transactional
    @Override
    public CrudOperationResponseDto deleteRole(long roleId) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            rolesRepository.deleteRolesById(requestedRoleOptional.get().getId());
            return new CrudOperationResponseDto(200, "OPERATION SUCCESSFUL");
        } else {
            throw new ResourceNotFoundException("Role", roleId);
        }
    }
}
