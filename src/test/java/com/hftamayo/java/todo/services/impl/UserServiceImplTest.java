package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.pagination.PaginatedDataDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.mapper.UserMapper;
import com.hftamayo.java.todo.repository.RolesRepository;
import com.hftamayo.java.todo.repository.UserRepository;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        // Arrange
        List<User> usersList = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> responseDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );

        when(userRepository.findAll()).thenReturn(usersList);
        when(userMapper.userToDto(usersList.get(0))).thenReturn(responseDtos.get(0));
        when(userMapper.userToDto(usersList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        List<UserResponseDto> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(userRepository).findAll();
        verify(userMapper, times(2)).userToDto(any(User.class));
    }

    @Test
    void getUsers_WhenNoUsersExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUsers());
        
        assertEquals("User with identifier all not found", exception.getMessage());
        verify(userRepository).findAll();
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        // Arrange
        Long userId = 1L;
        User user = createUser(userId, "John Doe");
        UserResponseDto responseDto = createUserDto(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.getUser(userId);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userMapper).userToDto(user);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserByCriteria_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String criteria = "email";
        String value = "john@example.com";
        User user = createUser(1L, "John Doe");
        UserResponseDto responseDto = createUserDto(1L, "John Doe");

        when(userRepository.findAll((Specification<User>) any())).thenReturn(List.of(user));
        when(userMapper.userToDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.getUserByCriteria(criteria, value);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findAll((Specification<User>) any());
        verify(userMapper).userToDto(user);
    }

    @Test
    void getUserByCriteria_WhenNoUsersExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        String criteria = "email";
        String value = "nonexistent@example.com";
        when(userRepository.findAll((Specification<User>) any())).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByCriteria(criteria, value));
        
        assertEquals("User with identifier email=nonexistent@example.com not found", exception.getMessage());
        verify(userRepository).findAll((Specification<User>) any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserByCriterias_WhenUserExists_ShouldReturnUser() {
        // Arrange
        String criteria = "email";
        String value = "john@example.com";
        String criteria2 = "status";
        String value2 = "true";
        User user = createUser(1L, "John Doe");
        UserResponseDto responseDto = createUserDto(1L, "John Doe");

        when(userRepository.findAll((Specification<User>) any())).thenReturn(List.of(user));
        when(userMapper.userToDto(user)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.getUserByCriterias(criteria, value, criteria2, value2);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findAll((Specification<User>) any());
        verify(userMapper).userToDto(user);
    }

    @Test
    void getUserByCriterias_WhenNoUsersExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        String criteria = "email";
        String value = "nonexistent@example.com";
        String criteria2 = "status";
        String value2 = "true";
        when(userRepository.findAll((Specification<User>) any())).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByCriterias(criteria, value, criteria2, value2));
        
        assertEquals("User with identifier email=nonexistent@example.com, status=true not found", exception.getMessage());
        verify(userRepository).findAll((Specification<User>) any());
        verifyNoInteractions(userMapper);
    }

    @Test
    void getPaginatedUsers_WhenUsersExist_ShouldReturnPaginatedData() {
        // Arrange
        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);
        List<User> usersList = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> responseDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );
        
        Page<User> userPage = new PageImpl<>(usersList, PageRequest.of(0, 2), 2);
        
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(userMapper.toUserResponseDto(usersList.get(0))).thenReturn(responseDtos.get(0));
        when(userMapper.toUserResponseDto(usersList.get(1))).thenReturn(responseDtos.get(1));

        // Act
        PaginatedDataDto<UserResponseDto> result = userService.getPaginatedUsers(pageRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        verify(userRepository).findAll(any(PageRequest.class));
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
    }

    @Test
    void saveUser_WhenValidUser_ShouldReturnSavedUser() {
        // Arrange
        User newUser = createUser(null, "New User");
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password123");
        
        User savedUser = createUser(1L, "New User");
        savedUser.setEmail("newuser@example.com");
        
        UserResponseDto responseDto = createUserDto(1L, "New User");
        responseDto.setEmail("newuser@example.com");
        
        Roles defaultRole = createRole(1L, ERole.ROLE_USER);

        when(userRepository.findUserByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.userToDto(savedUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.saveUser(newUser);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findUserByEmail("newuser@example.com");
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToDto(savedUser);
    }

    @Test
    void saveUser_WhenUserWithSameEmailExists_ShouldThrowDuplicateResourceException() {
        // Arrange
        User newUser = createUser(null, "New User");
        newUser.setEmail("existing@example.com");
        User existingUser = createUser(1L, "Existing User");
        existingUser.setEmail("existing@example.com");

        when(userRepository.findUserByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> userService.saveUser(newUser));
        
        assertTrue(exception.getMessage().contains("email"));
        assertTrue(exception.getMessage().contains("existing@example.com"));
        verify(userRepository).findUserByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = createUser(userId, "Original User");
        User updatedUser = createUser(userId, "Updated User");
        UserResponseDto responseDto = createUserDto(userId, "Updated User");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.userToDto(updatedUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.updateUser(userId, updatedUser);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToDto(updatedUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 999L;
        User updatedUser = createUser(userId, "Updated User");

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userId, updatedUser));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUserStatus_WhenUserExists_ShouldReturnUpdatedUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = createUser(userId, "User");
        existingUser.setStatus(true);
        
        User updatedUser = createUser(userId, "User");
        updatedUser.setStatus(false);
        
        UserResponseDto responseDto = createUserDto(userId, "User");
        responseDto.setStatus(false);

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.userToDto(updatedUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.updateUserStatus(userId, false);

        // Assert
        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToDto(updatedUser);
    }

    @Test
    void updateUserStatus_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUserStatus(userId, false));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        // Arrange
        Long userId = 1L;
        User user = createUser(userId, "User to Delete");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userRepository.deleteUserById(userId)).thenReturn(Optional.of(user));

        // Act
        assertDoesNotThrow(() -> userService.deleteUser(userId));

        // Assert
        verify(userRepository).findUserById(userId);
        verify(userRepository).deleteUserById(userId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verify(userRepository, never()).deleteUserById(any(Long.class));
    }

    // Helper methods
    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(name.toLowerCase().replace(" ", "") + "@example.com");
        user.setAge(25);
        user.setStatus(true);
        user.setAdmin(false);
        return user;
    }

    private UserResponseDto createUserDto(Long id, String name) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(name.toLowerCase().replace(" ", "") + "@example.com");
        dto.setAge(25);
        dto.setStatus(true);
        dto.setAdmin(false);
        return dto;
    }

    private Roles createRole(Long id, ERole roleEnum) {
        Roles role = new Roles();
        role.setId(id);
        role.setRoleEnum(roleEnum);
        role.setDescription("Description for " + roleEnum.name());
        role.setStatus(true);
        return role;
    }
}