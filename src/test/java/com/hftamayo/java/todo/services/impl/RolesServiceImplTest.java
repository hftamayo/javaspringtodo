package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.CrudOperationResponseDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolesServiceImplTest {

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RolesServiceImpl rolesService;

    @Test
    void getRoles_WhenRolesExist_ShouldReturnSuccessResponse() {
        List<Roles> rolesList = List.of(
                createRole(1L, ERole.ROLE_ADMIN),
                createRole(2L, ERole.ROLE_USER)
        );
        List<RolesResponseDto> responseDtos = List.of(
                createRoleDto(1L, "ADMIN"),
                createRoleDto(2L, "USER")
        );

        when(rolesRepository.findAll()).thenReturn(rolesList);
        when(roleMapper.toRolesResponseDto(rolesList.get(0))).thenReturn(responseDtos.get(0));
        when(roleMapper.toRolesResponseDto(rolesList.get(1))).thenReturn(responseDtos.get(1));

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.getRoles();

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(2, result.getDataList().size());
        verify(rolesRepository).findAll();
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnSuccessResponse() {
        Roles role = createRole(1L, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(1L, "ADMIN");

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(roleMapper.toRolesResponseDto(role)).thenReturn(responseDto);

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.getRoleByName("ROLE_ADMIN");

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals("ADMIN", ((RolesResponseDto)result.getData()).getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(roleMapper).toRolesResponseDto(role);
    }

    @Test
    void getRoleByName_WhenRoleDoesNotExist_ShouldReturnNotFoundResponse() {
        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.getRoleByName("ROLE_ADMIN");

        assertEquals(404, result.getCode());
        assertEquals("ROLE NOT FOUND", result.getResultMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void getRoleByName_WhenRoleNameIsInvalid_ShouldReturnErrorResponse() {
        CrudOperationResponseDto<RolesResponseDto> result = rolesService.getRoleByName("INVALID_ROLE");

        assertEquals(500, result.getCode());
        assertEquals("INTERNAL SERVER ERROR", result.getResultMessage());
        verifyNoInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void saveRole_WhenRoleDoesNotExist_ShouldReturnSuccessResponse() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(1L, "ADMIN");

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(rolesRepository.save(newRole)).thenReturn(newRole);
        when(roleMapper.toRolesResponseDto(newRole)).thenReturn(responseDto);

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.saveRole(newRole);

        assertEquals(201, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals("ADMIN", ((RolesResponseDto)result.getData()).getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(rolesRepository).save(newRole);
        verify(roleMapper).toRolesResponseDto(newRole);
    }

    @Test
    void saveRole_WhenRoleExists_ShouldReturnErrorResponse() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.of(newRole));

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.saveRole(newRole);

        assertEquals(400, result.getCode());
        assertEquals("ROLE ALREADY EXISTS", result.getResultMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verifyNoMoreInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldReturnSuccessResponse() {
        Roles existingRole = createRole(1L, ERole.ROLE_ADMIN);
        Roles updatedRole = createRole(1L, ERole.ROLE_USER);
        RolesResponseDto responseDto = createRoleDto(1L, "USER");

        when(rolesRepository.findRolesById(1L)).thenReturn(Optional.of(existingRole));
        when(rolesRepository.save(any(Roles.class))).thenReturn(existingRole);
        when(roleMapper.toRolesResponseDto(existingRole)).thenReturn(responseDto);

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.updateRole(1L, updatedRole);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals("USER", ((RolesResponseDto)result.getData()).getRoleName());
        verify(rolesRepository).findRolesById(1L);
        verify(rolesRepository).save(any(Roles.class));
        verify(roleMapper).toRolesResponseDto(existingRole);
    }

    @Test
    void updateRole_WhenRoleDoesNotExist_ShouldReturnErrorResponse() {
        Roles updatedRole = createRole(1L, ERole.ROLE_USER);

        when(rolesRepository.findById(1L)).thenReturn(Optional.empty());

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.updateRole(1L, updatedRole);

        assertEquals(500, result.getCode());
        assertEquals("INTERNAL SERVER ERROR", result.getResultMessage());
        verify(rolesRepository).findById(1L);
        verifyNoInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void deleteRole_WhenRoleExists_ShouldReturnSuccessResponse() {
        Roles existingRole = createRole(1L, ERole.ROLE_ADMIN);

        when(rolesRepository.findById(1L)).thenReturn(Optional.of(existingRole));

        CrudOperationResponseDto result = rolesService.deleteRole(1L);

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        verify(rolesRepository).findById(1L);
        verify(rolesRepository).deleteRolesById(1L);
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ShouldReturnNotFoundResponse() {
        when(rolesRepository.findById(1L)).thenReturn(Optional.empty());

        CrudOperationResponseDto result = rolesService.deleteRole(1L);

        assertEquals(404, result.getCode());
        assertEquals("ROLE NOT FOUND", result.getResultMessage());
        verify(rolesRepository).findById(1L);
        verifyNoInteractions(rolesRepository);
    }

    @Test
    void deleteRole_WhenRoleNameIsInvalid_ShouldReturnErrorResponse() {
        CrudOperationResponseDto result = rolesService.deleteRole(0L);

        assertEquals(500, result.getCode());
        assertEquals("INTERNAL SERVER ERROR", result.getResultMessage());
        verifyNoInteractions(rolesRepository);
    }

    //helper methods
    private Roles createRole(Long id, ERole roleEnum) {
        Roles role = new Roles();
        role.setId(id);
        role.setRoleEnum(roleEnum);
        role.setStatus(true);
        return role;
    }

    private RolesResponseDto createRoleDto(Long id, String name) {
        RolesResponseDto dto = new RolesResponseDto();
        dto.setId(id);
        dto.setRoleName(name);
        dto.setStatus(true);
        return dto;
    }
}