package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.services.RolesService;
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

    @Override
    public List<RolesResponseDto> getRoles()
    {
        List<Roles> rolesList = rolesRepository.findAll();
        return rolesList.stream().map(roleMapper::toRolesResponseDto).toList();
    }

    @Override
    public Optional<RolesResponseDto> getRoleByEnum(String roleEnum) {
        ERole eRole = ERole.valueOf(roleEnum);
        Optional<Roles> roleOptional = rolesRepository.findByRoleEnum(eRole);
        if(roleOptional.isPresent()){
            Roles role = roleOptional.get();
            RolesResponseDto dto = roleMapper.toRolesResponseDto(role);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Roles> getRoleById(long roleId) {
        return rolesRepository.findRolesById(roleId);
    }

    @Transactional
    @Override
    public RolesResponseDto saveRole(Roles newRole) {
        Optional<RolesResponseDto> requestedRole = getRoleByEnum(newRole.getRoleEnum().toString());
        if (!requestedRole.isPresent()) {
            Roles savedRole = rolesRepository.save(newRole);
            RolesResponseDto dto = roleMapper.toRolesResponseDto(savedRole);
            return dto;
        } else {
            throw new EntityAlreadyExistsException("Role already exists");
        }
    }

    @Transactional
    @Override
    public RolesResponseDto updateRole(long roleId, Roles updatedRole) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            Roles existingRole = requestedRoleOptional.get();
            existingRole.setRoleEnum(updatedRole.getRoleEnum());
            existingRole.setDescription(updatedRole.getDescription());
            existingRole.setStatus(updatedRole.isStatus());
            Roles savedRole = rolesRepository.save(existingRole);
            return roleMapper.toRolesResponseDto(savedRole);
        } else {
            throw new EntityNotFoundException("Role not found");
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
            return new CrudOperationResponseDto(404, "ROLE NOT FOUND");
        }
    }
}
