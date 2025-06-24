package com.hftamayo.java.todo.security.interfaces;

import com.hftamayo.java.todo.entity.Roles;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class RoleGrantedAuthority implements GrantedAuthority {
    private final Roles role;

    @Override
    public String getAuthority() {
        return role.getRoleEnum().toString();
    }
}
