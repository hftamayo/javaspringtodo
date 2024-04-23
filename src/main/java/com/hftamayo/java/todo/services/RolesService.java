package com.hftamayo.java.todo.services;

import com.hftamayo.java.todo.model.Roles;

import java.util.List;
import java.util.Optional;

public interface RolesService {
    List<Roles> getRoles();
    Optional<Roles> getRoleByName(String name);

    Roles saveRole(Roles newRole);
    Roles updateRole(long roleId, Roles updatedRole);
    void deleteRole(long roleId);
}
