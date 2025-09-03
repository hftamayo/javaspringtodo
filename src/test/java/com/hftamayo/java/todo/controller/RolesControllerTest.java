package com.hftamayo.java.todo.controller;

import com.hftamayo.java.todo.dto.EndpointResponseDto;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.services.RolesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.hftamayo.java.todo.entity.ERole.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolesControllerTest {

    @Mock
    private RolesService rolesService;

    @InjectMocks
    private RolesController rolesController;

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnSuccessResponse() {
        // Arrange
        String roleName = "ROLE_ADMIN";
        RolesResponseDto roleResponse = new RolesResponseDto();
        roleResponse.setRoleName(roleName);
        roleResponse.setRoleDescription("Administrator role");

        when(rolesService.getRoleByName(roleName)).thenReturn(roleResponse);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.getRoleByName(roleName);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(rolesService).getRoleByName(roleName)
        );
    }

    @Test
    void getRoleByName_WhenRoleNotFound_ShouldReturnNotFoundResponse() {
        // Arrange
        String roleName = "ROLE_INVALID";
        when(rolesService.getRoleByName(roleName)).thenThrow(new ResourceNotFoundException("Role not found: " + roleName));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.getRoleByName(roleName);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Role not found", response.getBody().getResultMessage()),
                () -> assertEquals(404, response.getBody().getCode()),
                () -> verify(rolesService).getRoleByName(roleName)
        );
    }

    @Test
    void saveRole_WhenValidRole_ShouldReturnCreatedResponse() {
        // Arrange
        Roles role = new Roles();
        role.setRoleEnum(ROLE_USER);
        role.setDescription("Test role");

        RolesResponseDto savedRole = new RolesResponseDto();
        savedRole.setRoleName("ROLE_TEST");
        savedRole.setRoleDescription("Test role");

        when(rolesService.saveRole(role)).thenReturn(savedRole);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.saveRole(role);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("ROLE_CREATED", response.getBody().getResultMessage()),
                () -> assertEquals(201, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(rolesService).saveRole(role)
        );
    }

    @Test
    void saveRole_WhenInvalidRole_ShouldReturnBadRequestResponse() {
        // Arrange
        Roles role = new Roles();
        // Invalid role without required fields

        when(rolesService.saveRole(role)).thenThrow(new RuntimeException("Invalid role data"));

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.saveRole(role);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Failed to create role", response.getBody().getResultMessage()),
                () -> assertEquals(400, response.getBody().getCode()),
                () -> verify(rolesService).saveRole(role)
        );
    }

    @Test
    void getRoles_WhenRolesExist_ShouldReturnSuccessResponse() {
        // Arrange
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<RolesResponseDto> paginatedData = getRolesResponseDtoPaginatedDataDto();

        when(rolesService.getPaginatedRoles(any(PageRequestDto.class))).thenReturn(paginatedData);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.getRoles(page, size, sort);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(rolesService).getPaginatedRoles(any(PageRequestDto.class))
        );
    }

    @Test
    void getRoles_WhenNoRolesExist_ShouldReturnEmptyPaginatedResponse() {
        // Arrange
        int page = 0;
        int size = 2;
        String sort = null;

        PaginatedDataDto<RolesResponseDto> emptyPaginatedData = new PaginatedDataDto<>();
        emptyPaginatedData.setContent(List.of());
        emptyPaginatedData.setTotalElements(0L);
        emptyPaginatedData.setTotalPages(0);
        emptyPaginatedData.setSize(2);
        emptyPaginatedData.setNumber(0);
        emptyPaginatedData.setFirst(true);
        emptyPaginatedData.setLast(true);
        emptyPaginatedData.setNumberOfElements(0);

        when(rolesService.getPaginatedRoles(any(PageRequestDto.class))).thenReturn(emptyPaginatedData);

        // Act
        ResponseEntity<EndpointResponseDto<?>> response = rolesController.getRoles(page, size, sort);

        // Assert
        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("OPERATION_SUCCESS", response.getBody().getResultMessage()),
                () -> assertEquals(200, response.getBody().getCode()),
                () -> assertNotNull(response.getBody().getData()),
                () -> verify(rolesService).getPaginatedRoles(any(PageRequestDto.class))
        );
    }

    // Helper method
    private PaginatedDataDto<RolesResponseDto> getRolesResponseDtoPaginatedDataDto() {
        RolesResponseDto roleResponse = new RolesResponseDto();
        roleResponse.setName("ROLE_USER");
        roleResponse.setDescription("User role");

        PaginatedDataDto<RolesResponseDto> paginatedData = new PaginatedDataDto<>();
        paginatedData.setContent(List.of(roleResponse));
        paginatedData.setTotalElements(1L);
        paginatedData.setTotalPages(1);
        paginatedData.setSize(2);
        paginatedData.setNumber(0);
        paginatedData.setFirst(true);
        paginatedData.setLast(true);
        paginatedData.setNumberOfElements(1);

        return paginatedData;
    }
}