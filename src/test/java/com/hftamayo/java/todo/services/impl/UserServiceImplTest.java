package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.mapper.UserMapper;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.repository.RolesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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

        List<UserResponseDto> result = userService.getUsers().getDataList();

        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(userRepository).findAll();
        verify(userMapper).userToDto(usersList.get(0));
        verify(userMapper).userToDto(usersList.get(1));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        Long userId = 1L;
        User user = createUser(userId, "John Doe");
        UserResponseDto responseDto = createUserDto(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUser(userId).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userMapper).userToDto(user);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
    }

    @Test
    void saveUser_WhenUserIsValid_ShouldReturnSavedUser() {
        User newUser = createUser(null, "John Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setPassword("password123");
        
        User savedUser = createUser(1L, "John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setPassword("encodedPassword");
        
        UserResponseDto responseDto = createUserDto(1L, "John Doe");
        Roles defaultRole = new Roles();
        defaultRole.setRoleEnum(ERole.ROLE_USER);

        when(userRepository.findUserByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode(newUser.getPassword().trim())).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.userToDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.saveUser(newUser).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserByEmail(newUser.getEmail());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(newUser);
        verify(userMapper).userToDto(savedUser);
    }

    @Test
    void saveUser_WhenUserAlreadyExists_ShouldThrowResourceAlreadyExistsException() {
        User existingUser = createUser(1L, "John Doe");
        existingUser.setEmail("john.doe@example.com");

        when(userRepository.findUserByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> userService.saveUser(existingUser));
        
        assertEquals("Resource with email '" + existingUser.getEmail() + "' already exists", exception.getMessage());
        verify(userRepository).findUserByEmail(existingUser.getEmail());
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");
        User updatedUser = createUser(userId, "John Smith");
        UserResponseDto responseDto = createUserDto(userId, "John Smith");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.userToDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(userId, updatedUser).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userRepository).save(any(User.class));
        verify(userMapper).userToDto(updatedUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        User updatedUser = createUser(userId, "John Smith");

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userId, updatedUser));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userRepository).findUserById(userId);
        verify(userRepository).deleteUserById(userId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
    }

    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setStatus(true);
        return user;
    }

    private UserResponseDto createUserDto(Long id, String name) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setStatus(true);
        return dto;
    }
}