package com.hftamayo.java.todo.Services.Impl;

import com.hftamayo.java.todo.Model.Roles;
import com.hftamayo.java.todo.Repository.RolesRepository;
import com.hftamayo.java.todo.Services.RolesService;
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
        Optional<Roles> requestedRole = rolesRepository.findByName(newRole.getName());
        if(requestedRole == null){
            return rolesRepository.save(newRole);
        } else {
            throw new RuntimeException("Role already exists");
        }
    }

    @Override
    public Roles updateRole(long roleId, Roles updatedRole) {
        Roles requestedRole = rolesRepository.findById(roleId).get();
        if(requestedRole != null){
            requestedRole.setName(updatedRole.getName());
            requestedRole.setDescription(updatedRole.getDescription());
            requestedRole.setStatus(updatedRole.isStatus());
            return rolesRepository.save(requestedRole);
        } else {
            throw new RuntimeException("Role not found");
        }
    }

    @Override
    public void deleteRole(long roleId) {
        Roles requestedRole = rolesRepository.findById(roleId).get();
        if(requestedRole != null){
            rolesRepository.delete(requestedRole);
        } else {
            throw new RuntimeException("Role not found");
        }
    }
}
