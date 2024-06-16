package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesService {
    @Autowired
    private RolesDao rolesDao;

    public List<Roles> getRoles() {
        return rolesDao.getRoles();
    }

    public Optional<Roles> getRoleByEnum(String roleEnum) {
        ERole eRole = ERole.valueOf(roleEnum);
        return rolesDao.getRoleByEnum(eRole.toString());
    }

    public Optional<Roles> getRoleById(long roleId) {
        return rolesDao.findById(roleId);
    }

    @Transactional
    @Override
    public Roles saveRole(Roles newRole) {
        Optional<Roles> requestedRole = getRoleByEnum(newRole.getRoleEnum().toString());
        if (!requestedRole.isPresent()) {
            return rolesDao.saveRole(newRole);
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
            return rolesDao.updateRole(roleId, requestedRole);
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

}
