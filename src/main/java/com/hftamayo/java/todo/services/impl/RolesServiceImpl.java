package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.services.RolesService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {

    private final RolesDao rolesDao;

    public List<RolesResponseDto> getRoles()
    {
        List<Roles> rolesList = rolesDao.getRoles();
        return rolesList.stream().map(this::roleToDto).toList();
    }

    public Optional<RolesResponseDto> getRoleByEnum(String roleEnum) {
        ERole eRole = ERole.valueOf(roleEnum);
        Optional<Roles> roleOptional = rolesDao.getRoleByEnum(eRole.toString());
        if(roleOptional.isPresent()){
            Roles role = roleOptional.get();
            RolesResponseDto dto = roleToDto(role);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }

    public Optional<Roles> getRoleById(long roleId) {
        return rolesDao.getRoleById(roleId);
    }

    @Transactional
    @Override
    public RolesResponseDto saveRole(Roles newRole) {
        Optional<RolesResponseDto> requestedRole = getRoleByEnum(newRole.getRoleEnum().toString());
        if (!requestedRole.isPresent()) {
            Roles savedRole = rolesDao.saveRole(newRole);
            RolesResponseDto dto = roleToDto(savedRole);
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
            Map<String, Object> propertiesToUpdate = new HashMap<>();
            propertiesToUpdate.put("roleEnum", updatedRole.getRoleEnum());
            propertiesToUpdate.put("description", updatedRole.getDescription());
            propertiesToUpdate.put("status", updatedRole.isStatus());
            Roles role = rolesDao.updateRole(roleId, propertiesToUpdate);
            return roleToDto(role);
        } else {
            throw new EntityNotFoundException("Role not found");
        }
    }

    @Transactional
    @Override
    public void deleteRole(long roleId) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            rolesDao.deleteRole(requestedRoleOptional.get().getId());
        } else {
            throw new EntityNotFoundException("Role not found");
        }
    }

    @Override
    public RolesResponseDto roleToDto(Roles role) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = role.getDateAdded().format(formatter);
        String roleName = role.getRoleEnum().name();

        return new RolesResponseDto(
                role.getId(),
                roleName,
                role.getDescription(),
                role.isStatus(),
                formattedDate
        );
    }
}
