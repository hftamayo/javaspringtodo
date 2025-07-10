package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.roles.RolesResponseDto;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.mapper.RoleMapper;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.exceptions.ValidationException;
import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PageResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

        List<RolesResponseDto> result = rolesService.getRoles().getDataList();

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

        RolesResponseDto result = rolesService.getRoleByName("ROLE_ADMIN").getData();

        assertEquals("ADMIN", result.getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(roleMapper).toRolesResponseDto(role);
    }

    @Test
    void getRoleByName_WhenRoleDoesNotExist_ShouldThrowResourceNotFoundException() {
        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleByName("ROLE_ADMIN"));
        
        assertEquals("Role with identifier ROLE_ADMIN not found", exception.getMessage());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verifyNoInteractions(roleMapper);
    }
/*
    @Test
    void getRoleByName_WhenRoleNameIsInvalid_ShouldThrowInvalidRequestException() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> rolesService.getRoleByName("INVALID_ROLE"));

        assertEquals("Role with identifier ROLE_ADMIN not found", exception.getMessage());
        verifyNoInteractions(rolesRepository);
        verifyNoInteractions(roleMapper);
    }
*/
    @Test
    void saveRole_WhenRoleDoesNotExist_ShouldReturnSavedRole() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);
        RolesResponseDto responseDto = createRoleDto(1L, "ADMIN");

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.empty());
        when(rolesRepository.save(newRole)).thenReturn(newRole);
        when(roleMapper.toRolesResponseDto(newRole)).thenReturn(responseDto);

        RolesResponseDto result = rolesService.saveRole(newRole).getData();

        assertEquals("ADMIN", result.getRoleName());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_ADMIN);
        verify(rolesRepository).save(newRole);
        verify(roleMapper).toRolesResponseDto(newRole);
    }

    @Test
    void saveRole_WhenRoleExists_ShouldThrowResourceAlreadyExistsException() {
        Roles newRole = createRole(1L, ERole.ROLE_ADMIN);

        when(rolesRepository.findByRoleEnum(ERole.ROLE_ADMIN)).thenReturn(Optional.of(newRole));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> rolesService.saveRole(newRole));
        
        assertEquals("Role with identifier ROLE_ADMIN already exists", exception.getMessage());
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

        RolesResponseDto result = rolesService.updateRole(1L, updatedRole).getData();

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
        
        assertEquals("Role with id 1 not found", exception.getMessage());
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
        
        assertEquals("Role with id 1 not found", exception.getMessage());
        verify(rolesRepository).findRolesById(1L);
        verifyNoMoreInteractions(rolesRepository);
    }

    @Test
    void getPaginatedRoles_WhenRolesExist_ShouldReturnPaginatedRoles() {
        List<Roles> rolesList = List.of(
            createRole(1L, ERole.ROLE_ADMIN),
            createRole(2L, ERole.ROLE_USER)
        );
        List<RolesResponseDto> responseDtos = List.of(
            createRoleDto(1L, "ADMIN"),
            createRoleDto(2L, "USER")
        );
        Page<Roles> rolesPage = new PageImpl<>(rolesList, PageRequest.of(0, 2), 2);

        when(rolesRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);
        when(roleMapper.toRolesResponseDto(rolesList.get(0))).thenReturn(responseDtos.get(0));
        when(roleMapper.toRolesResponseDto(rolesList.get(1))).thenReturn(responseDtos.get(1));

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        PageResponseDto<RolesResponseDto> result = rolesService.getPaginatedRoles(pageRequestDto);

        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
        verify(rolesRepository).findAll(any(PageRequest.class));
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getPaginatedRoles_WhenMultiplePages_ShouldReturnFirstPageWithCorrectMetadata() {
        // Arrange
        List<Roles> firstPageRoles = List.of(
                createRole(1L, ERole.ROLE_ADMIN),
                createRole(2L, ERole.ROLE_USER)
        );
        List<RolesResponseDto> firstPageDtos = List.of(
                createRoleDto(1L, "ADMIN"),
                createRoleDto(2L, "USER")
        );

        // Creamos una página con 2 elementos, pero indicamos que hay 5 elementos en total
        // Esto significa que habrá 3 páginas en total (5 elementos / 2 por página = 3 páginas)
        Page<Roles> rolesPage = new PageImpl<>(firstPageRoles, PageRequest.of(0, 2), 5);

        // Configuramos los mocks
        when(rolesRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);
        when(roleMapper.toRolesResponseDto(firstPageRoles.get(0))).thenReturn(firstPageDtos.get(0));
        when(roleMapper.toRolesResponseDto(firstPageRoles.get(1))).thenReturn(firstPageDtos.get(1));

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        // Act
        PageResponseDto<RolesResponseDto> result = rolesService.getPaginatedRoles(pageRequestDto);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage()); // Primera página (índice 0)
        assertEquals(2, result.getSize()); // 2 elementos por página
        assertEquals(5, result.getTotalElements()); // 5 elementos en total
        assertEquals(3, result.getTotalPages()); // 3 páginas en total
        assertFalse(result.isLast()); // No es la última página
        assertEquals(firstPageDtos, result.getContent());

        verify(rolesRepository).findAll(any(PageRequest.class));
        verify(roleMapper, times(2)).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getPaginatedRoles_WhenOnLastPage_ShouldIndicateIsLastPage() {
        // Arrange
        List<Roles> lastPageRoles = List.of(
                createRole(5L, ERole.ROLE_ADMIN)
        );
        List<RolesResponseDto> lastPageDtos = List.of(
                createRoleDto(5L, "ADMIN")
        );

        // Creamos la última página (índice 2) con 1 elemento, de un total de 5 elementos
        Page<Roles> rolesPage = new PageImpl<>(lastPageRoles, PageRequest.of(2, 2), 5);

        when(rolesRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);
        when(roleMapper.toRolesResponseDto(lastPageRoles.get(0))).thenReturn(lastPageDtos.get(0));

        PageRequestDto pageRequestDto = new PageRequestDto(2, 2, null);

        // Act
        PageResponseDto<RolesResponseDto> result = rolesService.getPaginatedRoles(pageRequestDto);

        // Assert
        assertEquals(1, result.getContent().size()); // Solo 1 elemento en la última página
        assertEquals(2, result.getPage()); // Tercera página (índice 2)
        assertEquals(2, result.getSize());
        assertEquals(5, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
        assertTrue(result.isLast()); // Es la última página

        verify(rolesRepository).findAll(any(PageRequest.class));
        verify(roleMapper).toRolesResponseDto(any(Roles.class));
    }

    @Test
    void getPaginatedRoles_WhenNoRolesExist_ShouldReturnEmptyPage() {
        List<Roles> rolesList = List.of();
        Page<Roles> rolesPage = new PageImpl<>(rolesList, PageRequest.of(0, 2), 0);

        when(rolesRepository.findAll(any(PageRequest.class))).thenReturn(rolesPage);

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        PageResponseDto<RolesResponseDto> result = rolesService.getPaginatedRoles(pageRequestDto);

        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        assertTrue(result.isLast());
        verify(rolesRepository).findAll(any(PageRequest.class));
        verifyNoInteractions(roleMapper);
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