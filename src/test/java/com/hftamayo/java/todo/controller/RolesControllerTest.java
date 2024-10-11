package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.model.Roles;
import com.hftamayo.java.todo.services.RolesService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RolesControllerTest {

    private RolesController rolesController;
    private RolesService rolesService;

    @BeforeEach
    public void setUp() {
        rolesService = Mockito.mock(RolesService.class);
        rolesController = new RolesController(rolesService);
    }

    @Test
    public void testGetRoles() {
        RolesResponseDto role = new RolesResponseDto();
        when(rolesService.getRoles()).thenReturn(Collections.singletonList(role));

        List<RolesResponseDto> roles = rolesController.getRoles();
        assertEquals(1, roles.size());
        verify(rolesService, times(1)).getRoles();
    }

    @Test
    public void testGetRoleByName() {
        RolesResponseDto role = new RolesResponseDto();
        when(rolesService.getRoleByEnum("ROLE_USER")).thenReturn(Optional.of(role));

        Optional<RolesResponseDto> result = rolesController.getRoleByName("ROLE_USER");
        assertEquals(true, result.isPresent());
        verify(rolesService, times(1)).getRoleByEnum("ROLE_USER");
    }

    @Test
    public void testSaveRole() {
        Roles role = new Roles();
        RolesResponseDto roleResponse = new RolesResponseDto();
        when(rolesService.saveRole(role)).thenReturn(roleResponse);

        RolesResponseDto result = rolesController.saveRole(role);
        assertEquals(roleResponse, result);
        verify(rolesService, times(1)).saveRole(role);
    }

    @Test
    public void testUpdateRole_Success() {
        Roles role = new Roles();
        RolesResponseDto roleResponse = new RolesResponseDto();
        when(rolesService.updateRole(1L, role)).thenReturn(roleResponse);

        RolesResponseDto result = rolesController.updateRole(1L, role);
        assertEquals(roleResponse, result);
        verify(rolesService, times(1)).updateRole(1L, role);
    }

    @Test
    public void testUpdateRole_NotFound() {
        Roles role = new Roles();
        when(rolesService.updateRole(1L, role)).thenThrow(new EntityNotFoundException());

        assertThrows(ResponseStatusException.class, () -> rolesController.updateRole(1L, role));
        verify(rolesService, times(1)).updateRole(1L, role);
    }

    @Test
    public void testDeleteRole_Success() {
        doNothing().when(rolesService).deleteRole(1L);

        ResponseEntity<?> response = rolesController.deleteRole(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rolesService, times(1)).deleteRole(1L);
    }

    @Test
    public void testDeleteRole_NotFound() {
        doThrow(new EntityNotFoundException()).when(rolesService).deleteRole(1L);

        ResponseEntity<?> response = rolesController.deleteRole(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(rolesService, times(1)).deleteRole(1L);
    }

    @Test
    public void testDeleteRole_InternalServerError() {
        doThrow(new RuntimeException()).when(rolesService).deleteRole(1L);

        ResponseEntity<?> response = rolesController.deleteRole(1L);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(rolesService, times(1)).deleteRole(1L);
    }
}