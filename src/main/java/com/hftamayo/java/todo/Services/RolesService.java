package com.hftamayo.java.todo.Services;

import com.hftamayo.java.todo.Model.Roles;

import java.util.List;

public interface RolesService {
    List<Roles> getRoles();
    Roles getRoleByName(String name);

    Roles saveRole(Roles newRole);
    Roles updateRole(long roleId, Roles updatedRole);
    void deleteRole(long roleId);
}
