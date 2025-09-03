package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        // Arrange
        List<Roles> rolesList = List.of(
                createRole(1L, ERole.ROLE_USER),
                createRole(2L, ERole.ROLE_ADMIN)
        );
        List<RolesResponseDto> responseDtos = List.of(
                createRoleDto(1L, ERole.ROLE_USER),
                createRoleDto(2L, ERole.ROLE_ADMIN)
        );

        when(rolesRepository.findAll()).thenReturn(rolesList);
        when(roleMapper.toRolesResponseDto(rolesList.get(0))).thenReturn(responseDtos.get(0));
        when(roleMapper.toRolesResponseDto(rolesList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        List<RolesResponseDto> result = rolesService.getRoles();

        // Assert
        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(rolesRepository).findAll();
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getRoles_WhenNoRolesExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(rolesRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoles());
        
        assertEquals("Role with identifier all not found", exception.getMessage());
        verify(rolesRepository).findAll();
        verifyNoInteractions(roleMapper);
    }

    @Test
    void getRoleByName_WhenRoleExists_ShouldReturnRole() {
        // Arrange
        String roleName = "ROLE_USER";
        Roles role = createRole(1L, ERole.ROLE_USER);
        RolesResponseDto responseDto = createRoleDto(1L, ERole.ROLE_USER);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.of(role));
        when(roleMapper.toRolesResponseDto(role)).thenReturn(responseDto);

        // Act
        RolesResponseDto result = rolesService.getRoleByName(roleName);

        // Assert
        assertEquals(responseDto, result);
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(roleMapper).toRolesResponseDto(role);
    }

    @Test
    void getRoleByName_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        String roleName = "ROLE_SUPERVISOR";
        when(rolesRepository.findByRoleEnum(ERole.ROLE_SUPERVISOR)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleByName(roleName));
        
        assertEquals("Role with identifier ROLE_SUPERVISOR not found", exception.getMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_SUPERVISOR);
        verifyNoInteractions(roleMapper);
    }

    @Test
    void getPaginatedRoles_WhenRolesExist_ShouldReturnPaginatedData() {
        // Arrange
        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);
        List<Roles> rolesList = List.of(
                createRole(1L, ERole.ROLE_USER),
                createRole(2L, ERole.ROLE_ADMIN)
        );
        List<RolesResponseDto> responseDtos = List.of(
                createRoleDto(1L, ERole.ROLE_USER),
                createRoleDto(2L, ERole.ROLE_ADMIN)
        );
        
        Page<Roles> rolesPage = new PageImpl<>(rolesList, PageRequest.of(0, 2), 2);
        
        when(rolesRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);
        when(roleMapper.toRolesResponseDto(rolesList.get(0))).thenReturn(responseDtos.get(0));
        when(roleMapper.toRolesResponseDto(rolesList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        PaginatedDataDto<RolesResponseDto> result = rolesService.getPaginatedRoles(pageRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        verify(rolesRepository).findAll(any(PageRequest.class));
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void saveRole_WhenValidRole_ShouldReturnSavedRole() {
        // Arrange
        Roles newRole = createRole(null, ERole.ROLE_USER);
        Roles savedRole = createRole(1L, ERole.ROLE_USER);
        RolesResponseDto responseDto = createRoleDto(1L, ERole.ROLE_USER);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.empty());
        when(rolesRepository.save(newRole)).thenReturn(savedRole);
        when(roleMapper.toRolesResponseDto(savedRole)).thenReturn(responseDto);

        // Act
        RolesResponseDto result = rolesService.saveRole(newRole);

        // Assert
        assertEquals(responseDto, result);
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(rolesRepository).save(newRole);
        verify(roleMapper).toRolesResponseDto(savedRole);
    }

    @Test
    void saveRole_WhenRoleWithSameEnumExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        Roles newRole = createRole(null, ERole.ROLE_USER);
        Roles existingRole = createRole(1L, ERole.ROLE_USER);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.of(existingRole));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> rolesService.saveRole(newRole));
        
        assertTrue(exception.getMessage().contains("Role"));
        assertTrue(exception.getMessage().contains("ROLE_USER"));
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(rolesRepository, never()).save(any(Roles.class));
        verifyNoInteractions(roleMapper);
    }

    @Test
    void updateRole_WhenRoleExists_ShouldReturnUpdatedRole() {
        // Arrange
        Long roleId = 1L;
        Roles existingRole = createRole(roleId, ERole.ROLE_USER);
        Roles updatedRole = createRole(roleId, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(roleId, ERole.ROLE_ADMIN);

        when(rolesRepository.findRolesById(roleId)).thenReturn(Optional.of(existingRole));
        when(rolesRepository.save(any(Roles.class))).thenReturn(updatedRole);
        when(roleMapper.toRolesResponseDto(updatedRole)).thenReturn(responseDto);

        // Act
        RolesResponseDto result = rolesService.updateRole(roleId, updatedRole);

        // Assert
        assertEquals(responseDto, result);
        verify(rolesRepository).findRolesById(roleId);
        verify(rolesRepository).save(any(Roles.class));
        verify(roleMapper).toRolesResponseDto(updatedRole);
    }

    @Test
    void updateRole_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long roleId = 999L;
        Roles updatedRole = createRole(roleId, ERole.ROLE_ADMIN);

        when(rolesRepository.findRolesById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.updateRole(roleId, updatedRole));
        
        assertEquals("Role with id " + roleId + " not found", exception.getMessage());
        verify(rolesRepository).findRolesById(roleId);
        verify(rolesRepository, never()).save(any(Roles.class));
        verifyNoInteractions(roleMapper);
    }

    @Test
    void deleteRole_WhenRoleExists_ShouldDeleteRole() {
        // Arrange
        Long roleId = 1L;
        Roles role = createRole(roleId, ERole.ROLE_USER);

        when(rolesRepository.findRolesById(roleId)).thenReturn(Optional.of(role));
        when(rolesRepository.deleteRolesById(roleId)).thenReturn(Optional.of(role));

        // Act
        assertDoesNotThrow(() -> rolesService.deleteRole(roleId));

        // Assert
        verify(rolesRepository).findRolesById(roleId);
        verify(rolesRepository).deleteRolesById(roleId);
    }

    @Test
    void deleteRole_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long roleId = 999L;
        when(rolesRepository.findRolesById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.deleteRole(roleId));
        
        assertEquals("Role with id " + roleId + " not found", exception.getMessage());
        verify(rolesRepository).findRolesById(roleId);
        verify(rolesRepository, never()).deleteRolesById(any(Long.class));
    }

    // Helper methods
    private Roles createRole(Long id, ERole roleEnum) {
        Roles role = new Roles();
        role.setId(id);
        role.setRoleEnum(roleEnum);
        role.setDescription("Description for " + roleEnum.name());
        role.setStatus(true);
        return role;
    }

    private RolesResponseDto createRoleDto(Long id, ERole roleEnum) {
        RolesResponseDto dto = new RolesResponseDto();
        dto.setId(id);
        dto.setRoleName(roleEnum.name());
        dto.setRoleDescription("Description for " + roleEnum.name());
        dto.setStatus(true);
        return dto;
    }
}