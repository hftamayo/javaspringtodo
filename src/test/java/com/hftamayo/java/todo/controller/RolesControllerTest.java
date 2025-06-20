package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
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
}