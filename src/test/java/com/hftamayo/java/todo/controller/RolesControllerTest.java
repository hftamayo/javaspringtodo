package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.ValidationException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.services.RolesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @Mock
    private RolesService rolesService;

    @InjectMocks
    private RolesController rolesController;

    @Test
    void getRoles_WhenRolesExist_ShouldReturnSuccessResponse() {
        CrudOperationResponseDto<RolesResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setDataList(List.of(new RolesResponseDto()));

        when(rolesService.getRoles()).thenReturn(expectedResponse);

        CrudOperationResponseDto<RolesResponseDto> response = rolesController.getRoles();

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertEquals(1, response.getDataList().size()),
                () -> verify(rolesService).getRoles()
        );
    }

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnSuccessResponse() {
        String roleName = "ROLE_ADMIN";
        CrudOperationResponseDto<RolesResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new RolesResponseDto());

        when(rolesService.getRoleByName(roleName)).thenReturn(expectedResponse);

        CrudOperationResponseDto<RolesResponseDto> response = rolesController.getRoleByName(roleName);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(rolesService).getRoleByName(roleName)
        );
    }

    @Test
    void getRoleByName_WhenRoleNotFound_ShouldThrowResourceNotFoundException() {
        String roleName = "ROLE_INVALID";
        when(rolesService.getRoleByName(roleName)).thenThrow(new ResourceNotFoundException("Role not found: " + roleName));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> rolesController.getRoleByName(roleName));
        
        assertEquals("Role not found: ROLE_INVALID", exception.getMessage());
        verify(rolesService).getRoleByName(roleName);
    }

    @Test
    void saveRole_WhenValidRole_ShouldReturnSuccessResponse() {
        Roles role = new Roles();
        CrudOperationResponseDto<RolesResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(201);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new RolesResponseDto());

        when(rolesService.saveRole(role)).thenReturn(expectedResponse);

        CrudOperationResponseDto<RolesResponseDto> response = rolesController.saveRole(role);

        assertAll(
                () -> assertEquals(201, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(rolesService).saveRole(role)
        );
    }

    @Test
    void saveRole_WhenInvalidRole_ShouldThrowValidationException() {
        Roles role = new Roles();
        when(rolesService.saveRole(role)).thenThrow(new ValidationException("Role name is required"));

        ValidationException exception = assertThrows(ValidationException.class, 
            () -> rolesController.saveRole(role));
        
        assertEquals("Role name is required", exception.getMessage());
        verify(rolesService).saveRole(role);
    }

    @Test
    void saveRole_WhenDuplicateRole_ShouldThrowDuplicateResourceException() {
        Roles role = new Roles();
        role.setName("ROLE_ADMIN");
        when(rolesService.saveRole(role)).thenThrow(new DuplicateResourceException("Role already exists"));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, 
            () -> rolesController.saveRole(role));
        
        assertEquals("Role already exists", exception.getMessage());
        verify(rolesService).saveRole(role);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldReturnSuccessResponse() {
        long roleId = 1L;
        Roles role = new Roles();
        CrudOperationResponseDto<RolesResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");
        expectedResponse.setData(new RolesResponseDto());

        when(rolesService.updateRole(roleId, role)).thenReturn(expectedResponse);

        CrudOperationResponseDto<RolesResponseDto> response = rolesController.updateRole(roleId, role);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> assertNotNull(response.getData()),
                () -> verify(rolesService).updateRole(roleId, role)
        );
    }

    @Test
    void updateRole_WhenRoleNotFound_ShouldThrowResourceNotFoundException() {
        long roleId = 999L;
        Roles role = new Roles();
        when(rolesService.updateRole(roleId, role)).thenThrow(new ResourceNotFoundException("Role not found with id: " + roleId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> rolesController.updateRole(roleId, role));
        
        assertEquals("Role not found with id: 999", exception.getMessage());
        verify(rolesService).updateRole(roleId, role);
    }

    @Test
    void deleteRole_WhenRoleExists_ShouldReturnSuccessResponse() {
        long roleId = 1L;
        CrudOperationResponseDto<RolesResponseDto> expectedResponse = new CrudOperationResponseDto<>();
        expectedResponse.setCode(200);
        expectedResponse.setResultMessage("OPERATION SUCCESSFUL");

        when(rolesService.deleteRole(roleId)).thenReturn(expectedResponse);

        CrudOperationResponseDto<RolesResponseDto> response = rolesController.deleteRole(roleId);

        assertAll(
                () -> assertEquals(200, response.getCode()),
                () -> assertEquals("OPERATION SUCCESSFUL", response.getResultMessage()),
                () -> verify(rolesService).deleteRole(roleId)
        );
    }

    @Test
    void deleteRole_WhenRoleNotFound_ShouldThrowResourceNotFoundException() {
        long roleId = 999L;
        when(rolesService.deleteRole(roleId)).thenThrow(new ResourceNotFoundException("Role not found with id: " + roleId));

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, 
            () -> rolesController.deleteRole(roleId));
        
        assertEquals("Role not found with id: 999", exception.getMessage());
        verify(rolesService).deleteRole(roleId);
    }
}