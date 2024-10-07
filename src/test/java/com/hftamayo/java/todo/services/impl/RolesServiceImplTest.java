package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dao.RolesDao;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.exceptions.EntityAlreadyExistsException;
import com.hftamayo.java.todo.model.ERole;
import com.hftamayo.java.todo.model.Roles;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RolesServiceImplTest {

    private RolesServiceImpl rolesService;
    private RolesDao rolesDao;

    @BeforeEach
    public void setUp() {
        rolesDao = Mockito.mock(RolesDao.class);
        rolesService = new RolesServiceImpl();

        try {
            Field rolesDaoField = RolesServiceImpl.class
                    .getDeclaredField("rolesDao");
            rolesDaoField.setAccessible(true);
            rolesDaoField.set(rolesService, rolesDao);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetRoles() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoles()).thenReturn(Collections.singletonList(role));

        List<RolesResponseDto> roles = rolesService.getRoles();
        assertFalse(roles.isEmpty());
        assertEquals(1, roles.size());
        assertEquals("ADMIN", roles.get(0).getRoleName());
    }

    @Test
    public void testGetRoleByEnum() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoleByEnum("ADMIN")).thenReturn(Optional.of(role));

        Optional<RolesResponseDto> foundRole = rolesService.getRoleByEnum("ADMIN");
        assertTrue(foundRole.isPresent());
        assertEquals("ADMIN", foundRole.get().getRoleName());
    }

    @Test
    public void testSaveRole() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoleByEnum("ADMIN")).thenReturn(Optional.empty());
        when(rolesDao.saveRole(any(Roles.class))).thenReturn(role);

        RolesResponseDto savedRole = rolesService.saveRole(role);
        assertEquals("ADMIN", savedRole.getRoleName());
    }

    @Test
    public void testSaveRoleAlreadyExists() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoleByEnum("ADMIN")).thenReturn(Optional.of(role));

        assertThrows(EntityAlreadyExistsException.class, () -> rolesService.saveRole(role));
    }

    @Test
    public void testUpdateRole() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoleById(1L)).thenReturn(Optional.of(role));
        when(rolesDao.updateRole(anyLong(), anyMap())).thenReturn(role);

        Roles updatedRole = new Roles();
        updatedRole.setRoleEnum(ERole.ROLE_USER);
        updatedRole.setDescription("User role");
        updatedRole.setStatus(false);

        RolesResponseDto updatedRoleDto = rolesService.updateRole(1L, updatedRole);
        assertEquals("USER", updatedRoleDto.getRoleName());
        assertFalse(updatedRoleDto.isStatus());
    }

    @Test
    public void testUpdateRoleNotFound() {
        when(rolesDao.getRoleById(1L)).thenReturn(Optional.empty());

        Roles updatedRole = new Roles();
        updatedRole.setRoleEnum(ERole.ROLE_USER);
        updatedRole.setDescription("User role");
        updatedRole.setStatus(false);

        assertThrows(EntityNotFoundException.class, () -> rolesService.updateRole(1L, updatedRole));
    }

    @Test
    public void testDeleteRole() {
        Roles role = new Roles();
        role.setId(1L);
        role.setRoleEnum(ERole.ROLE_ADMIN);
        role.setDescription("Administrator role");
        role.setStatus(true);
        role.setDateAdded(LocalDateTime.now());

        when(rolesDao.getRoleById(1L)).thenReturn(Optional.of(role));
        doNothing().when(rolesDao).deleteRole(1L);

        rolesService.deleteRole(1L);
        verify(rolesDao, times(1)).deleteRole(1L);
    }

    @Test
    public void testDeleteRoleNotFound() {
        when(rolesDao.getRoleById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> rolesService.deleteRole(1L));
    }
}
