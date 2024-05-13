package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.services.RolesService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImpl implements RolesService {
    private final RolesRepository rolesRepository;

    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<Roles> getRoles(){
        return rolesRepository.findAll();
    }

    public Optional<Roles> getRoleByName(String name){
        return rolesRepository.findByName(name);
    }

    @Override
    public Roles saveRole(Roles newRole) {
        Optional<Roles> requestedRole = rolesRepository.findByName(newRole.getRoleEnum().toString());
        if(requestedRole == null){
            return rolesRepository.save(newRole);
        } else {
            throw new RuntimeException("Role already exists");
        }
    }

    @Override
    public Roles updateRole(long roleId, Roles updatedRole) {
        Optional<Roles> requestedRoleOptional = rolesRepository.findById(roleId);
        if(requestedRoleOptional.isPresent()){
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
        if(requestedRoleOptional.isPresent()){
            rolesRepository.delete(requestedRoleOptional.get());
        } else {
            throw new RuntimeException("Role not found");
        }
    }
}
