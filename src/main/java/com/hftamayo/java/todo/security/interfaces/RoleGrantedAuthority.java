package com.hftamayo.java.todo.security.interfaces;

import com.hftamayo.java.todo.model.Roles;
import org.springframework.security.core.GrantedAuthority;

public class RoleGrantedAuthority implements GrantedAuthority {
    private final Roles role;

    public RoleGrantedAuthority(Roles role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getRoleEnum().toString();
    }
}
