package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.model.User;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public Roles saveRole(Roles newRole) {
        Optional<Roles> requestedRole = rolesRepository.findByRoleEnum(newRole.getRoleEnum());
        if (requestedRole == null) {
            return rolesRepository.save(newRole);
        } else {
            throw new RuntimeException("Role already exists");
        }
    }

    @Override
    public Roles updateRole(long roleId, Roles updatedRole) {
        Optional<Roles> requestedRoleOptional = rolesRepository.findById(roleId);
        if (requestedRoleOptional.isPresent()) {
            Roles requestedRole = requestedRoleOptional.get();
            requestedRole.setRoleEnum(updatedRole.getRoleEnum());
            requestedRole.setDescription(updatedRole.getDescription());
            requestedRole.setStatus(updatedRole.isStatus());
            return rolesRepository.save(requestedRole);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    @Override
    public void deleteRole(long roleId) {
        Optional<Roles> requestedRoleOptional = rolesRepository.findById(roleId);
        if (requestedRoleOptional.isPresent()) {
            rolesRepository.delete(requestedRoleOptional.get());
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    @Transactional
    @Override
    public void addRoleToUser(User user, String roleEnum) {
        Optional<Roles> roleOptional = getRoleByEnum(roleEnum);
        if (!roleOptional.isPresent()) {
            throw new EntityNotFoundException("Role not found");
        }
        user.getRoles().add(roleOptional.get());
    }
}
