package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.ResourceAlreadyExistsException;
import com.hftamayo.java.todo.exceptions.InvalidRequestException;

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
    void getRoles_WhenRolesExist_ShouldReturnListOfRoles() {
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

        List<RolesResponseDto> result = rolesService.getRoles();

        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(rolesRepository).findAll();
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnRole() {
        Roles role = createRole(1L, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(1L, "ADMIN");

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(roleMapper.toRolesResponseDto(role)).thenReturn(responseDto);

        RolesResponseDto result = rolesService.getRoleByName("ROLE_ADMIN");

        assertEquals("ADMIN", result.getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(roleMapper).toRolesResponseDto(role);
    }

    @Test
    void getRoleByName_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleByName("ROLE_ADMIN"));
        
        assertEquals("Role not found with name: ROLE_ADMIN", exception.getMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void getRoleByName_WhenRoleNameIsInvalid_ShouldThrowInvalidRequestException() {
        InvalidRequestException exception = assertThrows(InvalidRequestException.class,
                () -> rolesService.getRoleByName("INVALID_ROLE"));
        
        assertEquals("Invalid role name: INVALID_ROLE", exception.getMessage());
        verifyNoInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void saveRole_WhenRoleDoesNotExist_ShouldReturnSavedRole() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(1L, "ADMIN");

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(rolesRepository.save(newRole)).thenReturn(newRole);
        when(roleMapper.toRolesResponseDto(newRole)).thenReturn(responseDto);

        RolesResponseDto result = rolesService.saveRole(newRole);

        assertEquals("ADMIN", result.getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(rolesRepository).save(newRole);
        verify(roleMapper).toRolesResponseDto(newRole);
    }

    @Test
    void saveRole_WhenRoleExists_ShouldThrowResourceAlreadyExistsException() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.of(newRole));

        ResourceAlreadyExistsException exception = assertThrows(ResourceAlreadyExistsException.class,
                () -> rolesService.saveRole(newRole));
        
        assertEquals("Role already exists with name: ROLE_ADMIN", exception.getMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verifyNoMoreInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldReturnUpdatedRole() {
        Roles existingRole = createRole(1L, ERole.ROLE_ADMIN);
        Roles updatedRole = createRole(1L, ERole.ROLE_USER);
        RolesResponseDto responseDto = createRoleDto(1L, "USER");

        when(rolesRepository.findRolesById(1L)).thenReturn(Optional.of(existingRole));
        when(rolesRepository.save(any(Roles.class))).thenReturn(existingRole);
        when(roleMapper.toRolesResponseDto(existingRole)).thenReturn(responseDto);

        RolesResponseDto result = rolesService.updateRole(1L, updatedRole);

        assertEquals("USER", result.getRoleName());
        verify(rolesRepository).findRolesById(1L);
        verify(rolesRepository).save(any(Roles.class));
        verify(roleMapper).toRolesResponseDto(existingRole);
    }

    @Test
    void updateRole_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        Roles updatedRole = createRole(1L, ERole.ROLE_USER);

        when(rolesRepository.findRolesById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.updateRole(1L, updatedRole));
        
        assertEquals("Role not found with id: 1", exception.getMessage());
        verify(rolesRepository).findRolesById(1L);
        verifyNoMoreInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void deleteRole_WhenRoleExists_ShouldDeleteRole() {
        Roles existingRole = createRole(1L, ERole.ROLE_ADMIN);

        when(rolesRepository.findRolesById(1L)).thenReturn(Optional.of(existingRole));

        rolesService.deleteRole(1L);

        verify(rolesRepository).findRolesById(1L);
        verify(rolesRepository).deleteRolesById(1L);
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(rolesRepository.findRolesById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.deleteRole(1L));
        
        assertEquals("Role not found with id: 1", exception.getMessage());
        verify(rolesRepository).findRolesById(1L);
        verifyNoMoreInteractions(rolesRepository);
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