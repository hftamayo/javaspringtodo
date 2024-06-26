package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;

    public List<Roles> getRoles() {
        return rolesRepository.findAll();
    }

    public Optional<Roles> getRoleByEnum(String roleEnum) {
        ERole eRole = ERole.valueOf(roleEnum);
        return rolesRepository.findByRoleEnum(eRole);
    }

    public Optional<Roles> getRoleById(long roleId) {
        return rolesRepository.findById(roleId);
    }

    @Transactional
    @Override
    public Roles saveRole(Roles newRole) {
        Optional<Roles> requestedRole = getRoleByEnum(newRole.getRoleEnum().toString());
        if (!requestedRole.isPresent()) {
            return rolesRepository.save(newRole);
        } else {
            throw new EntityAlreadyExistsException("Role already exists");
        }
    }

    @Transactional
    @Override
    public Roles updateRole(long roleId, Roles updatedRole) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            Roles requestedRole = requestedRoleOptional.get();
            requestedRole.setRoleEnum(updatedRole.getRoleEnum());
            requestedRole.setDescription(updatedRole.getDescription());
            requestedRole.setStatus(updatedRole.isStatus());
            return rolesRepository.save(requestedRole);
        } else {
            throw new EntityNotFoundException("Role not found");
        }
    }

    @Transactional
    @Override
    public void deleteRole(long roleId) {
        Optional<Roles> requestedRoleOptional = getRoleById(roleId);
        if (requestedRoleOptional.isPresent()) {
            rolesRepository.delete(requestedRoleOptional.get());
        } else {
            throw new EntityNotFoundException("Role not found");
        }
    }

}
