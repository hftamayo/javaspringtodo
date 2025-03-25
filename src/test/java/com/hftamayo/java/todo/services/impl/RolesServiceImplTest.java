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
class RolesServiceImplTest {

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
        when(roleMapper.toRolesResponseDto(any(Roles.class)))
                .thenReturn(responseDtos.get(0), responseDtos.get(1));

        CrudOperationResponseDto<RolesResponseDto> result = rolesService.getRoles();

        assertEquals(200, result.getCode());
        assertEquals("OPERATION SUCCESSFUL", result.getResultMessage());
        assertEquals(2, ((List<RolesResponseDto>)result.getData()).size());
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